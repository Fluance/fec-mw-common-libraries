/**
 * 
 */
package net.fluance.commons.lang;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtils {

	private CollectionUtils() {}
	
	/**
	 * 
	 * @param containerCollection
	 * @param contentCollection
	 * @return
	 */
	public static boolean containsNoneOf(Collection<?> containerCollection, Collection<?> contentCollection) {
		if(containerCollection==null || containerCollection.isEmpty()) {
			return true;
		}
		if(contentCollection==null || contentCollection.isEmpty()) {
			return true;
		}
		boolean ok = true;
		Iterator<?> contentIterator = contentCollection.iterator();
		while(ok && contentIterator.hasNext()) {
			ok = !containerCollection.contains(contentIterator.next());
		}
		return ok;
	}
	
}
