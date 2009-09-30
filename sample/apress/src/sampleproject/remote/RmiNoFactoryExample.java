package sampleproject.remote;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.Naming;
import java.rmi.registry.*;
import sampleproject.db.*;

/**
 * An example of how you could create an RMI server and connect multiple
 * clientsto it without bothering with a factory. Refer to the RMI chapter for
 * a discussion on this code.
 */
public class RmiNoFactoryExample extends Thread {
    /**
     * Creates a client to connect to our RMI server.
     */
    public RmiNoFactoryExample() {
        super();

    }

    /**
     * Creates an identifiable client to connect to our factory.
     *
     * @param id the identifier for this particular client.
     */
    public RmiNoFactoryExample(String id) {
        super(id);
    }

    /**
     * Application entry point - starts the factory, then connects two clients.
     *
     * @param args command line arguments (which are ignored).
     * @throws Exception this example program is not interested in exceptions -
     * they should not occur, and even if they do, there is no point in
     * handling them. So they are all allowed to propogate up and appear on
     * screen.
     */
    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);
        Naming.rebind("RmiNoFactoryExample", new DvdDatabaseImpl("."));

        Thread a = new RmiNoFactoryExample("A");
        a.start();

        Thread.sleep(1000);

        Thread b = new RmiNoFactoryExample("B");
        b.start();

        a.join();
        b.join();

        System.exit(0);
    }

    /**
     * Each client runs this thread to connect to the factory.
     */
    public void run() {
        try {
            System.out.println("getting a remote handle to a DvdDatabase."
                               + this.hashCode());
            DvdDatabaseRemote remote
                    = (DvdDatabaseRemote) Naming.lookup("RmiNoFactoryExample");
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
