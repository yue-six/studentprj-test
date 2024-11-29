package com.student.view;

import com.student.util.Constant;
import com.student.util.FileUtil;
import com.student.entity.Group;
import com.student.entity.Student;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/*
这个类是用于更改班级的面板，继承自JScrollPane。
 */
public class ChangeClassPanel extends JScrollPane {
    // 用于显示信息的标签
    JLabel infoLbl = new JLabel();

    /*
    构造函数，接收一个MainFrame对象作为参数。
    @param mainFrame 主窗口框架
     */
    public ChangeClassPanel(MainFrame mainFrame) {
        // 设置边框样式
        this.setBorder(new TitledBorder(new EtchedBorder(), "选择班级"));
        int x = 160, y = 100;
        this.setLayout(null);

        // 读取目录获取班级
        File classDir = new File(Constant.FILE_PATH);
        // 如果目录不存在，则创建
        if (!classDir.exists()) {
            classDir.mkdirs();
        }
        // 获取目录下的所有文件（文件夹）
        File[] files = classDir.listFiles(File::isDirectory);

        // 如果没有文件或文件夹
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(this, "请先创建班级", "", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // 创建按钮组，用于单选按钮
            ButtonGroup btnGroup = new ButtonGroup();
            for (File file : files) {
                if (file.isDirectory()) {
                    // 创建单选按钮并添加到按钮组
                    JRadioButton classRadio = new JRadioButton(file.getName());
                    btnGroup.add(classRadio);
                    this.add(classRadio);
                    classRadio.setBounds(x, y, 200, 30);
                    y += 40;
                }
            }

            // 创建确认选择班级的按钮
            JButton btnChooseClass = new JButton("确认选择班级");
            this.add(btnChooseClass);
            btnChooseClass.setBounds(x, y, 120, 30);

            // 为按钮添加事件监听器
            btnChooseClass.addActionListener(e -> {
                // 遍历按钮组，检查是否有按钮被选中
                Enumeration<AbstractButton> elements = btnGroup.getElements();
                boolean isSelected = false;
                while (elements.hasMoreElements()) {
                    JRadioButton btn = (JRadioButton) elements.nextElement();
                    if (btn.isSelected()) {
                        isSelected = true;
                        String className = btn.getText();
                        mainFrame.setTitle(className);
                        Constant.CLASS_PATH = Constant.FILE_PATH + className;
                        infoLbl.setText("班级：" + className + "，班级学生总数：");
                        break;
                    }
                }

                // 如果有班级被选中
                if (isSelected) {
                    try {
                        // 清空小组和学生列表
                        Constant.groups.clear();
                        Constant.students.clear();

                        // 加载小组信息
                        List<Group> groupList = FileUtil.loadGroups();
                        for (Group group : groupList) {
                            Constant.groups.put(group, new ArrayList<>());
                        }

                        // 加载学生信息并分配到小组
                        List<Student> studentList = FileUtil.loadStudents();
                        for (Student student : studentList) {
                            Constant.students.add(student);
                            for (Map.Entry<Group, List<Student>> entry : Constant.groups.entrySet()) {
                                if (entry.getKey().getName().equals(student.getGroupName())) {
                                    entry.getValue().add(student);
                                    break;
                                }
                            }
                        }

                        // 更新界面
                        this.removeAll();
                        infoLbl.setText(infoLbl.getText() + Constant.students.size());
                        infoLbl.setBounds(160, 100, 200, 30);
                        this.add(infoLbl);
                        this.repaint();
                        this.validate();

                    } catch (Exception e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                                "初始化小组和学生信息失败，请检查相关文件",
                                "", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "请先选择班级",
                            "", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            this.repaint();
            this.validate();
        }
    }
}