package com.qlsv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp phục vụ chức năng THỐNG KÊ / BÁO CÁO.
 * Các truy vấn đều có JOIN và/hoặc GROUP BY, ORDER BY theo đúng yêu cầu.
 * Trả về {@code List<Object[]>} để nạp thẳng vào JTable.
 */
public class ThongKeDAO {

    /**
     * Thống kê SỐ LƯỢNG SINH VIÊN THEO LỚP.
     * JOIN lop với sinhvien, GROUP BY lớp, ORDER BY số lượng giảm dần.
     * Cột: Mã lớp | Tên lớp | Khoa | Số sinh viên
     */
    public List<Object[]> thongKeSinhVienTheoLop() throws SQLException {
        String sql = "SELECT l.ma_lop, l.ten_lop, l.khoa, COUNT(sv.ma_sv) AS so_sv "
                   + "FROM lop l "
                   + "LEFT JOIN sinhvien sv ON l.ma_lop = sv.ma_lop "
                   + "GROUP BY l.ma_lop, l.ten_lop, l.khoa "
                   + "ORDER BY so_sv DESC, l.ma_lop";
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getString("ma_lop"),
                        rs.getString("ten_lop"),
                        rs.getString("khoa"),
                        rs.getInt("so_sv")});
            }
        }
        return rows;
    }

    /**
     * Thống kê ĐIỂM TRUNG BÌNH CỦA TỪNG SINH VIÊN.
     * JOIN sinhvien - lop - diem, GROUP BY sinh viên, ORDER BY điểm TB giảm dần.
     * Cột: Mã SV | Họ tên | Lớp | Số môn | Điểm TB
     */
    public List<Object[]> thongKeDiemTrungBinh() throws SQLException {
        String sql = "SELECT sv.ma_sv, sv.ho_ten, l.ten_lop, "
                   + "       COUNT(d.id) AS so_mon, "
                   + "       ROUND(AVG(d.diem_so), 2) AS diem_tb "
                   + "FROM sinhvien sv "
                   + "LEFT JOIN lop l   ON sv.ma_lop = l.ma_lop "
                   + "LEFT JOIN diem d  ON sv.ma_sv = d.ma_sv "
                   + "GROUP BY sv.ma_sv, sv.ho_ten, l.ten_lop "
                   + "ORDER BY diem_tb DESC, sv.ma_sv";
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object diemTb = rs.getObject("diem_tb"); // có thể null nếu chưa có điểm
                rows.add(new Object[]{
                        rs.getString("ma_sv"),
                        rs.getString("ho_ten"),
                        rs.getString("ten_lop"),
                        rs.getInt("so_mon"),
                        diemTb == null ? "(chưa có)" : diemTb});
            }
        }
        return rows;
    }

    /**
     * Thống kê ĐIỂM TRUNG BÌNH THEO MÔN HỌC.
     * JOIN monhoc - diem, GROUP BY môn, ORDER BY điểm TB giảm dần.
     * Cột: Mã môn | Tên môn | Số lượt | Điểm TB | Cao nhất | Thấp nhất
     */
    public List<Object[]> thongKeDiemTheoMon() throws SQLException {
        String sql = "SELECT mh.ma_mh, mh.ten_mh, "
                   + "       COUNT(d.id) AS so_luot, "
                   + "       ROUND(AVG(d.diem_so), 2) AS diem_tb, "
                   + "       MAX(d.diem_so) AS cao_nhat, "
                   + "       MIN(d.diem_so) AS thap_nhat "
                   + "FROM monhoc mh "
                   + "LEFT JOIN diem d ON mh.ma_mh = d.ma_mh "
                   + "GROUP BY mh.ma_mh, mh.ten_mh "
                   + "ORDER BY diem_tb DESC";
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object diemTb = rs.getObject("diem_tb");
                Object cao = rs.getObject("cao_nhat");
                Object thap = rs.getObject("thap_nhat");
                rows.add(new Object[]{
                        rs.getString("ma_mh"),
                        rs.getString("ten_mh"),
                        rs.getInt("so_luot"),
                        diemTb == null ? "(chưa có)" : diemTb,
                        cao == null ? "-" : cao,
                        thap == null ? "-" : thap});
            }
        }
        return rows;
    }
}
