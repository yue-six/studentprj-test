package com.student.view;

import com.student.entity.Group;
import com.student.entity.Student;
import com.student.util.Constant;
import com.student.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/*
 学生列表面板，用于显示和管理学生信息。
 */
public class StudentListPanel extends JPanel {
    // 表格头部标题
    String[] headers = {"学号", "姓名", "小组"};
    // 表格数据
    String[][] data;
    // 表格组件
    JTable studentTable;
    // 文本框和下拉框，用于输入和选择学生信息
    JTextField txtId = new JTextField();
    JTextField txtName = new JTextField();
    JComboBox<String> cmbGroup = new JComboBox<>();
    // 按钮，用于修改和删除学生信息
    JButton btnEdit = new JButton("修改");
    JButton btnDelete = new JButton("删除");

    /*
     构造函数，初始化面板。
     */
    public StudentListPanel() {
        // 设置边框样式
        this.setBorder(new TitledBorder(new EtchedBorder(), "学生列表"));
        // 设置布局管理器为BorderLayout
        this.setLayout(new BorderLayout());

        // 加载学生和小组数据
        List<Student> students = FileUtil.loadStudents();
        List<Group> groups = FileUtil.loadGroups();

        // 构建表格数据
        data = new String[students.size()][3];
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            data[i][0] = student.getId(); // 学号
            data[i][1] = student.getName(); // 姓名
            data[i][2] = student.getGroupName(); // 小组名
        }

        // 创建表格模型并设置表格
        DefaultTableModel tableModel = new DefaultTableModel(data, headers);
        studentTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置表格不可编辑
            }
        };
        // 设置表格选择模式为单选
        studentTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 将表格放入滚动面板中
        JScrollPane scrollPane = new JScrollPane(studentTable);
        this.add(scrollPane, BorderLayout.CENTER);

        // 构建按钮面板
        JPanel btnPanel = new JPanel();
        btnPanel.add(txtId);
        txtId.setPreferredSize(new Dimension(100, 30)); // 设置文本框大小
        btnPanel.add(txtName);
        txtName.setPreferredSize(new Dimension(200, 30)); // 设置文本框大小
        btnPanel.add(cmbGroup);
        cmbGroup.setPreferredSize(new Dimension(100, 30)); // 设置下拉框大小

        // 添加小组到下拉框
        cmbGroup.addItem("请选择小组");
        for (Group group : groups) {
            cmbGroup.addItem(group.getName());
        }

        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        this.add(btnPanel, BorderLayout.SOUTH); // 将按钮面板添加到底部

        // 表格选择事件
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                txtId.setText(data[selectedRow][0]); // 显示选中行的学号
                txtName.setText(data[selectedRow][1]); // 显示选中行的姓名
                cmbGroup.setSelectedItem(data[selectedRow][2]); // 显示选中行的小组名
            }
        });

        // 修改学生信息
        btnEdit.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtId.getText() == null || txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写学号", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写姓名", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (cmbGroup.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "请选择小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 更新学生信息
            String oldId = data[selectedRow][0];
            for (Student student : students) {
                if (student.getId().equals(oldId)) {
                    student.setId(txtId.getText().trim());
                    student.setName(txtName.getText().trim());
                    student.setGroupName(cmbGroup.getSelectedItem().toString());
                    break;
                }
            }

            // 更新表格和文件
            data[selectedRow][0] = txtId.getText().trim();
            data[selectedRow][1] = txtName.getText().trim();
            data[selectedRow][2] = cmbGroup.getSelectedItem().toString();
            ((DefaultTableModel) studentTable.getModel()).setDataVector(data, headers);
            FileUtil.saveStudents(students);

            JOptionPane.showMessageDialog(this, "修改成功", "", JOptionPane.INFORMATION_MESSAGE);
        });

        // 删除学生
        btnDelete.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "删除学生会删除他的考勤、成绩等，确认删除？", "", JOptionPane.YES_NO_OPTION) != 0) {
                return;
            }

            // 从列表中删除学生
            String studentId = data[selectedRow][0];
            students.removeIf(student -> student.getId().equals(studentId));

            // 更新表格和文件
            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            model.removeRow(selectedRow);
            FileUtil.saveStudents(students);

            JOptionPane.showMessageDialog(this, "删除学生成功", "", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}