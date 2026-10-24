package com.qlsv.controller;

import com.qlsv.dao.DiemDAO;
import com.qlsv.dao.MonHocDAO;
import com.qlsv.dao.SinhVienDAO;
import com.qlsv.model.Diem;
import com.qlsv.model.MonHoc;
import com.qlsv.model.SinhVien;
import com.qlsv.view.DiemPanel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller cho màn hình Quản lý Điểm.
 * Nạp danh sách Sinh viên và Môn học vào hai JComboBox (hai khóa ngoại),
 * và thực hiện CRUD trên bảng điểm.
 */
public class DiemController {

    private final DiemPanel view;
    private final DiemDAO dao = new DiemDAO();
    private final SinhVienDAO svDAO = new SinhVienDAO();
    private final MonHocDAO mhDAO = new MonHocDAO();

    public DiemController(DiemPanel view) {
        this.view = view;
        attachEvents();
        loadCombos();
        loadData();
    }

    private void attachEvents() {
        view.getBtnThem().addActionListener(e -> them());
        view.getBtnSua().addActionListener(e -> sua());
        view.getBtnXoa().addActionListener(e -> xoa());
        view.getBtnLamMoi().addActionListener(e -> {
            clearForm();
            loadCombos();
            loadData();
        });
        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
    }

    /** Nạp Sinh viên và Môn học vào combo (hai khóa ngoại). */
    private void loadCombos() {
        try {
            view.getCboSinhVien().removeAllItems();
            for (SinhVien sv : svDAO.findAll()) {
                view.getCboSinhVien().addItem(sv);
            }
            view.getCboMonHoc().removeAllItems();
            for (MonHoc mh : mhDAO.findAll()) {
                view.getCboMonHoc().addItem(mh);
            }
        } catch (SQLException ex) {
            showError("Lỗi khi tải dữ liệu sinh viên / môn học", ex);
        }
    }

    private void loadData() {
        try {
            List<Diem> list = dao.findAll();
            var model = view.getTableModel();
            model.setRowCount(0);
            for (Diem d : list) {
                model.addRow(new Object[]{
                        d.getId(), d.getMaSv(), d.getTenSv(),
                        d.getMaMh(), d.getTenMh(), d.getDiemSo(), d.getHocKy()});
            }
        } catch (SQLException ex) {
            showError("Lỗi khi tải bảng điểm", ex);
        }
    }

    private void them() {
        Diem d = readForm(0);
        if (d == null) {
            return;
        }
        try {
            if (dao.insert(d)) {
                info("Thêm điểm thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            // Lỗi phổ biến: trùng (sinh viên, môn, học kỳ) do ràng buộc UNIQUE
            showError("Lỗi khi thêm điểm (có thể đã tồn tại điểm của sinh viên "
                    + "cho môn này trong học kỳ đó)", ex);
        }
    }

    private void sua() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            warn("Hãy chọn dòng điểm cần sửa trên bảng.");
            return;
        }
        int id = (int) view.getTableModel().getValueAt(row, 0);
        Diem d = readForm(id);
        if (d == null) {
            return;
        }
        try {
            if (dao.update(d)) {
                info("Cập nhật điểm thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi cập nhật điểm", ex);
        }
    }

    private void xoa() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            warn("Hãy chọn dòng điểm cần xóa trên bảng.");
            return;
        }
        int id = (int) view.getTableModel().getValueAt(row, 0);
        int ok = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa dòng điểm này?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (dao.delete(id)) {
                info("Xóa điểm thành công!");
                clearForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Lỗi khi xóa điểm", ex);
        }
    }

    /** Đọc form thành đối tượng Diem với id cho trước. Trả về null nếu không hợp lệ. */
    private Diem readForm(int id) {
        SinhVien sv = (SinhVien) view.getCboSinhVien().getSelectedItem();
        MonHoc mh = (MonHoc) view.getCboMonHoc().getSelectedItem();
        String diemStr = view.getTxtDiemSo().getText().trim();
        String hocKy = view.getTxtHocKy().getText().trim();

        if (sv == null || mh == null) {
            warn("Vui lòng chọn Sinh viên và Môn học!");
            return null;
        }
        double diemSo;
        try {
            diemSo = Double.parseDouble(diemStr.replace(",", "."));
        } catch (NumberFormatException ex) {
            warn("Điểm số phải là một con số (ví dụ 8.5).");
            return null;
        }
        if (diemSo < 0 || diemSo > 10) {
            warn("Điểm phải nằm trong khoảng từ 0 đến 10.");
            return null;
        }

        Diem d = new Diem();
        d.setId(id);
        d.setMaSv(sv.getMaSv());
        d.setMaMh(mh.getMaMh());
        d.setDiemSo(diemSo);
        d.setHocKy(hocKy);
        return d;
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            return;
        }
        var m = view.getTableModel();
        String maSv = str(m.getValueAt(row, 1));
        String maMh = str(m.getValueAt(row, 3));
        selectSinhVien(maSv);
        selectMonHoc(maMh);
        view.getTxtDiemSo().setText(str(m.getValueAt(row, 5)));
        view.getTxtHocKy().setText(str(m.getValueAt(row, 6)));
    }

    private void selectSinhVien(String maSv) {
        var combo = view.getCboSinhVien();
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getMaSv().equals(maSv)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectMonHoc(String maMh) {
        var combo = view.getCboMonHoc();
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getMaMh().equals(maMh)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void clearForm() {
        if (view.getCboSinhVien().getItemCount() > 0) {
            view.getCboSinhVien().setSelectedIndex(0);
        }
        if (view.getCboMonHoc().getItemCount() > 0) {
            view.getCboMonHoc().setSelectedIndex(0);
        }
        view.getTxtDiemSo().setText("");
        view.getTxtHocKy().setText("");
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
