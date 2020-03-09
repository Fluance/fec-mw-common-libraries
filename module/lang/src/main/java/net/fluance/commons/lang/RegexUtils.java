/**
 * 
 */
package net.fluance.commons.lang;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegexUtils {
	
	private static Logger LOGGER = LogManager.getLogger(RegexUtils.class);
	
	/**
	 * 
	 * @param patternStr
	 * @param matcherStr
	 * @return
	 */
	public static boolean matches(String patternStr, String matcherStr) {
		Pattern pattern = Pattern.compile(patternStr);
		return pattern.matcher(matcherStr).matches();
	}

	/**
	 * 
	 * @param patternStr
	 * @param matcherStrs
	 * @return
	 */
	public static boolean matches(String patternStr, List<String> matcherStrs) {
		if(matcherStrs == null || matcherStrs.isEmpty()) {
			throw new IllegalArgumentException("Matchers cannot be null or empty");
		}
		boolean matches = true;
		Pattern pattern = Pattern.compile(patternStr);
		Iterator<String> matchersIter = matcherStrs.iterator();
		while (matchersIter.hasNext() && matches )
		for(String matcherStr: matcherStrs) { 
			matches = matches && pattern.matcher(matcherStr).matches();
		}
		return matches;
	}

	/**
	 * 
	 * @param patternStrs
	 * @param matcherStr
	 * @return
	 */
	public static boolean matches(List<String> patternStrs, String matcherStr) {
		if(matcherStr == null) {
			String msg = "Matcher cannot be null";
			LOGGER.error(msg);
			throw new IllegalArgumentException(msg);
		}
		boolean matches = false;
		Iterator<String> patternStrsIter = patternStrs.iterator();
		while (patternStrsIter.hasNext() && !matches ) {
			Pattern pattern = Pattern.compile(patternStrsIter.next());
			matches = matches || pattern.matcher(matcherStr).matches();
		}
		return matches;
	}
	
	/**
	 * 
	 * @param patternStr
	 * @param matcherStrs
	 * @return
	 */
	public static boolean matchesAll(String patternStr, List<String> matcherStrs) {
		if(matcherStrs == null || matcherStrs.isEmpty()) {
			throw new IllegalArgumentException("Matchers cannot be null or empty");
		}
		boolean matches = false;
		Pattern pattern = Pattern.compile(patternStr);
		Iterator<String> matchersIter = matcherStrs.iterator();
		while (matchersIter.hasNext() && !matches) {
			for(String matcherStr: matcherStrs) { 
				matches = matches || pattern.matcher(matcherStr).matches();
			}
		}
		return matches;
	}
	
}
