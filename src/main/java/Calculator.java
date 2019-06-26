import java.util.Scanner;

import uk.gav.expression.ExpressionHolder;
import uk.gav.parsing.InvalidExpression;
import uk.gav.parsing.Parser;

public class Calculator {

	private final Scanner scanner;

	private Calculator() {
		this.scanner = new Scanner(System.in);
		
		System.out.println("***********************************");
		System.out.println("    Welcome to Word Calculator");
		System.out.println("***********************************\n");
	}

	public static void main(String[] args) {
		Calculator c = new Calculator();
		
		boolean isActive = true;
		
		do {
			ExpressionHolder eh = c.readInput();
			
			if (eh == null) {
				isActive = false;
			}
			else {
				System.out.println("Expression::" + eh.toString());
				
				try {
					System.out.println("Result::" + eh.solve());
					System.out.println();
				}
				catch (Exception e) {
					System.out.println("ERROR:" + e.getMessage());
					System.out.println();
				}				
			}
		} while (isActive);
		
		System.out.println();
		System.out.println("Bye!");
	}

	private ExpressionHolder readInput() {
		ExpressionHolder exp = null;

		boolean validInput = false;

		do {
			System.out.print("->");
			String line = scanner.nextLine();

			if (!line.equalsIgnoreCase("Q")) {

				try {
					exp = new Parser(line).parse();
					validInput = true;
				}
				catch (Exception e) {
					System.out.println("ERROR:" + e);
					System.out.println();
				}
			}
			else {
				validInput = true;
			}

		} while (!validInput);

		return exp;
	}

}
