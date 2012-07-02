import java.io.IOException;


public class TestJob extends Thread {

	@Override
	public void run() {
		
		try {
			org.ofbiz.party.test.WebServiceTest.postXmlString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
