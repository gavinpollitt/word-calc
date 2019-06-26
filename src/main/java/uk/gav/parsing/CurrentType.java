package uk.gav.parsing;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CurrentType {

	SIGN("[\\+-MmPp]"),
	NUMBER("\\d"),
	NUMBER_STRING("[A-Za-z]"),
	PROD("[\\/\\*\\(]"),
	MOD("\\%"),
	ADD("[\\+-]"),
	OPEN_BRACKET("\\("),
	CLOSE_BRACKET("\\)"),
	SPACE(" ");
	
	private final Pattern identifier;
	
	CurrentType(String pattern) {
		this.identifier = Pattern.compile(pattern);
	}
	
	public static Set<CurrentType> identify(char c) {
		final HashSet<CurrentType> types = new HashSet<>();
		String cS = c + "";
		for (CurrentType t: CurrentType.values()) {
			Matcher m = t.identifier.matcher(cS);
			
			if (m.matches()) {
				types.add(t);
			}
		}
		
		return types;
	}
	
	public static void main(String[] args) {
		InputScanner scanner = new InputScanner("+(TEN  + 3)");
		
		while (scanner.hasNext()) {
			System.out.println(CurrentType.identify(scanner.getCurrent()));
			scanner.next();
		}
	}
}
