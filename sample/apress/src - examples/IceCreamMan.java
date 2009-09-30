import java.util.* ;

public class IceCreamMan extends Thread {
    /**
     * a list to hold all the children's IceCreamDish objects
     */
    private List<IceCreamDish> dishes = new ArrayList<IceCreamDish>();

    /**
     * Start a thread that waits for ice cream bowls to be given to it.
     */
    public void run() {
        String clientExists = "IceCreamMan: has a client";
        String clientDoesntExist = "IceCreamMan: does not have a client";

        while (true) {
            if (!dishes.isEmpty()) {
                System.out.println(clientExists);
                serveIceCream();
            } else {
                try {
                    System.out.println(clientDoesntExist);
                    // sleep so that children have a chance to add their
                    // dishes. see note in book about why it is not this
                    // is not a yield statement.
                    sleep(1000);
                }  catch(InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    /**
     * Serve Ice Cream to a Child object.
     */
    private void serveIceCream() {
        // get an ice cream dish
        IceCreamDish currentDish = dishes.get(0);

        // wait sometimes, don't wait sometimes
        if (Math.random() > .5) {
            delay();
        }

        String msg = "notify client that the ice cream is ready";
        System.out.println("IceCreamMan: " + msg);

        synchronized (currentDish) {
            currentDish.readyToEat = true;
            // notify the dish's owner that the dish is ready
            currentDish.notify();
        }

        // remove the dish from the queue of dishes that need service
        dishes.remove(currentDish);
    }

    /**
     * Allow client objects to add dishes
     */
    public synchronized void requestIceCream(IceCreamDish dish) {
            dishes.add(dish);
    }

    /**
     * build in a delay
     */
    private void delay() {
        try {
            System.out.println("IceCreamMan: delayed");
            Thread.sleep((long) (Math.random()* 1000) );
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
