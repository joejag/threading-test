package sampleproject.remote;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 * RegDvdDatabase starts the rmi registry on the client machine. Registers the
 * DvdDatabase object for the RMI naming service.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public class RegDvdDatabase {
    /**
     * Since this is a utility class (it only exists for other classes to call
     * it's static methods), lets stop users creating unneeded instances of
     * this class by creating a private constructor.
     */
    private RegDvdDatabase() {
    }

    /**
     * Creates the DvdDatabase class and binds it to the name "DvdDatabase".
     *
     * @throws RemoteException on network error.
     * @throws MalformedURLException
     */
    public static void register()
            throws RemoteException {
        register("Apress", java.rmi.registry.Registry.REGISTRY_PORT);
    }

    /**
     * Creates the DvdDatabase class and binds it to the name "DvdDatabase".
     *
     * @param dbLocation the location of the data file on disk.
     * @param rmiPort the port the RMI Registry will listen on.
     * @throws RemoteException on network error.
     */
    public static void register(String dbLocation, int rmiPort)
            throws RemoteException {
        Registry r = java.rmi.registry.LocateRegistry.createRegistry(rmiPort);

        // make a dvd database instance on a random port and register
        // our service name and our port number on the RMI registry.
        r.rebind("DvdMediator", new DvdDatabaseFactoryImpl(dbLocation));
    }


    /**
     * Simple entry point so that the RMI server can be started manually for
     * testing purposes.
     *
     * @param args the command line arguments which will be ignored.
     * @throws RemoteException on network error.
     */
    public static void main(String[] args) throws RemoteException {
        register();
    }
}
