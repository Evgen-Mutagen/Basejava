package com.urise.webapp;

public class MainDeadlock {
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();
   
        Thread thread1 = new Thread() {
            public void run() {
                synchronized (LOCK1) {
                    System.out.println("Thread 1: locked LOCK1");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (LOCK2) {
                        System.out.println("Thread 1: locked LOCK2");
                    }
                }
            }
        };

        Thread thread2 = new Thread() {
            public void run() {
                synchronized (LOCK2) {
                    System.out.println("Thread 2: locked LOCK2");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (LOCK1) {
                        System.out.println("Thread 2: locked LOCK1");
                    }
                }
            }
        };
    }

