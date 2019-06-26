package uk.gav.parsing;

public class InvalidExpression extends Exception {
	private String mess;
	
	public InvalidExpression(InputScanner s) {
		String ex = "Invalid expression supplied:\n"+s.toString()+"\n";
		int errPos = s.getCurrentPosition() - 1;
		ex += String.format("%" + (errPos<1?1:errPos+1) + "s","^");
		this.mess = ex;
	}
	
	public String toString() {
		return this.mess;
	}
	
	public static void main(String[] args) {
		InputScanner scanner = new InputScanner("test exception");
		
		scanner.next();
		scanner.next();
		scanner.next();
		scanner.next();

		try {
			throw new InvalidExpression(scanner);
		}
		catch (InvalidExpression e) {
			System.out.println(e);
		}
	}

}
