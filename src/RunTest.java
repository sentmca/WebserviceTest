import java.io.IOException;

import org.ofbiz.party.test.WebServiceTest;


public class RunTest extends Thread {
	
	public String config="";
	
	public RunTest(String config){
		this.config=config;
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		
		try {
			
			if(!"".equals(config)){
				WebServiceTest webtest = new WebServiceTest(config);
				webtest.postXmlString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
