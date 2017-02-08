package Server;

import Common.Game;
import Common.InGameLogicInterface;
import Common.IntIntPair;
import Common.Pair;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Created by azaz on 08.02.17.
 */
public class InGameLogic implements InGameLogicInterface {
    Game g;
    int WIDTH=10;
    int HEIGHT=10;
    int[][] field = new int[HEIGHT][WIDTH];
    private boolean isBlack=true;

    public InGameLogic(Game g){
        this.g=g;
        defaultFill();
    }

    /**
     * client-server method
     * @return
     */
    public int[][] getState(){
        return field;
    }

    /**
     * server method
     */
    private void defaultFill() {
        for (int i = 0; i < field.length; i++) {
            Arrays.fill(field[i], 0);
        }
        field[HEIGHT/2][WIDTH/2] = -1;
        field[HEIGHT/2 -1][WIDTH/2 -1] = -1;
        field[HEIGHT/2][WIDTH/2 -1] = 1;
        field[HEIGHT/2 -1][WIDTH/2] = 1;
    }

    /**
     * client run it on server
     *
     * check turn, set marker and modify field
     *
     * @param x
     * @param y
     * @return is success
     */
    public boolean playerTurn(int x, int y) {
        if (checkTurn(x, y, isBlack)) {
            modifyField(x, y, isBlack);
            field[y][x] = isBlack ? -1 : 1;
            this.isBlack = !isBlack;
            return true;
        } else return false;
    }

    /**
     * only on server
     *
     * reverse color of horizontal,vertical and both diagonal lines if needed
     *
     * @param x
     * @param y
     * @param isBlack
     */
    private void modifyField(int x, int y, boolean isBlack) {
        walkHorizontal(x, y, isBlack, true);
        walkVertical(x, y, isBlack, true);
        walkDiagonal(x, y, isBlack, true);
    }

    /**
     * run on server
     *
     * @param x
     * @param y
     * @param isBlack
     * @return posibility of turn
     */
    private boolean checkTurn(int x, int y, boolean isBlack) {
        if (y < 0 || y >= HEIGHT || x < 0 || x >= WIDTH) {
            return false;
        }
        if (field[y][x] != 0) return false;
        return walkHorizontal(x, y, isBlack, false) ||
                walkVertical(x, y, isBlack, false) ||
                walkDiagonal(x, y, isBlack, false);
    }

    /**
     * find  continuous line of field another color limited by first color and reverse it if needed
     *
     * @param x0      startX
     * @param y0      startY
     * @param isBlack
     * @param dx      stepX
     * @param dy      stepY
     * @param reverse modifyField
     * @return existing of a line
     */
    private boolean walkAndFind(int x0, int y0, boolean isBlack, int dx, int dy, boolean reverse) {
        int color = isBlack ? -1 : 1;
        int anotherColor = -color;
        int stepNumber = 0;
        boolean result = false;
        int y = y0;
        int x = x0;
        while (true) {
            y += dy;
            x += dx;

            if (y < 0 || y >= HEIGHT || x < 0 || x >= WIDTH) {
                result = false;
                break;
            }

            if (stepNumber == 0 && (field[y][x] == 0 || field[y][x] == color))
                return false;//neighbor field empty or equals color
            if (field[y][x] == color) {// find field with equals color
                result = true;
                break;
            }
            if (field[y][x] == 0) {
                result = false;
                break;
            }
            if (field[y][x] == anotherColor) {
                stepNumber++;
            }
        }


        //TODO refactor and rewrite copy of for-for
        y = y0;
        x = x0;
        if (result && reverse) {
            while (true) {
                y += dy;
                x += dx;

                if (y < 0 || y >= HEIGHT || x < 0 || x >= WIDTH) {
                    break;
                }

                if (field[y][x] == color) {// find field with equals color
                    break;
                }
                if (field[y][x] == anotherColor) {
                    field[y][x] *= -1;
                }
            }
        }
        return result;
    }

    private boolean walkDiagonal(int x, int y, boolean isBlack, boolean reverse) {
        boolean b = false;
        b = b | walkAndFind(x, y, isBlack, 1, 1, reverse);
        b = b | walkAndFind(x, y, isBlack, 1, -1, reverse);
        b = b | walkAndFind(x, y, isBlack, -1, 1, reverse);
        b = b | walkAndFind(x, y, isBlack, -1, -1, reverse);
        return b;

    }

    private boolean walkVertical(int x, int y, boolean isBlack, boolean reverse) {
        boolean b = false;
        b = b | walkAndFind(x, y, isBlack, 0, 1, reverse);
        b = b | walkAndFind(x, y, isBlack, 0, -1, reverse);
        return b;
    }

    private boolean walkHorizontal(int x, int y, boolean isBlack, boolean reverse) {
        boolean b = false;
        b = b | walkAndFind(x, y, isBlack, 1, 0, reverse);
        b = b | walkAndFind(x, y, isBlack, -1, 0, reverse);
        return b;
    }


    /**
     * @return +1 if win white, -1 if win black, 0 if draw(??)
     */
    public Integer checkWin() {
        IntIntPair score = calculateScore();
        if (score.getVal1() > score.getVal2()) return 1;
        if (score.getVal1() < score.getVal2()) return -1;
        return 0;
    }

    public IntIntPair calculateScore() {
        int scoreBlack = 0;
        int scoreWhite = 0;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {

                if (field[y][x] == 1) {
                    scoreWhite += 1;
                }
                if (field[y][x] == -1) {
                    scoreBlack += 1;
                }
            }
        }
        return new IntIntPair(scoreWhite, scoreBlack);
    }

    public Boolean isEnded() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (checkTurn(x, y, isBlack)) return false;
            }
        }
        return true;
    }

    @Override
    public int getGameStatus() throws RemoteException {
        return g.getStatus();
    }

    @Override
    public int getCurrentPlayer() throws RemoteException {
        return isBlack?1:2;
    }


}
