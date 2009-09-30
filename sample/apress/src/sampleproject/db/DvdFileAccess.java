package sampleproject.db;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.logging.*;
import java.util.regex.*;

/**
 * This is the worker class that does all the access and manipulation of the
 * physical file that is our database.
 * <br/>
 * Having all the code here, instead of in the DvdDatabase class, means that
 * private methods are only in here - the DvdDatabase class is much cleaner.
 * It is also much easier to swap this file out at a later point and put a
 * commercial database in it's place. Finally, we can limit responsibilities
 * of classes - this class does not care about locking and the locking class
 * does not care about file access.
 * <br/>
 * This class can be thought of as using the Adapter design pattern (where this
 * class is hiding the low level database access information from the end user
 * - they are unaware of whether we are accessing a file directly or whether we
 * are connecting to a commercial database).
 * <br/>
 * Note that since this should only be used by the DvdDatabase class, the class
 * has been set to have default access.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
class DvdFileAccess {
    /**
     * The unique name of the database.
     */
    private static final String DATABASE_NAME = "dvd_db.dvd";

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>sampleproject.db</code>.
     */
    private Logger log = Logger.getLogger("sampleproject.db"); // Log output

    /**
     * The physical file on disk containing our Dvd records.
     */
    private RandomAccessFile database = null;

    /**
     * Contains an index for our primary key (UPC number) to file location.
     */
    private static Map<String, Long> recordNumbers
            = new HashMap<String, Long>();

    /**
     * Ensures that many users can read the <code>recordsNumbers</code>
     * collection as long as nobody is updating it.
     */
    private static ReadWriteLock recordNumbersLock
            = new ReentrantReadWriteLock();

    /**
     * A <code>String</code> which will be the same size as a Dvd record
     * and filled with nulls. Having this prebuilt will save us some time
     * when building a complete record to write to disk.
     */
    private static String emptyRecordString = null;

    /**
     * The location where the database is stored.
     */
    private static String dbPath = null;

    /**
     * Initializer for static objects. Builds the <code>emptyRecordString
     * </code>mentioned earlier.
     */
    static {
        emptyRecordString = new String(new byte[DVD.RECORD_LENGTH]);
    }

    /**
     * Default constructor that accepts the database path as a parameter.<br/>
     *
     * All instances of this class share the same data file.
     *
     * @param suppliedDbPath the path to the dvd_db directory
     * @throws FileNotFoundException if the database file cannot be found.
     * @throws IOException if the database file cannot be written to.
     */
    public DvdFileAccess(String suppliedDbPath)
            throws FileNotFoundException, IOException {
        log.entering("DvdFileAccess", "getDvdFileAccess", suppliedDbPath);
        if (database == null) {
            database = new RandomAccessFile(suppliedDbPath, "rw");
            getDvdList(true);
            dbPath = suppliedDbPath;
            log.fine("database opened and file location table populated");
        } else if (dbPath != suppliedDbPath) {
            log.warning("Only one database location can be specified. "
                        + "Current location: " + dbPath + " "
                        + "Ignoring specified path: " + suppliedDbPath);
        }
        log.exiting("DvdFileAccess", "DvdFileAccess");
    }

    /**
     * Gets the store's inventory.
     * All of the Dvds in the system.
     *
     * @return A collection of all found Dvd's.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public List<DVD> getDvds() throws IOException {
        return getDvdList(false);
    }

    /**
     * Gets the store's inventory - all of the Dvds in the system.
     *
     * This is a private method, as we call this from our constructor in order
     * to populate our map of UPC numbers to file locations. If there was any
     * other accessability modifier on this method, then another class could
     * override this class and method, with the potential result that following
     * instantiation our map may not contain the required data.
     *
     * @param buildRecordNumbers build internal map of UPCs to file locations
     * @return A collection of all found Dvd's.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    private List<DVD> getDvdList(boolean buildRecordNumbers)
            throws IOException {
        log.entering("DvdFileAccess", "getDvdList", buildRecordNumbers);
        List<DVD> returnValue = new ArrayList<DVD>();

        if (buildRecordNumbers) {
            recordNumbersLock.writeLock().lock();
        }

        try {
            for (long locationInFile = 0;
                    locationInFile < database.length();
                    locationInFile += DVD.RECORD_LENGTH) {
                DVD dvd = retrieveDvd(locationInFile);
                log.fine("retrieving record at " + locationInFile);
                if (dvd == null) {
                    log.fine("found deleted record ");
                } else {
                    log.fine("found record " + dvd.getUPC());
                    if (buildRecordNumbers) {
                        recordNumbers.put(dvd.getUPC(), locationInFile);
                    }
                    returnValue.add(dvd);
                }
            }
        } finally {
            if (buildRecordNumbers) {
                recordNumbersLock.writeLock().unlock();
            }
        }

        log.exiting("DvdFileAccess", "getDvdList");
        return returnValue;
    }

    /**
     * Locates a Dvd using the upc identification number.
     *
     * @param upc The UPC of the Dvd to locate.
     * @return The Dvd object matching the upc or null if the Dvd doesnt exist.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public DVD getDvd(String upc) throws IOException {
        log.entering("DvdFileAccess", "getDvd", upc);

        recordNumbersLock.readLock().lock();
        try {
            // Determine where in the file this record should be.
            // note: if this is null the record does not exist
            Long locationInFile = recordNumbers.get(upc);
            return (locationInFile != null) ? retrieveDvd(locationInFile)
                                            : null;
        } finally {
            recordNumbersLock.readLock().unlock();
            log.exiting("DvdFileAccess", "getDvd");
        }
    }

    /**
     * This method retrieves a Dvd object from the database.
     * then the Dvd will be created.
     *
     * @param locationInFile the offset into the file where the record starts.
     * @return The requested Dvd.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    private DVD retrieveDvd(long locationInFile) throws IOException {
        log.entering("DvdFileAccess", "retrieveDvd", locationInFile);
        final byte[] input = new byte[DVD.RECORD_LENGTH];

        // Block other users for minimum time possible. Get exlusive access to
        // the file, read the entire set of bytes, then release exclusive lock.
        synchronized (database) {
            database.seek(locationInFile);
            database.readFully(input);
        }

        // now convert those bytes into a Dvd value object. Note that the thread
        // that is doing this conversion can be running while other threads are
        // doing other work - we are no longer blocking them.

        /**
         * class to assist in converting from the one big byte[] into multiple
         * String[] - one String per field. Refer to the book for more info.
         */
        class RecordFieldReader {
            /** field to track the position within the byte array */
            private int offset = 0;
            /**
             * converts the required number of bytes into a String.
             *
             * @param length the length to be converted from current offset.
             * @return the converted String
             * @throws UnsupportedEncodingException if "UTF-8" not known.
             */
            String read(int length) throws UnsupportedEncodingException {
                String str = new String(input, offset, length, "UTF-8");
                offset += length;
                return str.trim();
            }
        }

        RecordFieldReader readRecord = new RecordFieldReader();
        String upc = readRecord.read(DVD.UPC_LENGTH);
        String name = readRecord.read(DVD.NAME_LENGTH);
        String composer = readRecord.read(DVD.COMPOSER_LENGTH);
        String director = readRecord.read(DVD.DIRECTOR_LENGTH);
        String leadActor = readRecord.read(DVD.LEAD_ACTOR_LENGTH);
        String supportingActor = readRecord.read(DVD.SUPPORTING_ACTOR_LENGTH);
        String year = readRecord.read(DVD.YEAR_LENGTH);
        int copy = Integer.parseInt(readRecord.read(DVD.COPIES_LENGTH));

        DVD returnValue = ("DELETED".equals(upc))
                        ? null
                        : new DVD(upc, name, composer, director, leadActor,
                                  supportingActor, year, copy);

        log.exiting("DvdFileAccess", "retrieveDvd", returnValue);
        return returnValue;
    }

    /**
     * Adds a dvd to the database or inventory.
     *
     * @param dvd The Dvd item to add to inventory.
     * @return true if the record could be added, false if it already exists.
     * @throws IOException Indicates there is a problem accessing the database.
     */
    public boolean addDvd(DVD dvd) throws IOException {
        return persistDvd(dvd, true);
    }

    /**
     * Removes Dvds from inventory using the unique upc.
     *
     * @param upc The upc or key of the Dvd to be removed.
     * @return true if the upc was found and the Dvd was removed.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public boolean removeDvd(String upc) throws IOException {
        log.entering("DvdFileAccess", "removeDvd", upc);
        boolean deleted = false;

        recordNumbersLock.writeLock().lock();
        try {
            Long offset = recordNumbers.get(upc);
            if (offset == null) {
                log.fine("Attempt to delete " + upc + " failed");
            } else {
                synchronized (database) {
                    database.seek(offset);
                    database.write("DELETED     ".getBytes());
                }

                recordNumbers.remove(upc);
                deleted = true;
            }
        } finally {
            recordNumbersLock.writeLock().unlock();
        }

        log.exiting("DvdFileAccess", "removeDvd", deleted);
        return deleted;
    }

    /**
     * Changes existing information of a Dvd item.
     * Modifications can occur on any of the attributes of Dvd except UPC.
     * The UPC is used to identify the Dvd to be modified.
     *
     * @param dvd The item in question
     * @return Returns true if the Dvd was found and modified.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    public boolean modifyDvd(DVD dvd) throws IOException {
        return persistDvd(dvd, false);
    }

    /**
     * A properly formatted <code>String</code> expressions returns all matching
     * Dvd items. The <code>String</code> must be formatted as a regular
     * expression.
     *
     * @param query formatted regular expression used as the search criteria.
     * @return The list of Dvds that match the query. Can be an empty
     * Collection.
     * @throws IOException Indicates there is a problem accessing the data.
     * @throws PatternSyntaxException Indicates there is a syntax problen in
     * the regular expression.
     */
    public Collection<DVD> find(String query)
            throws IOException, PatternSyntaxException {
        log.entering("DvdFileAccess", "find", query);
        Collection<DVD> returnValue = new ArrayList<DVD>();
        Pattern p = Pattern.compile(query);

        for (DVD dvd : getDvds()) {
            Matcher m = p.matcher(dvd.toString());
            if (m.find()) {
                returnValue.add(dvd);
            }
        }

        log.exiting("DvdFileAccess", "find", returnValue);
        return returnValue;
    }

    /**
     * This method persists a Dvd object to the database. If the Dvd does not
     * currently exist in the database( as determined by it's unique upc code).
     * then the Dvd will be created.
     *
     * @param dvd The Dvd to store
     * @param create true for creation, false for modification
     * @return Indicates success or failure
     * @throws IOException Indicates there is a problem accessing the data file
     */
    private boolean persistDvd(DVD dvd, boolean create) throws IOException {
        log.entering("DvdFileAccess", "persistDvd", dvd);

        // Perform as many operations as we can outside of the synchronized
        // block to improve concurrent operations.

        Long offset = 0L;
        recordNumbersLock.readLock().lock();
        try {
            offset = recordNumbers.get(dvd.getUPC());
        } finally {
            recordNumbersLock.readLock().unlock();
        }

        if (create == true && offset == null) {
            recordNumbersLock.writeLock().lock();
            try {
                offset = database.length();
                recordNumbers.put(dvd.getUPC(), offset);
            } finally {
                recordNumbersLock.writeLock().unlock();
            }
            log.info("creating record " + dvd.getUPC());
        } else if (create == false && offset != null) {
            log.info("updating existing record " + dvd.getUPC());
        } else {
            return false;
        }

        final StringBuilder out = new StringBuilder(emptyRecordString);

        /** assists in converting Strings to a byte[] */
        class RecordFieldWriter {
            /** current position in byte[] */
            private int currentPosition = 0;
            /**
             * converts a String of specified length to byte[]
             *
             * @param data the String to be converted into part of the byte[].
             * @param length the maximum size of the String
             */
            void write(String data, int length) {
                out.replace(currentPosition,
                            currentPosition + data.length(),
                            data);
                currentPosition += length;
            }
        }
        RecordFieldWriter writeRecord = new RecordFieldWriter();

        writeRecord.write(dvd.getUPC(), DVD.UPC_LENGTH);
        writeRecord.write(dvd.getName(), DVD.NAME_LENGTH);
        writeRecord.write(dvd.getComposer(), DVD.COMPOSER_LENGTH);
        writeRecord.write(dvd.getDirector(), DVD.DIRECTOR_LENGTH);
        writeRecord.write(dvd.getLeadActor(), DVD.LEAD_ACTOR_LENGTH);
        writeRecord.write(dvd.getSupportingActor(),
                          DVD.SUPPORTING_ACTOR_LENGTH);
        writeRecord.write(dvd.getYear(), DVD.YEAR_LENGTH);
        writeRecord.write("" + dvd.getCopy(), DVD.COPIES_LENGTH);

        // now that we have everything ready to go, we can go into our
        // synchronized block & perform our operations as quickly as possible
        // ensuring that we block other users for as little time as possible.

        synchronized (database) {
            database.seek(offset);
            database.write(out.toString().getBytes());
        }

        log.exiting("DvdFileAccess", "persistDvd", true);
        return true;
    }

    /**
     * Locks the database so that no other client can modify it. This might be
     * used if we were doing emergency maintenance, or if we were shutting down
     * the database.
     *
     * @param databaseLocked true if the database should be locked.
     */
    void setDatabaseLocked(boolean databaseLocked) {
        log.entering("DvdFileAccess", "setDatabaseLocked", databaseLocked);
        // Implementation details: any modification (create/update/delete) to
        // the database locks <code>recordNumbersLock</code> first, since the
        // modification might change the recordNumbers collection. Therefore
        // all we need do is lock this mutual exclusion lock, and no other
        // thread can modify it.
        if (databaseLocked) {
            recordNumbersLock.writeLock().lock();
        } else {
            recordNumbersLock.writeLock().unlock();
        }
        log.exiting("DvdFileAccess", "setDatabaseLocked");
    }
}
