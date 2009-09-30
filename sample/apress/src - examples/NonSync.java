import java.util.*;

public class NonSync extends Thread {
	public static Vector<String> myVector = new Vector<String>();
	public static ArrayList<String> myArrayList = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		new NonSync().start();

		System.out.print("10 ");
		myArrayList.add("Hello");
		myVector.add("Hello");
		for (int i = 1; i < 10000; i++) {
			Math.tan(50);
		}
		System.out.print("15 ");
		myArrayList.add("World");
		myVector.add("World");

		Thread.sleep(500);
		System.out.println();

		System.out.println("myArrayList: " + myArrayList);
		System.out.println("myVector: " + myVector);
	}

	public void run() {
		System.out.print("22 ");
		myArrayList.add("Cruel");
		myVector.add("Cruel");
	}
}