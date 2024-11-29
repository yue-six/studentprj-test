package com.student.view;

import com.student.entity.Group;
import com.student.entity.Student;
import com.student.util.Constant;
import com.student.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.util.List;

/*
  学生添加面板，用于新增学生信息。
 */
public class StudentAddPanel extends JPanel {
    /*
     构造函数，初始化面板。
     */
    public StudentAddPanel() {
        this.setLayout(null); // 设置布局管理器为null，使用绝对定位
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增学生")); // 设置边框

        // 创建UI组件
        JLabel lblId = new JLabel("学号：");
        JTextField txtId = new JTextField();
        JLabel lblName = new JLabel("姓名：");
        JTextField txtName = new JTextField();
        JLabel lblGroup = new JLabel("小组:");
        JComboBox<String> cmbGroup = new JComboBox<>();
        JButton btnName = new JButton("确认");

        // 将组件添加到面板
        this.add(lblId);
        this.add(txtId);
        this.add(lblName);
        this.add(txtName);
        this.add(lblGroup);
        this.add(cmbGroup);
        this.add(btnName);

        // 设置组件的位置和大小
        lblId.setBounds(200, 60, 100, 30);
        txtId.setBounds(200, 100, 100, 30);
        lblName.setBounds(200, 140, 100, 30);
        txtName.setBounds(200, 180, 200, 30);
        lblGroup.setBounds(200, 220, 100, 30);
        cmbGroup.setBounds(200, 260, 100, 30);
        btnName.setBounds(200, 300, 100, 30);

        // 加载小组列表并填充到下拉框
        List<Group> groups = FileUtil.loadGroups();
        cmbGroup.addItem("请选择小组");
        for (Group group : groups) {
            cmbGroup.addItem(group.getName());
        }

        // 确认按钮事件
        btnName.addActionListener(e -> {
            // 检查输入是否完整
            if (txtId.getText() == null || txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写学号", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写学生姓名", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (cmbGroup.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "请选择小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 加载现有学生列表
            List<Student> students = FileUtil.loadStudents();

            // 检查学号是否重复
            String studentId = txtId.getText().trim();
            boolean isDuplicate = students.stream()
                    .anyMatch(student -> student.getId().equals(studentId));
            if (isDuplicate) {
                JOptionPane.showMessageDialog(this, "学号已存在", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 创建新学生并添加到列表
            Student newStudent = new Student(
                    studentId,
                    txtName.getText().trim(),
                    cmbGroup.getSelectedItem().toString()
            );
            students.add(newStudent);

            // 保存到文件
            try {
                FileUtil.saveStudents(students);
                JOptionPane.showMessageDialog(this, "新增学生成功", "", JOptionPane.INFORMATION_MESSAGE);

                // 清空输入框
                txtId.setText("");
                txtName.setText("");
                cmbGroup.setSelectedIndex(0);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "保存学生信息失败：" + ex.getMessage(),
                        "", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}