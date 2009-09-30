/**
 * Demonstrate the concept of a starving thread.
 */
public class StarvationExample{
    public static void main(String args[]){
    	// Ensure the main thread competes with the other threads
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        // Create an instance of this object
        StarvationExample se = new StarvationExample();

        // Create 5 threads, marking number 1 as a very low priority
        for(int i=1; i< 5; i++){
            //create a runner
            Runner r = se.new Runner();
            r.setPriority(Thread.MAX_PRIORITY);

            //set the first thread to starve
            if (i == 1) {
                r.setPriority(Thread.MIN_PRIORITY);
                r.setName("Starvation Thread");
            }
            //start the thread.
            r.start();
        }

		// Exit as soon as we possibly can
    	System.exit(0);
    }

    /**
     * Create a thread, then cycle through its command ten times.
     */
    class Runner extends Thread{
        public void run(){
            for (int count = 10; count > 0; count--) {
                System.out.println(getName() + ": is working " + count);
            }
        }
    }
}
