/**
 * 
 */
package net.fluance.commons.xml;

import java.util.List;

public class XacmlUtils {

	private XacmlUtils() {}

	public static String simpleRequest(String resource, String action, String subjectId, List<String> subjectRoles) {
		String request = "<Request xmlns=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\"\n" + "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + "    <Resource>\n"
				+ "        <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\"\n" + "                   DataType=\"http://www.w3.org/2001/XMLSchema#string\">\n" + "            <AttributeValue>" + resource
				+ "</AttributeValue>\n" + "        </Attribute>\n" + "    </Resource>\n" + "    <Subject>\n";
		request += (subjectId != null && !subjectId.isEmpty()) ? "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"\n" + "                   DataType=\"http://www.w3.org/2001/XMLSchema#string\">\n"
				+ "            <AttributeValue>" + subjectId + "</AttributeValue>\n" + "        </Attribute>\n" : "";
		if (subjectRoles != null && !subjectRoles.isEmpty()) {
			for (String role : subjectRoles) {
				request += "<Attribute AttributeId=\"http://wso2.org/claims/role\"\n" + "                   DataType=\"http://www.w3.org/2001/XMLSchema#string\">\n" + "            <AttributeValue>" + role + "</AttributeValue></Attribute>";
			}
		}
		request += "    </Subject>\n" + "    <Action>\n" + "        <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\"\n" + "                   DataType=\"http://www.w3.org/2001/XMLSchema#string\">\n"
				+ "            <AttributeValue>" + action + "</AttributeValue>\n" + "        </Attribute>\n" + "    </Action>\n" + "    <Environment/>\n" + "</Request>";
		return request;
	}
}
