package sampleproject.remote;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import java.util.logging.Logger;
import sampleproject.db.*;

/** A DvdDatabaseImpl object is the implementation of the DvdDatabaseRemote
 * interface. This class is an RMI implementation.
 *
 * A DvdDatabaseImpl object contains a reference to the db.
 * This class acts primarily as a wrapper for the database.
 *
 * @author Denny's DVDs
 * @version 2.0
 * @see sampleproject.remote.DvdDatabaseRemote
 */
public class DvdDatabaseImpl extends UnicastRemoteObject
        implements DvdDatabaseRemote {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>sampleproject.remote</code>.
     */
    private static Logger log = Logger.getLogger("sampleproject.remote");

    /**
     * The database handle.
     */
    private DBClient db = null;

    /**
     * DvdDatabaseImpl default constructor.
     *
     * @param dbLocation the location of the database.
     * @throws RemoteException Thrown if a <code>DvdDatabaseImpl</code>
     * instance cannot be created.
     */
    public DvdDatabaseImpl(String dbLocation) throws RemoteException {
        try {
            db = new DvdDatabase(dbLocation);
        } catch (FileNotFoundException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    /**
     * Returns the sampleproject.db.Dvd object matching the UPC.
     *
     * @param upc The upc code of the Dvd to retrieve.
     * @return The matching Dvd object.
     * @throws RemoteException  Thrown if an exception occurs in the
     * <code>DvdDatabaseImpl</code> class.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     * <br>
     * For more information, see {@link DvdDatabase}.
     * <br>
     * For more information, see {@link DvdDatabase}.
     */
    public DVD getDVD(String upc) throws RemoteException, IOException {
        return db.getDVD(upc);
    }

    /**
     * Gets the store's inventory.
     * All of the Dvds in the system.
     *
     * @return A collection of all found Dvd's.
     * @throws IOException Indicates there is a problem accessing the data.
     */
     public List<DVD> getDVDs() throws IOException {
        return db.getDVDs();
    }

    /**
     * A properly formatted <code>String</code> expressions returns all matching
     * Dvd items. The <code>String</code> must be formatted as a regular
     * expression.
     *
     * @param query A regular expression searcg string.
     * @return A <code>Collection</code> of <code>Dvd</code> objects that match
     * the search criteria.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     * @throws PatternSyntaxException Thrown if an
     * <code>PatternSyntaxException</code> is encountered in the
     * <code>db</code> class.
     */
     public  Collection<DVD> findDVD(String query)
            throws IOException, PatternSyntaxException {
        return db.findDVD(query);
     }

    /**
     * Modifies a Dvd database entry specified by a Dvd object.
     *
     * @param item The Dvd to modify.
     * @return A boolean indicating the success or failure of the modify
     * operation.
     * @throws RemoteException  Thrown if an exception occurs in the
     * <code>DvdDatabaseImpl</code> class.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     * <br>
     * For more information, see {@link DvdDatabase}.
     */
    public boolean modifyDVD(DVD item) throws RemoteException, IOException {
        return db.modifyDVD(item);
    }

    /**
     * Removes a Dvd database entry specified by a UPC.
     *
     * @param upc The UPC number of the Dvd to remove.
     * @return A boolean indicating the success or failure of the removal
     * operation.
     * @throws RemoteException  Thrown if an exception occurs in the
     * <code>DvdDatabaseImpl</code> class.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     * <br>
     * For more information, see {@link DvdDatabase}.
     */
    public boolean removeDVD(String upc) throws RemoteException, IOException {
        return db.removeDVD(upc);
    }

    /**
     * Lock the requested Dvd. This method blocks until the lock succeeds,
     * or for a maximum of 5 seconds, whichever comes first.
     *
     * @param upc The upc of the Dvd to reserve
     * @return Indicates the success/failure of the add operation.
     * @throws InterruptedException Indicates the thread is interrupted.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     */
    public boolean reserveDVD(String upc)
            throws InterruptedException, IOException {
        return db.reserveDVD(upc);
    }

    /**
     * Unlock the requested record. Ignored if the caller does not have
     * a current lock on the requested record.
     *
     * @param upc The upc of the Dvd to release
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     */
    public void releaseDVD(String upc) throws IOException {
        db.releaseDVD(upc);
    }

    /**
     * Adds a dvd to the database or inventory.
     *
     * @param dvd The Dvd item to add to inventory.
     * @return Indicates the success/failure of the add operation.
     * @throws IOException Indicates there is a problem accessing the database.
     * @throws RemoteException  Thrown if an exception occurs in the
     * <code>DvdDatabaseImpl</code> class.
     */
    public boolean addDVD(DVD dvd) throws IOException, RemoteException {
        return db.addDVD(dvd);
    }
}
