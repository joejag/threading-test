import java.util.Date;

public class NotifyVersusNotifyAll extends Thread {
    private static Object mutex = new Object();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new NotifyVersusNotifyAll().start();
        }

        Thread.sleep(2000);
        synchronized(mutex) {
            System.out.println("Main thread kicking off other threads");
//            mutex.notifyAll();
            mutex.notify();
        }
    }

    public void run() {
        // Note: This code has changed significantly from what was
        // published in the book, as the sample in the book does not show
        // that the multiple threads are all woken.
        try {
            System.out.println(getName() + " waiting");
            synchronized(mutex) {
                mutex.wait();
            }

            // if we called notifyAll(), then all threads will get to this line
            // at roughly the same time. If we called notify() then only one
            // thread will get here, then when it calls notify() another thread
            // will get here, and so on.
            System.out.println(getName() + " woken up");
            Thread.sleep(2000);
            System.out.println(getName() + " waking up another thread");

            // need to get back inside a synchronized block to call notify()
            synchronized(mutex) {
                mutex.notify();
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
