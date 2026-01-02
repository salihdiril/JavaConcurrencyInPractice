package com.salih.chapter1.ex1;

public class Main {
    public static void main(String[] args) {
        System.out.println("Running Example 1");
        Counter counter = new Counter();

        Thread a = new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    counter.increment();
                }
            }
        };
        Thread b = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    counter.increment();
                }
            }
        };

        a.start();
        b.start();

        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final counter value: " + counter.getValue());

    }
}
