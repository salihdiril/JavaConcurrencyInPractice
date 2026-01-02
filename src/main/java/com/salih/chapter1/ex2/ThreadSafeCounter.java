package com.salih.chapter1.ex2;

public class ThreadSafeCounter {
	private int count = 0;

	public synchronized void increment() {
		count++;
	}

	public synchronized int getValue() {
		return count;
	}
}
