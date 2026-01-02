package com.salih.chapter2;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public enum FactorizerUtil {
	;

	public static BigInteger extractFromRequest(ServletRequest req) {
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

	public static BigInteger[] factor(BigInteger i) {
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

	public static void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) throws IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		out.print(getFactorsArrString(factors));
		out.println();
	}

	public static String getFactorsArrString(BigInteger[] factors) {
		StringBuilder sb = new StringBuilder();
		sb.append("Factors: ");
		for (BigInteger factor : factors) {
			sb.append(factor).append(" ");
		}
		sb.append("\n");
		return sb.toString();
	}
}
