import java.net.*;

public class BlockingExample extends Thread {
    private static Object mylock = new Object();

    public static void main(String args[]) throws Exception {
        LockOwner lo = new LockOwner();
        lo.setName("Lock owner");
        lo.start();

        // Wait for a little while for the lock owner thread to start
        Thread.sleep(200);

        // Now start the thread that will be blocked
        BlockingExample be = new BlockingExample();
        be.setName("Blocked thread");
        be.start();

        // Wait for a little while for the blocked thread to start
        Thread.sleep(200);

        // Now print the two threads states
        printState(lo);
        printState(be);
    }

    // start a thread.
    public void run() {
        // wait for the mylock object to be freed,
        // which will never happen
        synchronized (mylock) {
            System.out.println(getName() + " owns lock");
            System.out.println("doing Stuff");
        }
        System.out.println(getName() + " released lock");
    }

    private static void printState(Thread t) {
        System.out.println();
        System.out.println("State of thread named: " + t.getName());
        System.out.println("State: " + t.getState());
        System.out.println("begin trace");
        for (StackTraceElement ste : t.getStackTrace()) {
            System.out.println("\t" + ste);
        }
        System.out.println("end trace");
    }

    static class LockOwner extends Thread {
        public void run() {
            synchronized (mylock) {
                System.out.println(getName() + " owns lock");
                try {
                    ServerSocket ss = new ServerSocket(8080);
                    ss.accept();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(getName() + " released lock");
        }
    }
}
