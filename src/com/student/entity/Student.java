package com.student.entity;

// Student类用于表示学生的相关信息
public class Student {

    // 学生的学号，用于唯一标识每个学生
    private String id;

    // 学生的姓名
    private String name;

    // 学生所属的小组名称，通过该名称可以关联到对应的小组对象
    private String groupName;

    // 学生的分数，用于记录学生在某项活动或任务中的得分情况
    private int score;

    // 构造函数，用于创建一个新的Student对象
    // 参数id用于指定学生的学号，创建学生对象时必须传入学号
    // 参数name用于指定学生的姓名，创建学生对象时必须传入姓名
    // 参数groupName用于指定学生所属的小组名称，创建学生对象时必须传入所属小组名称
    // 学生初始分数被设置为0
    public Student(String id, String name, String groupName) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
        this.score = 0;
    }

    // 获取学生学号的方法
    // 返回值：学生的学号
    public String getId() {
        return id;
    }

    // 设置学生学号的方法
    // 参数id：要设置的新的学号
    public void setId(String id) {
        this.id = id;
    }

    // 获取学生姓名的方法
    // 返回值：学生的姓名
    public String getName() {
        return name;
    }

    // 设置学生姓名的方法
    // 参数name：要设置的新的姓名
    // 注意：在实际应用中可能需要考虑姓名的合法性等相关情况
    public void setName(String name) {
        this.name = name;
    }

    // 获取学生所属小组名称的方法
    // 返回值：学生所属的小组名称
    public String getGroupName() {
        return groupName;
    }

    // 设置学生所属小组名称的方法
    // 参数groupName：要设置的新的所属小组名称
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    // 获取学生分数的方法
    // 返回值：学生当前的分数
    public int getScore() {
        return score;
    }

    // 设置学生分数的方法
    // 参数score：要设置的新的分数
    public void setScore(int score) {
        this.score = score;
    }
}