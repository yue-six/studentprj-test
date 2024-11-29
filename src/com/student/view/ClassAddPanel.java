package com.student.view;

import com.student.util.Constant;
import com.student.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

/*
 ClassAddPanel 类是用于新增班级的面板。
 该面板包含一个文本框用于输入班级名称，以及一个确认按钮来创建新的班级目录。
 */
public class ClassAddPanel extends JPanel {
    // 主框架引用
    private MainFrame mainFrame;

    /*
      构造函数，初始化面板并设置布局和组件。
      @param mainFrame 主框架实例
     */
    public ClassAddPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        // 设置绝对布局
        this.setLayout(null);
        // 设置边框样式
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增班级"));

        // 创建标签、文本框和按钮
        JLabel lblName = new JLabel("班级名称：");
        JTextField txtName = new JTextField();
        JButton btnName = new JButton("确认");

        // 将组件添加到面板
        this.add(lblName);
        this.add(txtName);
        this.add(btnName);

        // 设置组件的位置和大小
        lblName.setBounds(200, 80, 100, 30);
        txtName.setBounds(200, 130, 200, 30);
        btnName.setBounds(200, 180, 100, 30);

        // 为确认按钮添加动作监听器
        btnName.addActionListener(e -> {
            // 检查文本框是否为空
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写班级名称", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                // 获取并修剪文本框中的班级名称
                String className = txtName.getText().trim();
                File classDir = new File(Constant.FILE_PATH + className);

                // 检查班级名称是否已存在
                if (classDir.exists()) {
                    JOptionPane.showMessageDialog(this, "班级名称已存在", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 创建班级目录
                try {
                    FileUtil.createClassDirectory(className);
                    System.out.println("正在创建班级目录: " + classDir.getAbsolutePath());

                    // 检查目录是否成功创建
                    if (classDir.exists()) {
                        JOptionPane.showMessageDialog(this, "新增班级成功", "", JOptionPane.INFORMATION_MESSAGE);
                        txtName.setText(""); // 清空输入框
                        mainFrame.refreshClassList(); // 刷新主框架中的班级列表
                    } else {
                        throw new IOException("目录创建失败");
                    }
                } catch (IOException ex) {
                    System.err.println("创建班级失败: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "创建班级失败：" + ex.getMessage(),
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                System.err.println("未预期的错误: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "创建班级时发生错误：" + ex.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
