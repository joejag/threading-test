package sampleproject.sockets;

import java.io.*;
import java.net.UnknownHostException;
import sampleproject.db.*;

 /**
  * A  DvdConnector is used in cases where the GUI client wants to make a
  * network connection. In this case, that connection is a Socket connection.
  *
  * @author Denny's DVDs
  * @version 2.0
  */
public class DvdConnector {
    /**
     * Since this is a utility class (it only exists for other classes to call
     * it's static methods), lets stop users creating unneeded instances of this
     * class by creating a private constructor.
     */
    private DvdConnector() {
    }

    /**
     * static method to create a connection to the database.
     * The DBClient is a remote object.
     *
     * @param hostname The ip or ddress of the host machine.
     * @param port the socket port the server is listening on.
     * @return a connection to the database.
     * @throws UnknownHostException when the host is unreachable or cannot be
     * resloved
     * @throws IOException if a communication error occurs trying to connect
     * to the host.
     */
    public static DBClient getRemote(String hostname, String port)
            throws UnknownHostException, IOException {
        return new DvdSocketClient(hostname, port);
    }
}
