package com.xzc.arithmetic;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/18 14:21
 * @description：
 * @modified By：
 * @version: $
 */
public class Single {

    public Single() {
        System.out.println("create----");
    }

    public static Single getInstance(){
        return InnerClass.instance;
    }

    private static class InnerClass{
        private static final Single instance = new Single();
    }

    public static void main(String[] args) {

        System.out.println(ThirdCodeMatchEnum.CXZX_FACE.name());
    }
}
