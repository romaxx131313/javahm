package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Hello world!
 */
public class App {
    private static final Path FILE_PATH;
    private static final Path OUT_PATH;

    static {
        FILE_PATH = Path.of("alice.txt");
        OUT_PATH = Path.of("text.txt");
        if (Files.exists(OUT_PATH)) {
            try {
                Files.delete(OUT_PATH);
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final int begging = 0;
        final int countThreads = 10;
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        System.out.println("Программа выполняется...");
        try {
            inputStream = new FileInputStream(FILE_PATH.toString());
            outputStream = new FileOutputStream(OUT_PATH.toString());
            final List<Reader> readers = new ArrayList<>();
            for (int i = begging; i < countThreads; i++) {
                readers.add(new Reader(inputStream, outputStream, readWriteLock));
            }
            try {
                executorService.invokeAll(readers);
            } catch (InterruptedException e) {
                e.getStackTrace();
            } finally {
                executorService.shutdown();
            }
            System.out.println("Програма закончила выполнение кода");
            System.out.println("Досвидание  :)");
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } finally {
            try {
                Objects.requireNonNull(inputStream).close();
                Objects.requireNonNull(outputStream).close();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }
}