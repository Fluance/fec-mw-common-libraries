package net.fluance.commons.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.fluance.commons.xml.model.Namespace;

public class XMLUtils {

	private XMLUtils() {}

	private static Logger LOGGER = LogManager.getLogger(XMLUtils.class);

	/**
	 * Creates an empty DOM document
	 * 
	 * @return A new empty document
	 */
	public static Document createDocument() {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Load DOM document from java.io.File
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document loadDocument(File file) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(file);
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Document loadDocument(String filePath) throws SAXException, IOException, ParserConfigurationException {
		return loadDocument(new File(filePath));
	}

	/**
	 * Creates and returns a Document object that can be used to build XML Documents
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document createNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.newDocument();
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document readXML(File file) throws ParserConfigurationException, SAXException, IOException {
		if (file == null || !file.exists()) {
			String message = "File " + file + " doesn't exist!";
			LOGGER.error("A FileNotFoundException has occured: " + message);
			throw new FileNotFoundException(message);
		}
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		document.getDocumentElement().normalize();
		return document;
	}

	/**
	 * Returns an XML Document from the given String
	 * 
	 * @param string
	 * @return doc
	 * @throws Exception
	 */
	public static Document xmlStringToDocument(String xmlString) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		InputSource inputSource = new InputSource(new StringReader(xmlString));
		return documentBuilder.parse(inputSource);
	}

	/**
	 * 
	 * @param xmlString
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static DocumentFragment xmlStringToDocumentFragment(String xmlString) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		InputSource inputSource = new InputSource(new StringReader(xmlString));
		Document document = documentBuilder.parse(inputSource);
		return document.createDocumentFragment();
	}

	/**
	 * 
	 * @param source
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document xmlFromString(String source) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource inputSource = new InputSource();
		inputSource.setCharacterStream(new StringReader(source));
		return documentBuilder.parse(inputSource);
	}

	/**
	 * 
	 * @param doc
	 * @param xpathExpression
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	public static NodeList nodeSet(Document doc, String xpathExpression) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		return (NodeList) xPath.evaluate(xpathExpression, doc.getDocumentElement(), XPathConstants.NODESET);
	}

	/**
	 * 
	 * @param rootNode
	 * @param xpathExpression
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	public static NodeList queryNodeSet(Node rootNode, String xpathExpression) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		return (NodeList) xPath.evaluate(xpathExpression, rootNode, XPathConstants.NODESET);
	}

	public static List<String> queryNodesTextContentList(Node rootNode, String xpathExpression) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		List<String> strings = new ArrayList<>();
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList) xPath.evaluate(xpathExpression, rootNode, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			strings.add(nodes.item(i).getTextContent());
		}
		return strings;
	}

	/**
	 * 
	 * @param rootNode
	 * @param xpathExpression
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	public static Node queryNode(Node rootNode, String xpathExpression) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		return (Node) xPath.evaluate(xpathExpression, rootNode, XPathConstants.NODE);
	}

	/**
	 * 
	 * @param rootNode
	 * @param xpathExpression
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	public static String queryString(Node rootNode, String xpathExpression) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		return (String) xPath.evaluate(xpathExpression, rootNode, XPathConstants.STRING);
	}

	/**
	 * 
	 * @param rootNode
	 * @param xpathExpression
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	public static Number queryNumber(Node rootNode, String xpathExpression) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		return (Number) xPath.evaluate(xpathExpression, rootNode, XPathConstants.NUMBER);
	}

	/**
	 * 
	 * @param node
	 * @return
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static String nodeToString(Node node) throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter stringWriter = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(node), new StreamResult(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * Use it when the source data contains unicode data which is not allowed in XML
	 * 
	 * @param responseXML
	 * @return
	 */
	public static String cleanXml(String responseXML) {
		return responseXML.replaceAll("[\\000]*", "");
	}

	/**
	 * 
	 * @param xmlDoc
	 * @param publicKey
	 * @return
	 */
	public static boolean validateSignature(Document xmlDoc, Namespace signatureNamespace, String signatureLocalName, PublicKey publicKey) {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xPath.evaluate("/Assertion/" + signatureLocalName, xmlDoc.getDocumentElement(), XPathConstants.NODESET);
			if (nodes == null || nodes.getLength() != 1) {
				throw new IllegalArgumentException("Invalid signed XML document");
			}
			Element signatureElement = (Element) nodes.item(0);
			XMLValidateContext validateContext = new DOMValidateContext(publicKey, signatureElement);
			XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
			XMLSignature signature = signatureFactory.unmarshalXMLSignature(validateContext);
			return signature.validate(validateContext);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 
	 * @param source
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document xmlDocument(InputStream inputStream, boolean namespaceAware) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(namespaceAware);
		DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
		return docBuilder.parse(inputStream);
	}
}
