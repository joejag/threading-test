package sampleproject.test;

import java.util.Calendar;

/**
 * A master class to run all of the test clients.
 *
 * Note that unlike a production class, this class deliberately does not try to
 * handle Exceptions at all - any Exceptions will propogate to the very top of
 * the stack. That is because it is assumed that (in this particular case) the
 * person running this code is a developer, and can read a stack trace, or a
 * tester who will bring the stack trace to a developer. You shound never do this
 * with production code though.
 */
public class DBTestRunner {
    private int numberOfClients = 4; // how many test clients we will start
    private int rentalsPerClient = 2; // number of rentals each client will make
    private String dvdUpc = "32725349302"; // the DVD they will rent

    private DBTester[] clients = null; // an array of the test clients

    /**
     * Entry point for the application. Starts the test process running.
     *
     * @param args the command line arguments (which are ignored)
     * @throws Exception on any exception in the test harness
     */
    public static void main(String[] args) throws Exception {
        new DBTestRunner();
    }

    /**
     * Constructs a test harnes, starts the clients, waits for them to
     * finish, then displays statistics.
     *
     * @throws Exception on any exception in the test harness
     */
    DBTestRunner() throws Exception {
        clients = new DBTester[numberOfClients];

        startClients();
        waitForClientsToDie();
        displayStatistics();
    }

    /**
     * Creates a test client and starts it running.
     *
     * @throws Exception on any exception in the test harness
     */
    private void startClients() throws Exception {
       for (int i = 0; i < numberOfClients; i++) {
           String clientName = "Client " + i;
           clients[i] =  new DBTester(clientName, rentalsPerClient, dvdUpc);
           clients[i].start();
       }
   }

    /**
     * Waits for all clients to have completed running
     *
     * @throws Exception on any exception in the test harness
     */
    private void waitForClientsToDie() throws Exception {
       // wait for them all to finish
       for (DBTester client : clients) {
           client.join();
       }
   }

    /**
     * Displays the statistics for the client.
     *
     * Note that even though the client threads are now in the TERMINATED state
     * we can still access their variables and run their public methods.
     */
    private void displayStatistics() {
       // display some statistics
       System.out.println();
       formatLine("========", "========", "========", "========", "========");
       formatLine("Client #", "Rented", "No stock", "Timeout", "Total");
       formatLine("--------", "--------", "--------", "--------", "--------");
       for (DBTester client : clients) {
           formatLine(client.getName(),
                      client.getSuccessfulRentals(),
                      client.getOutOfStock(),
                      client.getTimeouts());
       }
       formatLine("========", "========", "========", "========", "========");
    }

    /**
     * Displays the specified fields in a standardized format.
     *
     * @param name the thread name
     * @parm rentals how many rentals the client made
     * @param noStock how many times the client could not rent due to lack of stock
     * @param timeout how many times the client timed out trying to reserve the DVD
     */
    private void formatLine(String name, int rentals, int noStock, int timeout) {
        formatLine(name,
                   "" + rentals,
                   "" + noStock,
                   "" + timeout,
                   "" + (rentals + noStock + timeout));
    }

    /**
     * Displays the specified fields in a standardized format. Note that all these
     * fields are Strings, allowing titles and ruled lines to be displayed in the
     * same standardized format.
     *
     * @param name the thread name
     * @parm rentals how many rentals the client made
     * @param noStock how many times the client could not rent due to lack of stock
     * @param timeout how many times the client timed out trying to reserve the DVD
     * @param total the total attempts to rent a DVD a client made
     */
    private void formatLine(String name, String rentals, String noStock,
                            String timeout, String total) {
        System.out.format("%tT %8s %8s %8s %8s %8s%n",
                          Calendar.getInstance(),
                          name,
                          rentals,
                          noStock,
                          timeout,
                          total);
    }
}
