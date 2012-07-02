
public class ThreadTest {

	public static void main(String args[]){
	
		Thread thr1 = new Thread(r1);
		Thread thr2 = new Thread(r2);
		thr1.start();
		thr2.start();
		
	}
	
	
	static Runnable r1 = new Runnable() {
		  public void run() {
		    try {
		      while (true) {
		        System.out.println("Hello, world!");
		        Thread.sleep(1000L);
		      }
		    } catch (InterruptedException iex) {}
		  }
		};
		
		
		static Runnable r2 = new Runnable() {
		  public void run() {
		    try {
		      while (true) {
		        System.out.println("Goodbye, " +
				"cruel world!");
		        Thread.sleep(2000L);
		      }
		    } catch (InterruptedException iex) {}
		  }
		};
	
}
