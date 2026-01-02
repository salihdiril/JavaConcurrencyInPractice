package com.salih.chapter2.ex1;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/factor")
public class StatelessFactorizer extends HttpServlet {
	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
		BigInteger i = extractFromRequest(servletRequest);
		BigInteger[] factors = factor(i);
		encodeIntoResponse(servletResponse, factors);
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		String numParam = req.getParameter("number");
		if (numParam == null || numParam.isEmpty()) {
			return BigInteger.ONE;
		}

		try {
			return new BigInteger(numParam);
		} catch (NumberFormatException e) {
			return BigInteger.ONE;
		}
	}

	private BigInteger[] factor(BigInteger i) {
		List<BigInteger> factors = new ArrayList<>();
		long n = i.longValue();

		for (int j = 2; j <= n; j++) {
			while (n % j == 0) {
				factors.add(BigInteger.valueOf(j));
				n /= j;
			}
		}

		return factors.toArray(new BigInteger[0]);
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) throws IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		out.print("Factors: ");
		for (BigInteger factor : factors) {
			out.print(factor + " ");
		}
		out.println();
	}
}
