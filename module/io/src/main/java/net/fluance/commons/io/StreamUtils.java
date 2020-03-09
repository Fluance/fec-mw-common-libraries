/**
 * 
 */
package net.fluance.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StreamUtils {

	private static Logger LOGGER = LogManager.getLogger(StreamUtils.class);

	/**
	 * Converts an InputStream into the corresponding String
	 * 
	 * @param inputStream
	 *            The stream from which we would like to get the corresponding String
	 * @return The String captured from inputStream
	 */
	public static String inputStreamToString(InputStream inputStream) {
		BufferedReader bufferReader = null;
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try {
			bufferReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = bufferReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		return stringBuilder.toString();
	}
}
