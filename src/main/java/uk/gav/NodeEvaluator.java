package uk.gav;

import uk.gav.expression.ExpressionHolder.Node;

public interface NodeEvaluator {
	public Evaluator getEvaluator(Node n);
}
