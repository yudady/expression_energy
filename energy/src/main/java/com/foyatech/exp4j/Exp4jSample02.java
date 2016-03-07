package com.foyatech.exp4j;

import java.util.ArrayList;
import java.util.Collection;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.CustomOperator;
import de.congrace.exp4j.ExpressionBuilder;

public class Exp4jSample02 {
	public static void main(final String[] args) throws Exception {
		final Collection<CustomOperator> operList = new ArrayList<CustomOperator>();

		operList.add(new CustomOperator(">") {

			@Override
			protected double applyOperation(double[] args) {
				return args[0] + args[1];
			}

		});

		final Calculable calc = new ExpressionBuilder("1 > 1").withOperations(operList).build();

		System.out.println(calc.calculate()); // imprime "2.0"
	}
}
