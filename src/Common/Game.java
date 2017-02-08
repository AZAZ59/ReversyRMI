package Common;

import java.io.Serializable;

/**
 * Created by azaz on 08.02.17.
 */
public class Game implements Serializable {
    private String player1,player2;
    private int status=0;
    private int id;



    public void start(){
        //TODO ????
    }

    @Override
    public String toString() {
        return "Game{" +
                "player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", id=" + id +
                '}';
    }

    public Game(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        status=1;
    }

    /**
     * 0 -- empty
     * 1 -- wait another player
     * 2 -- in game
     * 3 -- game is end
     */
    public int getId() {
        return id;
    }
    /**
     * 0 -- empty
     * 1 -- wait another player
     * 2 -- in game
     * 3 -- game is end
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
