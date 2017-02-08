package Server;

import Common.Game;
import Common.GameManagerInterfase;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by azaz on 08.02.17.
 */
public class GameManagerImp implements GameManagerInterfase {
    ArrayList<Game> list = new ArrayList<>();
    Registry registry;
    int lastID = 1;

    public GameManagerImp(Registry registry) {
        this.registry = registry;
    }

    @Override
    public List<Game> getGameList() {
        List<Game> ll = list.stream().filter(game -> game.getStatus() == 1).collect(Collectors.toList());
        System.out.println(ll);
        return ll;
    }

    @Override
    public void joinToGame(int gameID, String playerName) {
        System.out.println(gameID + " " + playerName);
        Game g = list.stream().filter(game -> game.getId() == gameID).findFirst().get();
        g.setPlayer2(playerName);
        g.setStatus(2);
//        g.start();
    }

    @Override
    public int createGame(String playerName) throws RemoteException, AlreadyBoundException {
        System.out.println(playerName);
        Game g = new Game(playerName, null);
        g.setId(lastID++);
        list.add(g);
        Remote stub = UnicastRemoteObject.exportObject(new InGameLogic(g), 0);
        registry.bind("server/game/" + g.getId(), stub);
        return g.getId();
    }


}
