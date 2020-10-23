package com.demo.singleton;

/**
 * @author ：xzc
 * @date ：Created in 2020/10/16 16:43
 * @description：
 * @modified By：
 * @version: $
 */
public class InnerClassSingleton {

    private InnerClassSingleton() {
    }

    public static final InnerClassSingleton getInstance(){
        return Inner.singleton;
    }


    private static class Inner{
        private static InnerClassSingleton singleton = new InnerClassSingleton();
    }
}
