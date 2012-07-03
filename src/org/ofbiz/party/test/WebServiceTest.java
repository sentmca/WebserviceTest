package org.ofbiz.party.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.ofbiz.party.test.config.ResourceMgr;
import org.ofbiz.party.test.response.RESPONSE;
import org.ofbiz.party.test.webservice.WEBSERVICE;
import org.ofbiz.party.test.webservice.WEBSERVICE.RESPONSE.RESPTRANSACTOINDETAILS;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import etm.core.renderer.SimpleTextRenderer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WebServiceTest {
	
	 private static EtmMonitor monitor;
	 private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();
	
	public static void main(String[] args) {

		try {
			System.out.println(postXmlString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String postXmlString() throws IOException {
		
		/* Setup Jetm */
		setup();

		FileHandler hand = new FileHandler("Debug2.log", true);
		Logger log = Logger.getLogger("LoggingExample1");

		log.setLevel(Level.INFO);
		hand.setFormatter(new SimpleFormatter());
		log.addHandler(hand);

		HttpClient client = new HttpClient();
//		 String url ="https://striderite.groupfio.com/crmsfa/control/updateCustomer";
//		 String url ="https://strideriteqa.groupfio.com/crmsfa/control/atgCustomerCertificateSearch";
//		 String url ="https://strideriteqa.groupfio.com/crmsfa/control/atgDistinctLoyaltySearch";
//		 String url ="https://strideriteqa.groupfio.com/crmsfa/control/getEarnedPointsForAtgOrder";
//		 String url = "https://striderite.groupfio.com/crmsfa/control/getAvailableBalancePointsByAtgCustomer";
		 String url = "http://strideritept.groupfio.com/crmsfa/control/getAvailableBalancePointsByAtgCustomer";
//		 String url ="http://localhost:3311/crmsfa/control/getAvailableBalancePointsByAtgCustomer";

		try {
			FileInputStream fstream = new FileInputStream(
					"TestFiles\\getCustomerPoint.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String InputXml = strLine;

				if(InputXml==null || "".equals(InputXml))continue;
				PostMethod method = new PostMethod(url);
				// String[] loyId = strLine.split("#");
				method.setParameter("XmlInput", InputXml);

				// method.setParameter("XmlInput","<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><WEBSERVICE><REQUEST><REQ_COUPON_DETAILS><REQ_COUPON_CODE>"+loyId[0].trim()+"</REQ_COUPON_CODE><REQ_COUPON_TYPE>SERIALIZE</REQ_COUPON_TYPE></REQ_COUPON_DETAILS><REQ_TRANSACTION_DETAILS><REQ_STORE_ID>"+loyId[1].trim()+"</REQ_STORE_ID><REQUEST_DATE>2011-09-30 13:17:00</REQUEST_DATE></REQ_TRANSACTION_DETAILS></REQUEST></WEBSERVICE>");

				log.info("XML INPUT: \n" + InputXml.trim());
				InputStream response = null;
				
				EtmPoint point = etmMonitor.createPoint("WebService:Post");
				client.executeMethod(method);
				point.collect();
				
				response = method.getResponseBodyAsStream();
				//method.removeParameter("XmlInput");
				String line;
				StringBuilder sb = new StringBuilder();

				String outPutXml = "";
				try {
					outPutXml = convertStreamToString(response);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				log.info("ResponseXml:" + outPutXml);

				log.info("XML OutPut: \n" + sb.toString());

				String resPonseCode = getStatusCode(outPutXml);

				if (resPonseCode != null) {
					log.info("Response Code:"
							+ resPonseCode
							+ " : "
							+ ResourceMgr
									.getResourceFromConfigBundle(resPonseCode));
				} else {
					log.info("Response Code:" + resPonseCode);
				}

				if (resPonseCode != null && resPonseCode.startsWith("S")) {
					log.info("Returened Success.");
				} else {
					log.warning("Service Failed.");
					// break;
				}
				
				method.releaseConnection();
			}
			in.close();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    etmMonitor.render(new SimpleTextRenderer());
		tearDown();
		return "Success";

	}


	
	public static String getStatusCode(String xml){
		
		EtmPoint point = etmMonitor.createPoint("WebService:ParsingXmlOutput");
		String out=null;
		try {
			  DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			  InputSource is = new InputSource();
			  is.setCharacterStream(new StringReader(xml));
			  
			  Document doc;
			  doc = db.parse(is);
			  NodeList nodes = doc.getElementsByTagName("RESPONSE_CODE");
			
			  Element line = (Element) nodes.item(0);
			  
			  if(line!=null)
			  out = getCharacterDataFromElement(line);
			  
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	    
		point.collect();
		return out;
	}
	
	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      return cd.getData();
	    }
	    return "";
	  }
	
	
	public static String convertStreamToString(InputStream is) throws Exception {
		
		EtmPoint point = etmMonitor.createPoint("WebService:InputStreamToString");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		point.collect();
		return sb.toString();
	}
	
	private static void setup() {
	    BasicEtmConfigurator.configure();
	    monitor = EtmManager.getEtmMonitor();
	    monitor.start();
	  }

	  private static void tearDown() {
	    monitor.stop();
	  }

}
