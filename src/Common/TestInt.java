package Common;

import java.io.ObjectInputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by azaz on 08.02.17.
 */
public interface TestInt extends Remote {

    Object writeMessage(String message) throws RemoteException;
}
