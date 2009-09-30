package sampleproject.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import sampleproject.db.*;

/**
 * The implementation of our remote factory for client connectivity.
 */
class DvdDatabaseFactoryImpl extends UnicastRemoteObject
        implements DvdDatabaseFactory {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * The physical location of the database.
     */
    private static String dbLocation = null;

    /**
     * Creates an instance of this factory, specifying where the database can
     * be found.
     *
     * @param dbLocation the location of the database.
     * @throws RemoteException Thrown if a <code>DvdDatabaseImpl</code>
     * instance cannot be created.
     */
    public DvdDatabaseFactoryImpl(String dbLocation) throws RemoteException {
        this.dbLocation = dbLocation;
    }

    /** {@inheritDoc} */
    public DvdDatabaseRemote getClient() throws RemoteException {
        return new DvdDatabaseImpl(dbLocation);
    }
}
