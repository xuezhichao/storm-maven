package com.demo;

/**
 * @author ：xzc
 * @date ：Created in 2020/10/16 09:24
 * @description：
 * @modified By：
 * @version: $
 */
public class TestInner {

    private int number = 100;

    public class Inner{
        private int number = 200;
        public void paint(){
            int number = 500;
            System.out.println(number);
            System.out.println(this.number);
            System.out.println(TestInner.this.number);
        }

        public M_Car getCar(){
            //方法体内的内部类
            class BWM extends M_Car{
                public void paint(){
                    System.out.println("BWM");
                }
            }
            return new BWM();
        }
    }

    interface Fly{
        void doFly();
    }

    public class InnerFly implements Fly{

        @Override
        public void doFly() {
            System.out.println("I'm Superman");
        }
    }


    private class A_Inner{

    }



    public static class StaticInnerClass{
        public static void paint(){
            System.out.println("inner ?");
        }
    }

    public static void main(String[] args) {
        //创建内部类对象分为两个步骤
        StaticInnerClass inner = new TestInner.StaticInnerClass();
        inner.paint();
    }
}


class M_Car{
    public  void paint(){
        System.out.println("car");
    }
}



class Test implements TestInner.Fly{

    @Override
    public void doFly() {

    }
}
