package sampleproject.remote;

import java.rmi.Naming;
import java.rmi.*;
import sampleproject.db.*;
import java.io.*;

/**
 * A  DvdConnector is used in cases where the GUI client wants to make a
 * network connection. In this case, that connection is an RMI connection.
 *
 * @author Denny's DVDs
 * @version 2.0
 * @see sampleproject.db.DvdDatabase
 */
public class DvdConnector {
    /**
     * Since this is a utility class (it only exists for other classes to call
     * it's static methods), lets stop users creating unneeded instances of
     * this class by creating a private constructor.
     */
    private DvdConnector() {
    }

    /**
     * Static method that creates an RMI connection.
     * The DBClient is a remote object.
     *
     * @param hostname The ip or ddress of the host machine.
     * @param port the port the RMI Registry is listening on.
     * @return A DBClient instance.
     * @throws RemoteException Indicates that a remote instance of the DBClient
     * interface cannot be created.
     */
    public static DBClient getRemote(String hostname, String port)
            throws RemoteException {
        String url = "rmi://" + hostname + ":" + port + "/DvdMediator";

        try {
            DvdDatabaseFactory factory
                    = (DvdDatabaseFactory) Naming.lookup(url);
            return (DBClient) factory.getClient();
        } catch (NotBoundException e) {
            System.err.println("Dvd Mediator not registered: "
                    + e.getMessage());
            throw new RemoteException("Dvd Mediator not registered: ", e);
        } catch (java.net.MalformedURLException e) {
            System.err.println(hostname + " not valid: " + e.getMessage());
            throw new RemoteException("cannot connect to " + hostname, e);
        }
    }
}
