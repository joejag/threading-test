package sampleproject.db;

import java.io.*;
import java.util.regex.*;
import java.util.*;

/**
 * An interface implemented by classes that provide access to the Dvd
 * data store, including DvdDatabase.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public interface DBClient {

    /**
     * Adds a DVD to the database or inventory.
     *
     * @param dvd The DVD item to add to inventory.
     * @return Indicates the success/failure of the add operation.
     * @throws IOException Indicates there is a problem accessing the database.
     */
    public boolean addDVD(DVD dvd) throws IOException;

    /**
     * Locates a DVD using the UPC identification number.
     *
     * @param UPC The UPC of the DVD to locate.
     * @return The DVD object which matches the UPC.
     * @throws IOException if there is a problem accessing the data.
     */
    public DVD getDVD(String UPC)throws IOException;

    /**
     * Changes existing information of a DV item.
     * Modifications can occur on any of the attributes of DVD except UPC.
     * The UPC is used to identify the DVD to be modified.
     *
     * @param dvd The Dvd to modify.
     * @return Returns true if the DVD was found and modified.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public  boolean modifyDVD(DVD dvd) throws IOException;

    /**
     * Removes DVDs from inventory using the unique UPC.
     *
     * @param UPC The UPC or key of the DVD to be removed.
     * @return Returns true if the UPC was found and the DVD was removed.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public  boolean removeDVD(String UPC) throws IOException;

    /**
     * Gets the store's inventory.
     * All of the DVDs in the system.
     *
     * @return A List containing all found DVD's.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public List<DVD> getDVDs() throws IOException;

     /**
      * A properly formatted <code>String</code> expressions returns all
      * matching DVD items. The <code>String</code> must be formatted as a
      * regular expression.
      *
      * @param query The formatted regular expression used as the search
      * criteria.
      * @return The list of DVDs that match the query. Can be an empty
      * Collection.
      * @throws IOException Indicates there is a problem accessing the data.
      * @throws PatternSyntaxException Indicates there is a syntax problem in
      * the regular expression.
     */
    public Collection<DVD> findDVD(String query)
            throws IOException, PatternSyntaxException;

    /**
     * Lock the requested DVD. This method blocks until the lock succeeds,
     * or for a maximum of 5 seconds, whichever comes first.
     *
     * @param UPC The UPC of the DVD to reserve
     * @return true if the DVD was reserved
     * @throws InterruptedException Indicates the thread is interrupted.
     * @throws IOException on any network problem
     */
    boolean reserveDVD(String UPC) throws IOException, InterruptedException;

    /**
     * Unlock the requested record. Ignored if the caller does not have
     * a current lock on the requested record.
     *
     * @param UPC The UPC of the DVD to release
     * @throws IOException on any network problem
     */
    void releaseDVD(String UPC) throws IOException;
}
