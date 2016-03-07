package com.foyatech.exp4j;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.CustomFunction;
import de.congrace.exp4j.ExpressionBuilder;

public class Exp4jSample03 {
	public static void main(final String[] args) throws Exception {
		final Calculable calc = new ExpressionBuilder("sample(1, 1)")
				.withCustomFunction(new CustomFunction("sample", 2) {

					@Override
					public double applyFunction(final double... args) {
						return args[0] + args[1];
					}

				}).build();

		System.out.println(calc.calculate()); // imprime "2.0"
	}
}
