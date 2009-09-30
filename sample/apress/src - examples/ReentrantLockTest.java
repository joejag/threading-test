public class ReentrantLockTest {
	public Object mutex = new Object();
	int counter = 0;

	public static void main(String[] args) {
		new ReentrantLockTest().work();
	}

	public synchronized void work() {
		System.out.println("work :" + counter);
		if (counter++ < 10) {
			work();
		}
	}
}