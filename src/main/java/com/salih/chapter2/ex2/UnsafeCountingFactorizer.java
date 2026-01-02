package com.salih.chapter2.ex2;

import static com.salih.chapter2.FactorizerUtil.encodeIntoResponse;
import static com.salih.chapter2.FactorizerUtil.extractFromRequest;
import static com.salih.chapter2.FactorizerUtil.factor;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/factor/unsafeCounting")
public class UnsafeCountingFactorizer extends HttpServlet {
	private long count = 0;

	/**
	 * A <b>Non-Thread-Safe</b> implementation of the service method that demonstrates a classic
	 * <b>Race Condition</b>.
	 * <p>
	 * <b>The Flaw (Shared State):</b>
	 * Unlike the previous "Stateless" example, this class has a <b>State</b> variable:
	 * {@code private long count = 0;}.
	 * </p>
	 * <p>
	 * <b>Why it fails:</b>
	 * <ul>
	 * <li>Servlet containers (like Jetty/Tomcat) typically create <b>only one instance</b> of this class.</li>
	 * <li>This single instance is shared among all concurrent request threads.</li>
	 * <li>The {@code count} variable is stored on the <b>Heap</b> (Shared Memory), not the Stack.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>The Race Condition (Read-Modify-Write):</b>
	 * The operation {@code count++} is not atomic. It consists of three steps:
	 * <ol>
	 * <li>Fetch the current value of {@code count}.</li>
	 * <li>Add one to it.</li>
	 * <li>Write the new value back.</li>
	 * </ol>
	 * If two threads enter this method at the same time, they may both read the same initial value (e.g., 9),
	 * and both write back the same result (10), causing a <b>Lost Update</b>.
	 * </p>
	 *
	 * @param servletRequest  The incoming request.
	 * @param servletResponse The outgoing response.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
		BigInteger i = extractFromRequest(servletRequest);
		BigInteger[] factors = factor(i);
		count++;
		System.out.println("Count: " + getCount());
		encodeIntoResponse(servletResponse, factors);
	}

	public long getCount() {
		return count;
	}
}
