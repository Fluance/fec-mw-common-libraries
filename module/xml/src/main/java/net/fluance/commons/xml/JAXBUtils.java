/**
 * 
 */
package net.fluance.commons.xml;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBUtils {
	private JAXBUtils() {}
	
	/**
	 * 
	 * @param object
	 * @param outStream
	 * @throws JAXBException
	 */
	public static void marshall(Object object, OutputStream outStream) throws JAXBException {
		JAXBContext jAXBContext = JAXBContext.newInstance( object.getClass() );
		Marshaller marshaller = jAXBContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal( object, outStream );
	}

}
