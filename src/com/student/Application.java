package com.student;

import com.student.view.MainFrame;

import javax.swing.*;

/*
  应用程序的入口类，负责启动整个应用。
 */
public class Application {
    /**
     * 程序的主入口方法。
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        try {
            // 遍历所有已安装的外观风格（LookAndFeel）
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                // 打印每种外观风格的名称
                System.out.println(info.getName());
                // 如果找到了名为"Nimbus"的外观风格，则设置为当前外观风格
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // 如果设置外观风格过程中出现异常，打印堆栈跟踪
            e.printStackTrace();
        }
        // 创建主窗口框架对象
        MainFrame mainFrame = new MainFrame();
        // 此处没有显示主窗口，因为MainFrame构造函数中已经调用了setVisible(true)来显示窗口
    }
}