package net.fluance.commons.lang;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import junit.framework.TestCase;

public class HTMLUtilsTest extends TestCase {
	
	private static Logger LOGGER = LogManager.getLogger(HTMLUtilsTest.class);
	
	@Test
	public void testCleanStringFromRealRtf(){
		LOGGER.debug("Executing testCleanStringFromRealRtf...");  
		String html = "<div class=\"wrap\"><strong class=\"name\">Type:</strong><span id=\"type-val\" class=\"value editable-field inactive\" title=\"Click to edit\"> <img alt=\"\" height=\"16\" src=\"/images/icons/issuetypes/task.png\" title=\"Task - A task that needs to be done.\" width=\"16\"> Task <span class=\"overlay-icon aui-icon aui-icon-small aui-iconfont-edit\"></span></span></div>";
		String result = HTMLUtils.cleanStringFromHtml(html);
		System.out.println("result = " + result);
//		assertEquals(expected, result);
	}

	@Test
	public void testCleanStringFromString(){
		LOGGER.debug("Executing testCleanStringFromString...");
		String html = "This is just a simple String";
		String expected = "This is just a simple String";
		String result = HTMLUtils.cleanStringFromHtml(html);
//		System.out.println("result = " + result);
		assertEquals(expected, result);
	}

	@Test
	public void testCleanStringFromNull(){
		LOGGER.debug("Executing testCleanStringFromNull...");
		String html = null;
		String expected = "";
		String result = HTMLUtils.cleanStringFromHtml(html);
//		System.out.println("result = " + result);
		assertEquals(expected, result);
	}

}
