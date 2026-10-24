package com.qlsv.view;

import com.qlsv.model.Lop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * GIAO DIỆN 4: Quản lý Sinh viên (Thực thể 2 - Chi tiết). Đầy đủ CRUD.
 * Có {@link JComboBox} chọn Lớp để liên kết khóa ngoại tới bảng {@code lop}.
 */
public class SinhVienPanel extends JPanel {

    private final JTextField txtMaSv = new JTextField(12);
    private final JTextField txtHoTen = new JTextField(18);
    private final JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
    private final JTextField txtNgaySinh = new JTextField(12); // yyyy-MM-dd
    private final JTextField txtEmail = new JTextField(18);
    private final JTextField txtDienThoai = new JTextField(14);
    private final JComboBox<Lop> cboLop = new JComboBox<>();     // liên kết khóa ngoại

    private final JTextField txtTimKiem = new JTextField(16);
    private final JButton btnTim = new JButton("Tìm theo tên");

    private final JButton btnThem = new JButton("Thêm");
    private final JButton btnSua = new JButton("Sửa");
    private final JButton btnXoa = new JButton("Xóa");
    private final JButton btnLamMoi = new JButton("Làm mới");

    private final DefaultTableModel tableModel;
    private final JTable table;

    public SinhVienPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTitle(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.WEST);

        tableModel = new DefaultTableModel(
                new String[]{"Mã SV", "Họ tên", "Giới tính", "Ngày sinh",
                        "Email", "Điện thoại", "Mã lớp", "Tên lớp"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JPanel buildTitle() {
        JLabel title = new JLabel("QUẢN LÝ SINH VIÊN", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(33, 102, 172));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        searchBar.add(new JLabel("Tìm nhanh:"));
        searchBar.add(txtTimKiem);
        searchBar.add(btnTim);

        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.WEST);
        top.add(searchBar, BorderLayout.EAST);
        return top;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 6, 5, 6);
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addRow(form, gc, row++, "Mã SV:", txtMaSv);
        addRow(form, gc, row++, "Họ tên:", txtHoTen);
        addRow(form, gc, row++, "Giới tính:", cboGioiTinh);
        addRow(form, gc, row++, "Ngày sinh:", txtNgaySinh);
        addRow(form, gc, row++, "Email:", txtEmail);
        addRow(form, gc, row++, "Điện thoại:", txtDienThoai);
        addRow(form, gc, row++, "Lớp:", cboLop);

        // Gợi ý định dạng ngày
        gc.gridx = 1; gc.gridy = row++;
        JLabel hint = new JLabel("<html><i>Ngày sinh dạng: yyyy-MM-dd</i></html>");
        hint.setForeground(Color.GRAY);
        form.add(hint, gc);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnLamMoi);
        gc.gridx = 0; gc.gridy = row; gc.gridwidth = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        form.add(buttons, gc);

        return form;
    }

    private void addRow(JPanel form, GridBagConstraints gc, int row, String label, JComponent field) {
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 0; gc.gridy = row;
        form.add(new JLabel(label), gc);
        gc.gridx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, gc);
    }

    // ----- Getter cho controller -----
    public JTextField getTxtMaSv() { return txtMaSv; }
    public JTextField getTxtHoTen() { return txtHoTen; }
    public JComboBox<String> getCboGioiTinh() { return cboGioiTinh; }
    public JTextField getTxtNgaySinh() { return txtNgaySinh; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtDienThoai() { return txtDienThoai; }
    public JComboBox<Lop> getCboLop() { return cboLop; }
    public JTextField getTxtTimKiem() { return txtTimKiem; }
    public JButton getBtnTim() { return btnTim; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
}
