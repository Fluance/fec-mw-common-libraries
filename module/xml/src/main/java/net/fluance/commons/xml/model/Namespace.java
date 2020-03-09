/**
 * 
 */
package net.fluance.commons.xml.model;

public class Namespace {

	private String uri;
	private String prefix;
	
	public Namespace() {}
	/**
	 * @param uri
	 * @param prefix
	 */
	public Namespace(String uri, String prefix) {
		this.uri = uri;
		this.prefix = prefix;
	}
	
	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
