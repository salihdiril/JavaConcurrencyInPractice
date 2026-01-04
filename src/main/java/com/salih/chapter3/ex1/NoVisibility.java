package com.salih.chapter3.ex1;

public class NoVisibility {
	private static boolean ready;
	private static int number;

	private static class ReaderThread extends Thread {
		@Override
		public void run() {
			while (!ready) {
				Thread.yield();
			}
			System.out.println(number);
		}

	}

	/**
	 * Demonstrates the combined dangers of <b>Visibility Issues</b> and <b>Race Conditions</b>
	 * in a multi-threaded environment without synchronization.
	 * <p>
	 * <b>The Experiment:</b>
	 * This loop launches 1,000 separate threads. For each thread, the main method:
	 * <ol>
	 * <li>Sets {@code number = 42}.</li>
	 * <li>Sets {@code ready = true} (The "Flag").</li>
	 * <li>Waits briefly.</li>
	 * <li>Resets {@code number = 0} and {@code ready = false}.</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>The Result (Why we see 0):</b>
	 * You observed that "42" is printed rarely (e.g., 11 times), while "0" is printed most of the time.
	 * This is caused by two distinct concurrency bugs working together:
	 * </p>
	 * <ul>
	 * <li><b>1. The Race Condition (Timing):</b> Even with the {@code sleep()}, there is no guarantee
	 * the Reader Thread will print before the Main Thread resets the variables. If the Main Thread
	 * wakes up and executes {@code number = 0} <i>microseconds before</i> the Reader calls
	 * {@code println(number)}, the Reader prints 0. This is a standard "Read-Write" race.</li>
	 *
	 * <li><b>2. Visibility & Reordering (The "Stale Data" Problem):</b>
	 * Without {@code volatile} or {@code synchronized}, the Java Memory Model (JMM) allows the CPU
	 * to reorder instructions. The Reader Thread might see {@code ready = true} (because that change
	 * flushed to main memory) but still see the <i>stale value</i> of {@code number} (because the
	 * write {@code number = 42} was still sitting in the Main Thread's local cache).</li>
	 * </ul>
	 * <p>
	 * <b>The Lesson:</b>
	 * Just because you write code in a specific order (Line A then Line B) does not mean
	 * other threads will see the effects in that same order. Without synchronization,
	 * memory writes are <b>eventually</b> visible, not <b>instantly</b> visible.
	 * </p>
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			new ReaderThread().start();
			number = 42;
			ready = true;
			number = 0;
			ready = false;
		}

		ready = true;
	}
}
