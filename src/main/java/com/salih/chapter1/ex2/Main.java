package com.salih.chapter1.ex2;

public class Main {

	/**
	 * Demonstrates <b>Thread Safety</b> by using a synchronized shared object to prevent Race Conditions.
	 * <p>
	 * <b>The Scenario:</b>
	 * Just like the previous example, two threads (Thread A and Thread B) share a single
	 * {@code ThreadSafeCounter} instance. Both attempt to increment it 10,000 times simultaneously.
	 * </p>
	 * <p>
	 * <b>The Solution (Intrinsic Locks):</b>
	 * Unlike the first example, this counter uses the {@code synchronized} keyword.
	 * This converts the "Read-Modify-Write" sequence into a single <b>Atomic</b> operation.
	 * </p>
	 * <p>
	 * <b>How it works:</b>
	 * <ul>
	 * <li>When Thread A calls {@code increment()}, it automatically acquires the <b>Intrinsic Lock</b> (Monitor) of the counter object.</li>
	 * <li>If Thread B tries to call {@code increment()} at the same time, it sees the lock is taken.</li>
	 * <li>Thread B is forced to <b>Block</b> (wait) until Thread A finishes and releases the lock.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>The Result:</b>
	 * The final value is guaranteed to be exactly <b>20,000</b>.
	 * The "Lost Update" problem is eliminated because the operations can no longer interleave.
	 * </p>
	 */
	public static void main(String[] args) {
		System.out.println("Running Example 2");
		ThreadSafeCounter counter = new ThreadSafeCounter();

		Thread a = new Thread(() -> {
			for (int i = 0; i < 10000; i++) {
				counter.increment();
			}
		});

		Thread b = new Thread(() -> {
			for (int i = 0; i < 10000; i++) {
				counter.increment();
			}
		});

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
