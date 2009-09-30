package sampleproject.remote;

import java.rmi.Remote;
import sampleproject.db.DBClient;

/**
 * The remote interface for the GUI-Client.
 * Exactly matches the DBClient interface in the db package.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public interface DvdDatabaseRemote extends Remote, DBClient {
}
