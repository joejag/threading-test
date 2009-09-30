package sampleproject.db;

import java.util.*;
import java.util.logging.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * Handles <b>logically</b> reserving and releasing a named object.
 * <br />
 * Note that since this should only be used by the DvdDatabase class, neither
 * this class, nor any of it's methods have been declared public - they all
 * have default access.
 */
class ReservationsManager {
    /** Length of time we will wait for a lock. */
    private static final int TIMEOUT = 5 * 1000;
    /**
     * A structure that keeps track of the locked Dvd records. Note that we
     * have not made this static, as doing so would mean that only one set
     * of reservations could exist at any given time. We rely on the class
     * which uses this ReservationManager to ensure that only one instance
     * exists for any reservations.
     */
    private static Map<String, DvdDatabase> reservations
            = new HashMap<String, DvdDatabase>();

    /**
     * A mutual exclusion lock limiting access to the <code>reserverations
     * </code> collection.
     */
    private static Lock lock = new ReentrantLock();

    /**
     * A <code>Condition</code> that can be used to signal threads waiting for
     * the <code>lock</code> that it is now available.
     */
    private static Condition lockReleased  = lock.newCondition();

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>sampleproject.db</code>.
     */
    private Logger log = Logger.getLogger("sampleproject.db"); // Log output

    /**
     * Lock the requested Dvd. This method blocks until the lock succeeds,
     * or for a maximum of 5 seconds, whichever comes first.
     *
     * @param upc The upc of the Dvd to reserve
     * @param renter The instance of DvdDatabase reserving this Dvd.
     * @return true if renter was able to reserve DVD
     * @throws InterruptedException Indicates the thread is interrupted.
     */
    boolean reserveDvd(String upc, DvdDatabase renter)
            throws InterruptedException {
        log.entering("ReservationsManager", "reserveDvd",
                     new Object[]{upc, renter});

        lock.lock();
        try {
            long endTimeMSec = System.currentTimeMillis() + TIMEOUT;
            while (reservations.containsKey(upc)) {
                long timeLeftMSec = endTimeMSec - System.currentTimeMillis();
                if (!lockReleased.await(timeLeftMSec, TimeUnit.MILLISECONDS)) {
                    log.fine(renter + " giving up after 5 seconds: " + upc);
                    return false;
                }
            }
            reservations.put(upc, renter);
            log.fine(renter + " got Lock for " + upc);
            log.fine("Locked record count = " + reservations.size());
            log.exiting("ReservationsManager", "reserveDvd", true);
            return true;
        } finally {
            // ensure lock is always released, even if an Exception is thrown
            lock.unlock();
        }
    }

    /**
     * Unlock the requested record. Ignored if the caller does not have
     * a current lock on the requested record.
     *
     * @param upc The upc of the Dvd to release
     * @param renter The instance of DvdDatabase releasing this Dvd.
     */
    void releaseDvd(String upc, DvdDatabase renter) {
        log.entering("ReservationsManager", "releaseDvd",
                     new Object[]{upc, renter});
        lock.lock();
        if (reservations.get(upc) == renter) {
            reservations.remove(upc);
            log.fine(renter + " released lock for " + upc);
            lockReleased.signal();
        } else {
            log.fine(renter + " can't release " + upc + " lock (not owner)");
        }
        lock.unlock();
        log.exiting("ReservationsManager", "releaseDvd");
    }
}
