public class MyNotify {
    public static void main(String[] args) {
        MyThread mt1 = new MyThread();
        mt1.start();
        MyNotifyThread mt2 = new MyNotifyThread();
        mt2.start();
    }

    public static  Object o = new Object();
    static class MyThread extends Thread {
        public void run() {
            synchronized(o) {
                for(int i = 0;i<4;i++) {
                    System.out.println("i = "+i);
                    if(i == 2) {
                        System.out.println(currentThread().getName()+".wait()");
                        try {
                        	o.wait();
                        } catch(Exception e) {
                        }
                    }
                }
            }
        }
    }

    static class MyNotifyThread extends Thread {
        public void run() {
            synchronized(o) {
            	  for(int J = 0;J<6;J++) {
                    System.out.println("J = "+J);
                    if(J == 3) {
            	      	System.out.println(currentThread().getName()+".notify()");
               			  o.notify();
                    }
                }

            }
        }
    }
}