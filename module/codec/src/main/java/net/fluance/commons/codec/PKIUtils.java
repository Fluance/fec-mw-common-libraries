/**
 * 
 */
package net.fluance.commons.codec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.security.cert.CertificateException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PKIUtils {

	private static Logger LOGGER = LogManager.getLogger(PKIUtils.class);
	public static final String DEFAULT_ALGORITHM = "RSA";
	public static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA1withRSA";
	public static final String DEFAULT_RNG_HASH_ALGORITHM = "SHA1PRNG";
	public static final String DEFAULT_PROVIDER = "SunRsaSign ";
	public static final int DEFAULT_KEY_SIZE = 2048;
	public static final String DEFAULT_CERTIFICATE_TYPE = "X.509";

	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] signData(byte[] data, PrivateKey key) throws Exception {
		return signData(data, key, DEFAULT_SIGNATURE_ALGORITHM);
	}

	/**
	 * 
	 * @param data
	 * @param key
	 * @param signatureAlgorithm
	 * @return
	 * @throws Exception
	 */
	public static byte[] signData(String data, PrivateKey key, String signatureAlgorithm) throws Exception {
		return signData(data.getBytes(), key, signatureAlgorithm);
	}

	/**
	 * 
	 * @param data
	 * @param key
	 * @param signatureAlgorithm
	 * @return
	 * @throws Exception
	 */
	public static byte[] signData(byte[] data, PrivateKey key, String signatureAlgorithm) throws Exception {
		Signature signer = Signature.getInstance(signatureAlgorithm);
		signer.initSign(key);
		signer.update(data);
		return (signer.sign());
	}

	/**
	 * 
	 * @param data
	 * @param key
	 * @param sig
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySignature(byte[] data, PublicKey key, byte[] sig) throws Exception {
		return verifySignature(data, key, sig, DEFAULT_SIGNATURE_ALGORITHM);
	}

	/**
	 * Verifies that this data was signed using the private key that corresponds to the specified public key.
	 * 
	 * @param data
	 * @param key
	 *            the PublicKey used to carry out the verification.
	 * @param sig
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySignature(byte[] data, PublicKey key, byte[] sig, String signatureAlgorithm) throws Exception {
		Signature signer = Signature.getInstance(signatureAlgorithm);
		signer.initVerify(key);
		signer.update(data);
		return (signer.verify(sig));
	}

	/**
	 * 
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static KeyPair generateKeyPair(long seed) throws Exception {
		return generateKeyPair(seed, DEFAULT_SIGNATURE_ALGORITHM, DEFAULT_RNG_HASH_ALGORITHM, DEFAULT_PROVIDER, DEFAULT_KEY_SIZE);
	}

	/**
	 * 
	 * @param seed
	 * @param keyEncryptAlgorithm
	 *            Example: DSA
	 * @param rngHashAlgorithm
	 *            Example: SHA1PRNG
	 * @param rngProvider
	 *            Example: SUN
	 * @param keySize
	 *            The number of bits to encode the key on
	 * @return
	 * @throws Exception
	 */
	public static KeyPair generateKeyPair(long seed, String signatureAlgorithm, String rngHashAlgorithm, String rngProvider, int keySize) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(signatureAlgorithm);
		SecureRandom rng = SecureRandom.getInstance(rngHashAlgorithm, rngProvider);
		rng.setSeed(seed);
		keyGenerator.initialize(keySize, rng);
		return keyGenerator.generateKeyPair();
	}

	/**
	 * Reads a Java keystore from a file.
	 * 
	 * @param keystoreFile
	 *            keystore file to read
	 * @param password
	 *            password for the keystore file
	 * @param keyStoreType
	 *            type of keystore, e.g., JKS or PKCS12
	 * @return the keystore object
	 * @throws KeyStoreException
	 *             if the type of KeyStore could not be created
	 * @throws IOException
	 *             if the keystore could not be loaded
	 * @throws NoSuchAlgorithmException
	 *             if the algorithm used to check the integrity of the keystore cannot be found
	 * @throws CertificateException
	 *             if any of the certificates in the keystore could not be loaded
	 * @throws java.security.cert.CertificateException
	 */
	public static KeyStore loadKeyStore(final File keystoreFile, final String password, final String keyStoreType)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, java.security.cert.CertificateException {
		if (null == keystoreFile) {
			throw new IllegalArgumentException("Keystore url may not be null");
		}
		final URI keystoreUri = keystoreFile.toURI();
		final URL keystoreUrl = keystoreUri.toURL();
		final KeyStore keystore = KeyStore.getInstance(keyStoreType);
		InputStream is = null;
		try {
			is = keystoreUrl.openStream();
			keystore.load(is, null == password ? null : password.toCharArray());
		} finally {
			if (null != is) {
				is.close();
			}
		}
		return keystore;
	}

	/**
	 * Read keypair from keystore
	 * 
	 * @param keystore
	 * @param alias
	 * @param password
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair readKeyPair(final KeyStore keystore, final String alias, final String password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		final Key key = (PrivateKey) keystore.getKey(alias, password.toCharArray());
		final Certificate cert = keystore.getCertificate(alias);
		final PublicKey publicKey = cert.getPublicKey();
		return new KeyPair(publicKey, (PrivateKey) key);
	}

	/**
	 * 
	 * @param keyStore
	 * @return
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableEntryException
	 */
	public static final Map<String, PublicKey> readPublicKeys(KeyStore keyStore) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {
		Map<String, PublicKey> keys = new HashMap<>();
		Enumeration<String> aliases = keyStore.aliases();
		while (aliases.hasMoreElements()) {
			String alias = aliases.nextElement();
			Entry entry = keyStore.getEntry(alias, null);
			if (entry instanceof TrustedCertificateEntry) {
				keys.put(alias, ((TrustedCertificateEntry) entry).getTrustedCertificate().getPublicKey());
			}
		}
		return keys;
	}

	/**
	 * 
	 * @param keyStoreFile
	 * @param password
	 * @param keyStoreType
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws java.security.cert.CertificateException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws UnrecoverableEntryException
	 */
	public static final Map<String, PublicKey> readPublicKeys(File keyStoreFile, String password, String keyStoreType)
			throws KeyStoreException, NoSuchAlgorithmException, java.security.cert.CertificateException, IOException, CertificateException, UnrecoverableEntryException {
		KeyStore keyStore = loadKeyStore(keyStoreFile, password, keyStoreType);
		Map<String, PublicKey> publicKeys = readPublicKeys(keyStore);
		return publicKeys;
	}

	/**
	 * 
	 * @param certificate
	 *            The certificate from where to load the certificate
	 * @param certType
	 *            The type of certificate (X.509 ...). If no value or empty String is provided, then X.509 will be
	 *            applied as default value.
	 * @return The public key enclosed in the certificate
	 * @throws KeyStoreException
	 * @throws SSLException
	 * @throws CertificateException
	 */
	public static final PublicKey readPublicKey(KeyStore keyStore, String certAlias, String certType) throws KeyStoreException, CertificateException {
		if (certAlias == null || certAlias.isEmpty() || keyStore == null) {
			return null;
		}
		Certificate certificate = keyStore.getCertificate(certAlias);
		String tmpCertiticateType = (certType == null || certType.isEmpty()) ? DEFAULT_CERTIFICATE_TYPE : certType;
		if (certificate == null) {
			throw new CertificateException("Could not load certificate with alias " + certAlias);
		}
		if (!certType.equalsIgnoreCase(certificate.getType())) {
			throw new KeyStoreException("The type of the actual certificate (" + certificate.getType() + ") does not match the requested one (" + certType + ")");
		}
		PublicKey publicKey = null;
		switch (tmpCertiticateType) {
			case DEFAULT_CERTIFICATE_TYPE:
				X509Certificate x509Certificate = (X509Certificate) certificate;
				publicKey = x509Certificate.getPublicKey();
				return publicKey;
			default:
				publicKey = ((X509Certificate) certificate).getPublicKey();
		}
		return publicKey;
	}

	/**
	 * 
	 * @param certificate
	 *            The certificate from where to load the certificate
	 * @param certType
	 *            The type of certificate (X.509 ...). If
	 * @return The public key enclosed in the certificate
	 * @throws KeyStoreException
	 * @throws SSLException
	 * @throws CertificateException
	 */
	public static final PublicKey readPublicKey(TrustedCertificateEntry trustedCert, String certType) throws KeyStoreException, CertificateException {
		Certificate certificate = trustedCert.getTrustedCertificate();
		String tmpCertiticateType = (certType == null || certType.isEmpty()) ? DEFAULT_CERTIFICATE_TYPE : certType;
		if (certificate == null) {
			throw new CertificateException("Could not load certificate");
		}
		if (!certType.equalsIgnoreCase(certificate.getType())) {
			throw new KeyStoreException("The type of the actual certificate (" + tmpCertiticateType + ") does not match the requested one (" + certType + ")");
		}
		PublicKey publicKey = null;
		switch (tmpCertiticateType) {
			case DEFAULT_CERTIFICATE_TYPE:
				X509Certificate x509Certificate = (X509Certificate) certificate;
				publicKey = x509Certificate.getPublicKey();
				return publicKey;
			default:
				publicKey = ((X509Certificate) certificate).getPublicKey();
		}
		return publicKey;
	}

	/**
	 * 
	 * @param certificate
	 *            The certificate from where to load the certificate
	 * @param certType
	 *            The type of certificate (X.509 ...). If
	 * @return The public key enclosed in the certificate
	 * @throws KeyStoreException
	 * @throws SSLException
	 * @throws CertificateException
	 */
	public static final PublicKey readPublicKey(Certificate certificate, String certType) throws KeyStoreException {
		if (certificate == null) {
			return null;
		}
		String tmpCertiticateType = (certType == null || certType.isEmpty()) ? DEFAULT_CERTIFICATE_TYPE : certType;
		if (!certType.equalsIgnoreCase(certificate.getType())) {
			throw new KeyStoreException("The type of the actual certificate (" + tmpCertiticateType + ") does not match the requested one (" + certType + ")");
		}
		PublicKey publicKey = null;
		switch (tmpCertiticateType) {
			case DEFAULT_CERTIFICATE_TYPE:
				X509Certificate x509Certificate = (X509Certificate) certificate;
				publicKey = x509Certificate.getPublicKey();
				return publicKey;
			default:
				publicKey = ((X509Certificate) certificate).getPublicKey();
		}
		return publicKey;
	}

	/**
	 * 
	 * @param keyStore
	 *            The keystore file from where to load the certificate
	 * @param password
	 *            The keyStore password
	 * @param keyStoreType
	 *            The keyStore type (JKS, JCEKS, etc.)
	 * @param certAlias
	 *            The alias of the certificate to load
	 * @param certType
	 *            The type of certificate (X.509 ...). If
	 * @return The public key enclosed in the certificate
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws java.security.cert.CertificateException
	 * @throws IOException
	 * @throws CertificateException
	 */
	public static final PublicKey readPublicKey(File keyStoreFile, String password, String keyStoreType, String certAlias, String certType)
			throws KeyStoreException, NoSuchAlgorithmException, java.security.cert.CertificateException, IOException, CertificateException {
		KeyStore keyStore = loadKeyStore(keyStoreFile, password, keyStoreType);
		return readPublicKey(keyStore, certAlias, certType);
	}

	/**
	 * 
	 * @param certAlias
	 *            The alias of the certificate to load
	 * @param certType
	 *            The type of certificate (X.509 ...). If
	 * @return The public key enclosed in the certificate
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws java.security.cert.CertificateException
	 * @throws IOException
	 * @throws CertificateException
	 */
	public static final PublicKey readPublicKey(String certAlias, String certType) throws KeyStoreException, NoSuchAlgorithmException, java.security.cert.CertificateException, IOException, CertificateException {
		String trustStoreFilePath = System.getProperty("javax.net.ssl.trustStore");
		String trustStoreType = System.getProperty("javax.net.ssl.trustStoreType");
		String trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
		File trustStoreFile = new File(trustStoreFilePath);
		if (trustStoreFile == null || !trustStoreFile.isFile()) {
			throw new FileNotFoundException("File " + trustStoreFilePath + " does not exist, thus cannot be considered as a truststore file");
		}
		return readPublicKey(trustStoreFile, trustStorePassword, trustStoreType, certAlias, certType);
	}

	public static final PrivateKey readPrivateKey(KeyStore keyStore, String keyAlias, String keyPassword)
			throws KeyStoreException, NoSuchAlgorithmException, java.security.cert.CertificateException, IOException, CertificateException, UnrecoverableKeyException {
		PrivateKey privateKey = null;
		Key key = keyStore.getKey(keyAlias, keyPassword.toCharArray());
		if (key instanceof PrivateKey) {
			privateKey = (PrivateKey) key;
		}
		return privateKey;
	}

	/**
	 * 
	 * @param keyStoreFile
	 * @param keyStorePassword
	 * @param keyStoreType
	 * @param keyAlias
	 * @param keyPassword
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws java.security.cert.CertificateException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 */
	public static final PrivateKey readPrivateKey(File keyStoreFile, String keyStorePassword, String keyStoreType, String keyAlias, String keyPassword)
			throws KeyStoreException, NoSuchAlgorithmException, java.security.cert.CertificateException, IOException, CertificateException, UnrecoverableKeyException {
		KeyStore keyStore = loadKeyStore(keyStoreFile, keyStorePassword, keyStoreType);
		return readPrivateKey(keyStore, keyAlias, keyPassword);
	}

	/**
	 * 
	 * @param keyAlias
	 * @param keyPassword
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws java.security.cert.CertificateException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 */
	public static final PrivateKey readPrivateKey(String keyAlias, String keyPassword)
			throws KeyStoreException, NoSuchAlgorithmException, java.security.cert.CertificateException, IOException, CertificateException, UnrecoverableKeyException {
		String keyStoreFilePath = System.getProperty("javax.net.ssl.keyStore");
		String keyStoreType = System.getProperty("javax.net.ssl.keyStoreType");
		String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
		File keyStoreFile = new File(keyStoreFilePath);
		if (keyStoreFile == null || !keyStoreFile.isFile()) {
			LOGGER.error("Throwing FileNotFoundException...");
			throw new FileNotFoundException("File " + keyStoreFilePath + " does not exist, thus cannot be considered as a keystore file");
		}
		return readPrivateKey(keyStoreFile, keyStorePassword, keyStoreType, keyAlias, keyPassword);
	}
}
