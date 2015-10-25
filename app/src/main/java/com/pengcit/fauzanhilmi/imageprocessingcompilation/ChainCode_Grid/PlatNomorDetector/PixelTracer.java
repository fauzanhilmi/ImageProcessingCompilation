package com.pengcit.fauzanhilmi.imageprocessingcompilation.ChainCode_Grid.PlatNomorDetector;

/**
 * Created by Fauzan Hilmi on 23/09/2015.
 */
import java.util.ArrayList;

/**
 *
 * @author khaidzir
 */
public class PixelTracer {


    public static final int WHITE_COLOR = 0,
            BLACK_COLOR = 1;

    static final int UP = 0, UP_RIGHT = 1, RIGHT = 2, DOWN_RIGHT = 3, DOWN = 4,
            DOWN_LEFT = 5, LEFT = 6, UP_LEFT = 7;

    public int [][] image;
    private boolean [][] edges;
    int currDir;
    int bgColor;
    Position currPos, startPos;
    public ArrayList<Area> areas;
    ArrayList<int[][]> gridImages;

    public PixelTracer() {
        currPos = new Position(0,0);
        startPos = new Position(0,0);
        bgColor = WHITE_COLOR;
        areas = new ArrayList<>();
        gridImages = new ArrayList<>();
    }

    public void setImage(int [][] image) {
        this.image = new int[image.length][image[0].length];
        for (int i=0; i<image.length; i++) {
            for(int j=0; j<image[i].length; j++) {
                this.image[i][j] = image[i][j];
            }
        }
        edges = new boolean[image.length][image[0].length];
        for (int i=0; i<edges.length; i++) {
            for(int j=0; j<edges[0].length; j++) {
                edges[i][j] = false;
            }
        }
    }

    public void setBgColor(int color) {
        bgColor = color;
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

    // Mengubah currPos menjadi posisi selanjutnya
    void nextPos(Position leftPos, int leftDir) {
//        System.out.println("Posisi awal : " + currPos.row + ", " + currPos.col);
        for (;;) {
//            System.out.println("Left Pos : " + leftPos.row + ", " + leftPos.col);
            // cek apakah valid
            if (leftPos.row < image.length && leftPos.col < image[0].length) {
                if (image[leftPos.row][leftPos.col] != bgColor)
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

    // Scan keseluruhan image (hitam putih)
    public void scan() {
        reset();
        toBinaryImage();

        System.out.println("Scanning..");

        System.out.println("Tracing..");
        // Tracing images
        for (int i=0; (i<image.length); i++) {
            for (int j=0; (j<image[i].length); j++) {
                if (image[i][j] != bgColor && !edges[i][j]) {
                    if (j > 0) {
                        if (image[i][j-1] == bgColor) {
                            currPos.setPos(i, j);
                            trace();
                        } else {
                            while(j<image[i].length && !edges[i][j])j++;
                        }
                    }
                }
            }
        }

        System.out.println("Tracing completed!");

        // Grid images
        for (Area area : areas) {
            int [][] img = new int[area.rowmax-area.rowmin+1][area.colmax-area.colmin+1];
            for (int i=0; i<img.length; i++) {
                for (int j=0; j<img[i].length; j++) {
                    img[i][j] = image[area.rowmin+i][area.colmin+j];
                }
            }
            gridImages.add(toGrid(img));
        }

        System.out.println("Scanning completed!");
    }

    // Tangan kiri basah (image harus hitam putih)
    void trace() {
        Area area = new Area();
        Position temp = new Position(currPos.row, currPos.col);
        int dir;
        edges[currPos.row][currPos.col] = true;
        startPos.copy(currPos);
        if (currPos.row < area.rowmin) area.rowmin = currPos.row;
        if (currPos.row > area.rowmax) area.rowmax = currPos.row;
        if (currPos.col < area.colmin) area.colmin = currPos.col;
        if (currPos.col > area.colmax) area.colmax = currPos.col;
        do {
            dir = getLeftDir(temp);
            nextPos(temp, dir);
            if (currPos.row < area.rowmin) area.rowmin = currPos.row;
            if (currPos.row > area.rowmax) area.rowmax = currPos.row;
            if (currPos.col < area.colmin) area.colmin = currPos.col;
            if (currPos.col > area.colmax) area.colmax = currPos.col;
            edges[currPos.row][currPos.col] = true;
            area.addCode(currDir);
        } while(!currPos.isEqual(startPos));
        areas.add(area);
    }

    void toBinaryImage() {
        if (image != null) {
            for(int i=0; i<image.length; i++) {
                for (int j=0; j<image[i].length; j++) {
                    if ( (image[i][j]&0xFFFFFF) == 0xFFFFFF)  // putih
                        image[i][j] = WHITE_COLOR;
                    else    // hitam
                        image[i][j] = BLACK_COLOR;
                }
            }
        }
    }

    public int[][] toGrid(int[][] in){
        int i_d=in.length/5,j_d=in[0].length/5;
        int i_x=in.length%5,j_x=in[0].length%5;
        int limit=(i_d*j_d)/2;
//		int hsl=0;
        int[][] res=new int[5][5];
        for (int i=0;i<5;i++){
            int[] tmp=new int[5];
            for (int j=0;j<5;j++){
                int tp=0;
                for (int i1=0;i1<i_d;i1++){
                    for (int j1=0;j1<j_d;j1++){
                        tp+=in[i*i_d+(i<i_x?i:i_x)+i1][j*j_d+(j<j_x?j:j_x)+j1];
                    }
                }
                if (tp>limit){
                    tmp[j]=1;
//					hsl+=(1<<(i*5+j));
                } else {
                    tmp[j]=0;
                }
            }
            res[i]=tmp;
        }
        return res;
    }

    public void printChainCode() {
        int j=1,i;
        for (int a=0; a<areas.size(); a++) {
            System.out.print(j + ". ");
            ArrayList<Integer> arr = areas.get(a).chainCode;
            for (i=0; i<arr.size(); i++) {
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
        areas = new ArrayList<>();
        gridImages = new ArrayList<>();
        for (int i=0; i<edges.length; i++) {
            for(int j=0; j<edges[0].length; j++) {
                edges[i][j] = false;
            }
        }
        currPos = new Position(0,0);
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

    public class Area {
        public int colmin, colmax, rowmin, rowmax;
        public ArrayList<Integer> chainCode;

        public Area() {
            colmin = ~(1<<31); colmax = 0;
            rowmin = ~(1<<31); rowmax = 0;
            chainCode = new ArrayList<>();
        }
        public void addCode(int code) {
            chainCode.add(code);
        }

    }
}

