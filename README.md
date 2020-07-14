# Домашнее задание 

## Задание 1.

В продолжении последнего дз реализовать копирование файла, но для синхронизации использвать ReentrantReadWriteLock,
запуск потоков выполнять черезе ServiceExecutor (создать пул на 10 потоков)

## Задание 2.

Ниже представлен пример, в котором демонстрируется ситуация deadlock (взаимной блокировки).
Необходимо исправить программу так, что бы азаимной блокировки не было.

```java
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
```
