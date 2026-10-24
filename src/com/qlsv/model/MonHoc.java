package com.qlsv.model;

/** Model môn học, ánh xạ tới bảng {@code monhoc}. */
public class MonHoc {

    private String maMh;
    private String tenMh;
    private int soTinChi;

    public MonHoc() {
    }

    public MonHoc(String maMh, String tenMh, int soTinChi) {
        this.maMh = maMh;
        this.tenMh = tenMh;
        this.soTinChi = soTinChi;
    }

    public String getMaMh() {
        return maMh;
    }

    public void setMaMh(String maMh) {
        this.maMh = maMh;
    }

    public String getTenMh() {
        return tenMh;
    }

    public void setTenMh(String tenMh) {
        this.tenMh = tenMh;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    /** Dùng để hiển thị gọn trong JComboBox. */
    @Override
    public String toString() {
        return maMh + " - " + tenMh;
    }
}
