package uk.gav;


import uk.gav.expression.ExpressionHolder.Node;

/**
 * Class to provide the necessary conversions between digitised and language-based numerics.
 * @author regen
 *
 */
public class NumberString implements NodeEvaluator {

	private final static String[] UNITS = new String[] {"ZERO","ONE","TWO","THREE","FOUR","FIVE","SIX","SEVEN","EIGHT","NINE"};
	private final static String[] TEENS = new String[] {"TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"};
	private final static String[] TENS = new String[] {"TWENTY","THIRTY","FORTY","FIFTY","SIXTY","SEVENTY", "EIGHTY", "NINETY"};
	
	private final int 		intVal;
	private final String	stringVal;
	
	public NumberString(final int intVal) {
		this.intVal = intVal;
		this.stringVal = convertToString(intVal);
	}
	
	public NumberString(final String stringVal) {
		this.stringVal = stringVal;
		this.intVal = convertToNumber(stringVal);
	}
	
	public int toDigit() {
		return this.intVal;
	}
	
	public String toString() {
		return this.stringVal;
	}
	
	@Override
	public Evaluator getEvaluator(Node n) {
		return () -> this;
	}

	/**
	 * 
	 * @param addNum The String version of a number (potentially with leading sign)
	 * @return integer equivalent
	 */
	public static int convertToNumber(String addNum)  {
		boolean hasSign = false;
		//Check for negative number
		if (addNum.equals("-1")) {
			return -1;
		}
		else if (addNum.startsWith("MINUS ")) {
			addNum = addNum.replace("MINUS ", "");
			hasSign = true;
		}
		
		String num = addNum.toUpperCase();
		int foundNum = -1;
		boolean fn = false;
		
		for (int i = 0; i < TENS.length && !fn; i++) {
			if (num.startsWith(TENS[i])) {
				fn = true;
				foundNum = (i+2)*10;
				num = num.replaceFirst(TENS[i], "").trim();
			}
		}
		
		if (fn) {
			if (num.length() > 0) {
				fn = false;
				for (int i = 1; i < UNITS.length && !fn; i++) {
					if (num.startsWith(UNITS[i])) {
						fn = true;
						foundNum += i;
						num = num.replaceFirst(UNITS[i], "");
					}
				}
				
			}
		}
		else {
			for (int i = 0; i < TEENS.length && !fn; i++) {
				if (num.startsWith(TEENS[i])) {
					fn = true;
					foundNum = i+10;
					num = num.replaceFirst(TEENS[i], "");
				}
			}
			
			if (!fn) {
				for (int i = 0; i < UNITS.length && !fn; i++) {
					if (num.startsWith(UNITS[i])) {
						fn = true;
						foundNum = i;
						num = num.replaceFirst(UNITS[i], "");
					}
				}

			}
		}
		
		if (!fn || (fn && num.length() != 0)) {
			throw new IllegalArgumentException("Bad Number: " + addNum + " supplied");
		}
		
		return hasSign?-foundNum:foundNum;
	}
	
	/**
	 * 
	 * @param intVal The integer version of the number to convert to language format
	 * @return The word version of the provided integer
	 */
	public static String convertToString(int intVal)  {
		boolean hasSign = false;
		
		if (intVal < 0) {
			hasSign = true;
			intVal = intVal*-1;
		}
		else if (intVal > 99) {
			throw new IllegalArgumentException("Bad parameter:" + intVal +  " - Only values between 0 and 99 can be converted");
		}
		
		String rVal = "";
		
		if (intVal < 10) {
			rVal =  UNITS[intVal];
		}
		else if (intVal < 20) {
			rVal =  TEENS[intVal - 10];
		}
		else {
			int t = intVal/10 - 2;
			int r = intVal%10;
			
			rVal =   TENS[t] + (r > 0?(" " + UNITS[r]):"");
		}
		
		return hasSign?"MINUS " + rVal:rVal;
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("0 is: " + convertToString(0));
		System.out.println("5 is: " + convertToString(5));
		System.out.println("15 is: " + convertToString(15));
		System.out.println("55 is: " + convertToString(55));
		System.out.println("99 is: " + convertToString(99));
		
		System.out.println("ZERO is: " + convertToNumber("ZERO"));
		System.out.println("FIVE is: " + convertToNumber("FIVE"));
		System.out.println("FIFTEEN is: " + convertToNumber("FIFTEEN"));
		System.out.println("FIFTY FIVE is: " + convertToNumber("FIFTY FIVE"));
		System.out.println("NINETY NINE is: " + convertToNumber("NINETY NINE"));
		
		NumberString nI1 = new NumberString(66);
		NumberString ns1 = new NumberString("SIXTY SIX");
		
		NumberString nI2 = new NumberString(10);
		NumberString ns2 = new NumberString("TEN");
		
		System.out.println(nI1.toDigit() + " is: " + nI1.toString());
		System.out.println(ns1.toDigit() + " is: " + ns1.toString());
		
		System.out.println(nI2.toDigit() + " is: " + nI2.toString());
		System.out.println(ns2.toDigit() + " is: " + ns2.toString());

	}
}
