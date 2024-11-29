package com.student.entity;

// Group类用于表示一个小组的相关信息
public class Group {

    // 小组的名称，用于唯一标识每个小组
    private String name;

    // 小组的分数，用于记录小组在某项活动或任务中的得分情况
    private int score;

    // 构造函数，用于创建一个新的Group对象
    // 参数name用于指定小组的名称，在创建小组时需要传入小组名称
    // 小组初始分数被设置为0
    public Group(String name) {
        this.name = name;
        this.score = 0;
    }

    // 获取小组名称的方法
    // 返回值：小组的名称
    public String getName() {
        return name;
    }

    // 设置小组名称的方法
    // 参数name：要设置的新的小组名称
    public void setName(String name) {
        this.name = name;
    }

    // 获取小组分数的方法
    // 返回值：小组当前的分数
    public int getScore() {
        return score;
    }

    // 设置小组分数的方法
    // 参数score：要设置的新的小组分数
    public void setScore(int score) {
        this.score = score;
    }
}