package com.salih.chapter1.ex1;

public class Main {

    /**
     * Demonstrates a classic <b>Race Condition</b> (Lost Update) using a shared mutable object.
     * * <p>
     * <b>The Scenario:</b>
     * We have a single instance of {@code Counter} that is shared between two separate threads.
     * Both threads attempt to increment the counter 10,000 times simultaneously.
     * </p>
     * * <p>
     * <b>The Expectation:</b>
     * Thread A adds 10,000. Thread B adds 10,000.
     * The final result should be <b>20,000</b>.
     * </p>
     * * <p>
     * <b>The Reality (The Race Condition):</b>
     * The final value will likely be <b>less than 20,000</b> (e.g., 15,432) because the
     * increment operation is not atomic.
     * </p>
     * * <p>
     * <b>How the Race Happens (Step-by-Step):</b>
     * Even though {@code counter.increment()} looks like one step, the CPU sees 3 steps:
     * <ol>
     * <li><b>Read:</b> Fetch current value (e.g., 50).</li>
     * <li><b>Modify:</b> Add 1 to it (50 + 1 = 51).</li>
     * <li><b>Write:</b> Save the new value (51) back to memory.</li>
     * </ol>
     * * If the OS pauses Thread A right after step 1:
     * <ul>
     * <li>Thread A reads 50. <b>(PAUSED)</b></li>
     * <li>Thread B runs, reads 50, adds 1, and writes <b>51</b>.</li>
     * <li>Thread A resumes. It still thinks the value is 50. It calculates 51.</li>
     * <li>Thread A writes <b>51</b>.</li>
     * </ul>
     * <b>Result:</b> Two increments happened, but the value only increased by 1. Thread B's work was "lost".
     * </p>
     */
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
