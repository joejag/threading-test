import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class HandOverHandLockingExample extends Thread {
    private static int instances = 0;
    private int id = instances++;

    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            new HandOverHandLockingExample().start();
        }
    }

    public void run() {
        try {
            reserveDvd("" + (id % 2), new DvdDatabase(id));
            System.out.println(id + " aint doin nothin");
            Thread.sleep(1000);
            releaseDvd("" + (id % 2));
        } catch (InterruptedException ignored) {
        }
    }

    private static Map<String, LockInformation> reservations
            = new HashMap<String, LockInformation>();

    private static Lock masterLock = new ReentrantLock();

    public boolean reserveDvd(String upc, DvdDatabase renter)
            throws InterruptedException {
        LockInformation dvdLock = null;
        masterLock.lock();
        try {
            dvdLock = reservations.get(upc);
            if (dvdLock == null) {
                dvdLock = new LockInformation();
                reservations.put(upc, dvdLock);
            }
            dvdLock.lock();
        } finally {
            masterLock.unlock();
        }

        try {
            long endTimeMSec = System.currentTimeMillis() + 5000;
            Condition dvdCondition = dvdLock.getCondition();
            while (dvdLock.isReserved()) {
                long timeLeftMSec = endTimeMSec - System.currentTimeMillis();
                if (!dvdCondition.await(timeLeftMSec, TimeUnit.MILLISECONDS)) {
                    return false;
                }
            }
            dvdLock.setReserver(renter);
        } finally {
            dvdLock.unlock();
        }
        return true;
    }

    class LockInformation extends ReentrantLock {
        private DvdDatabase reserver = null;
        private Condition notifier = newCondition();

        void setReserver(DvdDatabase reserver) {
            this.reserver = reserver;
        }

        void releaseReserver() {
            this.reserver = null;
        }

        Condition getCondition() {
            return notifier;
        }

        boolean isReserved() {
            return reserver != null;
        }
    }

    void releaseDvd(String upc) {
        LockInformation dvdLock = null;
        masterLock.lock();
        try {
            dvdLock = reservations.get(upc);
            dvdLock.lock();
        } finally {
            masterLock.unlock();
        }

        try {
            System.out.println(id + " releasing lock on " + upc);
            dvdLock.releaseReserver();
            dvdLock.getCondition().signal();
        } finally {
            dvdLock.unlock();
        }
    }

    class DvdDatabase {
        int id = 0;

        public DvdDatabase(int id) {
            this.id = id;
        }

        public String toString() {
            return "" + id;
        }
    }
}