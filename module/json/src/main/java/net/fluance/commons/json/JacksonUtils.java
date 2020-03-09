/**
 * 
 */
package net.fluance.commons.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JacksonUtils {

	private JacksonUtils() {}

	private static Logger LOGGER = LogManager.getLogger(JacksonUtils.class);

	/**
	 * 
	 * @param arrayNode
	 * @param toRemove
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayNode removeFromArrayNode(ArrayNode arrayNode, List<String> toRemove) {
		Iterator<JsonNode> unitsIterator = arrayNode.elements();
		List<String> currentElements = textNodesToStringsList(IteratorUtils.toList(unitsIterator));
		List<String> newArray = (List<String>) CollectionUtils.subtract(currentElements, toRemove);
		arrayNode.removeAll();
		for (String element : newArray) {
			arrayNode.add(element);
		}
		List<String> currentElementsAfterRemove = textNodesToStringsList(IteratorUtils.toList(arrayNode.elements()));
		if (!CollectionUtils.isEqualCollection(currentElementsAfterRemove, CollectionUtils.subtract(currentElements, toRemove))) {
			String msg = "New array must be " + CollectionUtils.subtract(currentElements, toRemove) + ", but is " + newArray;
			LOGGER.error("An IllegalStateException has occured: " + msg);
			throw new IllegalStateException(msg);
		}
		return arrayNode;
	}

	/**
	 * 
	 * @param textNodeList
	 * @return
	 */
	public static List<String> textNodesToStringsList(List<TextNode> textNodeList) {
		if (textNodeList == null) {
			return null;
		}
		List<String> textNodeStrList = new ArrayList<>();
		for (TextNode textNode : textNodeList) {
			textNodeStrList.add(textNode.textValue());
		}
		return textNodeStrList;
	}
}
