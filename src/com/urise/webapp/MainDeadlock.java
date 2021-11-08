package com.urise.webapp;

public class MainDeadlock {
        public void deadlock (Object lock1, Object lock2) {
           new Thread() {
               public void run() {
                   synchronized (lock1) {
                       System.out.println("Thread : locked LOCK1");
                       try {
                           Thread.sleep(100);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       synchronized (lock2) {
                           System.out.println("Thread : locked LOCK2");
                       }
                   }
               }
        }.start();
    }
}
