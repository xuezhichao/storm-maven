package com.demo.singleton;

/**
 * @author ：xzc
 * @date ：Created in 2020/10/16 16:36
 * @description：
 * @modified By：
 * @version: $
 */
public class StarveSingleton {

    private static StarveSingleton instance= new StarveSingleton();

    private StarveSingleton() {
    }

    public static StarveSingleton getInstance(){
        return instance;
    }
}
