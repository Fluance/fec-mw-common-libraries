package net.fluance.commons.yaml.test.snakeyaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import net.fluance.commons.yaml.snakeyaml.SnakeYamlConversionUtils;
import net.fluance.commons.yaml.snakeyaml.SnakeYamlUtils;

public class YamlUtilsTest {

	private static Logger LOGGER = LogManager.getLogger(SnakeYamlConversionUtils.class);
	
	@Test
	public void mustLoadWithSnakeYamlTest() throws FileNotFoundException {
		LOGGER.debug("Executing mustLoadWithSnakeYamlTest...");
		Map<String, Object> expected = new HashMap<>();
		expected.put("prop1", "value1");
		expected.put("prop2", "value2");
		InputStream is = new FileInputStream(getClass().getResource("/yaml/yaml1.yaml").getFile());
		@SuppressWarnings("unchecked")
		Map<String, Object> yaml = (Map<String, Object>) SnakeYamlUtils.load(is);
		assertNotNull(yaml);
		assertEquals(expected, yaml);
	}
	
}
