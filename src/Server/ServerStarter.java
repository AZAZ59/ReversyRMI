package Server;

import Common.GameManagerInterfase;
import Common.TestInt;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

/**
 * Created by azaz on 08.02.17.
 */
public class ServerStarter {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {
        Registry registry=LocateRegistry.createRegistry(1234);

        GameManagerInterfase service=new GameManagerImp(registry);
        Remote stub = UnicastRemoteObject.exportObject(service, 0);

        System.out.print("Binding service...");
        registry.bind("server/manager", stub);
        System.out.println(" OK");

        while (true) {
            Thread.sleep(Integer.MAX_VALUE);
        }

    }
}
