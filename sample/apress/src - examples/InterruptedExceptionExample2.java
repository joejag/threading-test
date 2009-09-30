import java.util.logging.*;

public class InterruptedExceptionExample2 extends Thread {
    static Logger log = Logger.getAnonymousLogger();

    public static void main(String[] args) {
        InterruptedExceptionExample2 iee
                = new InterruptedExceptionExample2();
        iee.start();

        while (iee.isAlive()) {
            try {
                // try waiting for the other thread to finish
                log.info("main: waiting for worker thread");
                iee.join(5000);
            } catch (InterruptedException ie) {
                // some other thread interrupted the main thread?
                log.log(Level.SEVERE, "the main thread has been interrupted", ie);
            }

            if (iee.isAlive()) {
                log.info("main: interrupting other thread.");
                iee.interrupt();
            }
        }
        log.info("main: finished");
    }

    public void run() {
        try {
            getLock();
        } catch (LockAttemptFailedException dle) {
            log.log(Level.WARNING, "Lock attempt failed", dle);
        }
    }

    public void getLock() throws LockAttemptFailedException, UserInterruptionException {
        String name = getName();

        // try to get some resource that we will presumably never get.
        for (;;) {
            try {
                synchronized (InterruptedExceptionExample2.class) {
                    log.info(name + ": waiting for some resource.");
                    InterruptedExceptionExample2.class.wait();
                }
            } catch (InterruptedException ie) {
                throw new RuntimeException(
                        "InterruptedException in getLock",
                        ie);
            }
        }
    }

    public class LockAttemptFailedException extends Exception {
        public LockAttemptFailedException() {
            super();
        }

        public LockAttemptFailedException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    public class UserInterruptionException extends RuntimeException {
        public UserInterruptionException() {
            super();
        }

        public UserInterruptionException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
