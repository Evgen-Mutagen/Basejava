package com.urise.webapp;

public class MainDeadlock {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();
        deadlock(lock1, lock2);
        deadlock(lock2, lock1);
    }

    public static void deadlock(Object lock1, Object lock2) {
        new Thread(() -> {
            System.out.println("Wait: lock1");
            synchronized (lock1) {
                System.out.println("Thread : locked lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Wait: lock2");
                synchronized (lock2) {
                    System.out.println("Thread : locked lock2");
                }
            }
        }).start();
    }
}
