import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

interface ServerReference extends Remote {
    public void serverThreadNumber(String id) throws RemoteException;
}

class Server extends UnicastRemoteObject implements ServerReference {
    public Server() throws RemoteException {
        // do nothing constructor
    }

    public void serverThreadNumber(String id) throws RemoteException {
        System.out.println(id + " running in thread "
                           + Thread.currentThread().hashCode());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            System.exit(1);
        }
    }
}

public class RmiProblem extends Thread {
    public RmiProblem(String id) {
        super(id);
    }

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);
        Naming.rebind("rmi://localhost:1099/RmiProblem", new Server());

        Thread a = new RmiProblem("A");
        a.start();

        Thread.sleep(1000);

        Thread b = new RmiProblem("B");
        b.start();

        a.join();
        b.join();

        System.exit(0);
    }

    public void run() {
        try {
            ServerReference remoteCode =
                    (ServerReference) Naming.lookup("RmiProblem");

            for (int i = 0; i < 5; i++) {
                remoteCode.serverThreadNumber(getName());
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
