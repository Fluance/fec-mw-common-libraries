/**
 * 
 */
package net.fluance.commons.lang.exception;

@SuppressWarnings("serial")
public class GenericException extends Exception {

	/**
	 * 
	 */
	public GenericException() {
	}

	/**
	 * @param message
	 */
	public GenericException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GenericException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GenericException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GenericException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
