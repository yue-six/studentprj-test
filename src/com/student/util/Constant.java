package com.student.util;

import com.student.entity.Group;
import com.student.entity.Student;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

// Constant类用于存放整个学生相关应用程序中的常量和一些共享的数据结构
public class Constant {

    // FILE_PATH是一个静态常量，用于指定文件的存储路径。
    // 这里通过System.getProperty("user.home")获取用户的主目录，然后拼接上特定的目录结构 "/starschool/classes/"，
    // 以此来确定应用程序相关文件的存储位置，例如可能用于存储班级信息、学生成绩等相关文件。
    public static final String FILE_PATH = System.getProperty("user.home") + "/starschool/classes/";

    // CLASS_PATH是一个静态变量，用于存储班级的路径信息。
    // 初始值为空字符串，在程序运行过程中可能会根据实际情况被赋值，比如根据用户选择的班级或系统配置来确定具体的班级路径。
    public static String CLASS_PATH = "";

    // groups是一个静态的LinkedHashMap，用于存放当前班级的小组和对应的学生列表。
    // 其中键（Key）是Group类型，表示班级中的各个小组；值（Value）是List<Student>类型，表示每个小组中的学生列表。
    // 这种数据结构方便根据小组来快速获取该小组下的所有学生信息，并且LinkedHashMap保留了插入顺序，便于后续按照插入顺序进行相关操作。
    public static LinkedHashMap<Group, List<Student>> groups = new LinkedHashMap<>();

    // students是一个静态的ArrayList，用于存放当前班级的所有学生信息。
    // 它可以方便地对班级内的所有学生进行统一管理，例如遍历所有学生进行成绩统计、信息查询等操作。
    public static List<Student> students = new ArrayList<>();

    // ABSENTEEISM_SCORE是一个静态常量，用于定义学生缺勤时所扣除的分数。
    // 在这里规定学生每缺勤一次会扣除5分，该常量在整个程序中用于统一的分数计算逻辑，确保扣分标准的一致性。
    public static final int ABSENTEEISM_SCORE = 5;

    // LEAVE_SCORE是一个静态常量，用于定义学生请假时所扣除的分数。
    // 规定学生每请假一次会扣除2分，同样用于程序中统一的分数计算逻辑，使得请假扣分的操作有明确且一致的标准。
    public static final int LEAVE_SCORE = 2;

    // ANSWER_QUESTION是一个静态常量，用于定义学生回答问题正确时所增加的分数。
    // 当学生正确回答一个问题时，会在其原有分数基础上增加3分，此常量确保了加分操作在整个程序中的标准统一。
    public static final int ANSWER_QUESTION = 3;
}