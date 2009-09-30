package sampleproject.sockets;

import java.util.Collection;
import java.util.List;
import sampleproject.db.*;


/**
 * <code>DvdResult</code> is a wrapper for the return values from the
 * <code>DBClient</code> methods.
 * The server will send this object to the socket client which will inspect it
 * for exceptions and valid return results.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public class DvdResult implements java.io.Serializable {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * An internal reference to a DVD object.
     */
    private DVD dvd = null;

    /**
     * The exception member. When an exception occurs,
     * the original exception is returned in the result.
     */
    private Exception exception = null;

    /**
     * The boolean result returned to the socket client.
     */
    private boolean booleanResult = false;

    /**
     * The collection result returned to the socket client.
     */
    private Collection<DVD> collection = null;

    /**
     * The list result returned to the socket client.
     */
    private List<DVD> list = null;

    /**
     * Constructor which takes in a dvd object.
     * Used for getDVD().
     *
     * @param dvd The <code>DVD</code> to initialize with.
     */
    public DvdResult(DVD dvd) {
        this.dvd = dvd;
    }

    /**
     * Constructor which takes in a Collection object.
     * Used for getDVDs().
     *
     * @param aCollection A <code>Collection</code> of <code>DVD</code>
     * objects to initialize with.
     */
    public DvdResult(Collection<DVD> aCollection) {
        this.collection = aCollection;
    }

    /**
     * Constructor which takes in a List object.
     * Used for findDVD().
     *
     * @param aList A <code>List</code> of <code>DVD</code>
     * objects to initialize with.
     */
    public DvdResult(List<DVD> aList) {
        this.list = aList;
    }

    /**
     * Constructor which takes in a boolean.
     * Used for rent(), returnRental, and other <code>DBClient</code> methods.
     *
     * @param retVal The <code>boolean</code> to initialize with.
     */
    public DvdResult(boolean retVal) {
        this.booleanResult = retVal;
    }

    /**
     * Constructor which takes in an exception.
     *
     * @param e The <code>Exception</code> to initialize with.
     */
    public DvdResult(Exception e) {
        this.exception   = e;
    }

    /**
     * Return the boolean value of this object.
     *
     * @return The boolean value of the return value.
     */
    public boolean getBoolean() {
        return booleanResult;
    }

    /**
     * Returns the <code>DVD</code> value of this object.
     *
     * @return The <code>DVD</code> object of the return value.
     */
    public DVD getDVD() {
        return this.dvd;
    }

    /**
     * Returns the <code>Exception</code> value of this object.
     *
     * @return The <code>Exception</code> object of the return value.
     */
    public Exception getException() {
        return this.exception;
    }

    /**
     * Returns the <code>Collection</code> value of this object.
     *
     * @return The <code>Collection</code> object of the return value.
     */
    public Collection<DVD> getCollection() {
        return this.collection;
    }

    /**
     * Returns the <code>List</code> value of this object.
     *
     * @return the <code>List</code> object of the return value.
     */
    public List<DVD> getList() {
        return this.list;
    }

    /**
     * Indicates if this object is a wrapper for an <code>Exception</code>.
     *
     * @return true if an exception has occured, false if a everything is ok.
     */
    public boolean isException() {
        return exception != null;
    }

    /**
     * Creates a string representing this result for debugging and logging
     * purposes.
     *
     * @return a string representing this DvdCommand.
     */
    public String toString() {
        return "DvdResult:["
               + "DVD: " + dvd + "; "
               + "Exception: " + exception + "; "
               + "booleanResult: " + booleanResult + "; "
               + "collection: " + collection + "; "
               + "list: " + list + "; "
               + "]";
    }
}
