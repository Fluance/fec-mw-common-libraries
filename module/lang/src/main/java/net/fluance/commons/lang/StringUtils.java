/**
 * 
 */
package net.fluance.commons.lang;

public class StringUtils {

	private StringUtils() {}
	/**
	 * 
	 * @param strToQuote
	 * @return
	 */
	public static String simpleQuote(String strToQuote) {
		return "'" + strToQuote + "'";
	}
	
	/**
	 * 
	 * @param strToQuote
	 * @return
	 */
	public static String simpleQuote(Character strToQuote) {
		return "'" + strToQuote + "'";
	}
	
	/**
	 * 
	 * @param strToQuote
	 * @return
	 */
	public static String doubleQuote(String strToQuote) {
		return "\"" + strToQuote + "\"";
	}
	
	/**
	 * 
	 * @param stringToChange
	 * @return
	 */
	public static String getRidOfCRLF(String stringToChange) {
		String lf = "%0D";
		String cr = "%0A";
		String now = lf;
		int index = stringToChange.indexOf(now);
		StringBuffer r = new StringBuffer();
		while (index!=-1) {
			r.append(stringToChange.substring(0,index));
			stringToChange = stringToChange.substring(index+3,stringToChange.length());
			if (now.equals(lf)) {
				now = cr;
			} else {
				now = lf;
			}
			index = stringToChange.indexOf(now);
		}
		return r.toString();
	}
	
}
