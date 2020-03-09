/**
 * 
 */
package net.fluance.commons.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class DeflateUtils {

	private DeflateUtils() {}

	public static ByteArrayOutputStream deflate(String logoutRequest) throws IOException {
		ByteArrayOutputStream deflatedBytes = new ByteArrayOutputStream();
		Deflater deflater = new Deflater(Deflater.DEFLATED, true);
		DeflaterOutputStream deflaterStream = new DeflaterOutputStream(deflatedBytes, deflater);
		deflaterStream.write(logoutRequest.getBytes());
		deflaterStream.finish();
		return deflatedBytes;
	}
}
