/**
 * 
 */
package net.fluance.commons.codec;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {

	private Base64Utils() {}

	public static String encode(byte[] stringBytes) {
		byte[] encodedBytes = Base64.encodeBase64(stringBytes);
		return new String(encodedBytes);
	}

	public static String encode(String str) {
		return encode(str.getBytes());
	}

	@Deprecated
	public static boolean isBase64(byte[] stringBytes) {
		return Base64.isBase64(stringBytes);
	}

	@Deprecated
	public static boolean isBase64(String str) {
		return Base64.isBase64(str);
	}

	@Deprecated
	public static byte[] decode(byte[] stringBytes) {
		return Base64.decodeBase64(stringBytes);
	}

	public static String base64UrlDecode(String input) {
		Base64 decoder = new Base64(true);
		byte[] decodedBytes = decoder.decode(input);
		return new String(decodedBytes);
	}
}
