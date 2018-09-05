public class MethodSynchronized {

  public static void main (String[] args) {
  	final Account a = new Account();
  	Thread[] threads = new Thread[10000];
  	for (int i = 0;i<1000;i++) {
  	    threads[i] = new Thread(new Runnable(){
  	        public void run() {
  	            a.reduceAccount();
  	            a.addAccount();
  	        }	
  	    });
  	    threads[i].start();
  	}
  	for (int i = 0;i<10000;i++) {
  		try {
  		    threads[i].join();
  		} catch(Exception e) {}
  	}
  	System.out.println(a.getAccount());
  }
}

	class Account {
    private float money = 1000;
    public  void reduceAccount() {
    	  synchronized(this) {
    	  	
            money = money - 1;	
        }
    }	
    public  void addAccount() {
    	  synchronized(this) {
          money = money + 1;
        }
    }
    
    public float getAccount() {
        return money;	
    }
  }