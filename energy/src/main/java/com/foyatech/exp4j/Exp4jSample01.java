package com.foyatech.exp4j;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.CustomOperator;
import de.congrace.exp4j.ExpressionBuilder;

public class Exp4jSample01 {
	public static void main(final String[] args) throws Exception {
		final Calculable calc = new ExpressionBuilder("1 > 1").withOperation(new CustomOperator(">") {

			@Override
			protected double applyOperation(final double[] args) {
				return args[0] + args[1];
			}

		}).build();

		System.out.println(calc.calculate()); // imprime "2.0"
	}
}
