/**
 * a Child object, designed to consume ice cream
 */
public class Child implements Runnable {
    private static IceCreamMan iceCreamMan = new IceCreamMan();
    private IceCreamDish myDish = new IceCreamDish();
    private String name;

    public Child(String name) {
        this.name = name;
    }

    public static void main(String args[]) {
        // start the ice cream man's thread.
        iceCreamMan.setDaemon(true);
        iceCreamMan.start();

        String[] names = {"Ricardo", "Sally", "Maria"};
        Thread[] children = new Thread[names.length];

        // create some child objects
        // create a thread for each child
        // get the Child threads started
        int counter = -1;
        for (String name : names) {
            Child child = new Child(name);
            children[++counter] = new Thread(child);
            children[counter].start();
        }

        // wait until all children have eaten their ice cream
        for (Thread child : children) {
            try {
                child.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        System.out.println("All children received ice cream");
    }

    public void run() {
        iceCreamMan.requestIceCream(myDish);
        eatIceCream();
    }

    private void eatIceCream() {
        String msg = name + " waiting for the IceCreamMan to fill dish";
        /*
         * The IceCreamMan will notify us when the dish is full, so we should
         * wait until we have received that notification. Otherwise we could
         * get a dish that is only half full (or even empty).
         */
        synchronized (myDish) {
            while (myDish.readyToEat == false) {
                // wait for the ice cream man's attention
                try {
                    System.out.println(msg);
                    myDish.wait();
                }  catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            myDish.readyToEat = false;
        }
        System.out.println(name +": yum");
    }
}

class IceCreamDish {
    public boolean readyToEat = false;
}