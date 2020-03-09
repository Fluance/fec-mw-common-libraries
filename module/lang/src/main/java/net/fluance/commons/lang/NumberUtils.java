/**
 * 
 */
package net.fluance.commons.lang;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NumberUtils {

	private static Logger LOGGER = LogManager.getLogger(NumberUtils.class);

	/**
	 * 
	 * @param string
	 * @param radix
	 * @return
	 */
	public static boolean isInteger(String string, int radix) {
		if (string.isEmpty()) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (i == 0 && string.charAt(i) == '-') {
				if (string.length() == 1) {
					return false;
				} else {
					continue;
				}
			}
			if (Character.digit(string.charAt(i), radix) < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		boolean isInteger = false;
		try {
			int idInt = Integer.parseInt(str);
			isInteger = idInt > 0 || idInt <= 0;
		} catch (NumberFormatException exc) {
		}
		return isInteger;
	}
}
