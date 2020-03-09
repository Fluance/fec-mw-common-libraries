/**
 * 
 */
package net.fluance.commons.codec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	private MD5Utils() {}

	public static final int HEXSTRING_COMPUTE_METHOD_1 = 1;
	public static final int HEXSTRING_COMPUTE_METHOD_2 = 2;

	/**
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static final byte[] md5Hash(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(bytes);
		return messageDigest.digest();
	}

	/**
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static final byte[] md5Hash(String str) throws NoSuchAlgorithmException {
		return md5Hash(str.getBytes());
	}

	/**
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static final String md5HexHash(String str, int computationMedhod) throws NoSuchAlgorithmException {
		byte[] digest = md5Hash(str);
		if (HEXSTRING_COMPUTE_METHOD_2 == computationMedhod) {
			return hexStringMethod2(digest);
		}
		return hexStringMethod1(digest);
	}

	/**
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static final String md5HexHash(String str) throws NoSuchAlgorithmException {
		return md5HexHash(str, HEXSTRING_COMPUTE_METHOD_1);
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private static final String hexStringMethod1(byte[] bytes) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			hexString.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return hexString.toString();
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private static final String hexStringMethod2(byte[] bytes) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xff & bytes[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
