package Common;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by azaz on 08.02.17.
 */
public interface GameManagerInterfase extends Remote {

    List<Game> getGameList()throws RemoteException;
    void joinToGame(int gameID,String playerName) throws RemoteException, AlreadyBoundException;
    int createGame(String playerName) throws RemoteException, AlreadyBoundException;

}
