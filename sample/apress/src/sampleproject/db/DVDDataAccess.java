package sampleproject.db;

import java.io.IOException;
import java.util.*;
import java.util.regex.PatternSyntaxException;

/**
 * This is the interface for all the data access and manipulation of the
 * database.
 * <br/>
 * Using this interface makes it easier to swap out the manually accessed
 * physical file system for a commercial database later. The exceptions thrown
 * by a commercial database might not match those we are throwing here, but
 * the SQL exceptions could easily be adapted to the exceptions we are throwing
 * here (hence the classes implementing this interface can be thought of as
 * using the Adapter design pattern).
 * <br/>
 * Note that since this should only be used by the DvdDatabase class, neither
 * this interface, nor any of it's methods have been declared public - they all
 * have default access.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
interface DvdDataAccess {

    /**
     * Adds a dvd to the database or inventory.
     *
     * @param dvd The Dvd item to add to inventory.
     *
     * @return Indicates the success/failure of the add operation.
     * @throws IOException Indicates there is a problem accessing the database.
     */
    boolean addDvd(DVD dvd) throws IOException;

    /**
     * Locates a Dvd using the upc identification number.
     *
     * @param upc The UPC of the Dvd to locate.
     * @return The Dvd object which matches the upc or null if the Dvd does not
     * exist.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    DVD getDvd(String upc) throws IOException;

    /**
     * Removes DVDs from inventory using the unique upc.
     *
     * @param upc The upc or key of the Dvd to be removed.
     * @return true if the upc was found and the Dvd was removed.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    boolean removeDvd(String upc) throws IOException;

    /**
     * Changes existing information of a Dvd item.
     * Modifications can occur on any of the attributes of Dvd except UPC.
     * The UPC is used to identify the Dvd to be modified.
     *
     * @param dvd The item in question
     * @return Returns true if the Dvd was found and modified.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    boolean modifyDvd(DVD dvd) throws IOException;

    /**
     * Gets the store's inventory.
     * All of the DVDs in the system.
     *
     * @return A collection of all found Dvd's.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    List<DVD> getDvds() throws IOException;

    /**
     * A properly formatted <code>String</code> expressions returns all matching
     * Dvd items. The <code>String</code> must be formatted as a regular
     * expression.
     *
     * @param query The formatted regular expression used as the search
     * criteria.
     * @return The list of DVDs that match the query. Can be an empty
     * Collection.
     * @throws IOException Indicates there is a problem accessing the data.
     * @throws PatternSyntaxException Indicates there is a syntax problen in
     * the regular expression.
     */
    Collection<DVD> find(String query)
            throws IOException, PatternSyntaxException;
}
