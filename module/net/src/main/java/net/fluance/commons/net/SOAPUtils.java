package net.fluance.commons.net;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Document;

public class SOAPUtils {
	private SOAPUtils() {}
	
	/**
	 * This method sends a SOAP request to the given url
	 * @param soapMessage The request message
	 * @param url The SOAP endpoint
	 * @return
	 * @throws SOAPException
	 */
	public static SOAPMessage sendSOAPRequest(SOAPMessage soapMessage, String url) throws SOAPException{
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
		soapConnection.close();
		return soapResponse;
	}

	/**
	 * This method creates an XML Document form a Java object
	 * @param object
	 * @return
	 * @throws JAXBException
	 * @throws ParserConfigurationException
	 */
	public static Document createXMLDocumentFromObject(Object object, Class<?> objectClass) throws JAXBException, ParserConfigurationException{
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Marshaller  marshaller = JAXBContext.newInstance(objectClass).createMarshaller();
		marshaller.marshal(object, document);
		return document;
	}

	/**
	 * This methods creates an Object from an XML Document
	 * @param document
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static Object createObjectFromXMLDocument(Document document, Class<?> objectClass) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(objectClass);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return jaxbUnmarshaller.unmarshal(document);
	}
}
