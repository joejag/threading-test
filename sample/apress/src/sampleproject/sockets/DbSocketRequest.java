package sampleproject.sockets;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import sampleproject.db.*;

/**
 * Processes a request received over a socket connection from a client.
 * This object runs on its own thread. It is spawned from the main worker
 * thread of the SocketServer and represents a singe client database request.
 * This class explicitly implements our application protocol.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public class DbSocketRequest extends Thread {
    /**
     * Holds the socket connection to the client.
     */
    private Socket client;

    /**
     * The reference to the internal <code>DBClient</code> instance.
     */
    private DBClient dvdDatabase;

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>sampleproject.sockets</code>.
     */
    private Logger logger = Logger.getLogger("sampleproject.sockets");

    /**
     * The Request Constructor.
     *
     * @param dbLocation the location of the data file.
     * @param socketClient The socket end-point that listens for a
     * client request.
     * @throws IOException on network error or on data file access error.
     */
    public DbSocketRequest(String dbLocation, Socket socketClient)
            throws IOException {
        super("DBRequestSocket");

        this.client = socketClient;
        this.dvdDatabase = new DvdDatabase(dbLocation);

        logger.finer("A socket request has been recieved.");
    }

    /**
     * Required for a class that extends thread, this is the main path
     * of execution for this thread.
     */
    public void run() {

        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream  in =
                    new ObjectInputStream(client.getInputStream());

            for (;;) {
                DvdCommand cmdObj = (DvdCommand) in.readObject();
                out.writeObject(execCmdObject(cmdObj));
            }
        } catch (SocketException e) {
            logger.log(Level.SEVERE,
                       "SocketException in Socket Server: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE,
                       "General Exception in Socket Server: "
                       + e.getMessage());
        }
    }

    /**
     * Helper method that takes the command object from the client
     * and hands it to the db.
     *
     * @param dvdCmd The command object from the socket client.
     * @return Object The return result from the database wrapped
     * in a <code>DvdResult</code> object.
     */
    private Object execCmdObject(DvdCommand dvdCmd) {

        DvdResult result = null;

        try {
            DVD aDvd = null;
            switch (dvdCmd.getCommandId()) {
                case FIND:
                    result = new DvdResult(dvdDatabase.findDVD(dvdCmd.getRegex()));
                    logger.finer("A Find request has been processed.");
                    break;
                case GET_DVDS:
                    result = new DvdResult(dvdDatabase.getDVDs());
                    logger.finer("A getDVDs request has been processed.");
                    break;
                case GET_DVD:
                    aDvd = dvdCmd.getDVD();
                    DVD foundDvd = dvdDatabase.getDVD(aDvd.getUPC());
                    result = new DvdResult(foundDvd);
                    logger.finer("A getDVD request has been processed.");
                    break;
                case RESERVE:
                    aDvd = dvdCmd.getDVD();
                    String upc = aDvd.getUPC();
                    result = new DvdResult(dvdDatabase.reserveDVD(upc));
                    logger.finer("A reserve DVD request has been processed.");
                    break;
                case RELEASE:
                    aDvd = dvdCmd.getDVD();
                    dvdDatabase.releaseDVD(aDvd.getUPC());
                    result = new DvdResult(true);
                    logger.finer("A release DVD request has been processed.");
                    break;
/*
                case RENT:
                    String upc = dvdCmd.getDVD().getUPC();
                    result = new DvdResult(dvdDatabase.rent(upc));
                    logger.finer("A rent request has been processed.");
                    break;
                case RETURN_RENTAL:
                    String upc = dvdCmd.getDVD().getUPC();
                    result = new DvdResult(dvdDatabase.returnRental(upc));
                    logger.finer("A returnRental request has been processed.");
                    break;
*/
                case ADD:
                    DVD addDVD = dvdCmd.getDVD();
                    result = new DvdResult(dvdDatabase.modifyDVD(addDVD));
                    logger.finer("A addDVD request has been processed.");
                    break;
                case MODIFY:
                    DVD modifyDVD = dvdCmd.getDVD();
                    result = new DvdResult(dvdDatabase.modifyDVD(modifyDVD));
                    logger.finer("A modifyDVD request has been processed.");
                    break;
                default:
                    logger.warning("unknown request received:"
                                   + dvdCmd.getCommandId());
                    break;
            }
        } catch (Exception e) {
            result = new DvdResult(e);
            logger.log(Level.SEVERE, "Request processing failed.", e);
        }

        return result;
    }
}
