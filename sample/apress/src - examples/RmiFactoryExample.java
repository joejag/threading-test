import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

interface Factory extends Remote {
    public Worker getWorker() throws RemoteException;
}

class FactoryImpl extends UnicastRemoteObject implements Factory {
    public FactoryImpl() throws RemoteException {
        // do nothing constructor
    }

    public Worker getWorker() throws RemoteException {
        return new WorkerImpl();
    }
}

interface Worker extends Remote {
    public void doSomething(String id) throws RemoteException;
}

class WorkerImpl extends UnicastRemoteObject implements Worker {
    public WorkerImpl() throws RemoteException {
        // do nothing constructor
    }

    public void doSomething(String id) throws RemoteException {
        System.out.println(id + " doing something in " + this.hashCode());
    }
}

public class RmiFactoryExample extends Thread {
    public RmiFactoryExample(String id) {
        super(id);
    }

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);
        Naming.rebind("RmiFactoryExample", new FactoryImpl());

        Thread a = new RmiFactoryExample("A");
        a.start();

        Thread.sleep(1000);

        Thread b = new RmiFactoryExample("B");
        b.start();

        a.join();
        b.join();

        System.exit(0);
    }

    public void run() {
        try {
            Factory factory = (Factory) Naming.lookup("RmiFactoryExample");
            Worker worker = factory.getWorker();

            for (int i = 0; i < 5; i++) {
                worker.doSomething(getName());
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
