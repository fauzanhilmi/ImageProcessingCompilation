package com.pengcit.fauzanhilmi.imageprocessingcompilation.Threshold;

import java.util.ArrayList;

/**
 *
 * @author khaidzir
 */
public class PixelTracer {

    static final int BACKGROUND_COLOR = 0xFFFFFFFF,
            LINE_COLOR = 0xFF000000;

    static final int UP = 0, UP_RIGHT = 1, RIGHT = 2, DOWN_RIGHT = 3, DOWN = 4,
            DOWN_LEFT = 5, LEFT = 6, UP_LEFT = 7;

    public int [][] image;
    private boolean [][] edges;
    int currDir;
    Position currPos, startPos;
    ArrayList<ArrayList<Integer>> chainCode;


    public PixelTracer() {
        chainCode = new ArrayList<>();
        currPos = new Position(0,0);
        startPos = new Position(0,0);
    }

    public void setImage(int [][] image) {
        this.image = image;
        edges = new boolean[image.length][image[0].length];
        for (int i=0; i<edges.length; i++) {
            for(int j=0; j<edges[0].length; j++) {
                edges[i][j] = false;
            }
        }
    }

    int getLeftDir(Position result) {
        result.copy(currPos);
        switch(currDir) {
            case UP         : result.col--;
                break;

            case UP_RIGHT   : result.row--;
                result.col--;
                break;

            case RIGHT      : result.row--;
                break;

            case DOWN_RIGHT : result.row--;
                result.col++;
                break;

            case DOWN       : result.col++;
                break;

            case DOWN_LEFT  : result.row++;
                result.col++;
                break;

            case LEFT       : result.row++;
                break;

            case UP_LEFT    : result.row++;
                result.col--;
                break;
        }
        return (currDir+6)%8;
    }

    void nextPos(Position leftPos, int leftDir) {
//        System.out.println("Posisi awal : " + currPos.row + ", " + currPos.col);
        for (;;) {
//            System.out.println("Left Pos : " + leftPos.row + ", " + leftPos.col);
            // cek apakah valid
            if (leftPos.row < image.length && leftPos.col < image[0].length) {
                if (image[leftPos.row][leftPos.col] != BACKGROUND_COLOR)
                    break;
            }

            // pindah posisi
            if (leftPos.row < currPos.row) {  // di bagian atas
                if (leftPos.col <= currPos.col)      // atas kiri atau atas
                    leftPos.col++;
                else                            // atas kanan
                    leftPos.row++;
            } else if (leftPos.row == currPos.row) { // kiri atau kanan
                if (leftPos.col < currPos.col)  // kiri
                    leftPos.row--;
                else                        // kanan
                    leftPos.row++;
            } else {        // di bagian bawah
                if (leftPos.col >= currPos.col)     // bawah atau kanan bawah
                    leftPos.col--;
                else
                    leftPos.row--;
            }
            leftDir++;
            if (leftDir == 8)
                leftDir = 0;
        }
        currPos.copy(leftPos);
        currDir = leftDir;
//        System.out.println(leftDir + " : " + stringDir(leftDir) + "\n");
    }

    public void scan() {
        System.out.println("Scanning..");
        reset();
//        System.out.println("Edges size : " + edges.length + " x " + edges[0].length);
        for (int i=0; (i<image.length); i++) {
            for (int j=0; (j<image[i].length); j++) {
                if (image[i][j] == LINE_COLOR && !edges[i][j]) {
                    if (j > 0) {
                        if (image[i][j-1] == BACKGROUND_COLOR) {
                            currPos.setPos(i, j);
                            trace();
                        } else {
                            while(j<image[i].length && !edges[i][j])j++;
                        }
                    }
                }
            }
        }
        System.out.println("Selesai!");
    }

    void trace() {
        ArrayList<Integer> codes = new ArrayList<>();
        Position temp = new Position(currPos.row, currPos.col);
        int dir;
        edges[currPos.row][currPos.col] = true;
        startPos.copy(currPos);
//        System.out.println("Tracing : starting point " + currPos.row + ", " + currPos.col);
        do {
            dir = getLeftDir(temp);
            nextPos(temp, dir);
            edges[currPos.row][currPos.col] = true;
            codes.add(currDir);
        } while(!currPos.isEqual(startPos));
        chainCode.add(codes);
//        for (int i=0; i<edges.length; i++) {
//            for (int j=0; j<edges[i].length; j++) {
//                System.out.print(edges[i][j]?"1 ":"  ");
//            }
//            System.out.println();
//        }
//        System.out.println("Tracing finished!\n");
    }

    public void printChainCode() {
        int j=1,i;
        for (ArrayList<Integer> arr : chainCode) {
            System.out.print(j + ". ");
            for (i=0; i<arr.size()-1; i++) {
                System.out.print(stringDir(arr.get(i))+", ");
            }
            System.out.println(stringDir(arr.get(i)));
            j++;
        }
    }

    String stringDir(int direction) {
        switch(direction) {
            case UP         : return "Atas";

            case UP_RIGHT   : return "Kanan-Atas";

            case RIGHT      : return "Kanan";

            case DOWN_RIGHT : return "Kanan-bawah";

            case DOWN       : return "Bawah";

            case DOWN_LEFT  : return "Kiri-bawah";

            case LEFT       : return "Kiri";

            case UP_LEFT    : return "Kiri-atas";

            default         : return "";
        }
    }

    public void reset() {
        currDir = RIGHT;
        edges = new boolean[image.length][image[0].length];
        for (int i=0; i<edges.length; i++) {
            for(int j=0; j<edges[0].length; j++) {
                edges[i][j] = false;
            }
        }
        currPos = new Position(0,0);
        chainCode = new ArrayList<>();
        currPos = new Position(0,0);
        startPos = new Position(0,0);
    }

    public class Position {
        public int row, col;
        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
        public boolean isEqual(Position other) {
            return (other.row == row && other.col == col);
        }
        public void copy(Position other) {
            row = other.row;
            col = other.col;
        }
        public void setPos(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

}


