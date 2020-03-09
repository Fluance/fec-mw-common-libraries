/**
 * 
 */
package net.fluance.commons.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PKIUtilsTest {

	private static Logger LOGGER = LogManager.getLogger();
	
	private final String helloWorldMsg = "Hello world!";
	private final String keystoreFilePath = "/keystore.jks";
	private final String keyStorePassword = "fluance";
	private final String keyStoreType = "JKS";
	private final String keyAlias = "fluance1024";
	private final String keyPassword = "fluance";
	private final String privateKeyAlgorithm = "RSA";
	private final String publicKeyAlgorithm = "RSA";
	private final String signatureAlgorithm = "SHA512withRSA";
	private final int privateKeySize = 1024;
	private final int publicKeySize = 1024;
	
	
	private final String trustStoreFilePath = "/truststore.jks";
	private final String trustStorePassword = "fluance";
	private final String trustStoreType = "JKS";
	private final String certificateAlias = "fluance1024";
	private final String certificateAliasNoPassword = "fluance1024_nopassword";
	private final String certificateType = "X.509";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
        String keyStorePath = new File(classLoader.getResource("keystore.jks").getFile()).getAbsolutePath();
        String trustStorePath = new File(classLoader.getResource("truststore.jks").getFile()).getAbsolutePath();
       
        System.out.println("keyStorePath: " + keyStorePath);
        System.out.println("trustStorePath: " + trustStorePath);
		
		System.setProperty("javax.net.ssl.keyStore", keyStorePath);
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.keyStorePassword", "fluance");	
		
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStorePassword", "fluance");		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSign() throws Exception {
		// Load key store
		File keystoreFile = new File(getClass().getResource(keystoreFilePath).getFile());
		KeyStore keyStore = PKIUtils.loadKeyStore(keystoreFile, keyStorePassword, keyStoreType);
		assertNotNull(keyStore);
		// Load private key
		KeyPair keyPair = PKIUtils.readKeyPair(keyStore, keyAlias, keyPassword);
		PrivateKey privateKey = keyPair.getPrivate();
		assertNotNull(privateKey);
		
		LOGGER.debug("Data to sign: " + helloWorldMsg);

		byte[] signedData = PKIUtils.signData(helloWorldMsg, privateKey, signatureAlgorithm);
		String signedDataStr = new String(signedData);
		
		LOGGER.debug("Signed data: " + signedDataStr);
		assertNotNull(signedData);
		assertNotEquals(signedData, helloWorldMsg.getBytes());
	}
	
	@Test
	public void testVerifySignature() throws Exception {
		// Load key store
		File keystoreFile = new File(getClass().getResource(keystoreFilePath).getFile());
		KeyStore keyStore = PKIUtils.loadKeyStore(keystoreFile, keyStorePassword, keyStoreType);
		assertNotNull(keyStore);
		// Load keys
		KeyPair keyPair = PKIUtils.readKeyPair(keyStore, keyAlias, keyPassword);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		assertNotNull(privateKey);
		assertNotNull(publicKey);
		
		// Sign message
		LOGGER.debug("Data to sign: " + helloWorldMsg);
		byte[] signedData = PKIUtils.signData(helloWorldMsg, privateKey, signatureAlgorithm);
		String signedDataStr = new String(signedData);
		LOGGER.debug("Signed data: " + signedDataStr);
		assertNotNull(signedData);
		assertNotEquals(signedData, helloWorldMsg.getBytes());
		
		// Load trust store and certificate
		File trustStoreFile = new File(getClass().getResource(trustStoreFilePath).getFile());
		KeyStore trustStore = PKIUtils.loadKeyStore(trustStoreFile, trustStorePassword, trustStoreType);
		assertNotNull(trustStore);
		LOGGER.debug("Found aliases ... ");
		Enumeration<String> aliases = trustStore.aliases();
		while(aliases.hasMoreElements()) {
			LOGGER.debug("Alias: " + aliases.nextElement());
		}
		System.out.println("Aliases: " + trustStore.aliases());
		PublicKey publicKeyFromCert = PKIUtils.readPublicKey(trustStore, certificateAlias, certificateType);

		// Verify signature
		boolean signatureOk = PKIUtils.verifySignature(helloWorldMsg.getBytes(), publicKeyFromCert, signedData, signatureAlgorithm);
		assertTrue(signatureOk);
	}
	
	@Test
	public void testLoadKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, javax.security.cert.CertificateException, IOException {
		File keystoreFile = new File(getClass().getResource(keystoreFilePath).getFile());
		KeyStore keyStore = PKIUtils.loadKeyStore(keystoreFile, keyStorePassword, keyStoreType);
		assertNotNull(keyStore);
	}

	@Test
	public void testReadPublicKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, javax.security.cert.CertificateException, IOException {
		File trustStoreFile = new File(getClass().getResource(trustStoreFilePath).getFile());
		KeyStore trustStore = PKIUtils.loadKeyStore(trustStoreFile, trustStorePassword, trustStoreType);
		assertNotNull(trustStore);
		PublicKey publicKeyFromKeyStore = PKIUtils.readPublicKey(trustStore, certificateAlias, certificateType);
		assertNotNull(publicKeyFromKeyStore);

		PublicKey publicKeyFromFile = PKIUtils.readPublicKey(trustStoreFile, trustStorePassword, trustStoreType, certificateAlias, certificateType);
		assertNotNull(publicKeyFromFile);
		assertEquals(publicKeyFromKeyStore.getAlgorithm(), publicKeyFromFile.getAlgorithm());
		assertEquals(publicKeyFromKeyStore.getFormat(), publicKeyFromFile.getFormat());
		assertEquals(publicKeyFromKeyStore.toString(), publicKeyFromFile.toString());

		PublicKey publicKeyFromSystemProps = PKIUtils.readPublicKey(certificateAlias, certificateType);
		assertNotNull(publicKeyFromFile);
		assertEquals(publicKeyFromKeyStore.getAlgorithm(), publicKeyFromSystemProps.getAlgorithm());
		assertEquals(publicKeyFromKeyStore.getFormat(), publicKeyFromSystemProps.getFormat());
		assertEquals(publicKeyFromKeyStore.toString(), publicKeyFromSystemProps.toString());
	}

	@Test
	public void testReadPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, javax.security.cert.CertificateException, IOException, UnrecoverableKeyException {
		
		File keyStoreFile = new File(getClass().getResource(keystoreFilePath).getFile());
		KeyStore keyStore = PKIUtils.loadKeyStore(keyStoreFile, keyStorePassword, keyStoreType);
		assertNotNull(keyStore);
		PrivateKey privateKeyFromKeyStore = PKIUtils.readPrivateKey(keyStore, keyAlias, keyPassword);
		assertNotNull(privateKeyFromKeyStore);
		
		PrivateKey privateKeyFromFile = PKIUtils.readPrivateKey(keyStoreFile, keyStorePassword, keyStoreType, keyAlias, keyPassword);
		assertNotNull(privateKeyFromFile);
		assertEquals(privateKeyFromKeyStore.getAlgorithm(), privateKeyFromFile.getAlgorithm());
		assertEquals(privateKeyFromKeyStore.getFormat(), privateKeyFromFile.getFormat());
		assertEquals(privateKeyFromKeyStore.toString(), privateKeyFromFile.toString());

		PrivateKey privateKeyFromClasspath = PKIUtils.readPrivateKey(keyAlias, keyPassword);
		assertNotNull(privateKeyFromClasspath);
		assertEquals(privateKeyFromKeyStore.getAlgorithm(), privateKeyFromClasspath.getAlgorithm());
		assertEquals(privateKeyFromKeyStore.getFormat(), privateKeyFromClasspath.getFormat());
		assertEquals(privateKeyFromKeyStore.toString(), privateKeyFromClasspath.toString());
	}
	
	@Test
	public void testReadRSAKeyPair() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, javax.security.cert.CertificateException, UnrecoverableKeyException {
		// Load key store
		File keystoreFile = new File(getClass().getResource(keystoreFilePath).getFile());
		KeyStore keyStore = PKIUtils.loadKeyStore(keystoreFile, keyStorePassword, keyStoreType);
		assertNotNull(keyStore);
		// Load keys
		KeyPair keyPair = PKIUtils.readKeyPair(keyStore, keyAlias, keyPassword);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		assertNotNull(privateKey);
		assertEquals(privateKeyAlgorithm, privateKey.getAlgorithm());
		assertEquals(privateKeySize, ((RSAPrivateKey)privateKey).getModulus().bitLength());
		assertNotNull(publicKey);
		assertEquals(publicKeyAlgorithm, publicKey.getAlgorithm());
		assertEquals(publicKeySize, ((RSAPublicKey)publicKey).getModulus().bitLength());
	}
}
