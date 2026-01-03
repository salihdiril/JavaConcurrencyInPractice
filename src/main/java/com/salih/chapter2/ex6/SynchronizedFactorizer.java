package com.salih.chapter2.ex6;

import static com.salih.chapter2.FactorizerUtil.encodeIntoResponse;
import static com.salih.chapter2.FactorizerUtil.extractFromRequest;
import static com.salih.chapter2.FactorizerUtil.factor;
import static com.salih.chapter2.FactorizerUtil.getFactorsArrString;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/factor/synchronized")
public class SynchronizedFactorizer extends HttpServlet {
	private BigInteger lastNumber;
	private BigInteger[] lastFactors;

	/**
	 * A <b>Thread-Safe</b> Servlet that solves race conditions by synchronizing the entire service method,
	 * but at the cost of <b>Poor Concurrency</b>.
	 * <p>
	 * <b>The Fix (Thread Safety):</b>
	 * By adding the {@code synchronized} keyword to the {@code service} method, we acquire the
	 * intrinsic lock of the Servlet instance.
	 * <ul>
	 * <li>This makes the entire method one giant <b>Atomic Block</b>.</li>
	 * <li>It is now impossible for "Thread B" to enter the method while "Thread A" is updating the cache.</li>
	 * <li>The invariant ({@code lastNumber} matches {@code lastFactors}) is preserved perfectly.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>The Price (Performance Issues):</b>
	 * While safe, this approach effectively turns a multi-threaded web server into a <b>Single-Threaded</b> application.
	 * <ul>
	 * <li><b>Serialization:</b> Requests are processed one at a time, in a queue.</li>
	 * <li><b>Blocking:</b> If User A requests a difficult number (taking 10 seconds to factor),
	 * User B (who just wants to factor '2') must wait 10 seconds just to <i>enter</i> the method.</li>
	 * <li><b>Throughput:</b> Under high load, the server will become unresponsive as threads pile up waiting for the lock.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>Conclusion:</b>
	 * As <i>Java Concurrency in Practice</i> states: "This approach is thread-safe, but satisfies the
	 * liveness and performance requirements only for the lowest-traffic sites."
	 * </p>
	 *
	 * @param req  The servlet request
	 * @param resp The servlet response
	 */
	@Override
	public synchronized void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BigInteger i = extractFromRequest(req);
		if (i.equals(lastNumber)) {
			System.out.println("Number: " + lastNumber + ", Cached Factors: " + getFactorsArrString(lastFactors));
			encodeIntoResponse(resp, lastFactors);
		} else {
			BigInteger[] factors = factor(i);
			lastNumber = i;
			lastFactors = factors;
			System.out.println("Number: " + i + ", New Factors: " + getFactorsArrString(factors));
			encodeIntoResponse(resp, factors);
		}
	}
}
