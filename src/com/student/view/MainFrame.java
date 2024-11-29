package com.student.view;

import com.student.util.Constant;
import com.student.util.FileUtil;
import com.student.entity.Group;
import com.student.entity.Student;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

/**
 * 主框架类，继承自JFrame，用于创建和管理班级管理系统的GUI。
 */
public class MainFrame extends JFrame {

    /**
     * 构造函数，初始化主窗口。
     */
    public MainFrame() {
        this.getContentPane().setLayout(new BorderLayout());
        initMenus();

        // 初始化中心面板为切换班级面板
        this.getContentPane().add(new ChangeClassPanel(this), BorderLayout.CENTER);

        // 设置窗口标题、大小、位置和可见性
        this.setTitle("班级管理系统");
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * 初始化菜单栏和菜单项。
     */
    public void initMenus() {
        // 创建菜单栏和菜单项
        JMenuBar mainMenu = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem changeClassMenuItem = new JMenuItem("切换当前班");
        JMenuItem exportScoreMenuItem = new JMenuItem("导出当前班成绩");
        JMenuItem exitMenuItem = new JMenuItem("退出");

        JMenu classMenu = new JMenu("班级管理");
        JMenuItem addClassMenuItem = new JMenuItem("新增班级");
        JMenuItem classListMenuItem = new JMenuItem("班级列表");

        JMenu groupMenu = new JMenu("小组管理");
        JMenuItem addGroupMenuItem = new JMenuItem("新增小组");
        JMenuItem groupListMenuItem = new JMenuItem("小组列表");

        JMenu studentMenu = new JMenu("学生管理");
        JMenuItem addStudentMenuItem = new JMenuItem("新增学生");
        JMenuItem studentListMenuItem = new JMenuItem("学生列表");

        JMenu onClassMenu = new JMenu("课堂管理");
        JMenuItem randomGroupMenuItem = new JMenuItem("随机小组");
        JMenuItem randomStudentMenuItem = new JMenuItem("随机学生");

        // 将菜单添加到窗口北边（顶部）
        this.getContentPane().add(mainMenu, BorderLayout.NORTH);
        mainMenu.add(fileMenu);
        mainMenu.add(classMenu);
        mainMenu.add(groupMenu);
        mainMenu.add(studentMenu);
        mainMenu.add(onClassMenu);
        fileMenu.add(changeClassMenuItem);
        fileMenu.add(exportScoreMenuItem);
        fileMenu.add(exitMenuItem);
        classMenu.add(addClassMenuItem);
        classMenu.add(classListMenuItem);
        groupMenu.add(addGroupMenuItem);
        groupMenu.add(groupListMenuItem);
        studentMenu.add(addStudentMenuItem);
        studentMenu.add(studentListMenuItem);
        onClassMenu.add(randomGroupMenuItem);
        onClassMenu.add(randomStudentMenuItem);

        // 添加菜单事件
        // 切换班级
        changeClassMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            ChangeClassPanel changeClassPanel = new ChangeClassPanel(this);
            this.getContentPane().add(changeClassPanel, BorderLayout.CENTER);
            this.getContentPane().validate();
            this.getContentPane().repaint();
        });
        // 导出成绩
        exportScoreMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                try {
                    // 创建成绩文件
                    String scoreFilePath = Constant.CLASS_PATH + "/scores.txt";
                    File scoreFile = new File(scoreFilePath);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(scoreFile));

                    // 写入小组成绩
                    writer.write("=== 小组成绩 ===\n");
                    List<Group> groups = FileUtil.loadGroups();
                    for (Group group : groups) {
                        writer.write(String.format("小组：%s，得分：%d\n",
                                group.getName(), group.getScore()));
                    }
                    writer.write("\n");

                    // 写入学生成绩
                    writer.write("=== 学生成绩 ===\n");
                    List<Student> students = FileUtil.loadStudents();
                    for (Student student : students) {
                        writer.write(String.format("学号：%s，姓名：%s，小组：%s，得分：%d\n",
                                student.getId(), student.getName(),
                                student.getGroupName(), student.getScore()));
                    }

                    writer.close();
                    JOptionPane.showMessageDialog(this,
                            "成绩已导出到：" + scoreFilePath,
                            "导出成功",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "导出失败：" + ex.getMessage(),
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // 退出程序
        exitMenuItem.addActionListener(e -> System.exit(0));
        // 新增班级
        addClassMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            ClassAddPanel classAddPanel = new ClassAddPanel(this);
            this.getContentPane().add(classAddPanel, BorderLayout.CENTER);
            this.getContentPane().validate();
        });
        // 班级列表
        classListMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            ClassListPanel classListPanel = new ClassListPanel(this);
            this.getContentPane().add(classListPanel, BorderLayout.CENTER);
            this.getContentPane().validate();
        });
        // 新增小组
        addGroupMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.getContentPane().removeAll();
                initMenus();
                this.getContentPane().add(new GroupAddPanel(), BorderLayout.CENTER);
                this.getContentPane().validate();
                this.getContentPane().repaint();
            }
        });
        // 小组列表
        groupListMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.getContentPane().removeAll();
                initMenus();
                this.getContentPane().add(new GroupListPanel(), BorderLayout.CENTER);
                this.getContentPane().repaint();
                this.getContentPane().validate();
            }
        });
        // 新增学生
        addStudentMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.getContentPane().removeAll();
                initMenus();
                this.getContentPane().add(new StudentAddPanel(), BorderLayout.CENTER);
                this.getContentPane().repaint();
                this.getContentPane().validate();
            }
        });
        // 学生列表
        studentListMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.getContentPane().removeAll();
                initMenus();
                this.getContentPane().add(new StudentListPanel(), BorderLayout.CENTER);
                this.getContentPane().repaint();
                this.getContentPane().validate();
            }
        });
        // 随机抽取小组
        randomGroupMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.getContentPane().removeAll();
                initMenus();
                this.getContentPane().add(new RandomGroupPanel(), BorderLayout.CENTER);
                this.getContentPane().repaint();
                this.getContentPane().validate();
            }
        });
        // 随机抽取学生
        randomStudentMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.getContentPane().removeAll();
                initMenus();
                this.getContentPane().add(new RandomStudentPanel(), BorderLayout.CENTER);
                this.getContentPane().repaint();
                this.getContentPane().validate();
            }
        });
    }

    /**
     * 刷新班级列表的方法。
     */
    public void refreshClassList() {
        SwingUtilities.invokeLater(() -> {
            this.getContentPane().removeAll();
            initMenus();
            ClassListPanel classListPanel = new ClassListPanel(this);
            this.getContentPane().add(classListPanel, BorderLayout.CENTER);
            this.getContentPane().validate();
            this.getContentPane().repaint();
        });
    }
}