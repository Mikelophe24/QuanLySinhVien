package com.qlsv.model;

/**
 * Model điểm, ánh xạ tới bảng {@code diem}.
 * Có 2 khóa ngoại {@code maSv} (tới sinhvien) và {@code maMh} (tới monhoc).
 * Các trường {@code tenSv}, {@code tenMh} chỉ dùng để hiển thị (lấy từ JOIN).
 */
public class Diem {

    private int id;
    private String maSv;    // khóa ngoại tới sinhvien
    private String maMh;    // khóa ngoại tới monhoc
    private double diemSo;
    private String hocKy;

    private String tenSv;   // chỉ để hiển thị
    private String tenMh;   // chỉ để hiển thị

    public Diem() {
    }

    public Diem(int id, String maSv, String maMh, double diemSo, String hocKy) {
        this.id = id;
        this.maSv = maSv;
        this.maMh = maMh;
        this.diemSo = diemSo;
        this.hocKy = hocKy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public String getMaMh() {
        return maMh;
    }

    public void setMaMh(String maMh) {
        this.maMh = maMh;
    }

    public double getDiemSo() {
        return diemSo;
    }

    public void setDiemSo(double diemSo) {
        this.diemSo = diemSo;
    }

    public String getHocKy() {
        return hocKy;
    }

    public void setHocKy(String hocKy) {
        this.hocKy = hocKy;
    }

    public String getTenSv() {
        return tenSv;
    }

    public void setTenSv(String tenSv) {
        this.tenSv = tenSv;
    }

    public String getTenMh() {
        return tenMh;
    }

    public void setTenMh(String tenMh) {
        this.tenMh = tenMh;
    }
}
