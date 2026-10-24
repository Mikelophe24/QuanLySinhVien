package com.qlsv.model;

import java.sql.Date;

/**
 * Model sinh viên (Thực thể 2 - Chi tiết), ánh xạ tới bảng {@code sinhvien}.
 * Trường {@code maLop} là khóa ngoại trỏ tới bảng {@code lop};
 * trường {@code tenLop} chỉ dùng để hiển thị (lấy từ phép JOIN).
 */
public class SinhVien {

    private String maSv;
    private String hoTen;
    private String gioiTinh;
    private Date ngaySinh;
    private String email;
    private String dienThoai;
    private String maLop;     // khóa ngoại
    private String tenLop;    // chỉ để hiển thị (không nằm trong bảng sinhvien)

    public SinhVien() {
    }

    public SinhVien(String maSv, String hoTen, String gioiTinh, Date ngaySinh,
                    String email, String dienThoai, String maLop) {
        this.maSv = maSv;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.email = email;
        this.dienThoai = dienThoai;
        this.maLop = maLop;
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
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
}
