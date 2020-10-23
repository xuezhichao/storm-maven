package com.demo.singleton;

/**
 * @author ：xzc
 * @date ：Created in 2020/10/16 16:30
 * @description：
 * @modified By：
 * @version: $
 */
public class LazySingleton {


    private static LazySingleton instance;

    private LazySingleton() {
    }

    public static synchronized LazySingleton getInstance(){
        if(instance == null){
            instance =  new LazySingleton();
        }
        return instance;
    }

}
