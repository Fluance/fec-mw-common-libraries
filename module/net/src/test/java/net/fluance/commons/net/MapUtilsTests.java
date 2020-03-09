package net.fluance.commons.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

public class MapUtilsTests {

	Map<Integer, List<String>> config;

	@Before
	public void initialise() {
		config = new HashMap<>();
		List<String> values = new ArrayList<>();
		values.add("Nathalie");
		values.add("Caroline");
		values.add("Alessia");
		values.add("Cheryl");
		config.put(1, values);
	}

	@Test
	public void isValueAvaiableForKey_is_avaiable() {
		boolean nathalie = MapUtils.isValueAvaiableForKey(config, 1, "Nathalie");
		boolean caroline = MapUtils.isValueAvaiableForKey(config, 1, "Caroline");
		Assert.assertTrue("Is Nathalie available?", nathalie);
		Assert.assertTrue("Is Caroline available?", caroline);
	}
	
	@Test
	public void isValueAvaiableForKey_is_not_avaiable() {
		boolean melanie = MapUtils.isValueAvaiableForKey(config, 1, "Melanie");
		boolean cindy = MapUtils.isValueAvaiableForKey(config, 1, "Cindy");
		Assert.assertFalse("Is Melanie not available?", melanie);
		Assert.assertFalse("Is Cindy not available?", cindy);
	}
	
	@Test
	public void isValueAvaiableForKey_is_not_avaiable_on_the_key() {
		boolean nathalie = MapUtils.isValueAvaiableForKey(config, 2, "Nathalie");
		boolean caroline = MapUtils.isValueAvaiableForKey(config, 2, "Caroline");
		Assert.assertFalse("Is Nathalie not available?", nathalie);
		Assert.assertFalse("Is Caroline not available?", caroline);
	}
}
