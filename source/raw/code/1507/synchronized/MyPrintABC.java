public class MyPrintABC {
	
  public static void main(String[] args) {
  	Object a = new Object();
  	Object b = new Object();
  	Object c = new Object();
  	PrintThread PA = new PrintThread("A",a,c);
  	PrintThread PB = new PrintThread("B",b,a);
  	PrintThread PC = new PrintThread("C",c,b); 
  	PA.start();
  	try {
  		PA.sleep(1);
  	} catch(Exception e) {}
  	PB.start();
  	try {
  		PB.sleep(1);
  	} catch(Exception e) {}
  	PC.start();
    try {
  		PC.sleep(1);
  	} catch(Exception e) {}
  }
    
  static class PrintThread extends Thread {
    
    private String mName = null;
    private Object mSelf = null;
    private Object mPre = null;
    public PrintThread(String name,Object self,Object pre) {
    	this.mName = name;
    	this.mSelf = self;
    	this.mPre = pre;
    }	
   
    public void run() {
    	int i = 0;
    	while(i < 10) {
    		try {
    	    synchronized(mPre) {
    		    synchronized(mSelf) {
    		      System.out.println(currentThread().getName()+"-->"+mName);
    		      i++;
    		      mSelf.notify();   		     		    
    		    }
    		    mPre.wait(); 
    	    } 
    	  } catch(Exception e) {}    		
    	}
    
    }
  }
}