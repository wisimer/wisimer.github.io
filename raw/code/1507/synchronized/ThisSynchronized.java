public class ThisSynchronized {
    
    public static void main(String[] args) {
    
        PrintRunnable pr = new PrintRunnable("PR");
        Thread t1 = new Thread(pr);
        Thread t2 = new Thread(pr);
        t1.start();
        t2.start();
    }
    static class PrintRunnable implements Runnable {
        private String name = null;
        public PrintRunnable (String name) {
            this.name = name;
        }
        public int value = 0;
        public void printValue() {
            synchronized(this) {
            	while(true) {
                value++;
                System.out.println(Thread.currentThread().getName()+"-->"+name+"-->"+value);
              }
            }
        }
        public void run() {
            printValue();
        }
    }
}