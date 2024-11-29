package com.student.view;

import com.student.entity.Student;
import com.student.util.Constant;
import com.student.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.Random;

/*
  随机学生点名面板，用于随机选择学生，并进行缺勤、请假和答题的记录。
 */
public class RandomStudentPanel extends JPanel {
    // UI组件
    private JLabel lbl2 = new JLabel("学生姓名：");
    private JLabel lbl3 = new JLabel("学生照片：");
    private JLabel lblPic = new JLabel("照片");
    private JTextField txtStudent = new JTextField();
    private JButton btnChooseStudent = new JButton("随机学生");
    private JButton btnAbsence = new JButton("缺勤");
    private JButton btnLeave = new JButton("请假");
    private JButton btnAnswer = new JButton("答题");
    // 线程，用于随机抽取学生
    Thread threadStudent = null;
    private Student currentStudent = null; // 当前选中的学生
    private List<Student> students; // 学生列表
    private Random random = new Random();
    private boolean isSelecting = false; // 是否正在随机选择中

    /**
     * 构造函数，初始化面板。
     */
    public RandomStudentPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "随机学生点名"));
        this.setLayout(null);
        // 添加UI组件
        this.add(lbl2);
        this.add(lbl3);
        this.add(txtStudent);
        this.add(lblPic);
        this.add(btnChooseStudent);
        this.add(btnAbsence);
        this.add(btnLeave);
        this.add(btnAnswer);

        // 设置UI组件的位置和属性
        lbl2.setBounds(160, 50, 100, 30);
        txtStudent.setBounds(160, 90, 130, 30);
        txtStudent.setEditable(false);
        lblPic.setBounds(160, 130, 130, 150);
        btnChooseStudent.setBounds(160, 300, 130, 30);
        btnAbsence.setBounds(160, 340, 60, 30);
        btnLeave.setBounds(230, 340, 60, 30);
        btnAnswer.setBounds(300, 340, 60, 30);

        // 初始化时禁用操作按钮
        btnAbsence.setEnabled(false);
        btnLeave.setEnabled(false);
        btnAnswer.setEnabled(false);

        // 加载学生列表
        students = FileUtil.loadStudents();

        // 随机选择学生按钮事件
        btnChooseStudent.addActionListener(e -> {
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this, "没有可选择的学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (isSelecting) {
                // 停止选择
                isSelecting = false;
                btnChooseStudent.setText("随机学生");
                if (threadStudent != null) {
                    threadStudent.interrupt();
                    threadStudent = null;
                }
                // 启用操作按钮
                btnAbsence.setEnabled(true);
                btnLeave.setEnabled(true);
                btnAnswer.setEnabled(true);
            } else {
                // 开始选择
                isSelecting = true;
                btnChooseStudent.setText("停");
                // 禁用操作按钮
                btnAbsence.setEnabled(false);
                btnLeave.setEnabled(false);
                btnAnswer.setEnabled(false);

                threadStudent = new Thread(() -> {
                    try {
                        while (!Thread.interrupted()) {
                            // 随机选择一个学生
                            int index = random.nextInt(students.size());
                            currentStudent = students.get(index);
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

        // 记录答题按钮事件
        btnAnswer.addActionListener(e -> {
            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "请先随机选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 增加分数并保存
            currentStudent.setScore(currentStudent.getScore() + Constant.ANSWER_QUESTION);
            FileUtil.saveStudents(students);
            JOptionPane.showMessageDialog(this, "回答正确，加" + Constant.ANSWER_QUESTION + "分",
                    "", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}