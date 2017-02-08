package Tratata;

import Common.Pair;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by azaz on 08.02.17.
 */
public class Starter {
    /**
     * ____1 2 3 4 5 6 7 8<br>
     * 1(0) _ _ _ _ _ _ _ _<br>
     * 2(1) _ _ _ _ _ _ _ _<br>
     * 3(2) _ _ _ _ _ _ _ _<br>
     * 4(3) _ _ _ * 0 _ _ _<br>
     * 5(4) _ _ _ 0 * _ _ _<br>
     * 6(5) _ _ _ _ _ _ _ _<br>
     * 7(6) _ _ _ _ _ _ _ _<br>
     * 8(7) _ _ _ _ _ _ _ _<br>
     * 0--empty,1--white,-1 -- black
     */
    public static final int WIDTH=8;
    public static final int HEIGHT=8;
    static int[][] field = new int[HEIGHT][WIDTH];
    static int[][] def = {{0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, -1, 1, 0, 0, 0}, {0, 0, 0, 1, -1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}};
    static int[][] chwin={{1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {-1, 1, 1, 0, 1, 1, 1, -1}};
    static boolean isBlack = true;

    public static void main(String[] args) {
//        field = chwin.clone();
        defaultFill();

        Scanner s = new Scanner(System.in);

        for (; ; ) {//gameLoop
            Pair<Integer, Integer> score = calculateScore();

            printField(field);
            System.out.println("white: " + score.getVal1() + " black:" + score.getVal2());

            if (isEnded(isBlack)) {
                switch (checkWin()) {
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

                if (!playerTurn(x, y, isBlack)) {
                    continue;
                }
            }catch (Exception e){
//                e.printStackTrace();
                s=new Scanner(System.in);//supress endless exception...
            }
        }

    }

    /**
     * client-server method
     * @return
     */
    private int[][] getState(){

        return field;
    }

    /**
     * server method
     */
    private static void defaultFill() {
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
     * @param isBlack color of player
     * @return is success
     */
    private static boolean playerTurn(int x, int y, boolean isBlack) {
        if (checkTurn(x, y, isBlack)) {
            modifyField(x, y, isBlack);
            field[y][x] = isBlack ? -1 : 1;
            Starter.isBlack = !isBlack;
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
    private static void modifyField(int x, int y, boolean isBlack) {
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
    private static boolean checkTurn(int x, int y, boolean isBlack) {
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
    private static boolean walkAndFind(int x0, int y0, boolean isBlack, int dx, int dy, boolean reverse) {
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

    private static boolean walkDiagonal(int x, int y, boolean isBlack, boolean reverse) {
        boolean b = false;
        b = b | walkAndFind(x, y, isBlack, 1, 1, reverse);
        b = b | walkAndFind(x, y, isBlack, 1, -1, reverse);
        b = b | walkAndFind(x, y, isBlack, -1, 1, reverse);
        b = b | walkAndFind(x, y, isBlack, -1, -1, reverse);
        return b;

    }

    private static boolean walkVertical(int x, int y, boolean isBlack, boolean reverse) {
        boolean b = false;
        b = b | walkAndFind(x, y, isBlack, 0, 1, reverse);
        b = b | walkAndFind(x, y, isBlack, 0, -1, reverse);
        return b;
    }

    private static boolean walkHorizontal(int x, int y, boolean isBlack, boolean reverse) {
        boolean b = false;
        b = b | walkAndFind(x, y, isBlack, 1, 0, reverse);
        b = b | walkAndFind(x, y, isBlack, -1, 0, reverse);
        return b;
    }


    public static void printField(int[][] field) {
        for (int i = 0; i < 5; i++) {
            System.out.println(" ");
        }
        System.out.print("  ");
        for (int i = 0; i < WIDTH; i++) {
            System.out.print((i + 1) + " ");
        }
        System.out.println();
        for (int i = 0; i < field.length; i++) {
            StringBuilder sb = new StringBuilder("");
            sb.append(i + 1 + " ");
            for (int j = 0; j < field[i].length; j++) {
                switch (field[i][j]) {
                    case 0:
                        sb.append("_");
                        break;
                    case -1:
                        sb.append("*");
                        break;
                    case 1:
                        sb.append("o");
                        break;
                }
                sb.append(" ");
            }
            System.out.println(sb.toString());
        }
    }

    /**
     * @return +1 if win white, -1 if win black, 0 if draw(??)
     */
    public static Integer checkWin() {
        Pair<Integer, Integer> score = calculateScore();
        if (score.getVal1() > score.getVal2()) return 1;
        if (score.getVal1() < score.getVal2()) return -1;
        return 0;
    }

    public static Pair<Integer, Integer> calculateScore() {
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
        return new Pair<>(scoreWhite, scoreBlack);
    }

    public static Boolean isEnded(boolean isBlack) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (checkTurn(x, y, isBlack)) return false;
            }
        }
        return true;
    }

}
