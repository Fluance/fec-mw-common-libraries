package net.fluance.commons.xml;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;

public class XSLUtils {

	private static Logger LOGGER = LogManager.getLogger(XSLUtils.class);

	/**
	 * 
	 * @param xml
	 * @param xsltFile
	 * @return 
	 * @throws TransformerException 
	 */
	public static String transform(String xml, File xsltFile) throws TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Source xslt = new StreamSource(xsltFile);
		Transformer transformer = factory.newTransformer(xslt);
		Source text = new SAXSource(new InputSource(new StringReader(xml)));
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(text, result);
		return writer.toString();
	}


	/**
	 * 
	 * @param xml
	 * @param xsltInputStream
	 * @return
	 * @throws TransformerException
	 */
	public static String transform(String xml, InputStream xsltInputStream) throws TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Source xslt = new StreamSource(xsltInputStream);
		Transformer transformer = factory.newTransformer(xslt);
		Source text = new SAXSource(new InputSource(new StringReader(xml)));
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(text, result);
		return writer.toString();
	}

	/**
	 * 
	 * @param xml
	 * @param xsltFile
	 * @return
	 * @throws TransformerException
	 */
	public static String transform(String xml, String xsltFile) throws TransformerException {
		return transform(xml, new File(xsltFile));
	}

}
