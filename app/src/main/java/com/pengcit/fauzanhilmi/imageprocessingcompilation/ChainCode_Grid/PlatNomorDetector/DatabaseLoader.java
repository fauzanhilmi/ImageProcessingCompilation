package com.pengcit.fauzanhilmi.imageprocessingcompilation.ChainCode_Grid.PlatNomorDetector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/**
 *
 * @author khaidzir
 */
public class DatabaseLoader {

    public static String folderPath = "/storage/emulated/0/DCIM/";
    public static String numberDataPath = folderPath + "semua_angka.bmp";
    public static String alfabetCapsDataPath = folderPath + "semua_alfabet_kapital.bmp";
    public static String alfabetDataPath = folderPath + "semua_alfabet_kecil.bmp";

    public ArrayList<MapData> data;
    public PixelTracer tracer;
    public Binarizer binarizer;

    public DatabaseLoader() {
        data = new ArrayList<>();
        tracer = new PixelTracer();
        binarizer = new Binarizer();
    }

    public void retrieveData() {
        char c;

        // Menambahkan angka
        openImageData(numberDataPath);
        c = '0';
        for (int i=0; i<tracer.areas.size(); i++) {
            MapData mapData = new MapData();
            mapData.chainCode = new ArrayList<>(tracer.areas.get(i).chainCode);
            mapData.identifier = c;
            mapData.gridImage = new int[tracer.gridImages.get(i).length]
                    [tracer.gridImages.get(i)[0].length];
            for(int j=0; j<tracer.gridImages.get(i).length; j++) {
                for(int k=0; k<tracer.gridImages.get(i)[j].length; k++) {
                    mapData.gridImage[j][k] = tracer.gridImages.get(i)[j][k];
                }
            }
            c = (char)(c + 1);
            data.add(mapData);
        }

        // Menambahkan huruf kapital
        openImageData(alfabetCapsDataPath);
        c = 'A';
        for (int i=0; i<tracer.areas.size(); i++) {
            MapData mapData = new MapData();
            mapData.chainCode = new ArrayList<>(tracer.areas.get(i).chainCode);
            mapData.identifier = c;
            mapData.gridImage = new int[tracer.gridImages.get(i).length]
                    [tracer.gridImages.get(i)[0].length];
            for(int j=0; j<tracer.gridImages.get(i).length; j++) {
                for(int k=0; k<tracer.gridImages.get(i)[j].length; k++) {
                    mapData.gridImage[j][k] = tracer.gridImages.get(i)[j][k];
                }
            }
            c = (char)(c+1);
            data.add(mapData);
        }

        // Menambahkan huruf kecil
        openImageData(alfabetDataPath);
        c = 'a';
        for (int i=0; i<tracer.areas.size(); i++) {
            MapData mapData = new MapData();
            mapData.chainCode = new ArrayList<>(tracer.areas.get(i).chainCode);
            mapData.identifier = c;
            mapData.gridImage = new int[tracer.gridImages.get(i).length]
                    [tracer.gridImages.get(i)[0].length];
            for(int j=0; j<tracer.gridImages.get(i).length; j++) {
                for(int k=0; k<tracer.gridImages.get(i)[j].length; k++) {
                    mapData.gridImage[j][k] = tracer.gridImages.get(i)[j][k];
                }
            }
            c = (char)(c+1);
            data.add(mapData);
        }
    }

    void openImageData(String filename) {
        //            BufferedImage image = ImageIO.read(new File(filename));
//            int [][] imageArr = new int[image.getHeight()][image.getWidth()];
        Bitmap b = BitmapFactory.decodeFile(filename);
        int[][] imageArr = new int[b.getHeight()][b.getWidth()];

        // Jadikan array
        for (int i=0; i<imageArr.length; i++) {
            for (int j=0; j<imageArr[i].length; j++) {
                imageArr[i][j] = b.getPixel(j,i);
            }
        }

        // Hitam putihkan
        imageArr = binarizer.binarize(imageArr);

        //Cari chaincode
        tracer.setImage(imageArr);
        tracer.scan();

    }
}

