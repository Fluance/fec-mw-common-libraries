/**
 * 
 */
package net.fluance.commons.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropUtils {
	private PropUtils() {}
	private static Logger LOGGER = LogManager.getLogger(PropUtils.class);

	/**
	 * From ClassLoader, all paths are "absolute" already - there's no context
	 * from which they could be relative. Therefore you don't need a leading slash.
	 * InputStream in = this.getClass().getClassLoader()
	 *                                .getResourceAsStream("SomeTextFile.txt");
	 * From Class, the path is relative to the package of the class unless
	 * you include a leading slash, so if you don't want to use the current
	 * package, include a slash like this:
	 * InputStream in = this.getClass().getResourceAsStream("/SomeTextFile.txt");
	 * @param propFileName
	 * @return
	 * @throws IOException
	 */
	public static Properties loadFromClassLoader(String propFileName) throws IOException {
		Properties props = new Properties();
		ClassLoader classLoader = PropUtils.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(propFileName);
		if (inputStream == null) {
			String msg = "property file '" + propFileName + "' not found in the classpath";
			LOGGER.error(msg);
			throw new FileNotFoundException(msg);
		}
		props.load(inputStream);
		return props;
	}

	/**
	 * 
	 * @param propFileName
	 * @return
	 * @throws IOException
	 */
	public static Properties loadFromContextClassLoader(String propFileName) throws IOException {
		Properties props = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(propFileName);
		if (inputStream == null) {
			String msg = "property file '" + propFileName + "' not found in the classpath";
			LOGGER.error(msg);
			throw new FileNotFoundException(msg);
		}
		props.load(inputStream);
		return props;
	}

	/**
	 * The provided path MUST be relative
	 * @param propFileName
	 * @return
	 * @throws IOException
	 */
	public static Properties loadFromClass(String propFileName) throws IOException {
		String filePath = propFileName;
		if(filePath != null && !filePath.startsWith("/")) {
			filePath = "/" + filePath;
		}
		Properties props = new Properties();
		InputStream inputStream = PropUtils.class.getResourceAsStream(propFileName);
		if (inputStream == null) {
			String msg = "property file '" + propFileName + "' not found in the classpath";
			LOGGER.error(msg);
			throw new FileNotFoundException(msg);
		}
		props.load(inputStream);
		return props;
	}

	/**
	 * 
	 * @param aFile
	 * @param aProps
	 * @param aEncoding
	 * @throws IOException
	 */
	public static void store(String aFile, Properties aProps, String aEncoding) throws IOException {
		OutputStreamWriter vOut = new OutputStreamWriter(
				new FileOutputStream(aFile), aEncoding);
		aProps.store(vOut, null);
		vOut.close();
	}

	/**
	 * Load properties from URL
	 * @param propsName
	 * @return Properties
	 * @throws Exception
	 */
	public static Properties load(URL propsUrl) throws IOException {
		return load(new File(propsUrl.toString()));
	}

	/**
	 * Load a properties file from the classpath
	 * @param propsName
	 * @return Properties
	 * @throws Exception
	 */
	public static Properties load(String propsName) throws IOException {
		return load(new File(propsName));
	}

	/**
	 * @param propsFile
	 * @return Properties
	 * @throws IOException
	 */
	public static Properties load(File propsFile) throws IOException {
		Properties props = new Properties();
		FileInputStream fileInputStream = new FileInputStream(propsFile);
		props.load(fileInputStream);    
		fileInputStream.close();
		return props;
	}
}
