package com.student.util;

import com.student.entity.Group;
import com.student.entity.Student;

import java.io.*;
import java.util.*;

/*
FileUtil 类提供了文件操作相关的工具方法。
包括保存和加载小组信息、学生信息，以及创建和删除班级目录等。
 */
public class FileUtil {

    // 保存小组信息到文件
    public static void saveGroups(List<Group> groups) {
        try {
            // 创建表示小组信息文件的File对象
            File groupFile = new File(Constant.CLASS_PATH + "/groups.txt");
            // 如果文件不存在，则创建新文件
            if (!groupFile.exists()) {
                groupFile.createNewFile();
            }
            // 使用BufferedWriter写入数据
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(groupFile))) {
                for (Group group : groups) {
                    // 写入小组名称和分数，用逗号分隔
                    writer.write(group.getName() + "," + group.getScore() + "\n");
                }
            }
        } catch (IOException e) {
            // 异常处理，打印堆栈跟踪
            e.printStackTrace();
        }
    }

    // 从文件中读取小组信息
    public static List<Group> loadGroups() {
        List<Group> groups = new ArrayList<>();
        try {
            // 创建表示小组信息文件的File对象
            File groupFile = new File(Constant.CLASS_PATH + "/groups.txt");
            // 如果文件不存在，则返回空列表
            if (!groupFile.exists()) {
                return groups;
            }
            // 使用BufferedReader读取数据
            try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 按逗号分割行内容
                    String[] parts = line.split(",");
                    // 创建新的小组对象并设置其名称和分数
                    Group group = new Group(parts[0]);
                    group.setScore(Integer.parseInt(parts[1]));
                    // 将小组添加到列表中
                    groups.add(group);
                }
            }
        } catch (IOException e) {
            // 异常处理，打印堆栈跟踪
            e.printStackTrace();
        }
        return groups;
    }

    // 保存学生信息到文件
    public static void saveStudents(List<Student> students) {
        try {
            // 创建表示学生信息文件的File对象
            File studentFile = new File(Constant.CLASS_PATH + "/students.txt");
            // 如果文件不存在，则创建新文件
            if (!studentFile.exists()) {
                studentFile.createNewFile();
            }
            // 使用BufferedWriter写入数据
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentFile))) {
                for (Student student : students) {
                    // 写入学生ID、姓名、所在小组名称和分数，用逗号分隔
                    writer.write(student.getId() + "," + student.getName() + "," +
                            student.getGroupName() + "," + student.getScore() + "\n");
                }
            }
        } catch (IOException e) {
            // 异常处理，打印堆栈跟踪
            e.printStackTrace();
        }
    }

    // 从文件中读取学生信息
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try {
            // 创建表示学生信息文件的File对象
            File studentFile = new File(Constant.CLASS_PATH + "/students.txt");
            // 如果文件不存在，则返回空列表
            if (!studentFile.exists()) {
                return students;
            }
            // 使用BufferedReader读取数据
            try (BufferedReader reader = new BufferedReader(new FileReader(studentFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 按逗号分割行内容
                    String[] parts = line.split(",");
                    // 创建新的学生对象并设置其ID、姓名、所在小组名称和分数
                    Student student = new Student(parts[0], parts[1], parts[2]);
                    student.setScore(Integer.parseInt(parts[3]));
                    // 将学生添加到列表中
                    students.add(student);
                }
            }
        } catch (IOException e) {
            // 异常处理，打印堆栈跟踪
            e.printStackTrace();
        }
        return students;
    }

    // 创建班级目录
    public static void createClassDirectory(String className) throws IOException {
        // 确保基础目录存在
        File baseDir = new File(Constant.FILE_PATH);
        if (!baseDir.exists()) {
            if (!baseDir.mkdirs()) {
                throw new IOException("无法创建基础目录");
            }
        }

        // 创建班级目录
        File classDir = new File(Constant.FILE_PATH + className);
        if (!classDir.mkdir()) {
            throw new IOException("无法创建班级目录");
        }
    }

    // 删除班级目录
    public static void deleteClassDirectory(String className) {
        // 创建表示班级目录的File对象
        File classDir = new File(Constant.FILE_PATH + className);
        // 如果班级目录存在
        if (classDir.exists()) {
            // 获取班级目录下的所有文件
            File[] files = classDir.listFiles();
            if (files != null) {
                // 删除班级目录下的所有文件
                for (File file : files) {
                    file.delete();
                }
            }
            // 删除班级目录
            classDir.delete();
        }
    }
}
