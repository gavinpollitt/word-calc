package uk.gav;

import uk.gav.expression.ExpressionHolder.Node;

public class OperatorFactory {
	
	private final static Calc MULT = (l,r) -> new NumberString(l.toDigit() * r.toDigit());
	private final static Calc DIV = (l,r) -> new NumberString(l.toDigit() / r.toDigit());
	private final static Calc PLUS = (l,r) -> new NumberString(l.toDigit() + r.toDigit());
	private final static Calc MINUS = (l,r) -> new NumberString(l.toDigit() - r.toDigit());
	private final static Calc MOD = (l,r) -> new NumberString(l.toDigit()%r.toDigit());
		
	public static NodeEvaluator getInstance(String op) {
		switch (op) {
		case "*":
			return new Multiply();
		case "/":
			return new Divide();
		case "+":
			return new Add();
		case "-":
			return new Subtract();
		case "%":
			return new Mod();
		default:
			throw new IllegalArgumentException("Invalid Operator " + op);
		}
	}
	
	private static class Multiply implements NodeEvaluator {
		private Multiply() {
			
		}

		@Override
		public Evaluator getEvaluator(final Node n) {
			// TODO Auto-generated method stub
			return () -> MULT.eval(n.getLeft().process(), n.getRight().process());
		}

	}
	
	private static class Divide implements NodeEvaluator {
		private Divide() {
			
		}

		@Override
		public Evaluator getEvaluator(final Node n) {
			// TODO Auto-generated method stub
			return () -> DIV.eval(n.getLeft().process(), n.getRight().process());
		}

	}
	
	private static class Add implements NodeEvaluator {
		
		private Add() {
			
		}

		@Override
		public Evaluator getEvaluator(final Node n) {
			// TODO Auto-generated method stub
			return () -> PLUS.eval(n.getLeft().process(), n.getRight().process());
		}

	}
	
	private static class Subtract implements NodeEvaluator {
		
		private Subtract() {
			
		}

		@Override
		public Evaluator getEvaluator(final Node n) {
			// TODO Auto-generated method stub
			return () -> MINUS.eval(n.getLeft().process(), n.getRight().process());
		}

	}
	
	private static class Mod implements NodeEvaluator {
		
		private Mod() {
			
		}

		@Override
		public Evaluator getEvaluator(final Node n) {
			// TODO Auto-generated method stub
			return () -> MOD.eval(n.getLeft().process(), n.getRight().process());
		}

	}
	
	@FunctionalInterface
	private static interface Calc {
		public NumberString eval(NumberString l, NumberString r);
	}

}
