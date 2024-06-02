package com.example.myapplication;

public class ReportData {


    private String title;
    private String nama;
    private String lokasi;
    private String tanggal;
    private String laporan;
    private String telepon;
    private String image;
    private String uid;

    public ReportData() {
    }

    public ReportData(String title, String nama, String lokasi, String tanggal, String laporan, String telepon, String image, String uid) {
        this.title = title;
        this.nama = nama;
        this.lokasi = lokasi;
        this.tanggal = tanggal;
        this.laporan = laporan;
        this.telepon = telepon;
        this.image = image;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLaporan() {
        return laporan;
    }

    public void setLaporan(String laporan) {
        this.laporan = laporan;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



}

