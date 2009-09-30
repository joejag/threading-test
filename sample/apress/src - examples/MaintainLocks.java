public class MaintainLocks {
    private static Object objectOne = new Object();
    private static Object objectTwo = new Object();

    public static void main(String[] args) {
	new MaintainLocks();
    }

    public MaintainLocks() {
	new LockTwoObjects().start();
	new LockOneObject().start();
    }

    class LockTwoObjects extends Thread {
        public void run() {
	    synchronized(objectOne) {
	        synchronized(objectTwo) {
		    System.out.println("LockTwoObjects has both locks");
		    try {
			objectOne.wait(5000);
		    } catch (InterruptedException ignored) {}
		    System.out.println("LockTwoObjects releasing both locks");
		}
	    }
	}
    }

    class LockOneObject extends Thread {
        public void run() {
	    synchronized(objectOne) {
		    System.out.println("LockOneObject has both locks");
		    try {
			Thread.sleep(5000);
		    } catch (InterruptedException ignored) {}
		    System.out.println("LockOneObject releasing both locks");
	    }
	}
    }
}
