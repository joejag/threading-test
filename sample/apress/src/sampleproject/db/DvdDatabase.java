package sampleproject.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * A DvdDatabase object provides access to all of the Dvds in the system and
 * all the operations that act upon the dvds.
 * <br/>
 * This is the only class that a programmer should use to access or modify the
 * Dvds. This class uses the Facade design pattern (this class calls other
 * classes, of which the end user has no knowledge). This enables us to write
 * classes in the db package that have only a single responsibility (easier to
 * code and maintain), while giving users of our package one simple set of APIs
 * the can call for all access to the database.
 * <br />
 * This class also uses the Adapter pattern, changing method calls to suit
 * the requirements of the classes it calls - as an example, check out the
 * <code>reserveDvd</code> and <code>releaseDvd</code> methods.
 * <br />
 * One instance of this class should be created for each connected client. The
 * instance of this class can then be used to identify the client to the
 * reservations module.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public class DvdDatabase implements DBClient {
    /**
     * A static structure that keeps track of the locked Dvd records.
     */
    private static ReservationsManager reservationsManager
            = new ReservationsManager();

    /**
     * The class that handles all our physical access to the database.
     */
    private static DvdFileAccess database = null;

    /**
     * Constructor that assumes the Dvd database is in the current working
     * directory.
     *
     * @throws FileNotFoundException if the database file cannot be found.
     * @throws IOException if the database file cannot be written to.
     */
    public DvdDatabase() throws FileNotFoundException, IOException {
        // Note: This is the only place we are acknowledging that we are using
        // a flat file instead of a commercial database. Create another Adapter
        // & replace this one line, & the system can use a commercial database.
        this(System.getProperty("user.dir"));
    }

    /**
     * Constructor that takes the path of the Dvd database as a parameter.
     *
     * @param dbPath the path to the dvd_db directory
     * @throws FileNotFoundException if the database file cannot be found.
     * @throws IOException if the database file cannot be written to.
     */
    public DvdDatabase(String dbPath)
            throws FileNotFoundException, IOException {
        // Note: This is the only place we are acknowledging that we are using
        // a flat file instead of a commercial database. Create another Adapter
        // & replace this one line, & the system can use a commercial database.
        database = new DvdFileAccess(dbPath);
    }

    /**
     * Adds a dvd to the database or inventory.
     *
     * @param dvd The Dvd item to add to inventory.
     * @return Indicates the success/failure of the add operation.
     * @throws IOException Indicates there is a problem accessing the database.
     */
    public boolean addDVD(DVD dvd) throws IOException {
        return database.addDvd(dvd);
    }

    /**
     * Locates a Dvd using the upc identification number.
     *
     * @param upc The UPC of the Dvd to locate.
     *
     * @return The Dvd object which matches the upc or null if the Dvd does not
     * exist.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public DVD getDVD(String upc) throws IOException {
        return database.getDvd(upc);
    }

    /**
     * Removes Dvd from inventory using the unique upc.
     *
     * @param upc The upc or key of the Dvd to be removed.
     * @return true if the upc was found and the Dvd was removed.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public boolean removeDVD(String upc) throws IOException {
        return database.removeDvd(upc);
    }

    /**
     * Changes existing information of a Dvd item.
     * Modifications can occur on any of the attributes of Dvd except UPC.
     * The UPC is used to identify the Dvd to be modified.
     *
     * @param dvd The item in question
     * @return Returns true if the Dvd was found and modified.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public boolean modifyDVD(DVD dvd) throws IOException {
        return database.modifyDvd(dvd);
    }

    /**
     * Gets the store's inventory.
     * All of the Dvds in the system.
     *
     * @return A collection of all found Dvd's.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public List<DVD> getDVDs() throws IOException {
        return database.getDvds();
    }

    /**
     * A properly formatted <code>String</code> expressions returns all matching
     * Dvd items. The <code>String</code> must be formatted as a regular
     * expression.
     *
     * @param query formatted regular expression used as the search criteria.
     * @return The list of Dvds that match the query. Can be an empty
     * Collection.
     * @throws IOException Indicates there is a problem accessing the data.
     * be found.
     * @throws PatternSyntaxException Indicates there is a syntax problen in
     * the regular expression.
     */
    public Collection<DVD> findDVD(String query)
            throws IOException, PatternSyntaxException {
        return database.find(query);
    }

    /**
     * Lock the requested Dvd. This method blocks until the lock succeeds.
     *
     * @param upc The upc of the Dvd to reserve
     * @return true if DVD could be reserved
     * @throws InterruptedException Indicates the thread is interrupted.
     */
    public boolean reserveDVD(String upc) throws InterruptedException {
        return reservationsManager.reserveDvd(upc, this);
    }

    /**
     * Unlock the requested record. Ignored if the caller does not have
     * a current lock on the requested record.
     *
     * @param upc The upc of the Dvd to release
     */
    public void releaseDVD(String upc) {
        reservationsManager.releaseDvd(upc, this);
    }

    /**
     * Locks or unlocks the entire database.
     * @param locked true if the database is to be locked, false otherwise.
     */
    public void setDatabaseLocked(boolean locked) {
        if (database instanceof DvdFileAccess) {
            ((DvdFileAccess) database).setDatabaseLocked(locked);
        }
    }
}
