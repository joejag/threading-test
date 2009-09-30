public class DeadlockExample {
	/**
	 * Entry point to the application. Creates 2 threads that will deadlock.
	 */
	public static void main(String args[]){
		DeadlockExample dle = new DeadlockExample();

                Object lockA = "Lock A";
                Object lockB = "Lock B";

                Runner thread1 = new Runner(lockA, lockB);
                Runner thread2 = new Runner(lockB, lockA);

		thread1.start();
		thread2.start();
	}

	/**
	 * Lock two objects in the order they were specified in the constructor.
	 */
	static class Runner extends Thread {
		private Object lock1;
		private Object lock2;

		public Runner(Object firstLockToGet, Object secondLockToGet) {
			this.lock1 = firstLockToGet;
			this.lock2 = secondLockToGet;
		}

		public void run() {
			String name = Thread.currentThread().getName();
			synchronized(lock1) {
				System.out.println(name + ": locked " + lock1);
				delay(name);
				System.out.println(name + ": trying to get " + lock2);
				synchronized(lock2) {
					System.out.println(name + ": locked " + lock2);
				}
			}
		}
	}

	/**
	 * build in a delay to allow the other thread time to lock the object
	 * the delaying thread would like to get.
	 */
	private static void delay(String name) {
		try {
			System.out.println(name + ": delaying 1 second");
			Thread.currentThread().sleep(1000L);
		} catch(InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}
