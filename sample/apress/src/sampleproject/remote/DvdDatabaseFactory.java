
package sampleproject.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import sampleproject.db.*;

/**
 * Specifies the methods that may be remotely called on our DvdDatabaseFactory.
 */
interface DvdDatabaseFactory extends Remote {
    /**
     * Returns a reference to a remote instance of a class unique to the
     * connecting client containing all the methods that may be remotely called
     * on the database.
     *
     * @return a unique database connectivity class.
     * @throws RemoteException on network errors.
     */
    public DvdDatabaseRemote getClient() throws RemoteException;
}
