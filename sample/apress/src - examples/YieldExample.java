public class YieldExample {
	CommonObject commonObject = new CommonObject();

	public static void main(String[] args) {
		YieldExample ye = new YieldExample();

		for (int i = 0; i < 5; i++) {
			ye.new Runner().start();
		}
	}

	public class Runner extends Thread {
		public void run() {
//			synchronized (commonObject) {
				commonObject.value += 20;
				System.out.println(getName() + ": before yield = " + commonObject.value);
				yield();
				System.out.println(getName() + ": after yield = " + commonObject.value);
//			}
		}
	}

	public class CommonObject {
		public int value = 0;
	}
}