/**
 * 
 */
package net.fluance.commons.xml.jdom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;


public class JdomUtils {
	private JdomUtils() {}
	
	/**
	 * Converts the element into a String and applies the given format to the
	 * output
	 * 
	 * @param element
	 * @param format
	 * @return
	 * @throws IOException
	 */
	public static String toXmlFragmentString(Element element, Format format)
			throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(format);
		xmlOutput.output(element, stream);
		return stream.toString();
	}

	/**
	 * Converts the document into a String and applies the given format to the
	 * output. This
	 * 
	 * @param document
	 * @param format
	 * @return
	 * @throws IOException
	 */
	public static String toXmlDocumentString(Document document, Format format)
			throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(format);
		xmlOutput.output(document, stream);
		return stream.toString();
	}

	/**
	 * 
	 * @param element
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static org.w3c.dom.Element toDomElement(Element element)
			throws ParserConfigurationException, SAXException, IOException {
		org.w3c.dom.Element dom = null;
		if (element != null) {
			ByteArrayInputStream in = new ByteArrayInputStream(element
					.toString().getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(in).getDocumentElement();
		}
		return dom;
	}

	/**
	 * Converts a XML jdom document to a org.w3c.dom.Document 
	 * @param document the initial jdom XML document
	 * @param format the format to apply to the generated org.w3c.dom.Document 
	 * @return the org.w3c.dom.Document representation of the given jdom document
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	protected org.w3c.dom.Document toDomDocument(Document document, Format format)
			throws IOException, ParserConfigurationException, SAXException {
		org.w3c.dom.Document dom = null;
		if (document != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(format);
			xmlOutput.output(document, byteArrayOutputStream);
			ByteArrayInputStream in = new ByteArrayInputStream(
					byteArrayOutputStream.toByteArray());
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(false);
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			dom = documentBuilder.parse(in);
		}
		return dom;
	}

	/**
	 * 
	 * @param element
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static org.w3c.dom.Element toDom(Element element)
			throws ParserConfigurationException, SAXException, IOException {
		return toDomElement(element);
	}

	/**
	 * 
	 * @param document
	 * @param format
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	protected org.w3c.dom.Document toDom(Document document, Format format)
			throws IOException, ParserConfigurationException, SAXException {
		return toDomDocument(document, format);
	}
}
