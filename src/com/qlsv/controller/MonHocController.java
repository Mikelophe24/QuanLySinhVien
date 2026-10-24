package com.qlsv.controller;

import com.qlsv.dao.MonHocDAO;
import com.qlsv.model.MonHoc;
import com.qlsv.view.MonHocPanel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/** Controller cho màn hình Quản lý Môn học. */
public class MonHocController {

    private final MonHocPanel view;
    private final MonHocDAO dao = new MonHocDAO();

    public MonHocController(MonHocPanel view) {
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
        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
    }

    private void loadData() {
        try {
            List<MonHoc> list = dao.findAll();
            var model = view.getTableModel();
            model.setRowCount(0);
            for (MonHoc mh : list) {
                model.addRow(new Object[]{mh.getMaMh(), mh.getTenMh(), mh.getSoTinChi()});
            }
        } catch (SQLException ex) {
            showError("Lỗi khi tải danh sách môn học", ex);
        }
    }

    private void them() {
        MonHoc mh = readForm();
        if (mh == null) {
            return;
        }
        try {
            if (dao.exists(mh.getMaMh())) {
                warn("Mã môn '" + mh.getMaMh() + "' đã tồn tại!");
                return;
            }
            if (dao.insert(mh)) {
                info("Thêm môn học thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi thêm môn học", ex);
        }
    }

    private void sua() {
        MonHoc mh = readForm();
        if (mh == null) {
            return;
        }
        try {
            if (!dao.exists(mh.getMaMh())) {
                warn("Không tìm thấy môn học để sửa. Hãy chọn một dòng trên bảng.");
                return;
            }
            if (dao.update(mh)) {
                info("Cập nhật môn học thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi cập nhật môn học", ex);
        }
    }

    private void xoa() {
        String maMh = view.getTxtMaMh().getText().trim();
        if (maMh.isEmpty()) {
            warn("Hãy chọn môn học cần xóa trên bảng.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa môn '" + maMh + "'?\n"
                + "(Điểm liên quan tới môn này cũng sẽ bị xóa theo.)",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (dao.delete(maMh)) {
                info("Xóa môn học thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi xóa môn học", ex);
        }
    }

    private MonHoc readForm() {
        String maMh = view.getTxtMaMh().getText().trim();
        String tenMh = view.getTxtTenMh().getText().trim();
        int soTinChi = (int) view.getSpnSoTinChi().getValue();
        if (maMh.isEmpty() || tenMh.isEmpty()) {
            warn("Vui lòng nhập Mã môn và Tên môn!");
            return null;
        }
        return new MonHoc(maMh, tenMh, soTinChi);
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            return;
        }
        var m = view.getTableModel();
        view.getTxtMaMh().setText(str(m.getValueAt(row, 0)));
        view.getTxtTenMh().setText(str(m.getValueAt(row, 1)));
        try {
            view.getSpnSoTinChi().setValue(Integer.parseInt(str(m.getValueAt(row, 2))));
        } catch (NumberFormatException ignore) {
            view.getSpnSoTinChi().setValue(3);
        }
    }

    private void clearForm() {
        view.getTxtMaMh().setText("");
        view.getTxtTenMh().setText("");
        view.getSpnSoTinChi().setValue(3);
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
