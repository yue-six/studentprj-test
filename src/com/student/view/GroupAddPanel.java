package com.student.view;

import com.student.entity.Group;
import com.student.util.Constant;
import com.student.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.util.List;

/**
 * 这个类是用于添加新小组的面板，继承自JPanel。
 */
public class GroupAddPanel extends JPanel {
    /**
     * 构造函数，初始化面板。
     */
    public GroupAddPanel() {
        // 设置布局管理器为null，以便使用绝对定位
        this.setLayout(null);
        // 设置边框样式
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增小组"));

        // 创建标签、文本框和按钮用于输入和确认小组名称
        JLabel lblName = new JLabel("小组名称：");
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

        // 为确认按钮添加事件监听器
        btnName.addActionListener(e -> {
            // 检查文本框是否为空
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写小组名称", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 获取输入的小组名称并去除前后空格
            String groupName = txtName.getText().trim();

            // 加载现有的小组列表
            List<Group> groups = FileUtil.loadGroups();

            // 检查小组名称是否重复
            boolean isDuplicate = groups.stream()
                    .anyMatch(group -> group.getName().equals(groupName));
            if (isDuplicate) {
                JOptionPane.showMessageDialog(this, "小组名称已存在", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 创建新小组并添加到列表
            Group newGroup = new Group(groupName);
            groups.add(newGroup);

            // 保存到文件
            try {
                FileUtil.saveGroups(groups);
                JOptionPane.showMessageDialog(this, "新增小组成功", "", JOptionPane.INFORMATION_MESSAGE);
                txtName.setText(""); // 清空输入框
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "保存小组信息失败：" + ex.getMessage(),
                        "", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}