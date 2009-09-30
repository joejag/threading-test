package sampleproject.direct;

import sampleproject.db.*;
import java.io.*;

/**
 * A  DvdConnector is used in cases where the GUI client wants to make a
 * connection to the data file. In this case, that connection is an direct
 * connection.
 *
 * @author Denny's DVDs
 * @version 2.0
 * @see sampleproject.db.DvdDatabase
 */
public class DvdConnector {
    /**
     * Since this is a utility class (it only exists for other classes to call
     * it's static methods), lets stop users creating unneeded instances of
     * this class by creating a private constructor.
     */
    private DvdConnector() {
    }

    /**
     * Static method that gets a database handle.
     * The DBClient is a local object.
     *
     * @param dbLocation the path to the database on disk.
     * @return A <code>DBClient</code> instance.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>DvdDatabase</code> class.
     * <br>
     * For more information, see {@link DvdDatabase}.
     * @throws ClassNotFoundException Thrown if an
     * <code>ClassNotFoundException</code> is
     * encountered in the <code>DvdDatabase</code> class.
     * <br>
     * For more information, see {@link DvdDatabase}.
     */
    public static DBClient getLocal(String dbLocation)
            throws IOException, ClassNotFoundException {
        return new DvdDatabase(dbLocation);
    }
}
