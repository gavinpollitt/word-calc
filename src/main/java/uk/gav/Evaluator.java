package uk.gav;

/**
 * Functional Interface for operator implementations
 * @author regen
 *
 */
@FunctionalInterface
public interface Evaluator {
	public NumberString evaluate();
}
