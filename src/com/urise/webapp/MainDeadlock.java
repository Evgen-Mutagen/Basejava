package com.urise.webapp;

public class MainDeadlock {
    public static void main(String[] args) {
        String lock1 = "lock1";
        String lock2 = "lock2";
        deadlock(lock1, lock2);
        deadlock(lock2, lock1);
    }

    public static void deadlock(String lock1, String lock2) {
        new Thread(() -> {
            System.out.println("Wait " + lock1);
            synchronized (lock1) {
                System.out.println("Thread : locked " + lock1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Wait: " + lock2);
                synchronized (lock2) {
                    System.out.println("Thread : locked " + lock2);
                }
            }
        }).start();
    }
}
