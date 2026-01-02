package com.salih.chapter2.ex4;

import static com.salih.chapter2.FactorizerUtil.encodeIntoResponse;
import static com.salih.chapter2.FactorizerUtil.extractFromRequest;
import static com.salih.chapter2.FactorizerUtil.factor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/factor/counting")
public class CountingFactorizer extends HttpServlet {
	private final AtomicLong count = new AtomicLong(0);

	public long getCount() {
		return count.get();
	}

	/**
	 * A <b>Thread-Safe</b> Servlet that counts requests using Java's Atomic concurrency tools.
	 * <p>
	 * <b>The Fix (Atomicity):</b>
	 * Unlike {@code UnsafeCountingFactorizer}, this class uses an {@link java.util.concurrent.atomic.AtomicLong}
	 * to manage its state.
	 * </p>
	 * <p>
	 * <b>How it works:</b>
	 * The method {@code count.incrementAndGet()} is <b>atomic</b>. It combines the "Read-Modify-Write"
	 * sequence into a single, indivisible hardware instruction (CAS - Compare-And-Swap).
	 * <ul>
	 * <li>No "Lost Updates" can occur.</li>
	 * <li>No locks (synchronization) are needed, making it highly performant.</li>
	 * </ul>
	 * </p>
	 *
	 * @param req  The servlet request
	 * @param resp The servlet response
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = factor(i);
		final long value = count.incrementAndGet();
		System.out.println("Count: " + value);
		encodeIntoResponse(resp, factors);
	}
}
