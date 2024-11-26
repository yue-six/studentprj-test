package com.student.view;

import com.student.entity.SchoolClass;
import com.student.service.ClassService;
import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ChangeClassPanel extends JPanel {
    private ClassService classService;
    private MainFrame mainFrame;
    private JList<SchoolClass> classList;
    private DefaultListModel<SchoolClass> listModel;
    private JButton selectButton;
    private JLabel currentClassLabel;

    public ChangeClassPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.classService = ClassService.getInstance();
        initComponents();
        layoutComponents();
        addListeners();
    }

    private void initComponents() {
        setBorder(new TitledBorder(new EtchedBorder(), "切换班级"));

        listModel = new DefaultListModel<>();
        classList = new JList<>(listModel);
        classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classList.setCellRenderer(new ClassListCellRenderer());

        selectButton = new JButton("选择班级");
        currentClassLabel = new JLabel("当前未选择班级");
        currentClassLabel.setFont(Constant.FONT_BOLD);

        selectButton.setEnabled(false);
        refreshClassList();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(Constant.PADDING_MEDIUM, Constant.PADDING_MEDIUM));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(currentClassLabel);

        JScrollPane scrollPane = new JScrollPane(classList);
        scrollPane.setPreferredSize(new Dimension(Constant.LIST_WIDTH, Constant.LIST_HEIGHT));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(selectButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        classList.addListSelectionListener(e -> {
            selectButton.setEnabled(classList.getSelectedValue() != null);
        });

        selectButton.addActionListener(e -> {
            SchoolClass selectedClass = classList.getSelectedValue();
            if (selectedClass != null) {
                System.out.println("Selected class: " + selectedClass.getName()); // 调试信息
                mainFrame.onClassChanged(selectedClass);
                updateCurrentClassLabel(selectedClass);
            }
        });
    }

    public void refreshClassList() {
        listModel.clear();
        java.util.List<SchoolClass> classes = classService.getAllClasses();
        System.out.println("Refreshing class list, total classes: " + classes.size()); // 调试信息
        for (SchoolClass schoolClass : classes) {
            listModel.addElement(schoolClass);
        }
        selectButton.setEnabled(false);
    }

    public void updateCurrentClassLabel(SchoolClass currentClass) {
        if (currentClass != null) {
            currentClassLabel.setText("当前班级: " + currentClass.getName());
        } else {
            currentClassLabel.setText("当前未选择班级");
        }
    }

    private class ClassListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof SchoolClass) {
                SchoolClass schoolClass = (SchoolClass) value;
                setText(schoolClass.getName() + " (" +
                        schoolClass.getStudents().size() + "人, " +
                        schoolClass.getGroups().size() + "组)");
                setFont(Constant.FONT_NORMAL);
            }
            return this;
        }
    }
}