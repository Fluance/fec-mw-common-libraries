package net.fluance.commons.lang;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HTMLUtils {

	private static Logger LOGGER = LogManager.getLogger(HTMLUtils.class);
	
	public static String cleanStringFromHtml(String htmlInput){
		if(htmlInput == null){
			return "";
		}
		HTMLEditorKit parser = new HTMLEditorKit();
		Document document = parser.createDefaultDocument();
		Reader reader = new StringReader(htmlInput);
		try {
			parser.read(reader, document, 0);
			String cleanText = document.getText(0, document.getLength());
			if(cleanText != null && !cleanText.isEmpty()){
				return cleanText.replaceAll("\n", "");
			} else {
				return htmlInput;
			}
		} catch (IOException | BadLocationException e) {
			LOGGER.error(e.getMessage(), e);
			return htmlInput;
		}
	}
}
