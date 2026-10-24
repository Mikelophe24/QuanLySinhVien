package com.qlsv.controller;

import com.qlsv.dao.LopDAO;
import com.qlsv.model.Lop;
import com.qlsv.view.LopPanel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller cho màn hình Quản lý Lớp.
 * Gắn sự kiện cho các nút Thêm/Sửa/Xóa/Làm mới và chọn dòng trên bảng,
 * gọi {@link LopDAO} để thao tác CSDL.
 */
public class LopController {

    private final LopPanel view;
    private final LopDAO dao = new LopDAO();

    public LopController(LopPanel view) {
        this.view = view;
        attachEvents();
        loadData();
    }

    private void attachEvents() {
        view.getBtnThem().addActionListener(e -> them());
        view.getBtnSua().addActionListener(e -> sua());
        view.getBtnXoa().addActionListener(e -> xoa());
        view.getBtnLamMoi().addActionListener(e -> {
            clearForm();
            loadData();
        });

        // Khi chọn một dòng trên bảng thì đổ dữ liệu lên form
        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
    }

    /** Đọc toàn bộ danh sách lớp và nạp vào JTable. */
    private void loadData() {
        try {
            List<Lop> list = dao.findAll();
            var model = view.getTableModel();
            model.setRowCount(0);
            for (Lop l : list) {
                model.addRow(new Object[]{
                        l.getMaLop(), l.getTenLop(), l.getKhoa(), l.getNienKhoa()});
            }
        } catch (SQLException ex) {
            showError("Lỗi khi tải danh sách lớp", ex);
        }
    }

    private void them() {
        Lop lop = readForm();
        if (lop == null) {
            return;
        }
        try {
            if (dao.exists(lop.getMaLop())) {
                warn("Mã lớp '" + lop.getMaLop() + "' đã tồn tại!");
                return;
            }
            if (dao.insert(lop)) {
                info("Thêm lớp thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi thêm lớp", ex);
        }
    }

    private void sua() {
        Lop lop = readForm();
        if (lop == null) {
            return;
        }
        try {
            if (!dao.exists(lop.getMaLop())) {
                warn("Không tìm thấy lớp để sửa. Hãy chọn một dòng trên bảng.");
                return;
            }
            if (dao.update(lop)) {
                info("Cập nhật lớp thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi cập nhật lớp", ex);
        }
    }

    private void xoa() {
        String maLop = view.getTxtMaLop().getText().trim();
        if (maLop.isEmpty()) {
            warn("Hãy chọn lớp cần xóa trên bảng.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa lớp '" + maLop + "'?\n"
                + "(Sinh viên thuộc lớp này sẽ bị bỏ trống mã lớp.)",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (dao.delete(maLop)) {
                info("Xóa lớp thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi xóa lớp", ex);
        }
    }

    /** Đọc dữ liệu từ form, kiểm tra hợp lệ. Trả về null nếu không hợp lệ. */
    private Lop readForm() {
        String maLop = view.getTxtMaLop().getText().trim();
        String tenLop = view.getTxtTenLop().getText().trim();
        String khoa = view.getTxtKhoa().getText().trim();
        String nienKhoa = view.getTxtNienKhoa().getText().trim();

        if (maLop.isEmpty() || tenLop.isEmpty() || khoa.isEmpty()) {
            warn("Vui lòng nhập Mã lớp, Tên lớp và Khoa!");
            return null;
        }
        return new Lop(maLop, tenLop, khoa, nienKhoa);
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            return;
        }
        var m = view.getTableModel();
        view.getTxtMaLop().setText(str(m.getValueAt(row, 0)));
        view.getTxtTenLop().setText(str(m.getValueAt(row, 1)));
        view.getTxtKhoa().setText(str(m.getValueAt(row, 2)));
        view.getTxtNienKhoa().setText(str(m.getValueAt(row, 3)));
    }

    private void clearForm() {
        view.getTxtMaLop().setText("");
        view.getTxtTenLop().setText("");
        view.getTxtKhoa().setText("");
        view.getTxtNienKhoa().setText("");
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
