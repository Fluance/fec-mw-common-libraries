package net.fluance.commons.net;

import java.util.List;
import java.util.Map;

public class MapUtils {

	private MapUtils() {}
	/***
	 * This function does check if a value exist for a given key
	 * @param map
	 * @param searchedKey
	 * @param searchedValue
	 * @return
	 */
	public static boolean isValueAvaiableForKey(Map<Integer, List<String>> map,int searchedKey ,String searchedValue) {
		return map.get(searchedKey) != null && map.get(searchedKey).contains(searchedValue);
	}
}
