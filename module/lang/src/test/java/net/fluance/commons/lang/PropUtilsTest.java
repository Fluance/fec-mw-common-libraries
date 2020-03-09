/**
 * 
 */
package net.fluance.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropUtilsTest {

	private static Logger LOGGER = LogManager.getLogger(PropUtilsTest.class);
	
	private static final String DUMMY_PROP_KEY = "aKey";
	private static final String DUMMY_PROP_VALUE = "aValue";
	private static final String DUMMY_PROP_FILE_NAME = "dummy-file.properties";
	private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
	private File tmpPropFile;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		LOGGER.debug("Executing setUp..."); 
		String vPrefix = FilenameUtils.getBaseName(DUMMY_PROP_FILE_NAME);
		String vSuffix = "." + FilenameUtils.getExtension(DUMMY_PROP_FILE_NAME);
		tmpPropFile = createTmpFile(vPrefix, vSuffix, new File(TMP_DIR), true);
		assertTrue(tmpPropFile.exists());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		LOGGER.debug("Executing tearDown..."); 
		assertTrue(tmpPropFile.exists());
		Files.deleteIfExists(Paths.get(tmpPropFile.getAbsolutePath()));
		assertTrue(!tmpPropFile.exists());
	}

	/**
	 * Test method for
	 * {@link net.fluance.commons.lang.PropUtils#loadFromClassLoader(java.lang.String)}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLoadFromClassLoader() throws IOException {
		LOGGER.debug("Executing testLoadFromClassLoader..."); 
		Properties vProps = PropUtils.loadFromClassLoader(DUMMY_PROP_FILE_NAME);
		assertNotNull(vProps);
		assertEquals(1, vProps.size());
		assertTrue(vProps.containsKey(DUMMY_PROP_KEY));
		assertEquals(DUMMY_PROP_VALUE, vProps.getProperty(DUMMY_PROP_KEY));
	}


	/**
	 * Test method for
	 * {@link net.fluance.commons.lang.PropUtils#load(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLoadString() throws IOException {
		LOGGER.debug("Executing testLoadString...");
		Properties vProps = PropUtils.load(tmpPropFile.getAbsolutePath());
		assertNotNull(vProps);
	}

	/**
	 * Test method for
	 * {@link net.fluance.commons.utils.PropUtils#store(java.lang.String,java.utils.Properties,java.lang.String)}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void testStoreStringPropertiesString() throws IOException {
		LOGGER.debug("Executing testStoreStringPropertiesString...");
		Properties vProps = PropUtils.load(tmpPropFile);
		vProps.put(DUMMY_PROP_KEY, DUMMY_PROP_VALUE);
		PropUtils.store(tmpPropFile.getAbsolutePath(), vProps, "UTF-8");
		vProps = PropUtils.load(tmpPropFile);
		assertTrue(DUMMY_PROP_VALUE.equals(vProps.getProperty(DUMMY_PROP_KEY)));
	}

	/**
	 * Test method for
	 * {@link net.fluance.commons.utils.PropUtils#load(java.io.File)}.
	 * @throws IOException 
	 */
	@Test
	public void testLoadFile() throws IOException {
		LOGGER.debug("Executing testLoadFile...");
		Properties vProps = PropUtils.load(tmpPropFile);
		assertNotNull(vProps);
	}

	public static File createTmpFile(String aPrefix, String aSuffix, File aDir,
			boolean aDeleteOnExit) throws FileNotFoundException {
		LOGGER.debug("Executing createTmpFile...");
		File vTmpFile = null;
		if (!aDir.isDirectory()) {
			throw new FileNotFoundException("Could not find directory '" + aDir.getAbsolutePath() + "'");
		} 
		try {
			// create a temp file
			vTmpFile = File.createTempFile(aPrefix, aSuffix, aDir);
			if (aDeleteOnExit) {
				vTmpFile.deleteOnExit();
			}
			System.out.println("Temp file : " + vTmpFile.getAbsolutePath());
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			
		}
		return vTmpFile;
	}
	
}
