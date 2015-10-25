package com.pengcit.fauzanhilmi.imageprocessingcompilation.ChainCode_Grid_KodeBelok.NumberDetector;

import java.util.ArrayList;
//import static javanumberdetector.NumberDatabase.DUA;
//import static javanumberdetector.NumberDatabase.EMPAT;
//import static javanumberdetector.NumberDatabase.ENAM;
//import static javanumberdetector.NumberDatabase.LIMA;
//import static javanumberdetector.NumberDatabase.NOL;
//import static javanumberdetector.NumberDatabase.SATU;
//import static javanumberdetector.NumberDatabase.SEMBILAN;
//import static javanumberdetector.NumberDatabase.TIGA;
//import static javanumberdetector.NumberDatabase.TUJUH;

/**
 * Created by Fauzan Hilmi on 20/09/2015.
 */
public class Number {

    public ArrayList<Integer> patternClockwise, patternCounterClockwise;
    public int numHole;

    public Number (ArrayList<Integer> pCw, int numHole) {
        patternClockwise = new ArrayList<>(pCw);
        //patternClockwise = new ArrayList<>();
        //patternClockwise = (ArrayList<Integer>) pCw.clone();
//        for (int i=0; i<pCw.size(); i++) {
////            patternClockwise.add(pCw.get(i));
//            System.out.println(pCw.get(i));
//        }
        //patternClockwise = pCw;
//        for (int i=0; i<patternClockwise.size(); i++) {
//            patternClockwise.add(pCw.get(i));
//            System.out.println(patternClockwise.get(i));
//        }
        CounterPattern();
        this.numHole = numHole;
    }

    public Number(int[] pattern, int numHole) {
        patternClockwise = new ArrayList<>();
        for (int i=0; i<pattern.length; i++) {
            patternClockwise.add(pattern[i]);
        }
        CounterPattern();
        this.numHole = numHole;
    }

    public void CounterPattern() {
        patternCounterClockwise = new ArrayList<>();
        for (int i=patternClockwise.size()-1; i>=0; i--) {
            patternCounterClockwise.add(Reverse(patternClockwise.get(i)));
//            System.out.println(i+" "+patternClockwise.get(i)+" = "+patternCounterClockwise.get(patternCounterClockwise.size()-1));
        }
//        System.out.println("done");
    }

    int Reverse(int direction) {
        switch(direction) {
            case NumberDatabase.ATAS : return NumberDatabase.BAWAH;
            case NumberDatabase.KANAN_ATAS : return NumberDatabase.KIRI_BAWAH;
            case NumberDatabase.KANAN : return NumberDatabase.KIRI;
            case NumberDatabase.KANAN_BAWAH : return NumberDatabase.KIRI_ATAS;
            case NumberDatabase.BAWAH : return NumberDatabase.ATAS;
            case NumberDatabase.KIRI_BAWAH : return NumberDatabase.KANAN_ATAS;
            case NumberDatabase.KIRI : return NumberDatabase.KANAN;
            case NumberDatabase.KIRI_ATAS : return NumberDatabase.KANAN_ATAS;
        }
        return -1;
    }

    public static int getNumber(Number n) {
//        System.out.println(n.patternClockwise.equals(SATU.patternClockwise));
//        for(int i=0; i<n.patternClockwise.size(); i++) {
//            System.out.print(n.patternClockwise.get(i)+" ");
//        }
//        System.out.println("");
//        for(int i=0; i<SATU.patternClockwise.size(); i++) {
//            System.out.print(SATU.patternClockwise.get(i)+" ");
//        }
        if((n.patternClockwise.equals(NumberDatabase.NOL.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.NOL.patternClockwise)) && (n.numHole == NumberDatabase.NOL.numHole)) return 0;
        else if((n.patternClockwise.equals(NumberDatabase.SATU.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.SATU.patternClockwise)) && (n.numHole == NumberDatabase.SATU.numHole)) return 1;
        else if((n.patternClockwise.equals(NumberDatabase.DUA.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.DUA.patternClockwise)) && (n.numHole == NumberDatabase.DUA.numHole)) return 2;
        else if((n.patternClockwise.equals(NumberDatabase.TIGA.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.TIGA.patternClockwise)) && (n.numHole == NumberDatabase.TIGA.numHole)) return 3;
        else if((n.patternClockwise.equals(NumberDatabase.EMPAT.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.EMPAT.patternClockwise)) && (n.numHole == NumberDatabase.EMPAT.numHole)) return 4;
        else if((n.patternClockwise.equals(NumberDatabase.LIMA.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.LIMA.patternClockwise)) && (n.numHole == NumberDatabase.LIMA.numHole)) return 5;
        else if((n.patternClockwise.equals(NumberDatabase.ENAM.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.ENAM.patternClockwise)) && (n.numHole == NumberDatabase.ENAM.numHole)) return 6;
        else if((n.patternClockwise.equals(NumberDatabase.TUJUH.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.TUJUH.patternClockwise)) && (n.numHole == NumberDatabase.TUJUH.numHole)) return 7;
        else if((n.patternClockwise.equals(NumberDatabase.DELAPAN.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.DELAPAN.patternClockwise)) && (n.numHole == NumberDatabase.DELAPAN.numHole)) return 8;
        else if((n.patternClockwise.equals(NumberDatabase.SEMBILAN.patternClockwise) || n.patternCounterClockwise.equals(NumberDatabase.SEMBILAN.patternClockwise)) && (n.numHole == NumberDatabase.SEMBILAN.numHole)) return 9;
        return -1;
    }
}
