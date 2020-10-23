package com.xzc.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/9 09:14
 * @description：
 * @modified By：
 * @version: $
 */
public class CglibTest implements MethodInterceptor {

    public Object getProxy(Class clazz){
        Enhancer eh = new Enhancer();
        eh.setSuperclass(clazz);
        eh.setCallback(this);
        return eh.create();
    }


    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("******************");
        methodProxy.invokeSuper(o,objects);
        System.out.println("##################");
        return null;
    }
}
