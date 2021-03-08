package com.kozlov.ipaddress.v2;

import java.util.BitSet;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class BufferUniqueIPReader implements Runnable {

    // 512 memory usage
    // https://stackoverflow.com/a/51421736
    // По памяти более лучшее решение чем boolean[]
    // По операциям вставки чуть хуже...
    // Слишком много для файлов менее 512 мб
    // Можно сказать, что подходит по использованию памяти для больших файлов.
    private final BitSet bits0 = new BitSet(Integer.MAX_VALUE); // буфер пометки уникальности отрицательных значений IP аддресса
    private final BitSet bits1 = new BitSet(Integer.MAX_VALUE); // буфер пометки уникальности положительных значений IP аддресса

    private final BlockingQueue<byte[]> buffers;

    private int result = -1;

    public BufferUniqueIPReader(BlockingQueue<byte[]> buffers) {
        Objects.requireNonNull(buffers);
        this.buffers = buffers;
    }

    public int getResult() {
        return result;
    }

    @Override
    public void run() {
        int next = -1;
        byte[] bytes = new byte[4];
        int lastIndex = 0; // Псоледний считанный индекс байта IP. 0..3
        int lastValue = 0; // Последнее считанное значение байта IP до точки или конца адресса (0..255)
        int byteCount = 0; // Количество считанных байт IP. MAX = 1..4

        while (next != 0) {
            byte[] buffer = buffers.poll();
            if (buffer == null) continue;
            // Парсинг IPv4
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
        result = bits0.cardinality() + bits1.cardinality();
    }

    private int byteToInt(byte[] bytes) {
        return  ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF) << 0);
    }

}