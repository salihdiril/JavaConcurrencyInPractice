package com.salih.chapter2.ex1;

import static com.salih.chapter2.FactorizerUtil.encodeIntoResponse;
import static com.salih.chapter2.FactorizerUtil.extractFromRequest;
import static com.salih.chapter2.FactorizerUtil.factor;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/factor/stateless")
public class StatelessFactorizer extends HttpServlet {

	/**
	 * A <b>Thread-Safe</b> Servlet implementation that demonstrates the safety of <b>Stateless Objects</b>.
	 * <p>
	 * <b>Why is this Thread-Safe?</b>
	 * This class is thread-safe because it has <b>no state</b> (no instance variables or fields).
	 * </p>
	 * <p>
	 * <b>How it works internally:</b>
	 * <ul>
	 * <li>When the web server handles 1,000 concurrent requests, it creates 1,000 threads.</li>
	 * <li>Each thread calls this {@code service()} method independently.</li>
	 * <li>The variables {@code i} and {@code factors} are <b>Local Variables</b>.</li>
	 * <li>Local variables are stored on the <b>Thread Stack</b>, not the Heap. This means every thread
	 * has its own private copy of these variables.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>The Rule:</b>
	 * Since no data is shared between threads (no shared fields), it is physically impossible for one
	 * thread to interfere with another. As stated in <i>Java Concurrency in Practice</i>:
	 * <i>"Stateless objects are always thread-safe."</i>
	 * </p>
	 *
	 * @param servletRequest  The servlet request (local to the thread)
	 * @param servletResponse The servlet response (local to the thread)
	 */
	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
		BigInteger i = extractFromRequest(servletRequest);
		BigInteger[] factors = factor(i);
		encodeIntoResponse(servletResponse, factors);
	}

}
