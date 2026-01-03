package com.salih.chapter2.ex7;

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

@WebServlet("/factor/cached")
public class CachedFactorizer extends HttpServlet {
	private BigInteger lastNumber;
	private BigInteger[] lastFactors;
	private long hits;
	private long cacheHits;

	/**
	 * A <b>Thread-Safe and Performant</b> Servlet that uses fine-grained synchronization.
	 * <p>
	 * <b>The Goal (High Concurrency):</b>
	 * Unlike {@code SynchronizedFactorizer} which locks the entire method (allowing only 1 user at a time),
	 * this implementation locks <b>only</b> the short sections of code that touch shared variables.
	 * </p>
	 * <p>
	 * <b>How it works (The "Open Sandwich" Pattern):</b>
	 * The method is split into three distinct parts:
	 * <ol>
	 * <li><b>Synchronized Block 1 (Fast):</b> We acquire the lock briefly to check the cache and update statistics.
	 * If we find a match (Cache Hit), we grab the data and release the lock immediately.</li>
	 * <li><b>The Expensive Calculation (Slow & Unlocked):</b> If we missed the cache, we call {@code factor(i)}.
	 * Crucially, this happens <b>outside</b> any synchronized block. This means 100 threads can calculate
	 * factors for 100 different numbers simultaneously without blocking each other.</li>
	 * <li><b>Synchronized Block 2 (Fast):</b> Once the calculation is done, we re-acquire the lock briefly
	 * to update the cache ({@code lastNumber} and {@code lastFactors}) with the new result.</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>The Result:</b>
	 * We achieve <b>Thread Safety</b> (no race conditions on the counters or cache) without sacrificing
	 * <b>Liveness</b> (performance). The "Fast Lane" is guarded, but the "Slow Lane" is open to everyone.
	 * </p>
	 *
	 * @param req  The servlet request
	 * @param resp The servlet response
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = null;

		synchronized (this) {
			++hits;
			if (i.equals(lastNumber)) {
				++cacheHits;
				factors = lastFactors;
			}
			System.out.println("Hits: " + hits + ", Cache Hits: " + cacheHits);
		}

		if (factors == null) {
			factors = factor(i);
			synchronized (this) {
				lastNumber = i;
				lastFactors = factors;
			}
			System.out.println("Number: " + i + ", New Factors: " + getFactorsArrString(factors));
		} else {
			System.out.println("Number: " + i + ", Cached Factors: " + getFactorsArrString(factors));
		}

		encodeIntoResponse(resp, factors);
	}
}
