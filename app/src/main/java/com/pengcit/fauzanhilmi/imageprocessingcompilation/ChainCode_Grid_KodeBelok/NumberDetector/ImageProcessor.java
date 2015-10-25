package com.pengcit.fauzanhilmi.imageprocessingcompilation.ChainCode_Grid_KodeBelok.NumberDetector;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fauzan Hilmi on 13/09/2015.
 */
public class ImageProcessor {
    private static int BG = -1;
    private static int w;
    private static int h;
    private static int [][] matOri;
    private static int [][] mat;
    private static boolean [][] matTrack;
    private static List<List<Integer>> chainCode;

    public static void init (Bitmap b) {
        w = b.getWidth();
        h = b.getHeight();
        mat = new int[h][w];
        matOri = new int[h][w];
        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {
                matOri[i][j] = b.getPixel(i,j);
                mat[i][j] = b.getPixel(i,j);
            }
        }
    }

    public static int solveOne() { //cuma satu angka
        getChainCode();
        chainCode.size();
        for(int i=0; i<chainCode.size(); i++) {
            for(int j=0; j<chainCode.get(i).size(); j++) {
                Log.d(Integer.toString(i),Integer.toString(chainCode.get(i).get(j)));
            }
        }
        compressChainCode();
        ArrayList<Integer> listNumbers = getNumbers();
        return listNumbers.get(0);
    }

    public static void print() {
        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {
                Log.d(Integer.toString(h)+" "+Integer.toString(w),Integer.toString(mat[i][j]));
            }
        }
    }

    public static void initMats() {
        matTrack = new boolean[h][w];
        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {
                matTrack[i][j] = false;
            }
        }
    }

    public static void getChainCode() {
        chainCode = new ArrayList<>();
        boolean isPrevFilled = false;
        int prevx = 0;
        int prevy = 0;
        boolean found = false;
    Log.d("mulai","getchaincode");
        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {
                if(mat[i][j]!=BG && !found) {
                    found = true;
                    chainCode.add(new ArrayList<Integer>());
                    int sx = i;
                    int sy = j;
                    initMats();
                    borderTracing(i, j, -1, i, j);
                }
                prevx = i;
                prevy = j;
            }
        }
    }

    public static void borderTracing(int sx, int sy, int prev, int x, int y) {
        matTrack[x][y] = true;
        int px=0,py=0;
//        switch (prev) {
//            case 7 : px = x-1; py =  y-1; break;
//            case 0 : px = x-1; py = y; break;
//            case 1 : px = x-1; py = y+1; break;
//            case 6 : px = x; py = y-1; break;
//            case 2 : px = x; py = y+1; break;
//            case 5 : px = x+1; py = y-1; break;
//            case 4 : px = x+1; py = y; break;
//            case 3 : px = x+1; py = y+1; break;
//            default :
//                px = 0; py = 0;
//                Log.d("prev > px py","prev misvalued");
//        }
//        if(sx==x && sy==y) {
//            px = -1; py = -1;
//        }
        for(int i=x-1; i<=x+1; i++) {
            for(int j=y-1; j<=y+1; j++) {
//                if((i!=px || j!=py) && i>=0 && i<h && j>=0 && j<w && isBorder(i,j)) {
                  if((i!=x || j!=y) && !matTrack[i][j] && i>=0 && i<h && j>=0 && j<w && isBorder(i,j)) {
                    int next = 0;
                    if(i==x-1) {
                        if(j==y-1) next = 7;
                        else if(j==y) next = 0;
                        else if(j==y+1) next = 1;
                    }
                    else if(i==x) {
                        if(j==y-1) next = 6;
                        else if(j==y+1) next = 2;
                    }
                    else if(i==x+1) {
                        if(j==y-1) next = 5;
                        else if(j==y) next = 4;
                        else if(j==y+1) next = 3;
                    }
                    if(next%2==0) chainCode.get(chainCode.size()-1).add(next);
                    borderTracing(sx,sy,next,i,j);
                    return;
                }
            }
        }
        //chainCode.get(chainCode.size()-1).remove(chainCode.get(chainCode.size()-1).size()-1);
        return;
    }

    public static boolean isBorder(int x, int y) {
//        return ((i>0 && (j>0) && mat[i-1][j-1]==0)  ||
//                (i>0 && mat[i-1][j]==0)             ||
//                (i>0 && j<w && mat[i-1][j+1]==0)    ||
//                (j>0 && mat[i][j-1]==0)             ||
//                (j<w && mat[i][j+1]==0)             ||
//                (i<h && j>0 && mat[i+1][j-1]==0)    ||
//                (i<h && mat[i+1][j]==0)             ||
//                (i<h && j<w && mat[i+1][j+1]==0)
//        );
        if(mat[x][y]==BG) return false;
        for(int i=x-1; i<=x+1; i++) {
            for(int j=y-1; j<=y+1; j++) {
                if(i>=0 && i<h && j>=0 && j<w) {
                    if(mat[i][j]==BG)
                        return true;
                }
            }
        }
        return false;
    }

    public static void compressChainCode() {
        for(int i=0; i<chainCode.size(); i++) {
            if(chainCode.get(i).size()==1) {
                break;
            }
            int prev = chainCode.get(i).get(0);
            for(int j=1; j<chainCode.get(i).size(); j++) {
                if(chainCode.get(i).get(j) == prev) {
                    chainCode.get(i).remove(j-1);
                    prev = chainCode.get(i).get(j-1);
                    j--;
                }
                else {
                    prev = chainCode.get(i).get(j);
                }
            }
        }
    }

    public static ArrayList<Integer> getNumbers() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i=0; i<chainCode.size(); i++) {
            Number n = new Number((ArrayList<Integer>) chainCode.get(i),0); //BENERIN NUMHOLE
            numbers.add(Number.getNumber(n));
        }
        return numbers;
    }
}
