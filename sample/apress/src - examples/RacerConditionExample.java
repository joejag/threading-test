public class RacerConditionExample {
	public static void main(String args[]) {
		//create an instance of this object
		RacerConditionExample rce = new RacerConditionExample();

		//create two runners
		Runner johnson = rce.new Runner("Johnson");
		Runner smith = rce.new Runner("smith");

		//point both runners to the same resource
		smith.server =  "the common object";
		johnson.server = smith.server;

		//start the race, based on a random factor, one thread
		//or the other gets to start first.
		if (Math.random() > .5) {
			johnson.start();
			smith.start();
		} else {
			smith.start();
			johnson.start();
		}
	}

	/**
	* Creates a thread, then races for the resource
	*/
	class Runner extends Thread {
		public Object server;

		public Runner(String name) {
			super(name);
		}

		public void run() {
			System.out.println(getName() + ": trying for lock on " + server);
			synchronized (server) {
				System.out.println(getName() + ": has lock on " + server);
				// wait 2 seconds: show the other thread really is blocked
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				System.out.println(getName() + ": releasing lock ");
			}
		}
	}
}
