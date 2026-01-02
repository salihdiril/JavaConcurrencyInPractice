package com.salih.chapter2.ex3;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lazyInitRace")
public class LazyInitRace extends HttpServlet {
	private ExpensiveObject instance = null;

	public ExpensiveObject getInstance() {
		if (instance == null) {
			instance = new ExpensiveObject();
		}
		return instance;
	}

	/**
	 * A <b>Non-Thread-Safe</b> Servlet that demonstrates a <b>Check-Then-Act</b> Race Condition.
	 * <p>
	 * <b>The Goal (Lazy Initialization):</b>
	 * We want to create the {@code ExpensiveObject} only once (Singleton pattern), the first time
	 * it is needed.
	 * </p>
	 * <p>
	 * <b>The Race Condition (Check-Then-Act):</b>
	 * The method {@code getInstance()} contains a fatal flaw for concurrency:
	 * <pre>
	 * if (instance == null) {    // 1. CHECK (Observe)
	 * instance = new ...     // 2. ACT (Modify)
	 * }
	 * </pre>
	 * </p>
	 * <p>
	 * <b>How it fails:</b>
	 * As described in <i>Java Concurrency in Practice</i>, this relies on "lucky timing".
	 * <ul>
	 * <li><b>Thread A</b> checks {@code instance} and sees {@code null}.</li>
	 * <li><b>Thread A</b> pauses (Context Switch).</li>
	 * <li><b>Thread B</b> checks {@code instance} and <i>also</i> sees {@code null} (because A hasn't acted yet).</li>
	 * <li><b>Thread A</b> resumes and creates <b>Object #1</b> (UUID: 1234...).</li>
	 * <li><b>Thread B</b> resumes and creates <b>Object #2</b> (UUID: 5678...).</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>The Result:</b>
	 * Instead of a single shared object, multiple instances are created. If you run this with JMeter,
	 * you will see <b>different UUIDs</b> printed in the console, proving that the "Singleton" guarantee failed.
	 * </p>
	 *
	 * @param req  The servlet request
	 * @param resp The servlet response
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		ExpensiveObject expensiveObject = getInstance();
		System.out.println("ID: " + expensiveObject.getId());
	}
}
