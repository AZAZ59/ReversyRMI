package Common;

/**
 * Created by azaz on 08.02.17.
 */
public class Util {
    public static void printField(int[][] field) {
        for (int i = 0; i < 5; i++) {
            System.out.println(" ");
        }
        System.out.print("  ");
        for (int i = 0; i < field[0].length; i++) {
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
}
