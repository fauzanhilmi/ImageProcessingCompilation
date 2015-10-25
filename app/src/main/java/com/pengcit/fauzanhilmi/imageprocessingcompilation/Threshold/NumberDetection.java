package com.pengcit.fauzanhilmi.imageprocessingcompilation.Threshold;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author khaidzir
 */
public class NumberDetection {

    private PixelTracer pxTracer;
    private Binarizer binarizer;
    private DatabaseLoader dataLoader;
    private ArrayList<MapData> data;
    private ArrayList<Character> result;
    private int[][] image;

    public ArrayList<Integer> chainData, chainCompare;
    int [][]dpc;
    boolean[][]dpf;

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

    public ArrayList<Character> getResults () {
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
//            nd.identifyImage(imageArr);
//            nd.printResult();
//        } catch(IOException ex) {
//
//        }
//    }

    public void identifyImage() {
        result = new ArrayList<>();
        ArrayList<ArrayList<Integer>> chainCodeRes = scanImage(image);
        for (ArrayList<Integer> arr : chainCodeRes) {
//            chainCompare = compress_chain(arr);
            chainCompare = arr;
            result.add(identifyChainCode(arr));
        }
    }

    ArrayList< ArrayList<Integer> > scanImage(int[][] image) {
        image = binarizer.binarize(image);
        pxTracer.image = image;
        pxTracer.scan();
        return pxTracer.chainCode;
    }

    public char identifyChainCode(ArrayList<Integer> chainCode) {
        int[] distances = new int[data.size()];

//        System.out.print(chainCompare.size() + " = ");
        for (int i=0; i<data.size(); i++) {
            chainData = scale(data.get(i).chainCode, Math.round((float)chainCompare.size()/(float)data.get(i).chainCode.size()) );
//            chainData = compress_chain(numberData.get(i));
//            System.out.print(chainData.size() + " ");
            dpf = new boolean[chainData.size()][chainCompare.size()];
            for (int i1=0; i1<dpf.length; i1++) {
                for(int j=0; j<dpf[i1].length; j++) {
                    dpf[i1][j] = false;
                }
            }

            dpc = new int[chainData.size()][chainCompare.size()];
            distances[i] = dp(0,0);
//            System.out.println(chainData+" "+chainCompare);
//            for (int i1=0; i1<chainData.size(); i1++) {
//                for(int j=0; j<chainCompare.size(); j++) {
//                    System.out.print(dpc[i1][j]+" ");
//                }
//                System.out.println();
//            }
        }
//        System.out.println();

//        for (int a=0; a<distances.length; a++) {
//            System.out.print(distances[a] + " ");
//        }
//        System.out.println();

        int min=distances[0];
        int idxmin=0;
        for (int i=1; i<distances.length; i++) {
            if (distances[i] < min) {
                min = distances[i];
                idxmin = i;
            }
        }
        return data.get(idxmin).identifier;
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
