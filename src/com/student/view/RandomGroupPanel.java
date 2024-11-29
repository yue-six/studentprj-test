package com.student.view;

import com.student.entity.Group;
import com.student.entity.Student;
import com.student.util.Constant;
import com.student.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/*
  随机小组点名面板，用于随机选择小组和学生，并进行评分。
 */
public class RandomGroupPanel extends JPanel {
    // UI组件
    private JLabel lbl1 = new JLabel("小组名：");
    private JLabel lbl2 = new JLabel("学生姓名：");
    private JLabel lbl3 = new JLabel("学生照片：");
    private JLabel lblPic = new JLabel("照片");
    private JLabel lbl4 = new JLabel("小组评分");
    private JTextField txtGroup = new JTextField();
    private JTextField txtStudent = new JTextField();
    private JTextField txtScore = new JTextField();
    private JButton btnChooseGroup = new JButton("随机小组");
    private JButton btnChooseStudent = new JButton("随机学生");
    private JButton btnAbsence = new JButton("缺勤");
    private JButton btnLeave = new JButton("请假");
    private JButton btnScore = new JButton("小组评分");
    // 线程，用于随机抽取小组和学生
    Thread threadGroup = null;
    Thread threadStudent = null;

    // 数据列表
    private List<Group> groups;  // 小组列表
    private List<Student> students;  // 学生列表
    // 当前选中的小组和学生
    private Group currentGroup = null;
    private Student currentStudent = null;
    // 随机数生成器
    private Random random = new Random();

    /*
      构造函数，初始化面板。
     */
    public RandomGroupPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "随机小组点名"));
        this.setLayout(null);
        // 添加UI组件
        this.add(lbl1);
        this.add(lbl2);
        this.add(lbl3);
        this.add(txtGroup);
        this.add(txtStudent);
        this.add(lblPic);
        this.add(btnChooseGroup);
        this.add(btnChooseStudent);
        this.add(btnAbsence);
        this.add(btnLeave);
        this.add(lbl4);
        this.add(txtScore);
        this.add(btnScore);

        // 设置UI组件的位置和属性
        lbl1.setBounds(50, 50, 100, 30);
        txtGroup.setBounds(50, 90, 100, 30);
        txtGroup.setEditable(false);
        btnChooseGroup.setBounds(50, 130, 100, 30);

        lbl4.setBounds(50, 190, 100, 30);
        txtScore.setBounds(50, 230, 100, 30);
        btnScore.setBounds(50, 270, 100, 30);

        lbl2.setBounds(220, 50, 100, 30);
        txtStudent.setBounds(220, 90, 130, 30);
        txtStudent.setEditable(false);
        lblPic.setBounds(220, 130, 130, 150);
        btnChooseStudent.setBounds(220, 300, 100, 30);
        btnAbsence.setBounds(220, 340, 60, 30);
        btnLeave.setBounds(290, 340, 60, 30);

        // 加载数据
        groups = FileUtil.loadGroups();
        students = FileUtil.loadStudents();

        // 随机小组按钮事件
        btnChooseGroup.addActionListener(e -> {
            if (groups.isEmpty()) {
                JOptionPane.showMessageDialog(this, "没有可选择的小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (e.getActionCommand().equals("停")) {
                btnChooseGroup.setText("随机小组");
                if (threadGroup != null) {
                    threadGroup.interrupt();
                    threadGroup = null;
                }
            } else {
                btnChooseGroup.setText("停");
                threadGroup = new Thread(() -> {
                    try {
                        while (!Thread.interrupted()) {
                            // 随机选择一个小组
                            int index = random.nextInt(groups.size());
                            currentGroup = groups.get(index);
                            SwingUtilities.invokeLater(() ->
                                    txtGroup.setText(currentGroup.getName())
                            );
                            Thread.sleep(50); // 控制滚动速度
                        }
                    } catch (InterruptedException ex) {
                        // 正常中断，不需要处理
                    }
                });
                threadGroup.start();
            }
        });

        // 随机学生按钮事件
        btnChooseStudent.addActionListener(e -> {
            if (currentGroup == null) {
                JOptionPane.showMessageDialog(this, "请先随机抽取小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 获取当前小组的学生
            List<Student> groupStudents = students.stream()
                    .filter(s -> s.getGroupName().equals(currentGroup.getName()))
                    .collect(Collectors.toList());

            if (groupStudents.isEmpty()) {
                JOptionPane.showMessageDialog(this, "该小组没有学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (e.getActionCommand().equals("停")) {
                btnChooseStudent.setText("随机学生");
                if (threadStudent != null) {
                    threadStudent.interrupt();
                    threadStudent = null;
                }
            } else {
                btnChooseStudent.setText("停");
                threadStudent = new Thread(() -> {
                    try {
                        while (!Thread.interrupted()) {
                            // 随机选择一个学生
                            int index = random.nextInt(groupStudents.size());
                            currentStudent = groupStudents.get(index);
                            SwingUtilities.invokeLater(() ->
                                    txtStudent.setText(currentStudent.getName())
                            );
                            Thread.sleep(50); // 控制滚动速度
                        }
                    } catch (InterruptedException ex) {
                        // 正常中断，不需要处理
                    }
                });
                threadStudent.start();
            }
        });

        // 记录缺勤按钮事件
        btnAbsence.addActionListener(e -> {
            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "请先随机选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 扣除分数并保存
            currentStudent.setScore(currentStudent.getScore() - Constant.ABSENTEEISM_SCORE);
            FileUtil.saveStudents(students);
            JOptionPane.showMessageDialog(this, "已记录缺勤，扣除" + Constant.ABSENTEEISM_SCORE + "分",
                    "", JOptionPane.INFORMATION_MESSAGE);
        });

        // 记录请假按钮事件
        btnLeave.addActionListener(e -> {
            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "请先随机选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 扣除分数并保存
            currentStudent.setScore(currentStudent.getScore() - Constant.LEAVE_SCORE);
            FileUtil.saveStudents(students);
            JOptionPane.showMessageDialog(this, "已记录请假，扣除" + Constant.LEAVE_SCORE + "分",
                    "", JOptionPane.INFORMATION_MESSAGE);
        });

        // 给小组打分按钮事件
        btnScore.addActionListener(e -> {
            if (currentGroup == null) {
                JOptionPane.showMessageDialog(this, "请先抽取小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtScore.getText() == null || txtScore.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写分数", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                int score = Integer.parseInt(txtScore.getText().trim());
                currentGroup.setScore(currentGroup.getScore() + score);
                FileUtil.saveGroups(groups);
                JOptionPane.showMessageDialog(this, "小组评分成功，增加" + score + "分",
                        "", JOptionPane.INFORMATION_MESSAGE);
                txtScore.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的分数",
                        "", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}