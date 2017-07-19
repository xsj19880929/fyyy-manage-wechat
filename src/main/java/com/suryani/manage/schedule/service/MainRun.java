package com.suryani.manage.schedule.service;

import java.util.Scanner;

/**
 * @author soldier
 */
public class MainRun {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入第一个boolean值(true/false):");
        if (sc.nextBoolean()) {
            System.out.println("输入布尔：真的");
        } else {
            System.out.println("输入布尔：假的");
        }


        System.out.println("输入第一个数字:");
        System.out.println("输入数字：" + sc.nextInt());

        System.out.println("输入一个字符串:");
        System.out.println("输入字符串：" + sc.next());

        System.out.println("输入一个长整型:");
        System.out.println("输入长整型：" + sc.nextLong());

    }
}
