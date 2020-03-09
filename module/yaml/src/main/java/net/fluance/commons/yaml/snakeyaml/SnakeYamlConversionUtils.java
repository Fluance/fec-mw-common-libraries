package net.fluance.commons.yaml.snakeyaml;

import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

public class SnakeYamlConversionUtils {

	private SnakeYamlConversionUtils() {}


	/**
	 * @deprecated Could fail if root is list or primitive value. Most general solution is given by
	 *             {@link #toJson(java.lang.String)}. Use {@link #toJson(java.lang.String)} instead
	 * @param yamlSource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String convertToJson(String yamlSource) {
		Yaml yaml = new Yaml();
		Map<String, Object> map = (Map<String, Object>) yaml.load(yamlSource);
		return new JSONObject(map).toString();
	}

	/**
	 * 
	 * @param yamlSource
	 * @return
	 */
	public static String toJson(String yamlSource) {
		Yaml yaml = new Yaml();
		Object yamlObj = yaml.load(yamlSource);
		return JSONObject.valueToString(yamlObj);
	}
}
