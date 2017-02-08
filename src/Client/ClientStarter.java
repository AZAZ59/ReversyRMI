package Client;

import Common.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Created by azaz on 08.02.17.
 */
public class ClientStarter {
    public static void main(String... args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        GameManagerInterfase service = (GameManagerInterfase) registry.lookup("server/manager");

        int gameID = -100;

        Scanner s = new Scanner(System.in);
        System.out.print("Player name: ");
        String name = s.nextLine();
        int playerNumber = 0;
        while (gameID < 0) {
            System.out.println("1: create new game\n 2:get list of game\n 3:join to the game\n 0:exit");
            switch (s.nextInt()) {
                case 1:
                    gameID = service.createGame(name);
                    playerNumber = 1;
                    break;
                case 2:
                    for (Game g :
                            service.getGameList()) {
                        System.out.println(g.getPlayer1() + " " + g.getId());
                    }
                    break;
                case 3:
                    System.out.print("Game Id: ");
                    gameID = s.nextInt();
                    service.joinToGame(gameID, name);
                    playerNumber = 2;
                    break;
                case 0:
                    System.exit(0);
            }
        }

        InGameLogicInterface gameLogic = (InGameLogicInterface) registry.lookup("server/game/" + gameID);

        while (true) {
            int stat = gameLogic.getGameStatus();
//            System.out.println(stat);
            Thread.sleep(100);
            if (stat == 2) {
                break;
            }
        }

        //game loop
        int[][] field ;
        boolean isBlack = playerNumber == 1;
        field= gameLogic.getState();
        Util.printField(field);

        for (; ; ) {//gameLoop
            while (gameLogic.getCurrentPlayer() != playerNumber) {
//                System.out.println(gameLogic.getCurrentPlayer());
                Thread.sleep(100);//wait another player turn
            }
            field= gameLogic.getState();
            IntIntPair score = gameLogic.calculateScore();

            Util.printField(field);
            System.out.println("white: " + score.getVal1() + " black:" + score.getVal2());

            if (gameLogic.isEnded()) {
                switch (gameLogic.checkWin()) {
                    case 1:
                        System.out.println("White win");
                        break;
                    case -1:
                        System.out.println("Black win");
                        break;
                    case 0:
                        System.out.println("Draw");
                        break;
                }
                return;
            }

            System.out.println("You played " + (isBlack ? "black" : "white"));

            try {
                System.out.print("X: ");
                int x = s.nextInt() - 1;

                System.out.print("Y: ");
                int y = s.nextInt() - 1;

                if (!gameLogic.playerTurn(x, y)) {
                    continue;
                }
                field= gameLogic.getState();
                Util.printField(field);
            } catch (Exception e) {
                s = new Scanner(System.in);//supress endless exception...
            }
        }
    }
}
