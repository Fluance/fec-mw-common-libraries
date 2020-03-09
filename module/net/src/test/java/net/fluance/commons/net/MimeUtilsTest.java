package net.fluance.commons.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class MimeUtilsTest {

	private static Logger LOGGER = LogManager.getLogger(MimeUtilsTest.class);
	
	private static final int APPLICATION_MEDIATYPES_COUNT = 1182;
	private static final int AUDIO_MEDIATYPES_COUNT = 143;
	private static final int IMAGE_MEDIATYPES_COUNT = 56;
	private static final int MESSAGE_MEDIATYPES_COUNT = 21;
	private static final int MODEL_MEDIATYPES_COUNT = 22;
	private static final int MULTIPART_MEDIATYPES_COUNT = 15;
	private static final int TEXT_MEDIATYPES_COUNT = 71;
	private static final int VIDEO_MEDIATYPES_COUNT = 78;
	private static final int TOTAL_MEDIATYPES_COUNT = APPLICATION_MEDIATYPES_COUNT + AUDIO_MEDIATYPES_COUNT + IMAGE_MEDIATYPES_COUNT + MESSAGE_MEDIATYPES_COUNT + MODEL_MEDIATYPES_COUNT + MULTIPART_MEDIATYPES_COUNT + TEXT_MEDIATYPES_COUNT + VIDEO_MEDIATYPES_COUNT;
	private String applicationRegistry = "application";
	private String videoRegistry = "video";
	private String audioRegistry = "audio";
	private String imageRegistry = "image";
	private String messageRegistry = "message";
	private String modelRegistry = "model";
	private String multipartRegistry = "multipart";
	private String textRegistry = "text";

	@Test
	public void testListMediaTypes() {
		LOGGER.debug("Executing testListMediaTypes...");
		List<MediaType> mediaTypes = MimeUtils.ianaList();
		assertNotNull(mediaTypes);
		
		assertEquals(TOTAL_MEDIATYPES_COUNT, mediaTypes.size());

		mediaTypes = MimeUtils.ianaList(applicationRegistry);
		assertNotNull(mediaTypes);
		assertEquals(APPLICATION_MEDIATYPES_COUNT, mediaTypes.size());

		mediaTypes = MimeUtils.ianaList(audioRegistry);
		assertNotNull(mediaTypes);
		assertEquals(AUDIO_MEDIATYPES_COUNT, mediaTypes.size());

		mediaTypes = MimeUtils.ianaList(imageRegistry);
		assertNotNull(mediaTypes);
		assertEquals(IMAGE_MEDIATYPES_COUNT, mediaTypes.size());
		
		mediaTypes = MimeUtils.ianaList(messageRegistry);
		assertNotNull(mediaTypes);
		assertEquals(MESSAGE_MEDIATYPES_COUNT, mediaTypes.size());
		
		mediaTypes = MimeUtils.ianaList(modelRegistry);
		assertNotNull(mediaTypes);
		assertEquals(MODEL_MEDIATYPES_COUNT, mediaTypes.size());
		
		mediaTypes = MimeUtils.ianaList(multipartRegistry);
		assertNotNull(mediaTypes);
		assertEquals(MULTIPART_MEDIATYPES_COUNT, mediaTypes.size());
		
		mediaTypes = MimeUtils.ianaList(textRegistry);
		assertNotNull(mediaTypes);
		assertEquals(TEXT_MEDIATYPES_COUNT, mediaTypes.size());
		
		mediaTypes = MimeUtils.ianaList(videoRegistry);
		assertNotNull(mediaTypes);
		assertEquals(VIDEO_MEDIATYPES_COUNT, mediaTypes.size());
		
	}
	
	@Test
	public void testListMediaTypeNames() {
		LOGGER.debug("Executing testListMediaTypeNames...");
		List<String> mediaTypeNames = MimeUtils.ianaNameList();
		assertNotNull(mediaTypeNames);
		
		assertEquals(TOTAL_MEDIATYPES_COUNT, mediaTypeNames.size());
		
		mediaTypeNames = MimeUtils.ianaNameList(applicationRegistry);
		assertNotNull(mediaTypeNames);
		assertEquals(APPLICATION_MEDIATYPES_COUNT, mediaTypeNames.size());
		
		mediaTypeNames = MimeUtils.ianaNameList(audioRegistry);
		assertNotNull(mediaTypeNames);
		assertEquals(AUDIO_MEDIATYPES_COUNT, mediaTypeNames.size());
		
		mediaTypeNames = MimeUtils.ianaNameList(imageRegistry);
		assertNotNull(mediaTypeNames);
		assertEquals(IMAGE_MEDIATYPES_COUNT, mediaTypeNames.size());
		
		mediaTypeNames = MimeUtils.ianaNameList(messageRegistry);
		assertNotNull(mediaTypeNames);
		assertEquals(MESSAGE_MEDIATYPES_COUNT, mediaTypeNames.size());
		
		mediaTypeNames = MimeUtils.ianaNameList(modelRegistry);
		assertNotNull(mediaTypeNames);
		assertEquals(MODEL_MEDIATYPES_COUNT, mediaTypeNames.size());
		
		mediaTypeNames = MimeUtils.ianaNameList(multipartRegistry);
		assertNotNull(mediaTypeNames);
		assertEquals(MULTIPART_MEDIATYPES_COUNT, mediaTypeNames.size());
		
		mediaTypeNames = MimeUtils.ianaNameList(textRegistry);
		assertNotNull(mediaTypeNames);
		assertEquals(TEXT_MEDIATYPES_COUNT, mediaTypeNames.size());
		
		mediaTypeNames = MimeUtils.ianaNameList(videoRegistry);
		assertNotNull(mediaTypeNames);
		assertEquals(VIDEO_MEDIATYPES_COUNT, mediaTypeNames.size());
		
	}
	
	@Test
	public void testListMediaTypeTemplates() {
		LOGGER.debug("Executing testListMediaTypeTemplates...");
		List<String> mediaTypeTemplates = MimeUtils.ianaTemplateList();
		assertNotNull(mediaTypeTemplates);
		
		assertEquals(TOTAL_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
		mediaTypeTemplates = MimeUtils.ianaTemplateList(applicationRegistry);
		assertNotNull(mediaTypeTemplates);
		assertEquals(APPLICATION_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
		mediaTypeTemplates = MimeUtils.ianaTemplateList(audioRegistry);
		assertNotNull(mediaTypeTemplates);
		assertEquals(AUDIO_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
		mediaTypeTemplates = MimeUtils.ianaTemplateList(imageRegistry);
		assertNotNull(mediaTypeTemplates);
		assertEquals(IMAGE_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
		mediaTypeTemplates = MimeUtils.ianaTemplateList(messageRegistry);
		assertNotNull(mediaTypeTemplates);
		assertEquals(MESSAGE_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
		mediaTypeTemplates = MimeUtils.ianaTemplateList(modelRegistry);
		assertNotNull(mediaTypeTemplates);
		assertEquals(MODEL_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
		mediaTypeTemplates = MimeUtils.ianaTemplateList(multipartRegistry);
		assertNotNull(mediaTypeTemplates);
		assertEquals(MULTIPART_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
		mediaTypeTemplates = MimeUtils.ianaTemplateList(textRegistry);
		assertNotNull(mediaTypeTemplates);
		assertEquals(TEXT_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
		mediaTypeTemplates = MimeUtils.ianaTemplateList(videoRegistry);
		assertNotNull(mediaTypeTemplates);
		assertEquals(VIDEO_MEDIATYPES_COUNT, mediaTypeTemplates.size());
		
	}
	
	@Test
	public void testByName() {
		LOGGER.debug("Executing testByName...");
		String name = "form-data";
		String template = "multipart/form-data";
		String reference = "[RFC7578]";
		
		MediaType mediaType = MimeUtils.byName(name);
		assertNotNull(mediaType);
		assertEquals(name, mediaType.getName());
		assertEquals(template, mediaType.getTemplate());
		assertEquals(reference, mediaType.getReference());

		mediaType = MimeUtils.byName(name, multipartRegistry);
		assertNotNull(mediaType);
		assertEquals(name, mediaType.getName());
		assertEquals(template, mediaType.getTemplate());
		assertEquals(reference, mediaType.getReference());
		
	}

	@Test
	public void testByTemplate() {
		LOGGER.debug("Executing testByTemplate...");
		String name = "form-data";
		String template = "multipart/form-data";
		String reference = "[RFC7578]";
		
		MediaType mediaType = MimeUtils.byTemplate(template);
		assertNotNull(mediaType);
		assertEquals(name, mediaType.getName());
		assertEquals(template, mediaType.getTemplate());
		assertEquals(reference, mediaType.getReference());
		
		mediaType = MimeUtils.byTemplate(template, multipartRegistry);
		assertNotNull(mediaType);
		assertEquals(name, mediaType.getName());
		assertEquals(template, mediaType.getTemplate());
		assertEquals(reference, mediaType.getReference());
		
	}

}
