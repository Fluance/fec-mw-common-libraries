/**
 * 
 */
package net.fluance.commons.json;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {

	private static Logger LOGGER = LogManager.getLogger(JsonUtils.class);
	public static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
	public static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";

	public static final JsonNode baseJsonArraySchema() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode schemaNode = mapper.createObjectNode();
		schemaNode.put(JSON_V4_SCHEMA_IDENTIFIER, JSON_SCHEMA_IDENTIFIER_ELEMENT);
		schemaNode.put("type", "array");
		ObjectNode itemsNode = mapper.createObjectNode();
		schemaNode.set("items", itemsNode);
		return schemaNode;
	}

	/**
	 * Checks if a String is a valid JSON
	 * 
	 * @param jsonStr
	 *            The String to validate as JSON
	 * @return
	 */
	public static final boolean validate(String jsonStr) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.readTree(jsonStr);
		} catch (IOException jpe) {
			LOGGER.error(jpe.getMessage(), jpe);
			return false;
		}
		return true;
	}

	/**
	 * Checks if a String is a JSON array
	 * 
	 * @param jsonStr
	 *            The String to validate as JSON
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static final boolean isArray(String jsonStr) throws JsonProcessingException, IOException {
		JsonNode jsonNode = null;
		if (validate(jsonStr)) {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonNode = objectMapper.readTree(jsonStr);
		}
		return (jsonNode != null) ? jsonNode.isArray() : false;
	}

	/**
	 * Checks if a String is a valid JSON array
	 * 
	 * @param jsonStr
	 *            The String to validate as JSON
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static final boolean isObject(String jsonStr) throws JsonProcessingException, IOException {
		JsonNode jsonNode = null;
		if (validate(jsonStr)) {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonNode = objectMapper.readTree(jsonStr);
		}
		return (jsonNode != null) ? jsonNode.isObject() : false;
	}

	/**
	 * Checks if a String can be Deserialized into and Object
	 * 
	 * @param jsonStr
	 * @param valueType
	 *            : Class to check
	 * @return true if jsonStr can be serialized to Class<?> valueType , else false
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static boolean checkJsonCompatibility(String jsonStr, Class<?> valueType) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			Object obj = mapper.readValue(jsonStr, valueType);
			return obj != null;
		} catch (JsonMappingException | JsonParseException e) {
			return false;
		}
	}
}
