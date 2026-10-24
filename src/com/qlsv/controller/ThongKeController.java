package com.qlsv.controller;

import com.qlsv.dao.LopDAO;
import com.qlsv.dao.SinhVienDAO;
import com.qlsv.dao.ThongKeDAO;
import com.qlsv.model.Lop;
import com.qlsv.model.SinhVien;
import com.qlsv.view.ThongKePanel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller cho màn hình Thống kê / Tìm kiếm nâng cao.
 * Gọi {@link ThongKeDAO} cho các báo cáo tổng hợp và {@link SinhVienDAO#search}
 * cho tìm kiếm nhiều điều kiện.
 */
public class ThongKeController {

    private final ThongKePanel view;
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private final SinhVienDAO svDAO = new SinhVienDAO();
    private final LopDAO lopDAO = new LopDAO();

    public ThongKeController(ThongKePanel view) {
        this.view = view;
        attachEvents();
        loadLopComboBox();
    }

    private void attachEvents() {
        view.getBtnTkLop().addActionListener(e -> thongKeSinhVienTheoLop());
        view.getBtnTkDiemSv().addActionListener(e -> thongKeDiemTheoSinhVien());
        view.getBtnTkDiemMon().addActionListener(e -> thongKeDiemTheoMon());
        view.getBtnTimNangCao().addActionListener(e -> timKiemNangCao());
    }

    /** Nạp danh sách lớp vào combo, kèm mục "Tất cả". */
    private void loadLopComboBox() {
        try {
            view.getCboLop().removeAllItems();
            view.getCboLop().addItem(new Lop("", "-- Tất cả lớp --", "", ""));
            for (Lop l : lopDAO.findAll()) {
                view.getCboLop().addItem(l);
            }
        } catch (SQLException ex) {
            showError("Lỗi khi tải danh sách lớp", ex);
        }
    }

    private void thongKeSinhVienTheoLop() {
        try {
            view.setColumns("Số lượng sinh viên theo từng lớp",
                    new String[]{"Mã lớp", "Tên lớp", "Khoa", "Số sinh viên"});
            fill(thongKeDAO.thongKeSinhVienTheoLop());
        } catch (SQLException ex) {
            showError("Lỗi khi thống kê theo lớp", ex);
        }
    }

    private void thongKeDiemTheoSinhVien() {
        try {
            view.setColumns("Điểm trung bình của từng sinh viên",
                    new String[]{"Mã SV", "Họ tên", "Lớp", "Số môn", "Điểm TB"});
            fill(thongKeDAO.thongKeDiemTrungBinh());
        } catch (SQLException ex) {
            showError("Lỗi khi thống kê điểm theo sinh viên", ex);
        }
    }

    private void thongKeDiemTheoMon() {
        try {
            view.setColumns("Điểm trung bình theo môn học",
                    new String[]{"Mã môn", "Tên môn", "Số lượt", "Điểm TB", "Cao nhất", "Thấp nhất"});
            fill(thongKeDAO.thongKeDiemTheoMon());
        } catch (SQLException ex) {
            showError("Lỗi khi thống kê điểm theo môn", ex);
        }
    }

    /** Tìm kiếm kết hợp tên + lớp + điểm trung bình tối thiểu. */
    private void timKiemNangCao() {
        String ten = view.getTxtTen().getText().trim();
        Lop lop = (Lop) view.getCboLop().getSelectedItem();
        String maLop = (lop == null || lop.getMaLop().isEmpty()) ? null : lop.getMaLop();

        Double diemMin = null;
        String diemStr = view.getTxtDiemMin().getText().trim();
        if (!diemStr.isEmpty()) {
            try {
                diemMin = Double.parseDouble(diemStr.replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view,
                        "Điểm TB tối thiểu phải là một con số (ví dụ 7.0).",
                        "Chú ý", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            view.setColumns("Kết quả tìm kiếm nâng cao (sắp theo điểm TB giảm dần)",
                    new String[]{"Mã SV", "Họ tên", "Giới tính", "Ngày sinh",
                            "Email", "Điện thoại", "Mã lớp", "Tên lớp"});
            List<SinhVien> list = svDAO.search(ten, maLop, diemMin);
            var model = view.getTableModel();
            for (SinhVien sv : list) {
                model.addRow(new Object[]{
                        sv.getMaSv(), sv.getHoTen(), sv.getGioiTinh(),
                        sv.getNgaySinh(), sv.getEmail(), sv.getDienThoai(),
                        sv.getMaLop(), sv.getTenLop()});
            }
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy sinh viên phù hợp.",
                        "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            showError("Lỗi khi tìm kiếm nâng cao", ex);
        }
    }

    private void fill(List<Object[]> rows) {
        var model = view.getTableModel();
        for (Object[] r : rows) {
            model.addRow(r);
        }
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(view, title + ":\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
