package com.hieu.ThuChi.model;

public class GiaoDich {
    String tenGiaoDich;
    float soTien;
    String ngayGiaoDich;
    int loaiGiaoDich;
    String ID;
    float soTienTruoc;
    float soTienSau;

    public GiaoDich() {
    }

    public GiaoDich(String tenGiaoDich, float soTien, String ngayGiaoDich, int loaiGiaoDich) {
        this.tenGiaoDich = tenGiaoDich;
        this.soTien = soTien;
        this.ngayGiaoDich = ngayGiaoDich;
        this.loaiGiaoDich = loaiGiaoDich;
    }

    public String getTenGiaoDich() {
        return tenGiaoDich;
    }

    public void setTenGiaoDich(String tenGiaoDich) {
        this.tenGiaoDich = tenGiaoDich;
    }

    public float getSoTien() {
        return soTien;
    }

    public void setSoTien(float soTien) {
        this.soTien = soTien;
    }

    public String getNgayGiaoDich() {
        return ngayGiaoDich;
    }

    public void setNgayGiaoDich(String ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }

    public int getLoaiGiaoDich() {
        return loaiGiaoDich;
    }

    public void setLoaiGiaoDich(int loaiGiaoDich) {
        this.loaiGiaoDich = loaiGiaoDich;
    }

    public String getId() {
        return ID;
    }

    public void setId(String ID) {
        this.ID = ID;
    }

    public float getSoTienTruoc() {
        return soTienTruoc;
    }

    public void setSoTienTruoc(float soTienTruoc) {
        this.soTienTruoc = soTienTruoc;
    }

    public float getSoTienSau() {
        return soTienSau;
    }

    public void setSoTienSau(float soTienSau) {
        this.soTienSau = soTienSau;
    }
}
