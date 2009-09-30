package sampleproject.test;

import sampleproject.db.*;
import java.util.Date;

// rather than going through a factory, we are directly calling the connector
// Uncomment the DVDConnector of the protocol you want to use.
import sampleproject.remote.DvdConnector;
//import sampleproject.sockets.DVDConnector;

/**
 * A DBTester is the test equivalent of a client who is trying to book one or
 * more DVDs. However we know exactly how the DBTester is going to behave,
 * therefore we can predict the results of this testing.
 */
public class DBTester extends Thread {
    // various status for what can happen when we try to book over the network
    public enum Status {
        SUCCESS, OUT_OF_STOCK, TIMEOUT
    }

    private String dvdUpc; // the DVD we are supposed to rent
    private int numberOfRentals; // number of times to rent it
    private DBClient db; // connection to the remote database

    private int successfulRentals = 0;  // number of times we rented the DVD
    private int outOfStock = 0; // number of times we failed due to no copies left
    private int timeouts = 0; // number of times timed out trying to reserve DVD

    // To make the screen output easier to read, we are using a pretend logger
    // If we chose to convert to the real JDK logger, we could just change this
    private PretendLogger log = new PretendLogger();

    /**
     * Create a test client to book a certain DVD a certain number of times.
     * There is no exception handling here - it is all thrown back to the test
     * harness.
     *
     * @param title the name of this client - the thread name
     * @param numberOfRentals how many times we will attempt to rent the DVD
     * @param dvdUpc the unique identifier for the DVD we will attempt to rent
     * @throws Exception if we cannot connect to the database
     */
    public DBTester(String title, int numberOfRentals, String dvdUpc)
            throws Exception {
        super(title);
        this.numberOfRentals = numberOfRentals;
        this.dvdUpc = dvdUpc;

        db = DvdConnector.getRemote("localhost", "1099");
    }

    /**
     * Starts running the test with the parameters specified in the constructor.
     */
    public void run() {
        int secondsForWatchingDvd = 2;
        int secondsForComplaining = 1;
        int secondsForBrowsingStore = 2;

        try {
            for (int i = 0; i < this.numberOfRentals; i++) {
                switch (rentDvd(dvdUpc)) {
                    case SUCCESS:
                        successfulRentals++;
                        // watch the DVD
                        Thread.sleep(secondsForWatchingDvd * 1000);
                        // then return it so somebody else can rent it
                        returnDvd(dvdUpc);
                        break;
                    case OUT_OF_STOCK:
                        outOfStock++;
                        // complain that it is not in stock
                        Thread.sleep(secondsForComplaining * 1000);
                        break;
                    case TIMEOUT:
                        // just track that we had the problem, and continue
                        timeouts++;
                        break;
                }
                // wander around the DVD store looking at DVDs.
                Thread.sleep(secondsForBrowsingStore * 1000);
            }
        } catch (Exception e) {
            // This should never ever go into production code, but for testing
            // we are simply catching *every* exception and displaying it
            System.err.println("Exception thrown by " + getName());
            e.printStackTrace(System.err);
            System.err.println();
        }
    }

    /**
     * Try to rent the specified DVD using standard business logic.
     *
     * @param upc the unique identifier for the DVD we want to rent.
     * @throw Exception any exception that might appear
     */
    private Status rentDvd(String upc) throws Exception {
        if (db.reserveDVD(upc)) {
            try {
                DVD dvd = db.getDVD(upc);
                int copiesInStock = dvd.getCopy();
                if (copiesInStock > 0) {
                    copiesInStock--;
                    log.info(getName() +
                             "       -> (Rent)     " +
                             "Copies in stock = " + copiesInStock );
                    dvd.setCopy(copiesInStock);
                    db.modifyDVD(dvd);
                    return Status.SUCCESS;
                } else {
                    log.info(getName() + "    00    (No stock)");
                    return Status.OUT_OF_STOCK;
                }
            } finally {
                db.releaseDVD(upc);
            }
        } else {
            log.info(getName() + "    XX    (Timeout)");
            return Status.TIMEOUT;
        }
    }

    /**
     * Try to return the specified DVD using standard business logic.
     *
     * @param upc the unique identifier for the DVD we want to return.
     * @throw Exception any exception that might appear
     */
    private void returnDvd(String upc) throws Exception {
        if (db.reserveDVD(upc)) {
            try {
                DVD dvd = db.getDVD(upc);
                int copiesInStock = dvd.getCopy() + 1;
                dvd.setCopy(copiesInStock);
                log.info(getName() +
                         " <-       (Return)   Copies in stock = " +
                         copiesInStock );
                db.modifyDVD(dvd);
            } finally {
                db.releaseDVD(upc);
            }
        }
    }

    /**
     * Used by the test harness to determine how many successful rentals we made.
     *
     * @return how many successful rentals we made.
     */
    public int getSuccessfulRentals() {
        return successfulRentals;
    }

    /**
     * Used by the test harness to determine how many times we were thwarted by
     * lack of stock.
     *
     * @return how many times there wasn't enough stock.
     */
    public int getOutOfStock() {
        return outOfStock;
    }

    /**
     * Used by the test harness to determine how many timeouts occured while
     * reserving the DVD
     *
     * @return how many timeouts occured during reservations
     */
    public int getTimeouts() {
        return timeouts;
    }

    /**
     * A pretend logger. Using this displays a simple message on screen (more
     * readable for us than the standard JDK logger), but can be easily replaced
     * with the standard logger later if we so desire.
     */
    private class PretendLogger {
        /**
         * Displays the time and the information provided on screen.
         *
         * @param logInformation the information to display on screen.
         */
        void info(String logInformation) {
            System.out.format("%tT %s\n", new Date(), logInformation);
        }
    }
}
