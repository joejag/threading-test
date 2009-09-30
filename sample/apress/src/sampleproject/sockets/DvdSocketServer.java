package sampleproject.sockets;

import java.io.IOException;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sampleproject.db.*;

/**
 * DvdSocketServer is the class that handles socket client requests and
 * passes the request to the database. The class recieves parameters in
 * <code>DVDCommand</code> objects and returns results in
 * <code>DVDResult</code> objects.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public class DvdSocketServer extends Thread {
    private String dbLocation = null;
    private int port = 3000;

    /**
     * Starts the socket server.
     *
     * @param argv Command line arguments.
     */
    public static void main(String[] argv)
    {
      register("Apress", 3000);
    }

    /**
     * Registers a socket server, listening on the specified port, accessing
     * the specified data file.
     *
     * @param dbLocation the location of the data file on disk.
     * @param port the port to listen on.
     */
    public static void register(String dbLocation, int port) {
        new DvdSocketServer(dbLocation, port).start();
    }

    /**
     * Creates a socket server, listening on the specified port, accessing
     * the specified data file.
     *
     * @param dbLocation the location of the data file on disk.
     * @param port the port to listen on.
     */
    public DvdSocketServer(String dbLocation, int port) {
        this.dbLocation = dbLocation;
        this.port = port;
    }

    /**
     * Listens for connections, handling any errors.
     */
    public void run() {
        try {
            listenForConnections();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Listens for new client connections, creating a new thread to handle the
     * requests.
     *
     * @throws IOException on network error.
     */
    public void listenForConnections() throws IOException {
        ServerSocket aServerSocket = new ServerSocket(port);
        //block for 60,000 msecs or 1 minute
        aServerSocket.setSoTimeout(60000);

        (Logger.getLogger("sampleproject.sockets")).log(Level.INFO,
                "a server socket created on port "
                + aServerSocket.getLocalPort());

        while (true) {
            Socket aSocket = aServerSocket.accept();
            DbSocketRequest request = new DbSocketRequest(dbLocation, aSocket);
            request.start();
        }
    }
}
