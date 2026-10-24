package com.qlsv.dao;

import com.qlsv.model.SinhVien;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp thao tác CRUD cho bảng {@code sinhvien} (Thực thể 2 - Chi tiết).
 * Khi đọc dữ liệu có JOIN với bảng {@code lop} để lấy tên lớp hiển thị.
 */
public class SinhVienDAO {

    /** Lấy toàn bộ sinh viên (kèm tên lớp từ phép JOIN). */
    public List<SinhVien> findAll() throws SQLException {
        String sql = "SELECT sv.ma_sv, sv.ho_ten, sv.gioi_tinh, sv.ngay_sinh, "
                   + "       sv.email, sv.dien_thoai, sv.ma_lop, l.ten_lop "
                   + "FROM sinhvien sv "
                   + "LEFT JOIN lop l ON sv.ma_lop = l.ma_lop "
                   + "ORDER BY sv.ma_sv";
        List<SinhVien> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    /** Thêm mới một sinh viên. */
    public boolean insert(SinhVien sv) throws SQLException {
        String sql = "INSERT INTO sinhvien "
                   + "(ma_sv, ho_ten, gioi_tinh, ngay_sinh, email, dien_thoai, ma_lop) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, sv);
            return ps.executeUpdate() > 0;
        }
    }

    /** Cập nhật thông tin sinh viên theo mã sinh viên. */
    public boolean update(SinhVien sv) throws SQLException {
        String sql = "UPDATE sinhvien SET ho_ten = ?, gioi_tinh = ?, ngay_sinh = ?, "
                   + "email = ?, dien_thoai = ?, ma_lop = ? WHERE ma_sv = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sv.getHoTen());
            ps.setString(2, sv.getGioiTinh());
            ps.setDate(3, sv.getNgaySinh());
            ps.setString(4, sv.getEmail());
            ps.setString(5, sv.getDienThoai());
            ps.setString(6, sv.getMaLop());
            ps.setString(7, sv.getMaSv());
            return ps.executeUpdate() > 0;
        }
    }

    /** Xóa sinh viên theo mã. */
    public boolean delete(String maSv) throws SQLException {
        String sql = "DELETE FROM sinhvien WHERE ma_sv = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSv);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean exists(String maSv) throws SQLException {
        String sql = "SELECT 1 FROM sinhvien WHERE ma_sv = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSv);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * TÌM KIẾM NÂNG CAO: theo tên (gần đúng), mã lớp và điểm tối thiểu.
     * Truy vấn dùng JOIN + GROUP BY + HAVING + ORDER BY.
     * Mỗi tham số có thể để trống/null để bỏ qua điều kiện đó.
     *
     * @param tenKey   từ khóa tên (LIKE), có thể rỗng
     * @param maLop    mã lớp lọc chính xác, có thể rỗng
     * @param diemMin  điểm trung bình tối thiểu, null nếu bỏ qua
     */
    public List<SinhVien> search(String tenKey, String maLop, Double diemMin) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT sv.ma_sv, sv.ho_ten, sv.gioi_tinh, sv.ngay_sinh, "
              + "       sv.email, sv.dien_thoai, sv.ma_lop, l.ten_lop, "
              + "       AVG(d.diem_so) AS diem_tb "
              + "FROM sinhvien sv "
              + "LEFT JOIN lop l ON sv.ma_lop = l.ma_lop "
              + "LEFT JOIN diem d ON sv.ma_sv = d.ma_sv "
              + "WHERE 1 = 1 ");

        List<Object> params = new ArrayList<>();
        if (tenKey != null && !tenKey.trim().isEmpty()) {
            sql.append("AND sv.ho_ten LIKE ? ");
            params.add("%" + tenKey.trim() + "%");
        }
        if (maLop != null && !maLop.trim().isEmpty()) {
            sql.append("AND sv.ma_lop = ? ");
            params.add(maLop.trim());
        }
        sql.append("GROUP BY sv.ma_sv, sv.ho_ten, sv.gioi_tinh, sv.ngay_sinh, "
                 + "sv.email, sv.dien_thoai, sv.ma_lop, l.ten_lop ");
        if (diemMin != null) {
            sql.append("HAVING AVG(d.diem_so) >= ? ");
            params.add(diemMin);
        }
        sql.append("ORDER BY diem_tb DESC, sv.ma_sv");

        List<SinhVien> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    /** Gán các tham số chung cho câu INSERT. */
    private void bind(PreparedStatement ps, SinhVien sv) throws SQLException {
        ps.setString(1, sv.getMaSv());
        ps.setString(2, sv.getHoTen());
        ps.setString(3, sv.getGioiTinh());
        ps.setDate(4, sv.getNgaySinh());
        ps.setString(5, sv.getEmail());
        ps.setString(6, sv.getDienThoai());
        ps.setString(7, sv.getMaLop());
    }

    /** Chuyển một dòng ResultSet thành đối tượng SinhVien. */
    private SinhVien map(ResultSet rs) throws SQLException {
        SinhVien sv = new SinhVien();
        sv.setMaSv(rs.getString("ma_sv"));
        sv.setHoTen(rs.getString("ho_ten"));
        sv.setGioiTinh(rs.getString("gioi_tinh"));
        Date d = rs.getDate("ngay_sinh");
        sv.setNgaySinh(d);
        sv.setEmail(rs.getString("email"));
        sv.setDienThoai(rs.getString("dien_thoai"));
        sv.setMaLop(rs.getString("ma_lop"));
        sv.setTenLop(rs.getString("ten_lop"));
        return sv;
    }
}
