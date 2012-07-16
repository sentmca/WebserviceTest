

public class ExecuteTest {

	public static void main(String args[]){
		Thread testJob = new RunTest("customer.create.local");
		testJob.start();
	}
	
}
