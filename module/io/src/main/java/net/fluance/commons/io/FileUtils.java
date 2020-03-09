/**
 * 
 */
package net.fluance.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils {
	private FileUtils() {}
	private static Logger LOGGER = LogManager.getLogger(FileUtils.class);

	/**
	 * Create a temp file
	 * 
	 * @param aPrefix
	 * @param aSuffix
	 * @param aDir
	 * @param aDeleteOnExit
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File createTmpFile(String aPrefix, String aSuffix, String aDir, boolean aDeleteOnExit) throws FileNotFoundException {
		return createTmpFile(aPrefix, aSuffix, new File(aDir), aDeleteOnExit);
	}

	/**
	 * Create a temp file
	 * 
	 * @param aPrefix
	 * @param aSuffix
	 * @param aDir
	 * @param aDeleteOnExit
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File createTmpFile(String aPrefix, String aSuffix, File aDir, boolean aDeleteOnExit) throws FileNotFoundException {
		File vTmpFile = null;
		if (!aDir.isDirectory()) {
			String msg = "Could not find directory '" + aDir.getAbsolutePath() + "'";
			LOGGER.error("A FileNotFoundException has occured: " + msg);
			throw new FileNotFoundException(msg);
		}
		try {
			vTmpFile = File.createTempFile(aPrefix, aSuffix, aDir);
			if (aDeleteOnExit) {
				vTmpFile.deleteOnExit();
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return vTmpFile;
	}

	/**
	 * Creates a temp file using the new (Java 7) NIO way
	 * 
	 * @param aPrefix
	 * @param aSuffix
	 * @param aDir
	 * @param aDeleteOnExit
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Path createTmpNIOFile(String aPrefix, String aSuffix, String aDir, boolean aDeleteOnExit) throws FileNotFoundException {
		Path vPath = null;
		File vDir = new File(aDir);
		if (!vDir.isDirectory()) {
			throw new FileNotFoundException("Could not find directory '" + vDir.getAbsolutePath() + "'");
		}
		try {
			final Path path = Files.createTempFile(Paths.get(aDir), aPrefix, aSuffix);
			if (aDeleteOnExit) {
				path.toFile().deleteOnExit();
			}
			Runtime.getRuntime().addShutdownHook(new Thread() {

				public void run() {
					try {
						Files.delete(path);
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return vPath;
	}

	/**
	 * Change l'encodage du fichier inFileName en UTF-8, le r�sultat dans le fichier outFileName
	 * 
	 * @param inFileName
	 * @param outFileName
	 */
	public void encodeToUtf8(String inFileName, String outFileName) {
		File inFile = new File(inFileName);
		File tempFile = new File(inFile.getParent() + System.getProperty("file.separator") + FilenameUtils.getBaseName(inFile.getAbsolutePath()) + ".tmp");
		try {
			org.apache.commons.io.FileUtils.moveFile(inFile, tempFile);
			InputStreamReader in = new InputStreamReader(new FileInputStream(tempFile.getAbsolutePath()));
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outFileName), "UTF-8");
			int c = in.read();
			while (c != -1) {
				out.write(c);
				c = in.read();
			}
			in.close();
			out.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String datedFilename(String filename) {
		if (filename == null || filename.isEmpty()) {
			throw new IllegalArgumentException(filename + " is not a valid filename. Must provide a valid filename");
		}
		String newName = filename;
		String basename = FilenameUtils.getBaseName(filename);
		String extension = FilenameUtils.getExtension(filename);
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		newName += basename + "-" + dateFormat.format(date) + ((extension == null || extension.isEmpty()) ? "" : "." + extension);
		return newName;
	}

	/**
	 * Loads a file from class path
	 * 
	 * @param classPathRelativeLocation
	 * @return
	 */
	public static String loadFromClassPath(Class<?> requesterClass, String classPathRelativeLocation) {
		ClassLoader classLoader = requesterClass.getClassLoader();
		return new File(classLoader.getResource(classPathRelativeLocation).getFile()).getAbsolutePath();
	}
}
