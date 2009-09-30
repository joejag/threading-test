package sampleproject.remote;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.Naming;
import java.rmi.registry.*;
import sampleproject.db.*;

/**
 * An example of how you could create an RMI factory server and connect to it
 * using multiple clients. Refer to the RMI chapter for a discussion on this
 * code.
 */
public class RmiFactoryExample extends Thread {
    /**
     * Creates an identifiable client to connect to our factory.
     *
     * @param id the identifier for this particular client.
     */
    public RmiFactoryExample(String id) {
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
        Naming.rebind("RmiFactoryExample", new DvdDatabaseFactoryImpl("."));

        Thread a = new RmiFactoryExample("A");
        a.start();

        Thread.sleep(1000);

        Thread b = new RmiFactoryExample("B");
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
            System.out.println("Getting a remote handle to a factory. "
                               + this.hashCode());
            DvdDatabaseFactory factory
                    = (DvdDatabaseFactory) Naming.lookup("RmiFactoryExample");
            DvdDatabaseRemote worker = factory.getClient();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();

        }
    }
}
