package org.example;


import java.util.Arrays;

public class DeadLock {
    static final Object Lock1 = new Object();
    static final Object Lock2 = new Object();

    private static class Thread1 extends Thread {
        public void run() {
            synchronized (Lock1) {
                System.out.println("Thread 1: Has Lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException exception) {
                    System.out.println(Arrays.toString(exception.getStackTrace()));
                }
                System.out.println("Thread 1: Waiting for Lock 2");
                synchronized (Lock2) {
                    System.out.println("Thread 1: No DeadLock");
                }
            }
        }

        private static class Thread2 extends Thread {
            public void run() {
                synchronized (Lock1) {
                    System.out.println("Thread 2: Has Lock2");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exception) {
                        System.out.println(Arrays.toString(exception.getStackTrace()));
                    }
                    System.out.println("Thread 2: Waiting for Lock 1");
                }
                synchronized (Lock2) {
                    System.out.println("Thread 2: No DeadLock");
                }
            }
        }

        public static void main(String[] args) {
            Thread1 thread1 = new Thread1();
            Thread2 thread2 = new Thread2();
            thread1.start();
            thread2.start();
        }
    }
}

  
package org.example;

import org.example.classes.MyLogger;
import org.example.classes.ThreadReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;


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
                MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
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
        System.out.println("System is working...");
        try {
            inputStream = new FileInputStream(FILE_PATH.toString());
            outputStream = new FileOutputStream(OUT_PATH.toString());
            final List<ThreadReader> readers = new ArrayList<>();
            for (int i = begging; i < countThreads; i++) {
                readers.add(new ThreadReader(inputStream, outputStream, readWriteLock));
            }
            try {
                executorService.invokeAll(readers);
            } catch (InterruptedException e) {
                MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
            } finally {
                executorService.shutdown();
                MyLogger.logMessage("ExecutorService shutdown");
            }
            System.out.println("Program finished!");
            System.out.println("Good bye :)");
        } catch (FileNotFoundException e) {
            MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                Objects.requireNonNull(inputStream).close();
                Objects.requireNonNull(outputStream).close();
            } catch (IOException e) {
                MyLogger.logMessage(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}









Задание 1.

В продолжении последнего дз реализовать копирование файла, но для синхронизации использвать ReentrantReadWriteLock,
запуск потоков выполнять черезе ServiceExecutor (создать пул на 10 потоков)

Задание 2.

Ниже представлен пример, в котором демонстрируется ситуация deadlock (взаимной блокировки).
Необходимо исправить программу так, что бы азаимной блокировки не было.

public class DeadLock {
  static Object Lock1 = new Object();
  static Object Lock2 = new Object();

  private static class Thread1 extends Thread {
    public void run() {
      synchronized (Lock1) {
        System.out.println("Thread 1: Has Lock1");
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {

        }
        System.out.println("Thread 1: Waiting for Lock 2");
        synchronized (Lock2) {
          System.out.println("Thread 1: No DeadLock");
        }
      }
    }
  }

  private static class Thread2 extends Thread {
    public void run() {
      synchronized (Lock2) {
        System.out.println("Thread 2: Has Lock2");
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {

        }
        System.out.println("Thread 2: Waiting for Lock 1");
        synchronized (Lock1) {
          System.out.println("Thread 2: No DeadLock");
        }
      }
    }
  }

  public static void main(String args[]) {
    Thread1 thread1 = new Thread1();
    Thread2 thread2 = new Thread2();
    thread1.start();
    thread2.start();
  }
}