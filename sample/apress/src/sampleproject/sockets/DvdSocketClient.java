package sampleproject.sockets;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import sampleproject.db.*;


/**
 * DvdSocketClient is a point-to-point socket client.
 * Implements all of the <code>DVDDatabase</code> methods for the
 * GUI defined in <code>DBClient</code>.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public class DvdSocketClient implements DBClient {
    /**
     * The socket client that gets instaniated for the socket connection.
     */
    private Socket socket = null;
    /**
     * The outputstream used to write a serialized object to a socket server.
     */
    private ObjectOutputStream oos = null;
    /**
     * The inputstream used to read a serialized object (a response)
     * from the socket server.
     */
    private ObjectInputStream ois = null;
    /**
     * The ip address of the machine the client is going to attempt a
     * connection.
     */
    private String ip = null;
    /**
     * The port number we will be connecting on.
     */
    private int port = 3000;


    /**
     * Default constructor.
     *
     * @throws UnknownHostException if unable to connect to "localhost".
     * @throws IOException on network error.
     * @throws NumberFormatException if portNumber is not valid (never happens).
     */
    public DvdSocketClient()
            throws UnknownHostException, IOException, NumberFormatException {
        this("localhost", "3000");
    }

    /**
     * Constructor takes in a hostname of the server to connect.
     *
     * @param hostname The hostname to connect to.
     * @param portNumber the string representation of the port to connect on.
     * @throws UnknownHostException if unable to connect to "localhost".
     * @throws IOException on network error.
     * @throws NumberFormatException if portNumber is not valid.
     */
    public DvdSocketClient(String hostname, String portNumber)
            throws UnknownHostException, IOException, NumberFormatException {
        ip = hostname;
        this.port = Integer.parseInt(portNumber);
        this.initialize();
    }

    /**
     * Adds a dvd to the database or inventory.
     *
     * @param dvd The DVD item to add to inventory.
     * @return A boolean value that indicates the success/failure of the
     * add operation.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public boolean addDVD(DVD dvd) throws IOException {
        DvdCommand cmdObj = new DvdCommand(SocketCommand.ADD, dvd);
        return getResultFor(cmdObj).getBoolean();
    }

    /**
     * Gets a <code>DVD</code> from the system using a upc.
     *
     * @param upc The upc of the DVD you want to view.
     * @return A DVD that matches the supplied upc.
     *
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public DVD getDVD(String upc) throws IOException {
        DVD dvd = new DVD();
        dvd.setUPC(upc);

        DvdCommand cmdObj = new DvdCommand(SocketCommand.GET_DVD, dvd);
        return getResultFor(cmdObj).getDVD();
    }

    /**
     * Attempts to rent the DVD matching the provided UPC.
     *
     * @param upc is the upc of the DVD you want to rent.
     * @return true if the DVD was rented. false if it cannot be rented.
     *
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     * <br>
     * For more information, see {@link DvdDatabase}.
     */
    public boolean rent(String upc) throws IOException {
        DVD dvd = new DVD();
        dvd.setUPC(upc);

        DvdCommand cmdObj = new DvdCommand(SocketCommand.RENT, dvd);
        return getResultFor(cmdObj).getBoolean();
    }

    /**
     * Attempts to return the DVD matching the provided UPC.
     *
     * @param upc The upc of the DVD you want to rent.
     * @return true if the DVD was rented. false if it cannot be rented.
     *
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     * <br>
     * For more information, see {@link DvdDatabase}.
     * <br>
     * For more information, see {@link DvdDatabase}.
     */
    public boolean returnRental(String upc) throws IOException {
        DVD     dvd    = new DVD();
        dvd.setUPC(upc);

        DvdCommand cmdObj = new DvdCommand(SocketCommand.RETURN, dvd);
        return getResultFor(cmdObj).getBoolean();
    }

    /**
     * Gets the store's inventory.
     * All of the DVDs in the system.
     *
     * @return A collection of all found DVD's.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public List<DVD> getDVDs() throws IOException {
        DvdCommand cmdObj = new DvdCommand(SocketCommand.GET_DVDS);
        return getResultFor(cmdObj).getList();
    }

    /**
     * A properly formatted <code>String</code> expressions returns all matching
     * DVD items. The <code>String</code> must be formatted as a regular
     * expression.
     *
     * @param query A regular expression searcg string.
     * @return A <code>Collection</code> of <code>DVD</code> objects that match
     * the search criteria.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     * @throws PatternSyntaxException if requested query is not a valid regular
     * expression.
     */
    public Collection<DVD> findDVD(String query)
            throws IOException, PatternSyntaxException {
        DvdCommand cmdObj = new DvdCommand(SocketCommand.FIND);
        cmdObj.setRegex(query);

        DvdResult serialReturn = getResultFor(cmdObj);

        if (serialReturn.isException()
                && serialReturn.getException() instanceof PatternSyntaxException) {
            throw (PatternSyntaxException) serialReturn.getException();
        } else {
            return serialReturn.getCollection();
        }
    }

    /**
     * Removes a <code>DVD</code> from the system using a upc.
     *
     * @param upc The upc of the DVD you want to remove from the database.
     * @return true if the item was removed, false if it was not removed.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public boolean removeDVD(String upc) throws IOException {
        DVD     dvd    = new DVD();
        dvd.setUPC(upc);

        DvdCommand cmdObj = new DvdCommand(SocketCommand.REMOVE, dvd);
        return getResultFor(cmdObj).getBoolean();
    }

    /**
     * Modifies a DVD database entry specified by a DVD object.
     *
     * @param dvd The DVD to modify.
     * @return A boolean indicating the success or failure of the modify
     * operation.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>db</code> class.
     * <br>
     * For more information, see {@link DvdDatabase}.
     */
    public boolean modifyDVD(DVD dvd) throws IOException {
        DvdCommand cmdObj = new DvdCommand(SocketCommand.MODIFY, dvd);
        return getResultFor(cmdObj).getBoolean();
    }

    /**
     * Lock the requested DVD. This method blocks until the lock succeeds,
     * or for a maximum of 5 seconds, whichever comes first.
     *
     * @param upc The UPC of the DVD to reserve
     * @throws InterruptedException Indicates the thread is interrupted.
     * @throws IOException on any network problem
     * @return true if DVD reserved.
     */
    public boolean reserveDVD(String upc)
            throws IOException, InterruptedException {
        DVD dvd = new DVD();
        dvd.setUPC(upc);
        DvdCommand cmdObj = new DvdCommand(SocketCommand.RESERVE, dvd);
        return getResultFor(cmdObj).getBoolean();
    }

    /**
     * Unlock the requested record. Ignored if the caller does not have
     * a current lock on the requested record.
     *
     * @param upc The UPC of the DVD to release
     * @throws IOException on any network problem
     */
    public void releaseDVD(String upc) throws IOException {
        DVD dvd = new DVD();
        dvd.setUPC(upc);
        DvdCommand cmdObj = new DvdCommand(SocketCommand.RELEASE, dvd);
        getResultFor(cmdObj).getBoolean();
    }

    /**
     * Method that does the work of sending our request to the client and
     * getting the response back, doing any necessary conversions between
     * a DvdCommand object, the Serialized Objects sent and received over
     * the Socket, and the DvdResult needed.
     *
     * @param command the command to be performed on the remote database.
     * @return a value object containing the result of the command requested.
     * @throws IOException on network error.
     */
    private DvdResult getResultFor(DvdCommand command) throws IOException {
//        this.initialize();
        try {
            oos.writeObject(command);
            DvdResult result = (DvdResult) ois.readObject();
            Exception e = result.getException();

            if (!result.isException()) {
                return result;
            } else if (e instanceof ClassNotFoundException) {
                throw (ClassNotFoundException) e;
            } else if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                // well, we still have an exception, but it is up to the
                // calling method to handle it
                return result;
            }
        } catch (ClassNotFoundException cnfe) {
            IOException ioe
                    = new IOException("problem with demarshelling DvdResult)");
            ioe.initCause(cnfe);
            throw ioe;
//        } finally {
//            closeConnections();
        }
    }

    /**
     * Performs any clean-up necessary when this connection is no longer used.
     * E.g. closing any open connections.
     *
     * @throws IOException on network error.
     */
    public void finalize() throws java.io.IOException {
        closeConnections();
    }

    /**
     * A helper method which initializes a socket connection on specified port.
     *
     * @throws UnknownHostException if the IP address of the host could not be
     *         determined.
     * @throws IOException Thrown if the socket channel cannot be opened.
     */
    private void initialize() throws UnknownHostException, IOException {
        socket = new Socket(ip, port);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * A helper method which closes the socket connection.
     * Needs to be called from within a try-catch
     *
     * @throws IOException Thrown if the close operation fails.
     */
    private void closeConnections() throws IOException {
        oos.close();
        ois.close();
        socket.close();
    }
}
