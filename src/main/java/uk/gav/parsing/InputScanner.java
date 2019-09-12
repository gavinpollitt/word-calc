package uk.gav.parsing;
import java.util.Set;

/**
 * Class providing capability to scan character by character through an expression string
 * @author regen
 *
 */
public class InputScanner {
	
	private String input;
	private int pos = 0;
	
	public InputScanner(String input) {
		this.input = input!=null?input:" ";
	}

	public char getCurrent() {
		if (this.pos < this.input.length()) {
			return this.input.charAt(this.pos);
		}
		else {
			return '\0';
		}
	}
	
	public Set<CurrentType> getCurrentType() {
		return CurrentType.identify(this.getCurrent());		
	}
	
	public Set<CurrentType> next() {
		this.pos += 1;
		return CurrentType.identify(this.getCurrent());
	}
	
	public Set<CurrentType> next(boolean skipWS) {
		this.next();
		if (skipWS) {
			skipWS();
		}
		return CurrentType.identify(this.getCurrent());
	}
	
	/**
	 * Skip any following whitespace before identifying next type
	 * @return
	 */
	public Set<CurrentType> skipWS() {
		Set<CurrentType> nextType = CurrentType.identify(this.getCurrent());

		while (this.hasNext() && nextType.size() == 1 && nextType.contains(CurrentType.SPACE)) {
			this.next();
			nextType = CurrentType.identify(this.getCurrent());
		}
		
		return nextType;
			
	}
	
	public boolean startsWith(final String s, final boolean skip) {
		boolean res = false;
		
		int l = s.length();
		if (s != null && l > 0 && (pos + s.length() <= input.length())) {
			int scanPos = pos;
			
			String sub = this.input.substring(scanPos,scanPos+l);
			
			res = sub.equalsIgnoreCase(s);
			
			pos=skip&&res?scanPos+l:pos;
			this.skipWS();
		}
		
		return res;
		
	}
	
	public boolean hasNext() {
		return this.pos < this.input.length();
	}
	
	public int getCurrentPosition() {
		return this.pos < this.input.length()?this.pos+1:this.input.length();
	}
	
	public String toString() {
		return this.input;
	}
	
	public static void main(String[] args) {
		InputScanner scan = new InputScanner("gav was ere");
		System.out.println("RESULT:" + scan.startsWith("gAV", true));
		scan.skipWS();
		System.out.println("RESULT:" + scan.startsWith("Was", false));		
		System.out.println("RESULT:" + scan.startsWith("wis", true));		
		System.out.println("RESULT:" + scan.startsWith("WAS", true));		
		scan.skipWS();
		System.out.println("RESULT:" + scan.startsWith("errrrrrrrrrrrrrr", false));		
		System.out.println("RESULT:" + scan.startsWith("e", true));
		System.out.println("LEFT WITH:" + scan.getCurrent());
	}
}

