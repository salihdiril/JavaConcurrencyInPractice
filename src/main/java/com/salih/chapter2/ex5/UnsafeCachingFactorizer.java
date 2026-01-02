package com.salih.chapter2.ex5;

import static com.salih.chapter2.FactorizerUtil.encodeIntoResponse;
import static com.salih.chapter2.FactorizerUtil.extractFromRequest;
import static com.salih.chapter2.FactorizerUtil.factor;
import static com.salih.chapter2.FactorizerUtil.getFactorsArrString;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/factor/unsafeCaching")
public class UnsafeCachingFactorizer extends HttpServlet {
	private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
	private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();

	/**
	 * A <b>Non-Thread-Safe</b> Servlet that demonstrates a Race Condition involving multiple state variables.
	 * <p>
	 * <b>The Paradox:</b>
	 * This class uses {@link java.util.concurrent.atomic.AtomicReference} for both {@code lastNumber}
	 * and {@code lastFactors}. Since "Atomics are thread-safe", one might assume this class is safe.
	 * <b>It is not.</b>
	 * </p>
	 * <p>
	 * <b>The Invariant (The Rule):</b>
	 * This class has a logical rule (invariant) that must always hold true:
	 * <br><i>"The value in {@code lastFactors} MUST be the mathematical factors of {@code lastNumber}."</i>
	 * </p>
	 * <p>
	 * <b>The Race Condition:</b>
	 * The failure occurs because we update these two related variables in separate steps.
	 * This leaves a brief "window of vulnerability" where the invariant is broken.
	 * </p>
	 * <p>
	 * <b>The "Crime Scene" (Step-by-Step):</b>
	 * Valid State: {@code lastNumber=102}, {@code lastFactors=[2, 3, 17]}
	 * <ol>
	 * <li><b>Thread A</b> processes 103. It calls {@code lastNumber.set(103)}.</li>
	 * <li><b>Thread A</b> is paused by the OS (Context Switch). ⏸️</li>
	 * <li><b>State of World:</b> {@code lastNumber=103}, but {@code lastFactors=[2, 3, 17]} (Factors of 102!). <b>BROKEN INVARIANT.</b></li>
	 * <li><b>Thread B</b> arrives with request for 103.</li>
	 * <li><b>Thread B</b> checks {@code if (i.equals(lastNumber))}. It sees 103. The check passes!</li>
	 * <li><b>Thread B</b> returns {@code lastFactors.get()}. It returns the factors of 102. 💥</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>The Lesson:</b>
	 * Thread safety is not transitive. Even if individual components are thread-safe,
	 * the <i>composite action</i> of updating them together must be atomic.
	 * </p>
	 *
	 * @param req  The servlet request
	 * @param resp The servlet response
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BigInteger i = extractFromRequest(req);
		final BigInteger lastNum = lastNumber.get();
		if (i.equals(lastNum)) {
			final BigInteger[] factors = lastFactors.get();
			System.out.println("Number: " + lastNum + ", Cached Factors: " + getFactorsArrString(factors));
			encodeIntoResponse(resp, factors);
		} else {
			BigInteger[] factors = factor(i);
			lastNumber.set(i);
			lastFactors.set(factors);
			System.out.println("Number: " + i + ", New Factors: " + getFactorsArrString(factors));
			encodeIntoResponse(resp, factors);
		}
	}
}
