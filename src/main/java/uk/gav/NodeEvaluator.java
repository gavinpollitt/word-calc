package uk.gav;

import uk.gav.expression.ExpressionHolder.Node;

/**
 * Specification for classes providing node evaluations
 * @author regen
 *
 */
@FunctionalInterface
public interface NodeEvaluator {
	public Evaluator getEvaluator(Node n);
}
