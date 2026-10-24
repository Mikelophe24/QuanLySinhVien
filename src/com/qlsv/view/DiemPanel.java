package com.qlsv.view;

import com.qlsv.model.MonHoc;
import com.qlsv.model.SinhVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Giao diện Quản lý Điểm. Đầy đủ CRUD.
 * Có hai {@link JComboBox} liên kết hai khóa ngoại: tới Sinh viên và Môn học.
 */
public class DiemPanel extends JPanel {

    private final JComboBox<SinhVien> cboSinhVien = new JComboBox<>();
    private final JComboBox<MonHoc> cboMonHoc = new JComboBox<>();
    private final JTextField txtDiemSo = new JTextField(8);
    private final JTextField txtHocKy = new JTextField(12);

    private final JButton btnThem = new JButton("Thêm");
    private final JButton btnSua = new JButton("Sửa");
    private final JButton btnXoa = new JButton("Xóa");
    private final JButton btnLamMoi = new JButton("Làm mới");

    private final DefaultTableModel tableModel;
    private final JTable table;

    public DiemPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("QUẢN LÝ ĐIỂM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(33, 102, 172));
        add(title, BorderLayout.NORTH);

        // Hiển thị sinh viên dạng "maSv - hoTen" trong combo
        cboSinhVien.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, value, index, sel, focus);
                if (value instanceof SinhVien sv) {
                    setText(sv.getMaSv() + " - " + sv.getHoTen());
                }
                return this;
            }
        });

        add(buildForm(), BorderLayout.WEST);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Mã SV", "Họ tên", "Mã môn", "Tên môn", "Điểm", "Học kỳ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        // Ẩn cột ID (vẫn giữ dữ liệu để controller dùng)
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin điểm"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0; form.add(new JLabel("Sinh viên:"), gc);
        gc.gridx = 1;               form.add(cboSinhVien, gc);
        gc.gridx = 0; gc.gridy = 1; form.add(new JLabel("Môn học:"), gc);
        gc.gridx = 1;               form.add(cboMonHoc, gc);
        gc.gridx = 0; gc.gridy = 2; form.add(new JLabel("Điểm số:"), gc);
        gc.gridx = 1;               form.add(txtDiemSo, gc);
        gc.gridx = 0; gc.gridy = 3; form.add(new JLabel("Học kỳ:"), gc);
        gc.gridx = 1;               form.add(txtHocKy, gc);

        gc.gridx = 1; gc.gridy = 4;
        JLabel hint = new JLabel("<html><i>Điểm từ 0 đến 10</i></html>");
        hint.setForeground(Color.GRAY);
        form.add(hint, gc);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnLamMoi);
        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 2;
        form.add(buttons, gc);
        return form;
    }

    public JComboBox<SinhVien> getCboSinhVien() { return cboSinhVien; }
    public JComboBox<MonHoc> getCboMonHoc() { return cboMonHoc; }
    public JTextField getTxtDiemSo() { return txtDiemSo; }
    public JTextField getTxtHocKy() { return txtHocKy; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
}
