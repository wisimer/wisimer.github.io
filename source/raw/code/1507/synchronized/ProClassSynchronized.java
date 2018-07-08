class PrintThread extends Thread {
    
    private String v = "PrintThread";
    private String name = null;
    public PrintThread (String name) {
    	  this.name = name;
    }
    public void printValue() {
        synchronized (v) {
        	  while(true) {
        	     
                System.out.println(currentThread().getName()+"-->"+v);	
        	  }
        }
    }
    public void run() {
        printValue();
    }
}

public class ProClassSynchronized {
    public static void main(String[] args) {
        PrintThread p1 = new PrintThread("A");
        PrintThread p2 = new PrintThread("B");
        p1.start();
        p2.start();
    }

}