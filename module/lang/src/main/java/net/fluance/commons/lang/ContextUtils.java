/**
 * 
 */
package net.fluance.commons.lang;

public class ContextUtils {
	private ContextUtils() {}
	
	/**
	 * Gives the name of the caller method
	 * @param stackTraceElements
	 * @return
	 */
	public static String thisMethodName(StackTraceElement stackTraceElements[]) {
		String methodName = null;
		boolean doNext = false;
		for (StackTraceElement traceElement : stackTraceElements) {
			if (doNext) {
				methodName = traceElement.getMethodName();
			}
			doNext = traceElement.getMethodName().equals("getStackTrace");
		}
		return methodName;
	}

}
