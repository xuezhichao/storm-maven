package com.xzc.proxy.java;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/8 21:13
 * @description：
 * @modified By：
 * @version: $
 */
public class MainClass {
    public static void main(String[] args) {
        JavaProxy proxy = new JavaProxy();
        Worker worker = (Worker) proxy.bind(new Program());
        worker.doWork();
    }
}
