package com.pengcit.fauzanhilmi.imageprocessingcompilation.ChainCode_Grid_KodeBelok.NumberDetector;

/**
 * Created by Fauzan Hilmi on 20/09/2015.
 */
public class NumberDatabase {

    public static final int ATAS = 0,
            KANAN_ATAS = 1,
            KANAN = 2,
            KANAN_BAWAH = 3,
            BAWAH = 4,
            KIRI_BAWAH = 5,
            KIRI = 6,
            KIRI_ATAS = 7;

    static final int[] arrNol = {KANAN, BAWAH, KIRI, ATAS};

    static final int[] arrSatu = {KANAN, BAWAH, KIRI, ATAS};

    static final int[] arrDua = {KANAN, BAWAH, KIRI, BAWAH, KANAN, BAWAH, KIRI,
            ATAS, KANAN, ATAS, KIRI, ATAS};

    static final int[] arrTiga = {KANAN, BAWAH, KIRI, ATAS, KANAN, ATAS, KIRI,
            ATAS, KANAN, ATAS, KIRI, ATAS};

    static final int[] arrEmpat = {KANAN, BAWAH, KANAN, ATAS, KANAN, BAWAH,
            KIRI, ATAS, KIRI, ATAS};

    static final int[] arrLima = {KANAN, BAWAH, KIRI, BAWAH, KANAN, BAWAH, KIRI,
            ATAS, KANAN, ATAS, KIRI, ATAS};

    static final int[] arrEnam = {KANAN, BAWAH, KIRI, BAWAH, KANAN, BAWAH, KIRI,
            ATAS};

    static final int[] arrTujuh = {KANAN, BAWAH, KIRI, ATAS, KIRI, ATAS};

    static final int[] arrDelapan = {KANAN, BAWAH, KIRI, ATAS};

    static final int[] arrSembilan = {KANAN, BAWAH, KIRI, ATAS, KANAN, ATAS,
            KIRI, ATAS};


    public static final Number NOL = new Number(arrNol, 1),
            SATU = new Number(arrSatu, 0),
            DUA = new Number(arrDua, 0),
            TIGA = new Number(arrTiga, 0),
            EMPAT =  new Number(arrEmpat, 0),
            LIMA = new Number(arrLima, 0),
            ENAM = new Number(arrEnam, 1),
            TUJUH = new Number(arrTujuh, 0),
            DELAPAN = new Number(arrDelapan, 2),
            SEMBILAN = new Number(arrSembilan, 1);
}
