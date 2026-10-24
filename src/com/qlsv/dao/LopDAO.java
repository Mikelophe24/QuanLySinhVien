package com.qlsv.dao;

import com.qlsv.model.Lop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Lớp thao tác CRUD cho bảng {@code lop} (Thực thể 1 - Danh mục). */
public class LopDAO {

    /** Lấy toàn bộ danh sách lớp, sắp xếp theo mã lớp. */
    public List<Lop> findAll() throws SQLException {
        String sql = "SELECT ma_lop, ten_lop, khoa, nien_khoa FROM lop ORDER BY ma_lop";
        List<Lop> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    /** Thêm mới một lớp. */
    public boolean insert(Lop lop) throws SQLException {
        String sql = "INSERT INTO lop (ma_lop, ten_lop, khoa, nien_khoa) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getMaLop());
            ps.setString(2, lop.getTenLop());
            ps.setString(3, lop.getKhoa());
            ps.setString(4, lop.getNienKhoa());
            return ps.executeUpdate() > 0;
        }
    }

    /** Cập nhật thông tin một lớp (theo mã lớp). */
    public boolean update(Lop lop) throws SQLException {
        String sql = "UPDATE lop SET ten_lop = ?, khoa = ?, nien_khoa = ? WHERE ma_lop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getTenLop());
            ps.setString(2, lop.getKhoa());
            ps.setString(3, lop.getNienKhoa());
            ps.setString(4, lop.getMaLop());
            return ps.executeUpdate() > 0;
        }
    }

    /** Xóa một lớp theo mã lớp. */
    public boolean delete(String maLop) throws SQLException {
        String sql = "DELETE FROM lop WHERE ma_lop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        }
    }

    /** Kiểm tra mã lớp đã tồn tại chưa (phục vụ validate khi thêm mới). */
    public boolean exists(String maLop) throws SQLException {
        String sql = "SELECT 1 FROM lop WHERE ma_lop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Chuyển một dòng ResultSet thành đối tượng Lop. */
    private Lop map(ResultSet rs) throws SQLException {
        return new Lop(
                rs.getString("ma_lop"),
                rs.getString("ten_lop"),
                rs.getString("khoa"),
                rs.getString("nien_khoa"));
    }
}
