package net.fluance.commons.xml;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLSoapUtils {

	private XMLSoapUtils() {}

	/**
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Element basicSoapEnvelop() throws ParserConfigurationException {
		Document document = XMLUtils.createNewDocument();
		Element envelop = document.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "soapenv:Envelope");
		document.appendChild(envelop);
		Element body = document.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "soapenv:Body");
		envelop.appendChild(body);
		return envelop;
	}
}
