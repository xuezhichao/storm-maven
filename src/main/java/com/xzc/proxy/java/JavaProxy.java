package com.xzc.proxy.java;

import org.omg.CORBA.portable.InvokeHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/8 21:11
 * @description：
 * @modified By：
 * @version: $
 */
public class JavaProxy implements InvocationHandler {

    Object target;

    public Object bind(Object target){
        this.target=target;

        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target,args);
    }
}
