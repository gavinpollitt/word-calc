package uk.gav.expression;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import uk.gav.Evaluator;
import uk.gav.NumberString;
import uk.gav.OperatorFactory;
import uk.gav.parsing.CurrentType;

public class ExpressionHolder {
	private static Set<String> IGNORE_OPS = new HashSet<>(Arrays.asList("*","X"));
	
	private Node root;

	public static ExpressionHolder getInstance() {
		return new ExpressionHolder();
	}

	private ExpressionHolder() {
	}

	public ExpressionHolder addLeft(ExpressionHolder eh) {
		this.root.left = eh.root;
		return this;
	}

	public ExpressionHolder addRight(ExpressionHolder eh) {
		this.root.right = eh.root;
		return this;
	}

	public ExpressionHolder addOp(String op) {
		root = new OpNode(op);
		return this;
	}

	public ExpressionHolder addLeaf(String leafVal) {
		root = new LeafNode(leafVal);
		return this;
	}

	public ExpressionHolder negate() {
		Node l = new LeafNode("-1");
		Node o = new OpNode("*");
		o.left = l;
		o.right = this.root;
		this.root = o;

		return this;
	}
	
	public NumberString solve() {
		return this.root.evaluator.evaluate();
	}

	public String toString() {
		if (root != null) {
			return toString(root);
		} else {
			return "";
		}
	}

	private static boolean isNegtive(Node n) {
		return (n.getValue().equals("*") && n.getLeft() != null && n.getLeft().value.equals("-1"));

	}

	private static boolean bracketRequired(Node parent, Node child) {
		boolean br = false;
		
		Set<CurrentType> tp = CurrentType.identify(parent.getValue().charAt(0));
		
		if (child != null && (isNegtive(parent) || tp.contains(CurrentType.PROD))) {
			//Now check child
			tp = CurrentType.identify(child.getValue().charAt(0));
			
			if (tp.contains(CurrentType.ADD) || tp.contains(CurrentType.MOD)) {
				br = true;
			}
		}
				
		return br;
		
	}
	
	private static String toString(Node n) {
		String ss = "";
		boolean bracket = false;

		if (!isNegtive(n)) {
			if (n.getLeft() != null) {
				boolean innerB = bracketRequired(n, n.getLeft());
				if (innerB) {
					ss += "(";					
				}
				ss += toString(n.getLeft());
				if (innerB) {
					ss += ")";					
				}
			}
			
			if (bracketRequired(n, n.getRight())) {
				ss += (IGNORE_OPS.contains(n.getValue())?"":n.getValue());
			}
			else {
				ss += n.getValue();
			}
		}
		else {
			ss += "MINUS ";
			
//			if (n.getRight() != null && n.getRight() instanceof OpNode) {
//				bracket = true;
//				ss += "(";
//			}
		}
		
		if (n.getRight() != null) {
			boolean innerB = bracketRequired(n, n.getRight());
			if (innerB) {
				ss += "(";					
			}
			ss += toString(n.getRight());
			if (innerB) {
				ss += ")";					
			}
		}
		
//		if (bracket) {
//			ss += ")";
//		}

		return ss;
	}

	public static class LeafNode extends Node {
		public LeafNode(final String v) {
			this.value = v;
			this.evaluator = new NumberString(v).getEvaluator(this);
			this.left = null;
			this.right = null;
		}
	}

	public static class OpNode extends Node {
		public OpNode(String o) {
			if (o.equals("X")) {
				o = "*";
			}
			this.value = o;
			this.evaluator = OperatorFactory.getInstance(o).getEvaluator(this);
		}
	}

	public abstract static class Node {
		protected String value;
		protected Node left;
		protected Node right;
		protected Evaluator evaluator;

		public NumberString process() {
			return this.evaluator.evaluate();
		}

		public String getValue() {
			return this.value;
		}

		public Node getLeft() {
			return left;
		}

		public void setLeft(Node left) {
			this.left = left;
		}

		public Node getRight() {
			return right;
		}

		public void setRight(Node right) {
			this.right = right;
		}

		public Evaluator getEvaluator() {
			return this.evaluator;
		}
	}

}
