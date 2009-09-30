package sampleproject.gui;

import java.io.*;
import java.util.*;

/**
 * Provides read/write access to the user's saved configuration parameters on
 * disk, so that next time they connect, we can offer the same configuration
 * parameters as a default.
 *
 * @author Denny's DVDs
 */
public class SavedConfiguration {
    /**
     * key in Properties indicating that the value will be the database
     * location.
     */
    public static final String DATABASE_LOCATION = "DatabaseLocation";

    /**
     * key in Properties indicating that the value will be the RMI Registry
     * server address.
     */
    public static final String SERVER_ADDRESS = "ServerAddress";

    /**
     * key in Properties indicating that the value will be the port the RMI
     * Registry or the Socket Server listens on.
     */
    public static final String SERVER_PORT = "ServerPort";

    /**
     * key in Properties indicating that the what type of network connectivity
     * will be used between the client and server.
     */
    public static final String NETWORK_TYPE = "NetworkType";

    /**
     * the Properties for this application.
     */
    private Properties parameters = null;

    /**
     * The location where our configuration file will be saved.
     */
    private static final String BASE_DIRECTORY = System.getProperty("user.dir");

    /**
     * the name of our properties file.
     */
    private static final String OPTIONS_FILENAME = "dennys.properties";

    /**
     * The file containing our saved configuration.
     */
    private static File savedOptionsFile
            = new File(BASE_DIRECTORY, OPTIONS_FILENAME);

    /**
     * Placeholder for our singleton version of OptionsModel. Since we know
     * that we will want at least one of these, we are creating our instance as
     * soon as we possibly can. If we were unsure whether we would ever need
     * this or not we would probably perform a lazy instantiation.
     */
    private static SavedConfiguration savedConfiguration
            = new SavedConfiguration();


   /**
    * Creates a new instance of SavedConfiguration. There should only ever be
    * one instance of this class (a Singleton), so we have made it private.
    */
   private SavedConfiguration() {
        parameters = loadParametersFromFile();

        if (parameters == null) {
            parameters = new Properties();

            parameters.setProperty(SERVER_ADDRESS, "localhost");
            parameters.setProperty(SERVER_PORT,
                    "" + java.rmi.registry.Registry.REGISTRY_PORT);
        }
   }

    /**
     * return the single instance of the SavedConfiguration.
     *
     * @return the single instance of the SavedConfiguration.
     */
    public static SavedConfiguration getSavedConfiguration() {
        return savedConfiguration;
    }

    /**
     * returns the value of the named parameter.<p>
     *
     * @param parameterName the name of the parameter for which the user
     * is requesting the value.
     * @return the value of the named parameter.
     */
    public String getParameter(String parameterName) {
        return parameters.getProperty(parameterName);
    }

    /**
     * Updates the saved parameters with the new values. Always saves the new
     * values immediately - this means we will be saving the properties file
     * far more often than we need when we enter our first set of values,
     * however once the initial set of values have been entered, they should
     * rarely (if ever) be changed. So this will not be  a problem most of the
     * time. Doing it this way means that the user of this class need not
     * explictly save the parameters.
     *
     * @param parameterName the name of the parameter.
     * @param parameterValue the value to be stored for the parameter
     */
    public void setParameter(String parameterName, String parameterValue) {
        parameters.setProperty(parameterName, parameterValue);
        saveParametersToFile();
    }

    /**
     * saves the parameters to a file so that they can be used again next time
     * the application starts.
     */
    private void saveParametersToFile() {
        try {
            synchronized (savedOptionsFile) {
               if (savedOptionsFile.exists()) {
                   savedOptionsFile.delete();
               }
               savedOptionsFile.createNewFile();
               FileOutputStream fos = new FileOutputStream(savedOptionsFile);
               parameters.store(fos, "Denny's DVDs configuration");
               fos.close();
            }
        } catch (IOException e) {
            ApplicationRunner.handleException(
                    "Unable to save user parameters to file. "
                    + "They wont be remembered next time you start.");
        }
    }

    /**
     * attempts to load the saved parameters from the file so that the user does
     * not have to reenter all the information.
     *
     * @return Properties loaded from file or null.
     */
    private Properties loadParametersFromFile() {
        Properties loadedProperties = null;

        if (savedOptionsFile.exists() && savedOptionsFile.canRead()) {
            synchronized (savedOptionsFile) {
               try {
                   FileInputStream fis = new FileInputStream(savedOptionsFile);
                   loadedProperties = new Properties();
                   loadedProperties.load(fis);
                   fis.close();
               } catch (FileNotFoundException e) {
                   assert false : "File not found after existance verified";
                   ApplicationRunner.handleException("Unable to load user "
                              + "parameters. Default values will be used.\n"
                              + e);
               } catch (IOException e) {
                   assert false : "Bad data in parameters file";
                   ApplicationRunner.handleException("Unable to load user "
                              + "parameters. Default values will be used.\n"
                              + e);
               }
            }
        }
        return loadedProperties;
    }
}
