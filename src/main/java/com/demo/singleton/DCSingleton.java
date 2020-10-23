package com.demo.singleton;

/**
 * @author ：xzc
 * @date ：Created in 2020/10/16 16:40
 * @description：
 * @modified By：
 * @version: $
 */
public class DCSingleton {

    private volatile static DCSingleton instance;

    private DCSingleton(){}

    public static DCSingleton getInstance(){
        if(instance == null){
            synchronized (DCSingleton.class){
                if(instance == null){
                    instance = new DCSingleton();
                }
            }
        }
        return instance;
    }
}
