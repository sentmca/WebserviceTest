package org.ofbiz.party.test;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.ofbiz.party.test.response.RESPONSE;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Main {
	public static void main(String[] args){
	
		try {
			System.out.println(postXmlString());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		}
	
 public static String postXmlString() throws IOException{
		  FileHandler hand = new FileHandler("Debug.log", true);
		  Logger log = Logger.getLogger("LoggingExample1");

	      log.setLevel(Level.INFO);
	      hand.setFormatter(new SimpleFormatter());
	      log.addHandler(hand);
			
		  HttpClient client = new HttpClient( );
		  String url = "http://strideritept.groupfio.com/crmsfa/control/updateCustomer";
		  PostMethod method = new PostMethod( url );
		  
		  try {
		  FileInputStream fstream = new FileInputStream("CreateCustomer.txt");
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  while ((strLine = br.readLine()) != null)   {
			String InputXml=strLine;
			method.setParameter("XmlInput",InputXml);
			log.info("XML INPUT: \n"+InputXml);
		    InputStream response = null;
			client.executeMethod( method );
			response = method.getResponseBodyAsStream( );
			log.info("XML OutPut: \n"+response);
			
			String resPonseCode =validateOutPut(response);
			log.info("Response Code:"+resPonseCode);
			
			if(resPonseCode!=null && resPonseCode.startsWith("S")){
				log.info("Returened Success.");
			}else{
				log.warning("Service Failed.");
			}
			
		  }
		  
		  
//		  	String InputXml="<?xml version=\"1.0\" encoding=\"UTF-8\"?> <REQUEST>    <CUSTOMER>     <FIRST_NAME>Hollie</FIRST_NAME>     <LAST_NAME>Sailors</LAST_NAME>     <CUSTOMER_ID></CUSTOMER_ID> 	<ASSIGNED_STORE>00200</ASSIGNED_STORE>     <CUSTOMER_CROSS_REFERENCES>           </CUSTOMER_CROSS_REFERENCES>     <POSTAL_CONTACTS>       <CONTACT>         <TO_NAME>Hollie Sailors</TO_NAME>         <ADDRESS1>10210 Pearl Dr</ADDRESS1>         <ADDRESS2/>         <CITY>HOUSTON</CITY>         <STATE>TX</STATE>         <ZIPCODE>77064</ZIPCODE>         <COUNTRY>USA</COUNTRY>         <CONTACT_MECH_ID></CONTACT_MECH_ID>         <CUSTOMER_MECH_REFERENCES/>         <CONTACT_TYPE>GENERAL_LOCATION</CONTACT_TYPE>       </CONTACT>     </POSTAL_CONTACTS>     <EMAIL_CONTACTS>       <CONTACT>         <EMAIL>jhsailors@comcast.net</EMAIL>         <CONTACT_MECH_ID></CONTACT_MECH_ID>         <CUSTOMER_MECH_REFERENCES/>         <CONTACT_TYPE>PRIMARY_EMAIL</CONTACT_TYPE>       </CONTACT>     </EMAIL_CONTACTS>     <PHONE_CONTACTS>       <CONTACT>         <PHONE>2819703595</PHONE>         <CONTACT_MECH_ID></CONTACT_MECH_ID>         <CUSTOMER_MECH_REFERENCES/>         <CONTACT_TYPE>PRIMARY_PHONE</CONTACT_TYPE>       </CONTACT>     </PHONE_CONTACTS>     <LOYALTY_ID/>   </CUSTOMER> </REQUEST>";
//			method.setParameter("XmlInput",InputXml);
//			log.info("\n XML INPUT CREATE CUSTOMER: \n"+InputXml);
//		    InputStream response = null;
//			client.executeMethod( method );
//			response = method.getResponseBodyAsStream( );
//			//log.info("\n XML OutPut: \n"+response);
		  
			
		  
//		in.close();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 // System.out.println( response );
		  method.releaseConnection( );
		return "Success";
		
	}
	
	
	public static String validateOutPut(InputStream response){
		String resPonseCode=null;
		try{
	            JAXBContext jc = JAXBContext.newInstance(RESPONSE.class);
	            Unmarshaller u = jc.createUnmarshaller();
	 
	            RESPONSE responses = (RESPONSE) u.unmarshal(response);
	            resPonseCode = responses.getRESPONSECODE();
	       
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
		
		return resPonseCode;
	}
	
}
