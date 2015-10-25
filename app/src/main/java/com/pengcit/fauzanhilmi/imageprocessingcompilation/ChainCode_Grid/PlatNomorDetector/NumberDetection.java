package com.pengcit.fauzanhilmi.imageprocessingcompilation.ChainCode_Grid.PlatNomorDetector;

/**
 * Created by Fauzan Hilmi on 23/09/2015.
 */
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author khaidzir
 */
public class NumberDetection {

    static final int AVG_GEO_REC_SIZE = 10;

    private PixelTracer pxTracer;
    private Binarizer binarizer;
    private DatabaseLoader dataLoader;
    private ArrayList<MapData> data;
    private ArrayList<Character> result;
    String databasePath = "C:\\Users\\user\\Desktop\\";
    private int[][] image;

    public ArrayList<Integer> chainData, chainCompare;
    ArrayList<int[][]> gridImages;
    int [][]dpc;
    boolean[][]dpf;
    private int avgGeoRecSize;

    public NumberDetection() {
        pxTracer = new PixelTracer();
        binarizer = new Binarizer();
        dataLoader = new DatabaseLoader();
        result = new ArrayList<>();
        gridImages = new ArrayList<>();
        dataLoader.retrieveData();
        data = dataLoader.data;
        avgGeoRecSize = AVG_GEO_REC_SIZE;
        System.out.println("Banyak data : " + data.size());
    }

    public NumberDetection(Bitmap b) {
        pxTracer = new PixelTracer();
        binarizer = new Binarizer();
        dataLoader = new DatabaseLoader();
        result = new ArrayList<>();
        dataLoader.retrieveData();
        data = dataLoader.data;
        System.out.println("Banyak data : " + data.size());
        int h = b.getHeight();
        int w = b.getWidth();
        image = new int[h][w];
        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {
                image[i][j] = b.getPixel(j,i);
            }
        }
    }

    public NumberDetection(int a) {
        pxTracer = new PixelTracer();
        binarizer = new Binarizer();
        Scanner scan = new Scanner(System.in);
        int b = scan.nextInt();
        for (int i=0; i<b; i++) {
            int c = scan.nextInt();
            ArrayList<Integer> list = new ArrayList<>();
            for (int j=0; j<c; j++) {
                int d = scan.nextInt();
                list.add(d);
            }
        }
    }

    public ArrayList<Character> getResults() {
        return result;
    }

