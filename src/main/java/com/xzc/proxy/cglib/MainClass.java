package com.xzc.proxy.cglib;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/9 09:30
 * @description：
 * @modified By：
 * @version: $
 */
public class MainClass {
    public static void main(String[] args) {
        CglibTest ct = new CglibTest();
        StudentService service = (StudentService)ct.getProxy(StudentService.class);
        service.study();
    }
}
