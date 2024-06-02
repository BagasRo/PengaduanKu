package com.example.myapplication;

public class Constant {
    private static Constant instance;
    public static String lokasiPengaduan;

    private Constant() {
        // Private constructor to prevent external instantiation.
    }

    public static Constant getInstance() {
        if (instance == null) {
            instance = new Constant();
        }
        return instance;
    }
}

