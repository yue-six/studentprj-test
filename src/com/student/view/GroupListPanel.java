package com.student.view;

import com.student.entity.Group;
import com.student.util.Constant;
import com.student.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GroupListPanel extends JPanel {
    String[] headers = {"序号", "小组名称", "分数"};
    String[][] data;
    JTable classTable;
    JTextField txtName = new JTextField();
    JTextField txtScore = new JTextField();
    JButton btnEdit = new JButton("修改");
    JButton btnDelete = new JButton("删除");
    private List<Group> groups;

    public GroupListPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "小组列表"));
        this.setLayout(new BorderLayout());

        // 加载小组数据
        groups = FileUtil.loadGroups();
        data = new String[groups.size()][3];
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = group.getName();
            data[i][2] = String.valueOf(group.getScore());
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, headers);
        classTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        classTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(classTable);
        this.add(scrollPane, BorderLayout.CENTER);

        // 构建按钮面板
        JPanel btnPanel = new JPanel();
        btnPanel.add(txtName);
        txtName.setPreferredSize(new Dimension(200, 30));
        btnPanel.add(txtScore);
        txtScore.setPreferredSize(new Dimension(100, 30));
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        this.add(btnPanel, BorderLayout.SOUTH);

        // 表格选择事件
        classTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow >= 0) {
                txtName.setText(data[selectedRow][1]);
                txtScore.setText(data[selectedRow][2]);
            }
        });

        // 修改小组
        btnEdit.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写小组名称", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            try {
                int score = Integer.parseInt(txtScore.getText().trim());

                // 更新小组信息
                String oldName = data[selectedRow][1];
                String newName = txtName.getText().trim();

                // 检查新名称是否重复（排除自身）
                boolean isDuplicate = groups.stream()
                        .anyMatch(g -> !g.getName().equals(oldName) && g.getName().equals(newName));
                if (isDuplicate) {
                    JOptionPane.showMessageDialog(this, "小组名称已存在", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 更新小组对象
                for (Group group : groups) {
                    if (group.getName().equals(oldName)) {
                        group.setName(newName);
                        group.setScore(score);
                        break;
                    }
                }

                // 更新表格和文件
                data[selectedRow][1] = newName;
                data[selectedRow][2] = String.valueOf(score);
                ((DefaultTableModel) classTable.getModel()).setDataVector(data, headers);
                FileUtil.saveGroups(groups);

                JOptionPane.showMessageDialog(this, "修改成功", "", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的分数", "", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 删除小组
        btnDelete.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String groupName = data[selectedRow][1];

            // 检查小组是否有学生
            boolean hasStudents = Constant.students.stream()
                    .anyMatch(s -> s.getGroupName().equals(groupName));
            if (hasStudents) {
                JOptionPane.showMessageDialog(this, "该小组还有学生，不能删除", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (JOptionPane.showConfirmDialog(this, "确定要删除这个小组吗？", "", JOptionPane.YES_NO_OPTION) != 0) {
                return;
            }

            // 从列表中删除小组
            groups.removeIf(group -> group.getName().equals(groupName));

            // 更新表格和文件
            List<String[]> newData = new ArrayList<>();
            int index = 1;
            for (int i = 0; i < data.length; i++) {
                if (i != selectedRow) {
                    newData.add(new String[]{String.valueOf(index++), data[i][1], data[i][2]});
                }
            }
            data = newData.toArray(new String[0][]);
            ((DefaultTableModel) classTable.getModel()).setDataVector(data, headers);
            FileUtil.saveGroups(groups);

            JOptionPane.showMessageDialog(this, "删除小组成功", "", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
