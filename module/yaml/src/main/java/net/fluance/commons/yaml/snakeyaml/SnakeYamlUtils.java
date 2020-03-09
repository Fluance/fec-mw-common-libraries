package net.fluance.commons.yaml.snakeyaml;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

public class SnakeYamlUtils {

	private SnakeYamlUtils() {}

	@Deprecated
	public static Object load(InputStream is) {
		return new Yaml().load(is);
	}
}
