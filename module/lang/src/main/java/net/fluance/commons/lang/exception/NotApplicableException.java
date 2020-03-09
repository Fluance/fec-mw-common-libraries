/**
 * 
 */
package net.fluance.commons.lang.exception;

public class NotApplicableException extends Exception {

	private static final long serialVersionUID = -7107900860702154302L;

	public NotApplicableException() {
	}

	public NotApplicableException(String message) {
		super(message);
	}

	
	public NotApplicableException(Throwable cause) {
		super(cause);
	}

	
	public NotApplicableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NotApplicableException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
