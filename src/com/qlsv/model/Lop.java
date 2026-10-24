package com.qlsv.model;

/** Model lớp học (Thực thể 1 - Danh mục), ánh xạ tới bảng {@code lop}. */
public class Lop {

    private String maLop;
    private String tenLop;
    private String khoa;
    private String nienKhoa;

    public Lop() {
    }

    public Lop(String maLop, String tenLop, String khoa, String nienKhoa) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoa = khoa;
        this.nienKhoa = nienKhoa;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getNienKhoa() {
        return nienKhoa;
    }

    public void setNienKhoa(String nienKhoa) {
        this.nienKhoa = nienKhoa;
    }

    /** Dùng để hiển thị gọn trong JComboBox. */
    @Override
    public String toString() {
        return maLop + " - " + tenLop;
    }
}
