package com.pengcit.fauzanhilmi.imageprocessingcompilation.imageequalizer;

import android.graphics.Bitmap;

/**
 * Created by Fauzan Hilmi on 06/09/2015.
 */
public class ImageProcessor {
    private static int w;
    private static int h;
    private static int [][] matOri;
    private static int [][] mat;

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

    public static void convertToGreyScale() {
        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {
                int pixel = matOri[i][j];
                int alpha = (pixel>> 24) & 0x000000FF;
                int red = (pixel>> 16) & 0x000000FF;
                int green = (pixel >>8 ) & 0x000000FF;
                int blue = (pixel) & 0x000000FF;
                int gray= (red+green+blue)/3;
                mat[i][j] = (alpha<<24) | (gray<<16) | (gray<<8) | gray;
            }
        }
    }

    public static Bitmap getBuildImage() {
        Bitmap b = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        //b.setPixels(mat,0,w,0,0,w,h);
        //TES
        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {
                b.setPixel(i,j,mat[i][j]);
            }
        }
        return b;
    }

    public static void equalize(int adj) {
        int maxColor=256;
        int[] freq=new int[maxColor],freqResult=new int[maxColor],map=new int[maxColor];
        for (int i=0;i<maxColor;i++)freq[i]=0;

        // Buat Tabel frekuensi kumulatif
        for (int i=0;i<h;i++){
            for (int j=0;j<w;j++){
                  int gray = 0xFF & matOri[i][j];
                  freq[gray]++;
//                freq[mat[i][j]]++;
            }
        }
        for (int i=1;i<maxColor;i++){
            freq[i]=freq[i]+freq[i-1];
        }

        // Tabel frekuensi kumulatif 2
        for (int i=0;i<maxColor;i++){
            freqResult[i]=h*w/maxColor;
        }
        for (int i=0;i<((h*w)%maxColor);i++)freqResult[i]++;
        freqResult[0]+=adj;
        for (int i=1;i<maxColor;i++){
            freqResult[i]=freqResult[i]+freqResult[i-1];
        }

        // Mapping
        for (int i=0,curr=0;i<maxColor;i++){
            for (int k=curr+1;((k<maxColor)&&(Math.abs(freqResult[k]-freq[i])<Math.abs(freqResult[k-1]-freq[i])));k++)curr=k;
            map[i]=curr;
        }
        for (int i=0;i<h;i++){
            for (int j=0;j<w;j++){
//                mat[i][j]=map[mat[i][j]];
                  int gray = map[0xFF & matOri[i][j]];
                  mat[i][j] = (matOri[i][j] & 0xFF000000) | (gray<<24) | (gray<<16) | (gray<<8) | gray;
            }
        }

    }

}
