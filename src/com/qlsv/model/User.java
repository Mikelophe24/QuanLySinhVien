package com.qlsv.model;

/** Model tài khoản đăng nhập, ánh xạ tới bảng {@code users}. */
public class User {

    private int id;
    private String username;
    private String password;
    private String hoTen;
    private String vaiTro;   // 'admin' hoặc 'user'

    public User() {
    }

    public User(int id, String username, String password, String hoTen, String vaiTro) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(vaiTro);
    }
}
