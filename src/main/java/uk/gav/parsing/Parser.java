package uk.gav.parsing;

import java.util.Set;

import uk.gav.NumberString;
import uk.gav.expression.ExpressionHolder;

/**
 * Class responsible for taking a string expression a producing the expression tree based on the syntax model
 * @author regen
 *
 */
public class Parser {

	private InputScanner s;

	public static void main(String[] args) throws Exception {
		eval("-  TWO    *    (-  30+FIVE         *(8%   THREE))");
		eval("8 + EIGHT * EIGHT / 4 - 16");
		eval("-(EIGHTY EIGHT/11 - SEVEN)");
		eval("TWO");
		eval("+    TWO");
		eval("-TWO");
		eval("EIGHT/4");
		eval("TWO*(-30 + (FIVE*8)%THREE)");
		eval("MINUS 99/9");
		eval("TEN * NINE + NINE");
		eval("TEN * MINUS NINE - PLUS NINE");
		eval("(SIX + TEN)*(FOUR-FOUR)");
		eval("(SIX + TEN)(FOUR-FOUR)");
		eval("NINETY NINE * (FOUR % 2)");
		eval("99/0");
	}
	
	/**
	 * Evaluate the provided expression string
	 * @param exp
	 * @throws Exception
	 */
	private static void eval(String exp) throws Exception {
		Parser p = new Parser(exp);
		ExpressionHolder eh = p.parse();
		System.out.println(eh.toString());
		System.out.println("Result::" + eh.solve());		
	}

	/**
	 * Create a new parser instance based on the expression supplied
	 * @param exp
	 * @throws InvalidExpression
	 */
	public Parser(String exp) throws InvalidExpression {
		this.s = new InputScanner((exp == null || exp.length() == 0) ? " " : exp);
	}
	

	/** 
	 * Perform the parsing of the expression from the root node down
	 * @return The encompasssing expression holder
	 * @throws Exception
	 */
	public ExpressionHolder parse() throws Exception {

		ExpressionHolder eh = processExpression();

		if (s.hasNext()) {
			throw new InvalidExpression(s);
		}
		
		return eh;

	}

	/**
	 * 
	 * @return The expression at this level
	 * @throws InvalidExpression
	 */
	public ExpressionHolder processExpression() throws InvalidExpression {
		ExpressionHolder eh = processTerm();

		Set<CurrentType> nextType = this.s.skipWS();

		if (nextType.contains(CurrentType.ADD)) {
			String op = this.s.getCurrent() + "";
			nextType = this.s.next(true);

			ExpressionHolder curEh = eh;
			eh = ExpressionHolder.getInstance().addOp(op);
			eh = eh.addLeft(curEh);
			eh.addRight(processExpression());
		}
		
		return eh;
	}

	public ExpressionHolder processTerm() throws InvalidExpression {

		this.s.skipWS();

		ExpressionHolder eh = processSubTerm();

		Set<CurrentType> nextType = this.s.getCurrentType();
		
		if (nextType.contains(CurrentType.PROD) || nextType.contains(CurrentType.MOD)) {
			String op = this.s.getCurrent() + "";
			
			if (op.equals("(")) {
				op = "*";
			}
			else {
				this.s.next(true);
			}
			
			ExpressionHolder curEh = eh;
			eh = ExpressionHolder.getInstance().addOp(op);
			eh = eh.addLeft(curEh);
			eh.addRight(processTerm());
		}

		return eh;
	}

	public ExpressionHolder processSubTerm() throws InvalidExpression {
		ExpressionHolder eh = null;

		Set<CurrentType> nextType = this.s.skipWS();

		boolean signActive = this.hasSign();
		
		nextType = this.s.getCurrentType();
		
		if (nextType.contains(CurrentType.NUMBER_STRING) || nextType.contains(CurrentType.NUMBER)) {
			eh = processNumber();
		} else if (nextType.contains(CurrentType.OPEN_BRACKET)) {
			eh = processBracket();
		} else {
			throw new InvalidExpression(s);
		}
		
		if (signActive) {
			eh.negate();
		}
		
		return eh;
	}

	public ExpressionHolder processBracket() throws InvalidExpression {
		this.s.next();

		ExpressionHolder eh = processExpression();

		this.s.skipWS();

		if (!isSingleType(CurrentType.CLOSE_BRACKET)) {
			throw new InvalidExpression(s);
		} else {
			this.s.next(true);
		}

		return eh;

	}

	public ExpressionHolder processNumber() {
		Set<CurrentType> nextType = getCurrentType();
		final CurrentType numberType = nextType.contains(CurrentType.NUMBER) ? CurrentType.NUMBER
				: CurrentType.NUMBER_STRING;

		// Process the number
		boolean active = true;
		boolean spaceFound = false;

		String numString = "";
		String sep = "";

		while (active) {
			numString += (sep + s.getCurrent());
			sep = "";

			nextType = s.next();

			if (isSingleType(CurrentType.SPACE) && !spaceFound && numberType == CurrentType.NUMBER_STRING) {
				nextType = s.next();
				spaceFound = true;
				sep = " ";
			}

			if (!nextType.contains(numberType)) {
				active = false;
			}
		}

		if (numberType == CurrentType.NUMBER) {
			numString = NumberString.convertToString(Integer.parseInt(numString));
		}
		
		this.s.skipWS();
		return ExpressionHolder.getInstance().addLeaf(numString);

	}

	private boolean hasSign() {
		boolean signActive = false;
		Set<CurrentType> nextType = this.s.skipWS();
		
		if (nextType.contains(CurrentType.SIGN)) {
			if (this.s.getCurrent() == '-') {
				signActive = true;
				this.s.next(true);
			}
			else if (this.s.startsWith("MINUS", true)) {
				signActive = true;
			}
			else if (this.s.getCurrent() == '+') {
				this.s.next(true);
			}
			else  {
				this.s.startsWith("PLUS", true);
			}
		}		
		
		return signActive;
	}
	
	private boolean isSingleType(CurrentType t) {
		Set<CurrentType> types = getCurrentType();

		return types.size() == 1 && types.contains(t);
	}

	private Set<CurrentType> getCurrentType() {
		return CurrentType.identify(s.getCurrent());
	}

}
