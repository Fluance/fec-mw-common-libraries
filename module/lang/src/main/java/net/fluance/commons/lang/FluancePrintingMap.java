package net.fluance.commons.lang;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FluancePrintingMap<K, V> {

	private Map<K, V> map;

	public FluancePrintingMap(Map<K, V> map) {
		this.map = map;
	}

	/**
	 * Returns a string representation of the Map
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<K, V> entry = iterator.next();
			stringBuilder.append(entry.getKey());
			stringBuilder.append('=');
			if(entry.getValue() instanceof String[]){
				String[] values = (String[]) entry.getValue();
				stringBuilder.append("[");
				for(int i=0; i<values.length; i++){
					stringBuilder.append(values[i]);
					if (i<values.length-1){
						stringBuilder.append(',');
					}
				}
				stringBuilder.append("]");
			} else {
				stringBuilder.append(entry.getValue());
			}
			if (iterator.hasNext()) {
				stringBuilder.append(',').append(' ');
			}
		}
		return stringBuilder.toString();
	}

	public Map<K, V> getMap(){
		return this.map;
	}

}