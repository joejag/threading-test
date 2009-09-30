/*
 * CleanExit.java
 *
 * Created on 23 June 2005, 00:21
 */
package sampleproject.gui;

import java.io.IOException;
import java.util.logging.*;
import sampleproject.db.*;

/**
 * The <code>run</code> method of this class will be executed when the
 * application is shut down by the user, performing any necessary actions to
 * ensure that the application stops correctly (for example, ensuring that we
 * are not in the process of writing to the database, which might result in a
 * corrupted data file).<p>
 *
 * This class should be registered with the JVM to run when the application is
 * shutting down by calling <code>Runtime.addShutdownHook</code> with an
 * instance of this class as a parameter.
 *
 * @author Denny's DVDs
 * @version 2.0
 * @see java.lang.Runtime#addShutdownHook
 */
public class CleanExit extends Thread {
    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>sampleproject.gui</code>.
     */
    private Logger log = Logger.getLogger("sampleproject.gui");

    /**
     * The location of our database. This is needed so that we can connect to
     * the DvdDatabase correctly prior to locking it from updates.
     */
    private String dbLocation = null;

    /**
     * Create an instance of this class so that it can be run later - note that
     * it is not run at this point. We store the location of the database so
     * that we are referring to the same database when we later try to shut it
     * down.
     *
     * @param dbLocation the location of the data file on the disk.
     */
    public CleanExit(String dbLocation) {
        this.dbLocation = dbLocation;
    }

    /**
     * This method will be executed by the JVM when the application is shutdown.
     * It ensures that the database is in a clean state for shutdown (that is,
     * there are no outstanding writes occuring).
     */
    public void run() {
        log.info("Ensuring a clean shutdown");
        try {
            DvdDatabase database = new DvdDatabase(dbLocation);
            database.setDatabaseLocked(true);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to lock database before exiting", e);
        }
    }
}
