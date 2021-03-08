package com.kozlov.ipaddress.v1;

import com.kozlov.ipaddress.IPCalculator;
import com.kozlov.ipaddress.exception.CalculationException;
import com.kozlov.ipaddress.runner.IPCalculationParam;

import java.io.*;
import java.util.BitSet;
import java.util.Objects;

public class UniqueIPCalcV1 implements IPCalculator {

    private final IPCalculationParam param;
    private final InputStream inputStream;

    public UniqueIPCalcV1(IPCalculationParam param, InputStream inputStream) {
        Objects.requireNonNull(param);
        Objects.requireNonNull(inputStream);
        this.param = param;
        this.inputStream = inputStream;
    }

    @Override
    public int getCount() {
        try {
            return countIP();
        } catch (FileNotFoundException e) {
            throw new CalculationException("Файл не найден", e);
        } catch (IOException e) {
            throw new CalculationException("Ошибка чтения файла.", e);
        }
    }

    private int countIP() throws IOException {

        // 512 memory usage
        // https://stackoverflow.com/a/51421736
        // По памяти более лучшее решение чем boolean[]
        // По операциям вставки чуть хуже...
        // Слишком много для файлов менее 512 мб
        // Можно сказать, что подходит по использованию памяти для больших файлов.
        BitSet bits0 = new BitSet(Integer.MAX_VALUE); // буфер пометки уникальности отрицательных значений IP аддресса
        BitSet bits1 = new BitSet(Integer.MAX_VALUE); // буфер пометки уникальности положительных значений IP аддресса

        int bufferSize = param.getBufferSize();
        try (InputStream stream = inputStream) {
            byte[] buffer;
            int next = -1;
            byte[] bytes = new byte[4];
            int lastIndex = 0; // Псоледний считанный индекс байта IP. 0..3
            int lastValue = 0; // Последнее считанное значение байта IP до точки или конца адресса (0..255)
            int byteCount = 0; // Количество считанных байт IP. MAX = 1..4
            while (next != 0) {
                buffer = new byte[bufferSize];
                stream.read(buffer);
                // ----- парсинг IPv4
                int i = 0;
                while (i < buffer.length) {
                    int index = lastIndex;
                    int value = lastValue;
                    boolean complete = false;
                    while (i < buffer.length) {
                        next = buffer[i++];
                        complete = next == '\n' || next == 0;
                        if (complete) {
                            ++byteCount;
                            lastValue = 0;
                            lastIndex = 0;
                            break;
                        }
                        if (next == '\r') {
                            continue;
                        }
                        boolean isDot = next == 0x2E;
                        if (isDot) {
                            ++byteCount;
                            bytes[index++] = (byte) value;
                            value = 0;
                        } else {
                            value = value * 10 + Character.digit(next, 10);
                        }
                        lastValue = value;
                        lastIndex = index;
                    }

                    if (complete && byteCount == 4) {
                        bytes[index] = (byte) value;
                        int ip = byteToInt(bytes);

                        BitSet bits;
                        if (ip < 0) {
                            bits = bits0;
                        } else {
                            bits = bits1;
                        }

                        // Очищаем старший бит
                        // Отвечающий за знак
                        int bitIndex = ip & Integer.MAX_VALUE;
                        // https://stackoverflow.com/a/35644870
                        // complexity = O(1)
                        bits.set(bitIndex);

                        bytes = new byte[4];
                        byteCount = 0;
                    }
                    if (i == buffer.length && !complete || next == 0) {
                        break;
                    }
                }
            }
        }
        return bits0.cardinality() + bits1.cardinality();
    }

    public static int byteToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF) << 0);
    }

}
