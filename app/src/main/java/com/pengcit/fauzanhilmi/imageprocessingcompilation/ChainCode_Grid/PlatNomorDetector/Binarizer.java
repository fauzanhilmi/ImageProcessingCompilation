package com.pengcit.fauzanhilmi.imageprocessingcompilation.ChainCode_Grid.PlatNomorDetector;

/**
 * Created by Fauzan Hilmi on 23/09/2015.
 */
/**
 *
 * @author khaidzir
 */
public class Binarizer {

    // Luminance method
    public int[][] toGray(int[][] original) {

        int alpha, red, green, blue;

        int [][] lum = new int [original.length][original[0].length];

        for(int i=0; i<original.length; i++) {
            for(int j=0; j<original[i].length; j++) {

                // Get pixels by R, G, B
                alpha = (original[i][j] >> 24) & 0xFF;
                red = (original[i][j] >> 16) & 0xFF;
                green = (original[i][j] >> 8) & 0xFF;
                blue = original[i][j] & 0xFF;

                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                lum[i][j] = (alpha << 24) | (red << 16) | (red << 8) | red;
            }
        }

        return lum;
    }

    // Get binary treshold using Otsu's method
    private int otsuTreshold(int[][] original) {

        int[] histogram = imageHistogram(original);
        int total = original.length * original[0].length;

        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;

            if(wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;

    }

    // Return histogram of grayscale image
    private int[] imageHistogram(int[][] input) {

        int[] histogram = new int[256];

        for(int i=0; i<histogram.length; i++) histogram[i] = 0;

        for(int i=0; i<input.length; i++) {
            for(int j=0; j<input[i].length; j++) {
                int red = (input[i][j] >> 16) & 0xFF;
                histogram[red]++;
            }
        }

        return histogram;
    }

    public int[][] binarize(int [][] original) {
        original = toGray(original);

        int red, alpha;
        int newPixel;
        int treshold = otsuTreshold(original);
        int [][] binarized = new int [original.length][original[0].length];

        for (int i=0; i<original.length; i++) {
            for (int j=0; j<original[0].length; j++) {
                red = (original[i][j] >> 16) & 0xFF;
                alpha = original[i][j] & 0xFF000000;
                if (red > treshold) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }
                binarized[i][j] = alpha | (newPixel << 16) | (newPixel << 8) | newPixel;
            }
        }

        return binarized;
    }

}
