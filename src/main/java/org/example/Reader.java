package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Reader implements Callable<Void> {
    private InputStream inputStream;
    private OutputStream outputStream;
    private final ReentrantReadWriteLock lock;

    public Reader(InputStream inputStream, OutputStream outputStream, ReentrantReadWriteLock lock) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.lock = lock;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    @Override
    public Void call() throws Exception {
        int count;
        byte[] bytes = new byte[256];
        try {
            do {
                this.lock.writeLock().lock();
                try {
                    count = this.inputStream.read(bytes);
                } finally {
                    this.lock.writeLock().unlock();
                }
                if (count > 0) {
                    this.outputStream.write(bytes, 0, count);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.getStackTrace();
                }
            } while (count > 0);
        } catch (IOException e) {
            e.getStackTrace();
        }
        return null;
    }
}
