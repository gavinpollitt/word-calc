package uk.gav;

import java.util.HashMap;
import java.util.Map;

/**
 * Holding class for the various operator functions supported
 * @author regen
 *
 */
public class OperatorFactory {
	
	private final static Map<String, NodeEvaluator> Evaluators = new HashMap<>();
	
	private final static Calc MULT = (l,r) -> new NumberString(l.toDigit() * r.toDigit());
	private final static Calc DIV = (l,r) -> new NumberString(l.toDigit() / r.toDigit());
	private final static Calc PLUS = (l,r) -> new NumberString(l.toDigit() + r.toDigit());
	private final static Calc MINUS = (l,r) -> new NumberString(l.toDigit() - r.toDigit());
	private final static Calc MOD = (l,r) -> new NumberString(l.toDigit()%r.toDigit());
	
	private final static NodeEvaluator MULTIPLY = n -> () -> MULT.eval(n.getLeft().process(), n.getRight().process());
	private final static NodeEvaluator DIVIDE = n -> () -> DIV.eval(n.getLeft().process(), n.getRight().process());
	private final static NodeEvaluator ADD = n -> () -> PLUS.eval(n.getLeft().process(), n.getRight().process());
	private final static NodeEvaluator SUBTRACT = n -> () -> MINUS.eval(n.getLeft().process(), n.getRight().process());
	private final static NodeEvaluator MODULO = n -> () -> MOD.eval(n.getLeft().process(), n.getRight().process());
		
	static {
		Evaluators.put("*", MULTIPLY);
		Evaluators.put("/", DIVIDE);
		Evaluators.put("+", ADD);
		Evaluators.put("-", SUBTRACT);
		Evaluators.put("%", MODULO);
	}
	
	public static NodeEvaluator getInstance(final String op) {
		NodeEvaluator ne = Evaluators.get(op);
		
		if (ne == null) {
			throw new IllegalArgumentException("Invalid Operator " + op);
		}
		
		return ne;
	}
		
	@FunctionalInterface
	private static interface Calc {
		public NumberString eval(NumberString l, NumberString r);
	}

}
