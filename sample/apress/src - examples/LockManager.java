import java.util.concurrent.locks.*;
import java.util.*;


public class LockManager {
private ReentrantLock recordsLock;

private Condition recordsCondition;

private Map<Integer, Long> lockMap;

	public static void main(String[] args) {
		LockManager lm = new LockManager();
		lm.doSomething();
	}

	void doSomething() {
		recordsLock.unlock();
	}

    LockManager() {

        recordsLock = new ReentrantLock();

        recordsCondition = recordsLock.newCondition();

        lockMap = new HashMap<Integer, Long>(30);

    }

}
