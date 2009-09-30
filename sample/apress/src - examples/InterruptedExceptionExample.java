import java.util.logging.*;

public class InterruptedExceptionExample extends Thread {
    static Logger log = Logger.getAnonymousLogger();

    public static void main(String[] args) throws InterruptedException {
        InterruptedExceptionExample iee = new InterruptedExceptionExample();
        iee.start();

        while (iee.isAlive()) {
            log.info("main: waiting 5 seconds for other thread to finish");
            iee.join(5000);

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

    public void getLock() throws LockAttemptFailedException {
        // try to get some resource that we will presumably never get.
        for (;;) {
            try {
                synchronized (InterruptedExceptionExample.class) {
                    log.info(getName() + ": waiting for some resource.");
                    InterruptedExceptionExample.class.wait();
                }
            } catch (InterruptedException ie) {
                throw new UserInterruptionException("InterruptedException in getLock",ie);
            }
        }
    }

    public class LockAttemptFailedException extends Exception {
        public LockAttemptFailedException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    public class UserInterruptionException extends RuntimeException {
        public UserInterruptionException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
