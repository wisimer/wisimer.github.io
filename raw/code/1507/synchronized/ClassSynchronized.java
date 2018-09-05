class PrintThread extends Thread {
    
    private int v = 0;
    private String name = null;
    public PrintThread (String name) {
    	  this.name = name;
    }
    public void printValue() {
        synchronized (PrintThread.class) {
        	  while(true) {
        	      v++;
                System.out.println(currentThread().getName()+"-->"+name+"-->"+v);	
                try {
                   // sleep(1000);
                } catch(Exception e) {}
        	  }
        }
    }
    public void run() {
        printValue();
    }
}
public class ClassSynchronized {
    public static void main(String[] args) {
        PrintThread p1 = new PrintThread("A");
        PrintThread p2 = new PrintThread("B");
        p1.start();
        p2.start();
    }

}

