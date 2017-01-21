package mini.projet.test;

public class ThreadTest {
	public static void main(String args[]){  
		Multi1 m1=new Multi1();  
		Thread t1 =new Thread(m1);  
		t1.start();  
		Multi2 m2=new Multi2();  
		Thread t2 =new Thread(m2);  
		t2.start();  
		 }  
	static class Multi1 implements Runnable {
		public void run() {
			while(true){
				System.out.println(1);
			}
		}
	}
	static class Multi2 implements Runnable {
		public void run() {
			while(true){
				System.out.println(2);
			}
		}
	}
}
