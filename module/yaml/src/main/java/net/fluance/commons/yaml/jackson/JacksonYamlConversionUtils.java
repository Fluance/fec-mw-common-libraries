/**
 * 
 */
package net.fluance.commons.yaml.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class JacksonYamlConversionUtils {

	private JacksonYamlConversionUtils() {}

	/**
	 * 
	 * @param yamlSource
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static JsonNode toJson(String yamlSource) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		return mapper.readTree(yamlSource);
	}
}