//    public static void main(String args[]) {
//        NumberDetection nd = new NumberDetection();
//        try {
//            BufferedImage image = ImageIO.read(new File("C:\\Users\\user\\Desktop\\pengcit.bmp"));
//            int[][] imageArr = new int[image.getHeight()][image.getWidth()];
//            for (int i=0; i<imageArr.length; i++) {
//                for (int j=0; j<imageArr[i].length; j++) {
//                    imageArr[i][j] = image.getRGB(j,i);
//                }
//            }
//
////            System.out.println("Banyak data grid : " + nd.data.size());
////
////            for (MapData mapdata : nd.data) {
////                System.out.println(mapdata.identifier + " : ");
////                for(int i=0; i<mapdata.gridImage.length; i++) {
////                    for(int j=0; j<mapdata.gridImage[i].length; j++) {
////                        System.out.print(mapdata.gridImage[i][j]);
////                    }
////                    System.out.println();
////                }
////                System.out.println("\n");
////            }
//
//            /////////////////////////////////////////////////////////////////////////////////////
////            Binarizer binarizer = new Binarizer();
////            imageArr = nd.AveragingGeoMatrix(imageArr);
////            imageArr = binarizer.binarize(imageArr);
////            BufferedImage img = new BufferedImage(imageArr[0].length, imageArr.length, BufferedImage.TYPE_INT_RGB);
////            for(int i=0; i<img.getHeight(); i++) {
////                for (int j=0; j<img.getWidth(); j++) {
////                    img.setRGB(j, i, imageArr[i][j]);
////                }
////            }
////            File file = new File("C:\\Users\\user\\Desktop\\save-pengcit.bmp");
////            ImageIO.write(img, "png", file);
//            /////////////////////////////////////////////////////////////////////////////////////
//
//            nd.identifyImage(imageArr);
//            nd.printResult();
//        } catch(IOException ex) {
//
//        }
//    }

    public void setAvgGeoRecSize(int size) {
        avgGeoRecSize = size;
    }

    public void identifyImage(int [][] image) {
        result = new ArrayList<>();
        processImage(image);
        for (PixelTracer.Area area : pxTracer.areas) {
//            chainCompare = compress_chain(arr);
            ArrayList<Integer> arr = area.chainCode;
            chainCompare = arr;
            result.add(identifyChainCode(arr));
        }
    }

    public void identifyImage2() {
        result = new ArrayList<>();
        processImage(image);
        for (int[][] grid : pxTracer.gridImages) {
            result.add(identifyGridImage(grid));
        }
    }

    void processImage(int [][] image) {
        // averaging pixel value
        image = AveragingGeoMatrix(image);

        // hitam putihkan
        image = binarizer.binarize(image);

        // scan image
        pxTracer.image = image;
        pxTracer.scan();
    }

    public char identifyGridImage(int[][]grid) {
        int idxmin=0, distmin=~(1<<31), counter=0, distance;
        for (MapData db : data) {
            distance=0;
            for(int i=0; i<db.gridImage.length; i++) {
                for (int j=0; j<db.gridImage[i].length; j++) {
                    if (grid[i][j] != db.gridImage[i][j]) distance++;
                }
            }
            if (distance < distmin) {
                distmin = distance;
                idxmin=counter;
            }
            counter++;
        }
        return data.get(idxmin).identifier;
    }

    public char identifyChainCode(ArrayList<Integer> chainCode) {
        int[] distances = new int[data.size()];
        int mindist=~(1<<31);
        int idxmin=0;
        for (int i=0; i<data.size(); i++) {
            chainData = scale(data.get(i).chainCode, Math.round((float)chainCompare.size()/(float)data.get(i).chainCode.size()) );
            dpf = new boolean[chainData.size()][chainCompare.size()];
            for (int i1=0; i1<dpf.length; i1++) {
                for(int j=0; j<dpf[i1].length; j++) {
                    dpf[i1][j] = false;
                }
            }

            dpc = new int[chainData.size()][chainCompare.size()];
            distances[i] = dp(0,0);
            int dp = dp(0,0);
            if (dp < mindist) {
                mindist = dp;
                idxmin = i;
            }
        }
        return data.get(idxmin).identifier;
    }

    public int[][] AveragingGeoMatrix(int[][] in){
        int rect_size=avgGeoRecSize;
        int r_x=(rect_size-1)/2;
        int[][] hsl = new int[in.length][in[0].length];
        int n=in.length, m=in[0].length;
        for (int i=0;i<n;i++){
            int[] tmp = new int[m];
            for (int j=0;j<m;j++){
                int tp_r=0,tp_g=0,tp_b=0,num=0;
                for (int i1=-r_x;i1<=r_x;i1++){
                    for (int j1=-r_x;j1<=r_x;j1++){
                        int ix=i+i1,jx=j+j1;
                        if ((0<=ix)&&(ix<n)&&(0<=jx)&&(jx<m)){
//							System.out.println(i1+","+j1+"|"+ix+","+jx);
                            tp_r+=((in[ix][jx]>>16)&(0xFF));
                            tp_g+=((in[ix][jx]>>8)&(0xFF));
                            tp_b+=(in[ix][jx]&(0xFF));
                            num++;
                        }
                    }
                }
//				System.out.print(i+","+j+"=>"+tp+"/"+num+" ");
//                tmp[j]=tp/num;
                tmp[j] = (in[i][j]&0xFF000000) | ((tp_r/num) << 16) | ((tp_g/num) << 8) | (tp_b/num);
            }
            hsl[i]=tmp;
//			System.out.println();
        }
        return hsl;
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

    ArrayList<Integer> scale(ArrayList<Integer> input, double scale) {
        ArrayList<Integer> ret = new ArrayList<>();
        for (int i : input) {
            for (int j=0; j<scale; j++) {
                ret.add(i);
            }
        }
        return ret;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    int dp(int idxData, int idxCompare) {
        if (idxData >= chainData.size()-1) {
            return chainCompare.size()-idxCompare;
        } else if (idxCompare >= chainCompare.size()-1) {
            return chainData.size()-idxData;
        } else if (dpf[idxData][idxCompare]){
            return dpc[idxData][idxCompare];
        } else if (Objects.equals(chainData.get(idxData), chainCompare.get(idxCompare))) {
            return dp(idxData+1, idxCompare+1);
        } else {
            // insert
            dpf[idxData][idxCompare]=true;
            int min1 = 1 + dp(idxData, idxCompare+1);

            // diedit
            int min2 = 1 + dp(idxData+1, idxCompare+1);

            // dihapus
            int min3 = 1 + dp(idxData+1, idxCompare);
            dpc[idxData][idxCompare]=Math.min(min1, Math.min(min2, min3));
            return dpc[idxData][idxCompare];
        }
    }

    public  ArrayList<Integer> compress_chain(ArrayList<Integer> in){
        ArrayList<Integer> hsl=new ArrayList<Integer>();
        int n=0;
        hsl.add(in.get(0));
        for (int i=1;i<in.size();i++){
            if (hsl.get(n)!=in.get(i)){
                hsl.add(in.get(i));n++;
            }
        }
        if (hsl.get(0)==hsl.get(n)){
            hsl.remove(n);n--;
        }
        return hsl;
    }

    public void printResult() {
        for (char c : result) {
            System.out.print(c);
        }
        System.out.println();
    }

}
