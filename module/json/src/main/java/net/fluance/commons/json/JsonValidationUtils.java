package net.fluance.commons.json;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JsonValidationUtils {

	private JsonValidationUtils() {}

	public static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
	public static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";

	@Deprecated
	public static JsonNode getJsonNode(String jsonText) throws IOException {
		return JsonLoader.fromString(jsonText);
	}

	@Deprecated
	public static JsonNode getJsonNode(File jsonFile) throws IOException {
		return JsonLoader.fromFile(jsonFile);
	}

	@Deprecated
	public static JsonNode getJsonNode(URL url) throws IOException {
		return JsonLoader.fromURL(url);
	}

	@Deprecated
	public static JsonNode getJsonNodeFromResource(String resource) throws IOException {
		return JsonLoader.fromResource(resource);
	}

	/**
	 * 
	 * @param schemaText
	 * @return
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static JsonSchema getSchemaNode(String schemaText) throws IOException, ProcessingException {
		final JsonNode schemaNode = JsonLoader.fromString(schemaText);
		return getNodeFromSchema(schemaNode);
	}

	/**
	 * 
	 * @param schemaFile
	 * @return
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static JsonSchema getSchemaNode(File schemaFile) throws IOException, ProcessingException {
		final JsonNode schemaNode = JsonLoader.fromFile(schemaFile);
		return getNodeFromSchema(schemaNode);
	}

	/**
	 * 
	 * @param schemaFile
	 * @return
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static JsonSchema getSchemaNode(URL schemaFile) throws IOException, ProcessingException {
		final JsonNode schemaNode = JsonLoader.fromURL(schemaFile);
		return getNodeFromSchema(schemaNode);
	}

	/**
	 * 
	 * @param resource
	 * @return
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static JsonSchema getSchemaNodeFromResource(String resource) throws IOException, ProcessingException {
		final JsonNode schemaNode = JsonLoader.fromResource(resource);
		return getNodeFromSchema(schemaNode);
	}

	/**
	 * 
	 * @param jsonSchemaNode
	 * @param jsonNode
	 * @throws ProcessingException
	 */
	public static void validateJson(JsonSchema jsonSchemaNode, JsonNode jsonNode) throws ProcessingException {
		ProcessingReport report = jsonSchemaNode.validate(jsonNode);
		if (!report.isSuccess()) {
			for (ProcessingMessage processingMessage : report) {
				throw new ProcessingException(processingMessage);
			}
		}
	}

	/**
	 * 
	 * @param jsonSchemaNode
	 * @param jsonNode
	 * @return
	 * @throws ProcessingException
	 */
	public static boolean isJsonValid(JsonSchema jsonSchemaNode, JsonNode jsonNode) throws ProcessingException {
		return jsonSchemaNode.validate(jsonNode).isSuccess();
	}

	/**
	 * 
	 * @param schemaText
	 * @param jsonText
	 * @return
	 * @throws ProcessingException
	 * @throws IOException
	 */
	public static boolean isJsonValid(String schemaText, String jsonText) throws ProcessingException, IOException {
		final JsonSchema schemaNode = getSchemaNode(schemaText);
		final JsonNode jsonNode = JsonLoader.fromString(jsonText);
		if (jsonNode.isArray()) {
			boolean isValid = true;
			Iterator<JsonNode> i = jsonNode.iterator();
			while (i.hasNext()) {
				isValid = isValid & isJsonValid(schemaNode, i.next());
			}
			return isValid;
		} else {
			return isJsonValid(schemaNode, jsonNode);
		}
	}

	/**
	 * 
	 * @param schemaFile
	 * @param jsonFile
	 * @return
	 * @throws ProcessingException
	 * @throws IOException
	 */
	public static boolean isJsonValid(File schemaFile, File jsonFile) throws ProcessingException, IOException {
		final JsonSchema schemaNode = getSchemaNode(schemaFile);
		final JsonNode jsonNode = JsonLoader.fromFile(jsonFile);
		return isJsonValid(schemaNode, jsonNode);
	}

	/**
	 * 
	 * @param schemaURL
	 * @param jsonURL
	 * @return
	 * @throws ProcessingException
	 * @throws IOException
	 */
	public static boolean isJsonValid(URL schemaURL, URL jsonURL) throws ProcessingException, IOException {
		final JsonSchema schemaNode = getSchemaNode(schemaURL);
		final JsonNode jsonNode = JsonLoader.fromURL(jsonURL);
		return isJsonValid(schemaNode, jsonNode);
	}

	/**
	 * 
	 * @param schemaText
	 * @param jsonText
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static void validateJson(String schemaText, String jsonText) throws IOException, ProcessingException {
		final JsonSchema schemaNode = getSchemaNode(schemaText);
		final JsonNode jsonNode = JsonLoader.fromString(jsonText);
		validateJson(schemaNode, jsonNode);
	}

	/**
	 * 
	 * @param schemaFile
	 * @param jsonFile
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static void validateJson(File schemaFile, File jsonFile) throws IOException, ProcessingException {
		final JsonSchema schemaNode = getSchemaNode(schemaFile);
		final JsonNode jsonNode = JsonLoader.fromFile(jsonFile);
		validateJson(schemaNode, jsonNode);
	}

	/**
	 * 
	 * @param schemaDocument
	 * @param jsonDocument
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static void validateJson(URL schemaDocument, URL jsonDocument) throws IOException, ProcessingException {
		final JsonSchema schemaNode = getSchemaNode(schemaDocument);
		final JsonNode jsonNode = JsonLoader.fromURL(jsonDocument);
		validateJson(schemaNode, jsonNode);
	}

	/**
	 * 
	 * @param schemaResource
	 * @param jsonResource
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public static void validateJsonResource(String schemaResource, String jsonResource) throws IOException, ProcessingException {
		final JsonSchema schemaNode = getSchemaNode(schemaResource);
		final JsonNode jsonNode = JsonLoader.fromResource(jsonResource);
		validateJson(schemaNode, jsonNode);
	}

	/**
	 * 
	 * @param jsonNode
	 * @return
	 * @throws ProcessingException
	 */
	private static JsonSchema getNodeFromSchema(JsonNode jsonNode) throws ProcessingException {
		final JsonNode schemaIdentifier = jsonNode.get(JSON_SCHEMA_IDENTIFIER_ELEMENT);
		if (null == schemaIdentifier) {
			((ObjectNode) jsonNode).put(JSON_SCHEMA_IDENTIFIER_ELEMENT, JSON_V4_SCHEMA_IDENTIFIER);
		}
		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		return factory.getJsonSchema(jsonNode);
	}
}