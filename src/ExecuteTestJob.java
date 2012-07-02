
public class ExecuteTestJob {

	public static void main(String args[]){
		Thread testJob = new TestJob();
		testJob.start();
	}
	
}
