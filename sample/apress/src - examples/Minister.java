public class Minister {
    private CollectionPlate collectionPlate = new CollectionPlate();

    public static void main(String[] args) {
        Minister minister = new Minister();

        // create a Thread that checks  the amount on
        // money in the collection plate.
        minister.new CollectionChecker().start();

        //create several threads to accept contributions.
        for (int i = 0; i < 6; i++) {
            minister.new CollectionAcceptor(20).start();
        }
    }

    /**
     * the collection plate that get passed around
     */
    private class CollectionPlate {
        int amount = 0;
    }

    /**
     * Thread that accepts collections.
     */
    private class CollectionAcceptor extends Thread {
        int contribution = 0;

        public CollectionAcceptor(int contribution) {
            this.contribution = contribution;
        }

        public void run() {
            //Add the contributed amount to the collectionPlate.
            synchronized (collectionPlate) {
                int amount = collectionPlate.amount + contribution;
                String msg = "Contributing: current amount: " + amount;
                System.out.println(msg);
                collectionPlate.amount = amount;
                collectionPlate.notify();
            }
        }
    }

    /**
     * Thread that checks  the collections made.
     */
    private class CollectionChecker extends Thread {
        public void run() {
            // check the amount of money in the collection plate. If it's less
            // than  100, then release the collection plate, so other Threads
            // can modify it.
            synchronized (collectionPlate) {
                while (collectionPlate.amount < 100) {
                    try  {
                        System.out.println("Waiting ");
                        collectionPlate.wait();
                    } catch  (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                // getting past the while statement means that the
                // contribution goal has been met.
                System.out.println("Thank you");
            }
        }
    }
}















