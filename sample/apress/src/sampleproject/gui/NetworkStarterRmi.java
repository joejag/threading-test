/*
 * NetworkStarterRmi.java
 *
 * Created on 22 June 2005, 23:49
 */

package sampleproject.gui;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.logging.*;
import javax.swing.JLabel;

/**
 * Starts the server that accepts connections over RMI.
 */
public class NetworkStarterRmi {
    /**
     * An error code to be passed back to the operating system itself if the
     * port number provided is invalid.
     */
    public static final int ERR_CODE_INVALID_PORT_NUMBER = -1;
    /**
     * An error code to be passed back to the operating system itself if the
     * registry cannot be started.
     */
    public static final int ERR_CODE_CANT_START_REGISTRY = -2;

    /*
     * Strings that appear in log messages and in the status bar.
     */
    private static final String CANT_START_REGISTRY
            = "Unable to start the RMI registry, which is required in order to "
            + "run the server.\nPerhaps the port is already in use?";
    private static final String INVALID_PORT_NUMBER = "Invalid port number ";

    private static final String SERVER_STARTING
            = "Starting RMI Registry and Registring Server";
    private static final String SERVER_RUNNING = "Server running.";

    /**
     * Our default port - the same as the standard RMI port.
     */
    private int port = java.rmi.registry.Registry.REGISTRY_PORT;
    /**
     * The fully qualified path to the database file.
     */
    private String databaseLocation = null;

    /**
     * A reference to the registry we will instantiate.
     */
    private Registry registry = null;

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>sampleproject.gui</code>.
     */
    private Logger log = Logger.getLogger("sampleproject.gui");

    /**
     * Creates a new instance of NetworkStarterRmi.
     *
     * @param dbLocation the location of the data file on a local hard drive.
     * @param port the port number the RMI registry will listen on.
     * @param status a label on the server GUI we can update with our status.
     */
    public NetworkStarterRmi(String dbLocation, String port, JLabel status) {
        try {
           this.port = Integer.parseInt(port);
           log.info("Starting RMI registry on port " + port);
           status.setText(SERVER_STARTING);
           sampleproject.remote.RegDvdDatabase.register(dbLocation, this.port);

           log.info("Server started.");
           status.setText(SERVER_RUNNING);

           // Save our configuration now that it all seems to be working.
           SavedConfiguration config
                   = SavedConfiguration.getSavedConfiguration();

           config.setParameter(SavedConfiguration.DATABASE_LOCATION,
                               dbLocation);

           config.setParameter(SavedConfiguration.SERVER_PORT, port);
           config.setParameter(SavedConfiguration.NETWORK_TYPE,
                               "" + ConnectionType.RMI);
        } catch (NumberFormatException e) {
           // this should never happen, since we are taking pains to ensure
           // that only numbers can be entered into the text field. But
           // just in case ...
           log.log(Level.SEVERE, INVALID_PORT_NUMBER, e);
           System.exit(ERR_CODE_INVALID_PORT_NUMBER);
        } catch (RemoteException e) {
           // We cannot start the registry. Since we have not defined our
           // classpath, we cannot _easily_ attach to an already running
           // registry - just notify the user, and exit.
           log.log(Level.SEVERE, CANT_START_REGISTRY, e);
           System.exit(ERR_CODE_CANT_START_REGISTRY);
        }
    }
}
