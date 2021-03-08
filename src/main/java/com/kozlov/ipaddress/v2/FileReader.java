package com.kozlov.ipaddress.v2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class FileReader implements Runnable {

    private final InputStream input;
    private final BlockingQueue<byte[]> buffers;
    private final int bufferSize;

    public FileReader(InputStream input, BlockingQueue<byte[]> buffers, int bufferSize) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(buffers);
        this.input = input;
        this.buffers = buffers;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        try (InputStream stream = input) {
            byte[] buffer = new byte[bufferSize];
            int bytesCount = 0;
            while (bytesCount >= 0) {
                bytesCount = stream.read(buffer);
                buffers.put(buffer);
                buffer = new byte[bufferSize];
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Чтение файла прервано.", e);
        }
    }

}