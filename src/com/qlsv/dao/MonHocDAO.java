package com.qlsv.dao;

import com.qlsv.model.MonHoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Lớp thao tác CRUD cho bảng {@code monhoc}. */
public class MonHocDAO {

    public List<MonHoc> findAll() throws SQLException {
        String sql = "SELECT ma_mh, ten_mh, so_tin_chi FROM monhoc ORDER BY ma_mh";
        List<MonHoc> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MonHoc(
                        rs.getString("ma_mh"),
                        rs.getString("ten_mh"),
                        rs.getInt("so_tin_chi")));
            }
        }
        return list;
    }

    public boolean insert(MonHoc mh) throws SQLException {
        String sql = "INSERT INTO monhoc (ma_mh, ten_mh, so_tin_chi) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mh.getMaMh());
            ps.setString(2, mh.getTenMh());
            ps.setInt(3, mh.getSoTinChi());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(MonHoc mh) throws SQLException {
        String sql = "UPDATE monhoc SET ten_mh = ?, so_tin_chi = ? WHERE ma_mh = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mh.getTenMh());
            ps.setInt(2, mh.getSoTinChi());
            ps.setString(3, mh.getMaMh());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String maMh) throws SQLException {
        String sql = "DELETE FROM monhoc WHERE ma_mh = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMh);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean exists(String maMh) throws SQLException {
        String sql = "SELECT 1 FROM monhoc WHERE ma_mh = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMh);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
