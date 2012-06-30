package org.ofbiz.party.test;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.ofbiz.party.test.response.RESPONSE;
import org.ofbiz.party.test.webservice.WEBSERVICE;
import org.ofbiz.party.test.webservice.WEBSERVICE.RESPONSE.RESPTRANSACTOINDETAILS;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
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
		  String url = "http://strideritept.groupfio.com/crmsfa/control/couponPost";
		  PostMethod method = new PostMethod( url );
		  
		  try {
		  FileInputStream fstream = new FileInputStream("CouponTest.txt");
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  while ((strLine = br.readLine()) != null)   {
			String InputXml=strLine;
			
			String[] loyId = strLine.split("#");
			
			method.setParameter("XmlInput","<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><WEBSERVICE><REQUEST><REQ_COUPON_DETAILS><REQ_COUPON_CODE>"+loyId[0].trim()+"</REQ_COUPON_CODE><REQ_COUPON_TYPE>SERIALIZE</REQ_COUPON_TYPE></REQ_COUPON_DETAILS><REQ_TRANSACTION_DETAILS><REQ_STORE_ID>"+loyId[1].trim()+"</REQ_STORE_ID><REQUEST_DATE>2011-09-30 13:17:00</REQUEST_DATE></REQ_TRANSACTION_DETAILS></REQUEST></WEBSERVICE>");
			log.info("XML INPUT: \n"+InputXml);
		    InputStream response = null;
			client.executeMethod( method );
			response = method.getResponseBodyAsStream( );
			log.info("XML OutPut: \n"+response);
			
			String resPonseCode =validateWebservice(response);
			log.info("Response Code:"+resPonseCode);
			
			if(resPonseCode!=null && resPonseCode.startsWith("S")){
				log.info("Returened Success.");
			}else{
				log.warning("Service Failed.");
			}
			
		  }
		in.close();
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
	
	
	public static String validateWebservice(InputStream response){
		String resPonseCode=null;
		try{
	            JAXBContext jc = JAXBContext.newInstance(WEBSERVICE.class);
	            Unmarshaller u = jc.createUnmarshaller();
	 
	            WEBSERVICE webSevice = (WEBSERVICE) u.unmarshal(response);
				List<org.ofbiz.party.test.webservice.WEBSERVICE.RESPONSE> respTransDetails = webSevice.getRESPONSE();
	            
				org.ofbiz.party.test.webservice.WEBSERVICE.RESPONSE resp= respTransDetails.get(0);
				
				List<RESPTRANSACTOINDETAILS> transDetails = resp.getRESPTRANSACTOINDETAILS();
				
				RESPTRANSACTOINDETAILS respCouponDetails = transDetails.get(0);
				
				resPonseCode = respCouponDetails.getRESPONSECODE();
	       
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
		
		return resPonseCode;
	}
	
}
