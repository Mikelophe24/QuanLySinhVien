package com.qlsv.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Giao diện Quản lý Môn học. Đầy đủ CRUD. */
public class MonHocPanel extends JPanel {

    private final JTextField txtMaMh = new JTextField(12);
    private final JTextField txtTenMh = new JTextField(18);
    private final JSpinner spnSoTinChi =
            new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));

    private final JButton btnThem = new JButton("Thêm");
    private final JButton btnSua = new JButton("Sửa");
    private final JButton btnXoa = new JButton("Xóa");
    private final JButton btnLamMoi = new JButton("Làm mới");

    private final DefaultTableModel tableModel;
    private final JTable table;

    public MonHocPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("QUẢN LÝ MÔN HỌC");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(33, 102, 172));
        add(title, BorderLayout.NORTH);

        add(buildForm(), BorderLayout.WEST);

        tableModel = new DefaultTableModel(
                new String[]{"Mã môn", "Tên môn", "Số tín chỉ"}, 0) {
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

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin môn học"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0; form.add(new JLabel("Mã môn:"), gc);
        gc.gridx = 1;               form.add(txtMaMh, gc);
        gc.gridx = 0; gc.gridy = 1; form.add(new JLabel("Tên môn:"), gc);
        gc.gridx = 1;               form.add(txtTenMh, gc);
        gc.gridx = 0; gc.gridy = 2; form.add(new JLabel("Số tín chỉ:"), gc);
        gc.gridx = 1;               form.add(spnSoTinChi, gc);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnLamMoi);
        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2;
        form.add(buttons, gc);
        return form;
    }

    public JTextField getTxtMaMh() { return txtMaMh; }
    public JTextField getTxtTenMh() { return txtTenMh; }
    public JSpinner getSpnSoTinChi() { return spnSoTinChi; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
}
