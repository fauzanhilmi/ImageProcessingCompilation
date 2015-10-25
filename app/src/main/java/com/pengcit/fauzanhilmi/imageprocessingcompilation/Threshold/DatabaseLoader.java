package com.pengcit.fauzanhilmi.imageprocessingcompilation.Threshold;

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
    public static String alfabetDataPath = folderPath + "semua_alfabet_kapital.bmp";

    public ArrayList<MapData> data;
    public PixelTracer tracer;
    public Binarizer binarizer;

    public DatabaseLoader() {
        data = new ArrayList<>();
        tracer = new PixelTracer();
        binarizer = new Binarizer();
    }

    public void retrieveData() {
        // Menambahkan angka
        openImageData(numberDataPath);
        char c = '0';
        for (int i=0; i<tracer.chainCode.size(); i++) {
            MapData mapData = new MapData();
            mapData.chainCode = tracer.chainCode.get(i);
            mapData.identifier = c;
            c = (char)(c + 1);
            data.add(mapData);
        }

        // Menambahkan huruf kapital
        openImageData(alfabetDataPath);
        c = 'A';
        for (int i=0; i<tracer.chainCode.size(); i++) {
            MapData mapData = new MapData();
            mapData.chainCode = tracer.chainCode.get(i);
            mapData.identifier = c;
            c = (char)(c+1);
            data.add(mapData);
        }

        // Menambahkan huruf kecil
        //....
    }

    void openImageData(String filename) {
        //            BufferedImage image = ImageIO.read(new File(filename));
//            int [][] imageArr = new int[image.getHeight()][image.getWidth()];
        Bitmap b = BitmapFactory.decodeFile(filename);
        int[][] imageArr = new int[b.getHeight()][b.getWidth()];

        // Jadikan array
        for (int i=0; i<imageArr.length; i++) {
            for (int j=0; j<imageArr[i].length; j++) {
//                    imageArr[i][j] = image.getRGB(j, i);
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


