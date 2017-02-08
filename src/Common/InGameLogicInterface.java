package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by azaz on 08.02.17.
 */
public interface InGameLogicInterface extends Remote {
    int[][] getState() throws RemoteException;
    boolean playerTurn(int x, int y) throws RemoteException;
    public Integer checkWin() throws RemoteException;
    public IntIntPair calculateScore() throws RemoteException;
    public Boolean isEnded() throws RemoteException;
    public int getGameStatus()  throws RemoteException;
    public int getCurrentPlayer() throws RemoteException;
}
