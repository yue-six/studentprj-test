package com.student.view;

import com.student.util.Constant;
import com.student.util.FileUtil;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
  ClassListPanel 类用于显示和管理班级列表。
  该类继承自 JPanel，并提供了查看、修改和删除班级的功能。
 */
public class ClassListPanel extends JPanel {
    private MainFrame mainFrame; // 主框架引用
    String[] headers = {"序号", "班级名称"}; // 表格列标题
    String[][] data; // 表格数据
    JTable classTable; // 班级表格
    JTextField txtName = new JTextField(); // 用于输入或显示班级名称的文本框
    JButton btnEdit = new JButton("修改"); // 修改按钮
    JButton btnDelete = new JButton("删除"); // 删除按钮

    /**
      构造函数，初始化面板并设置布局和组件。
      @param mainFrame 主框架实例
     */
    public ClassListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        // 设置边框样式
        this.setBorder(new TitledBorder(new EtchedBorder(), "班级列表"));
        // 设置布局为 BorderLayout
        this.setLayout(new BorderLayout());

        // 获取班级目录
        File classDir = new File(Constant.FILE_PATH);
        if (!classDir.exists()) {
            classDir.mkdirs(); // 如果目录不存在，则创建
        }

        // 只获取目录，不获取文件
        File[] classes = classDir.listFiles(file -> file.isDirectory());
        if (classes == null) {
            classes = new File[0];
        }

        // 构建表格数据
        data = new String[classes.length][2];
        for (int i = 0; i < classes.length; i++) {
            data[i][0] = String.valueOf(i + 1); // 序号
            data[i][1] = classes[i].getName(); // 班级名称
        }

        // 创建表格模型和表格
        DefaultTableModel tableModel = new DefaultTableModel(data, headers);
        classTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格单元格不可编辑
            }
        };
        classTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选模式
        JScrollPane scrollPane = new JScrollPane(classTable); // 添加滚动条
        this.add(scrollPane, BorderLayout.CENTER); // 将表格添加到面板中心

        // 构建按钮面板
        JPanel btnPanel = new JPanel();
        btnPanel.add(txtName);
        txtName.setPreferredSize(new Dimension(200, 30)); // 设置文本框大小
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        this.add(btnPanel, BorderLayout.SOUTH); // 将按钮面板添加到面板底部

        // 表格选择事件
        classTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow >= 0) {
                txtName.setText(data[selectedRow][1]); // 显示选中的班级名称
            }
        });

        // 修改班级名称
        btnEdit.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写班级名称", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String oldName = data[selectedRow][1];
            String newName = txtName.getText().trim();
            // 检查新名称是否已存在
            File newDir = new File(Constant.FILE_PATH + newName);
            if (newDir.exists()) {
                JOptionPane.showMessageDialog(this, "班级名称已存在", "", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // 重命名目录
            File oldDir = new File(Constant.FILE_PATH + oldName);
            if (oldDir.renameTo(newDir)) {
                data[selectedRow][1] = newName;
                ((DefaultTableModel) classTable.getModel()).setDataVector(data, headers);
                JOptionPane.showMessageDialog(this, "修改成功", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "修改失败", "", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 删除班级
        btnDelete.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this,
                    "删除班级会把小组、学生和成绩都删除，您确定要删除这个班级？",
                    "", JOptionPane.YES_NO_OPTION) != 0) {
                return;
            }
            String className = data[selectedRow][1];
            FileUtil.deleteClassDirectory(className); // 删除班级目录及其内容
            // 更新表格
            List<String[]> newData = new ArrayList<>();
            int index = 1;
            for (int i = 0; i < data.length; i++) {
                if (i != selectedRow) {
                    newData.add(new String[]{String.valueOf(index++), data[i][1]});
                }
            }
            data = newData.toArray(new String[0][]);
            ((DefaultTableModel) classTable.getModel()).setDataVector(data, headers);
            // 如果删除的是当前选中的班级，清空当前班级
            if (Constant.CLASS_PATH.endsWith(className)) {
                Constant.CLASS_PATH = "";
                mainFrame.setTitle("班级管理系统");
            }
            JOptionPane.showMessageDialog(this, "删除班级成功", "", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // 刷新表格方法
    public void refreshTable() {
        File classDir = new File(Constant.FILE_PATH);
        File[] classes = classDir.listFiles(File::isDirectory);
        if (classes == null) {
            classes = new File[0];
        }
        data = new String[classes.length][2];
        for (int i = 0; i < classes.length; i++) {
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = classes[i].getName();
        }
        ((DefaultTableModel) classTable.getModel()).setDataVector(data, headers);
    }
}
