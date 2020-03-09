package net.fluance.commons.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

public class ValidationUtilsTest {
	
	private static Logger LOGGER = LogManager.getLogger(ValidationUtilsTest.class);
	
	@Test
	public void testIsJsonFileValidAgainstSchema() throws ProcessingException, IOException {
		LOGGER.debug("Executing testIsJsonFileValidAgainstSchema...");
	    File schemaFile = new File(getClass().getResource("/jsons/schema.json").getFile());
	    File jsonFile = new File(getClass().getResource("/jsons/data.json").getFile());
	    File wrongjsonFile = new File(getClass().getResource("/jsons/wrongData.json").getFile());
	    
	    assertTrue(JsonValidationUtils.isJsonValid(schemaFile, jsonFile));
	    assertFalse(JsonValidationUtils.isJsonValid(schemaFile, wrongjsonFile));

	}
	
	@Test
	public void testIsJsonStringValidAgainstSchema() throws ProcessingException, IOException {
	}

	@Test
	public void testIsJsonNodeValidAgainstSchema() throws ProcessingException, IOException {
	}
}
