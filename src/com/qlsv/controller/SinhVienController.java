package com.qlsv.controller;

import com.qlsv.dao.LopDAO;
import com.qlsv.dao.SinhVienDAO;
import com.qlsv.model.Lop;
import com.qlsv.model.SinhVien;
import com.qlsv.view.SinhVienPanel;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller cho màn hình Quản lý Sinh viên.
 * Ngoài CRUD còn nạp danh sách Lớp vào JComboBox (liên kết khóa ngoại)
 * và hỗ trợ tìm nhanh theo tên.
 */
public class SinhVienController {

    private final SinhVienPanel view;
    private final SinhVienDAO dao = new SinhVienDAO();
    private final LopDAO lopDAO = new LopDAO();

    public SinhVienController(SinhVienPanel view) {
        this.view = view;
        attachEvents();
        loadLopComboBox();
        loadData();
    }

    private void attachEvents() {
        view.getBtnThem().addActionListener(e -> them());
        view.getBtnSua().addActionListener(e -> sua());
        view.getBtnXoa().addActionListener(e -> xoa());
        view.getBtnLamMoi().addActionListener(e -> {
            clearForm();
            loadLopComboBox();
            loadData();
        });
        view.getBtnTim().addActionListener(e -> timTheoTen());

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
    }

    /** Nạp danh sách lớp vào JComboBox để chọn khóa ngoại. */
    private void loadLopComboBox() {
        try {
            List<Lop> lops = lopDAO.findAll();
            view.getCboLop().removeAllItems();
            for (Lop l : lops) {
                view.getCboLop().addItem(l);
            }
        } catch (SQLException ex) {
            showError("Lỗi khi tải danh sách lớp", ex);
        }
    }

    private void loadData() {
        try {
            fillTable(dao.findAll());
        } catch (SQLException ex) {
            showError("Lỗi khi tải danh sách sinh viên", ex);
        }
    }

    private void fillTable(List<SinhVien> list) {
        var model = view.getTableModel();
        model.setRowCount(0);
        for (SinhVien sv : list) {
            model.addRow(new Object[]{
                    sv.getMaSv(), sv.getHoTen(), sv.getGioiTinh(),
                    sv.getNgaySinh(), sv.getEmail(), sv.getDienThoai(),
                    sv.getMaLop(), sv.getTenLop()});
        }
    }

    private void them() {
        SinhVien sv = readForm();
        if (sv == null) {
            return;
        }
        try {
            if (dao.exists(sv.getMaSv())) {
                warn("Mã sinh viên '" + sv.getMaSv() + "' đã tồn tại!");
                return;
            }
            if (dao.insert(sv)) {
                info("Thêm sinh viên thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi thêm sinh viên", ex);
        }
    }

    private void sua() {
        SinhVien sv = readForm();
        if (sv == null) {
            return;
        }
        try {
            if (!dao.exists(sv.getMaSv())) {
                warn("Không tìm thấy sinh viên để sửa. Hãy chọn một dòng trên bảng.");
                return;
            }
            if (dao.update(sv)) {
                info("Cập nhật sinh viên thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi cập nhật sinh viên", ex);
        }
    }

    private void xoa() {
        String maSv = view.getTxtMaSv().getText().trim();
        if (maSv.isEmpty()) {
            warn("Hãy chọn sinh viên cần xóa trên bảng.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa sinh viên '" + maSv + "'?\n"
                + "(Điểm của sinh viên này cũng sẽ bị xóa theo.)",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (dao.delete(maSv)) {
                info("Xóa sinh viên thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi xóa sinh viên", ex);
        }
    }

    private void timTheoTen() {
        String key = view.getTxtTimKiem().getText().trim();
        try {
            fillTable(dao.search(key, null, null));
        } catch (SQLException ex) {
            showError("Lỗi khi tìm kiếm", ex);
        }
    }

    /** Đọc và kiểm tra dữ liệu từ form. Trả về null nếu không hợp lệ. */
    private SinhVien readForm() {
        String maSv = view.getTxtMaSv().getText().trim();
        String hoTen = view.getTxtHoTen().getText().trim();
        String gioiTinh = (String) view.getCboGioiTinh().getSelectedItem();
        String ngaySinhStr = view.getTxtNgaySinh().getText().trim();
        String email = view.getTxtEmail().getText().trim();
        String dienThoai = view.getTxtDienThoai().getText().trim();
        Lop lop = (Lop) view.getCboLop().getSelectedItem();

        if (maSv.isEmpty() || hoTen.isEmpty()) {
            warn("Vui lòng nhập Mã SV và Họ tên!");
            return null;
        }

        // Kiểm tra định dạng ngày sinh
        Date ngaySinh = null;
        if (!ngaySinhStr.isEmpty()) {
            try {
                ngaySinh = Date.valueOf(LocalDate.parse(ngaySinhStr));
            } catch (DateTimeParseException ex) {
                warn("Ngày sinh không hợp lệ. Định dạng đúng: yyyy-MM-dd (ví dụ 2004-01-15).");
                return null;
            }
        }

        // Kiểm tra email cơ bản
        if (!email.isEmpty() && !email.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            warn("Email không hợp lệ!");
            return null;
        }

        SinhVien sv = new SinhVien();
        sv.setMaSv(maSv);
        sv.setHoTen(hoTen);
        sv.setGioiTinh(gioiTinh);
        sv.setNgaySinh(ngaySinh);
        sv.setEmail(email);
        sv.setDienThoai(dienThoai);
        sv.setMaLop(lop == null ? null : lop.getMaLop());
        return sv;
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            return;
        }
        var m = view.getTableModel();
        view.getTxtMaSv().setText(str(m.getValueAt(row, 0)));
        view.getTxtHoTen().setText(str(m.getValueAt(row, 1)));
        view.getCboGioiTinh().setSelectedItem(str(m.getValueAt(row, 2)));
        view.getTxtNgaySinh().setText(str(m.getValueAt(row, 3)));
        view.getTxtEmail().setText(str(m.getValueAt(row, 4)));
        view.getTxtDienThoai().setText(str(m.getValueAt(row, 5)));
        selectLopInCombo(str(m.getValueAt(row, 6)));
    }

    /** Chọn đúng lớp trong combo theo mã lớp. */
    private void selectLopInCombo(String maLop) {
        var combo = view.getCboLop();
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getMaLop().equals(maLop)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(-1);
    }

    private void clearForm() {
        view.getTxtMaSv().setText("");
        view.getTxtHoTen().setText("");
        view.getCboGioiTinh().setSelectedIndex(0);
        view.getTxtNgaySinh().setText("");
        view.getTxtEmail().setText("");
        view.getTxtDienThoai().setText("");
        if (view.getCboLop().getItemCount() > 0) {
            view.getCboLop().setSelectedIndex(0);
        }
        view.getTable().clearSelection();
    }

    private String str(Object o) {
        return o == null ? "" : o.toString();
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Chú ý", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(view, title + ":\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
