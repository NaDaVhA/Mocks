package core;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Configurator {
	
	
	/**
	 * Extracts connection parameters from the configuration file.
	 * @param filename
	 * @return Array of strings holding the parameters:
	 * arr[0] = hostname;
	 * arr[1] = port;
	 * arr[2] = databaseName;
	 * arr[3] = username;
	 * arr[4] = password;
	 */
	public static String[] getConnectionParameters(String filename){
		
		String[] connectionParameters = new String[5];
		
		try {
			
			File stocks = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();
	
			
			NodeList nodes = doc.getElementsByTagName("Configuration");
			Node node = nodes.item(0);
			Element element = (Element) node;
			
			connectionParameters[0] = new String(getValue("hostname", element));
			connectionParameters[1] = new String(getValue("port", element));
			connectionParameters[2] = new String(getValue("databaseName", element));
			connectionParameters[3] = new String(getValue("username", element));
			connectionParameters[4] = new String(getValue("password", element));
							
			}catch(Exception ex) {
				
				System.out.println("getConnectionParameters: Error retreiving parameters.");

				ex.printStackTrace();
				
				return null;
			}finally{

			}
			
		return connectionParameters;
	}
	
	
	/**
	 * Extracts a path to the Yago files' folder from the configuration file.
	 * @param filename
	 * @return A string holding the path to the Yago files' folder.
	 */
	public static String getYagoFolderPath(String filename){
		
		String yagoFolderPath = null;
		
		try {
			
			File stocks = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();
	
			
			NodeList nodes = doc.getElementsByTagName("Configuration");
			Node node = nodes.item(0);
			Element element = (Element) node;
			
			yagoFolderPath = new String(getValue("YagoFolderPath", element));
						
			}catch(Exception ex) {
				
				System.out.println("getYagoFolderPath: Error retreiving parameter.");

				ex.printStackTrace();
				
				return null;
				
			}finally{

			}
			
		return yagoFolderPath;
	}
	
	
	/**
	 * Extracts a certain value by tag from the given XML element.
	 * @param tag
	 * @param element
	 * @return The requested value as a string.
	 */
	private static String getValue(String tag, Element element) {
		
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
	

}
