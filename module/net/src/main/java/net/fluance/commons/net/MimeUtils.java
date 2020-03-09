/**
 * 
 */
package net.fluance.commons.net;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MimeUtils {

	private static Logger LOGGER = LogManager.getLogger(MimeUtils.class);
	
	private static final char CSV_DELIMITER = ',';
	private static final CSVFormat CSV_FORMAT = CSVFormat.EXCEL.withHeader().withDelimiter(CSV_DELIMITER);
	private static final Map<String, String> REGISTRY_FILES;
	public static final Map<String, List<MediaType>> REGISTRY_MEDIA_TYPES;
	
	static {
		REGISTRY_FILES = new HashMap<>();
		REGISTRY_FILES.put("application", "mediatype/application.csv");
		REGISTRY_FILES.put("audio", "mediatype/audio.csv");
		REGISTRY_FILES.put("image", "mediatype/image.csv");
		REGISTRY_FILES.put("message", "mediatype/message.csv");
		REGISTRY_FILES.put("model", "mediatype/model.csv");
		REGISTRY_FILES.put("multipart", "mediatype/multipart.csv");
		REGISTRY_FILES.put("text", "mediatype/text.csv");
		REGISTRY_FILES.put("video", "mediatype/video.csv");
		
		REGISTRY_MEDIA_TYPES = new HashMap<>();
		for(String registry : REGISTRY_FILES.keySet()) {
			File registryFile = loadRegistryFile(REGISTRY_FILES.get(registry));
			try {
				REGISTRY_MEDIA_TYPES.put(registry, loadFromCsv(registryFile));
			} catch (IOException e) {
				LOGGER.error("Could not load " + registry + " media types from file " + registryFile.getAbsolutePath(), e);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static final List<MediaType> ianaList() {
		return ianaList(REGISTRY_MEDIA_TYPES.keySet());
	}
	
	/**
	 * 
	 * @return
	 */
	public static final List<String> ianaNameList() {
		return ianaNameList(REGISTRY_MEDIA_TYPES.keySet());
	}

	/**
	 * 
	 * @return
	 */
	public static final List<String> ianaTemplateList() {
		return ianaTemplateList(REGISTRY_MEDIA_TYPES.keySet());
	}

	/**
	 * 
	 * @param registries
	 * @return
	 */
	public static final List<MediaType> ianaList(Collection<String> registries) {
		LOGGER.debug("Executing ianaList...");
		List<MediaType> mediaTypes = new ArrayList<>();
		
		for(String registry : registries) {
			if(!REGISTRY_MEDIA_TYPES.containsKey(registry)) {
				LOGGER.warn("The requested media type registry does not exist: " + registry);
			}
			if(REGISTRY_MEDIA_TYPES.get(registry) != null) {
				mediaTypes.addAll(REGISTRY_MEDIA_TYPES.get(registry));
			}
		}
		
		LOGGER.debug("Returning media types for registries " + registries + " : " + mediaTypes);
		return mediaTypes;
		
	}

	/**
	 * 
	 * @param registry
	 * @return
	 */
	public static final List<MediaType> ianaList(String registry) {
		return ianaList(Arrays.asList(new String[] {registry}));
	}
	
	/**
	 * 
	 * @param registry
	 * @return
	 */
	public static final List<String> ianaNameList(String registry) {
		return ianaNameList(Arrays.asList(new String[] {registry}));
	}

	/**
	 * 
	 * @param registry
	 * @return
	 */
	public static final List<String> ianaTemplateList(String registry) {
		return ianaTemplateList(Arrays.asList(new String[] {registry}));
	}
	
	/**
	 * 
	 * @param name
	 * @param registry
	 * @param caseSensitive Wether comparison must be case sensitive or insensitive
	 * @return
	 */
	public static final MediaType byName(String name, String registry) {
		LOGGER.debug("Executing byName...");
		MediaType mediaType = null;
		
		if(null == name || name.isEmpty()) {
			return null;
		}
		
		List<MediaType> mediaTypes = ianaList(registry);
		for(MediaType type : mediaTypes) {
			if(name.equalsIgnoreCase(type.getName())) {
				mediaType = type;
			}
		}
		
		return mediaType;
	}

	/**
	 * 
	 * @param name
	 * @param registry
	 * @param caseSensitive Wether comparison must be case sensitive or insensitive
	 * @return
	 */
	//TODO Find more performant algo
	public static final MediaType byName(String name) {
		LOGGER.debug("Executing byName...");
		MediaType mediaType = null;
		
		if(null == name || name.isEmpty()) {
			return null;
		}
		
		List<MediaType> mediaTypes = ianaList();
		for(MediaType type : mediaTypes) {
			if(name.equalsIgnoreCase(type.getName())) {
				mediaType = type;
			}
		}
		
		return mediaType;
	}
	
	/**
	 * 
	 * @param template
	 * @param registry
	 * @param caseSensitive Wether comparison must be case sensitive or insensitive
	 * @return
	 */
	public static final MediaType byTemplate(String template, String registry) {
		LOGGER.debug("Executing byTemplate...");
		MediaType mediaType = null;
		
		if(null == template || template.isEmpty()) {
			return null;
		}
		
		List<MediaType> mediaTypes = ianaList(registry);
		for(MediaType type : mediaTypes) {
			if(template.equalsIgnoreCase(type.getTemplate())) {
				mediaType = type;
			}
		}
		
		return mediaType;
	}
	
	/**
	 * 
	 * @param name
	 * @param registry
	 * @param caseSensitive Wether comparison must be case sensitive or insensitive
	 * @return
	 */
	//TODO Find more performant algo
	public static final MediaType byTemplate(String template) {
		LOGGER.debug("Executing byTemplate...");
		MediaType mediaType = null;
		
		if(null == template || template.isEmpty()) {
			return null;
		}
		
		List<MediaType> mediaTypes = ianaList();
		for(MediaType type : mediaTypes) {
			if(template.equalsIgnoreCase(type.getTemplate())) {
				mediaType = type;
			}
		}
		
		return mediaType;
	}

	
	
	/**
	 * Get the IANA names of media types for the given registries
	 * @param registries
	 * @return
	 */
	public static final List<String> ianaNameList(Collection<String> registries) {
		LOGGER.debug("Executing ianaNameList...");
		List<String> mediaTypeNames = new ArrayList<>();
		
		List<MediaType> mediaTypes = ianaList(registries);
		
		for(MediaType mediaType : mediaTypes) {
			if(mediaType != null) {
				mediaTypeNames.add(mediaType.getName());
			}
		}
		
		LOGGER.debug("Returning IANA media type names for registries " + registries + " : " + mediaTypeNames);
		
		return mediaTypeNames;
		
	}

	public static final List<String> ianaTemplateList(Collection<String> registries) {
		LOGGER.debug("Executing ianaTemplateList...");
		List<String> mediaTypeTemplates = new ArrayList<>();
		
		List<MediaType> mediaTypes = ianaList(registries);
		
		for(MediaType mediaType : mediaTypes) {
			if(mediaType != null) {
				mediaTypeTemplates.add(mediaType.getTemplate());
			}
		}
		
		LOGGER.debug("Returning IANA media type templates for registries " + registries + " : " + mediaTypeTemplates);
		
		return mediaTypeTemplates;
		
	}
	
	/**
	 * 
	 * @param csvFile
	 * @return
	 * @throws IOException
	 */
	public static List<MediaType> loadFromCsv(File csvFile) throws IOException {
		LOGGER.debug("Executing loadFromCsv...");
		Reader in = new FileReader(csvFile);
		CSVParser parser = new CSVParser(in, CSV_FORMAT);
		List<CSVRecord> records = parser.getRecords();
		parser.close();
		List<MediaType> mediaTypes = new ArrayList<>();
		for (CSVRecord record : records) {
			String name = (record.isSet("Name")) ? record.get("Name") : null;
			String template = (record.isSet("Template")) ? record.get("Template") : null;
			String reference = (record.isSet("Reference")) ? record.get("Reference") : null;
			mediaTypes.add(new MediaType(name, template, reference));
		}
		return mediaTypes;
	}
	
	/**
	 * Loads a file from class path
	 * @param classPathRelativeLocation
	 * @return
	 */
	private static File loadRegistryFile(String fileRelativePath) {
		LOGGER.debug("Executing loadRegistryFile...");
		ClassLoader classLoader = MimeUtils.class.getClassLoader();
		File file = new File(classLoader.getResource(fileRelativePath).getFile());
		return file;
	}
}
