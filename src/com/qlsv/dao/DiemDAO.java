package com.qlsv.dao;

import com.qlsv.model.Diem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp thao tác CRUD cho bảng {@code diem}.
 * Khi đọc dữ liệu JOIN với {@code sinhvien} và {@code monhoc} để lấy tên hiển thị.
 */
public class DiemDAO {

    /** Lấy toàn bộ bảng điểm (kèm tên sinh viên, tên môn từ JOIN). */
    public List<Diem> findAll() throws SQLException {
        String sql = "SELECT d.id, d.ma_sv, d.ma_mh, d.diem_so, d.hoc_ky, "
                   + "       sv.ho_ten AS ten_sv, mh.ten_mh "
                   + "FROM diem d "
                   + "JOIN sinhvien sv ON d.ma_sv = sv.ma_sv "
                   + "JOIN monhoc   mh ON d.ma_mh = mh.ma_mh "
                   + "ORDER BY d.ma_sv, d.ma_mh";
        List<Diem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public boolean insert(Diem d) throws SQLException {
        String sql = "INSERT INTO diem (ma_sv, ma_mh, diem_so, hoc_ky) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getMaSv());
            ps.setString(2, d.getMaMh());
            ps.setDouble(3, d.getDiemSo());
            ps.setString(4, d.getHocKy());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Diem d) throws SQLException {
        String sql = "UPDATE diem SET ma_sv = ?, ma_mh = ?, diem_so = ?, hoc_ky = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getMaSv());
            ps.setString(2, d.getMaMh());
            ps.setDouble(3, d.getDiemSo());
            ps.setString(4, d.getHocKy());
            ps.setInt(5, d.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM diem WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Diem map(ResultSet rs) throws SQLException {
        Diem d = new Diem();
        d.setId(rs.getInt("id"));
        d.setMaSv(rs.getString("ma_sv"));
        d.setMaMh(rs.getString("ma_mh"));
        d.setDiemSo(rs.getDouble("diem_so"));
        d.setHocKy(rs.getString("hoc_ky"));
        d.setTenSv(rs.getString("ten_sv"));
        d.setTenMh(rs.getString("ten_mh"));
        return d;
    }
}
