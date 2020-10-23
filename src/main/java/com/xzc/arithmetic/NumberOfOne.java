package com.xzc.arithmetic;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/26 17:18
 * @description：
 * @modified By：
 * @version: $
 */
public class NumberOfOne {

    public static void main(String[] args) {
//        System.out.println(NumberOf1(-1));
        printToMax(2);
    }

   static void printToMax(int n){
        if(n <= 0){
            return;
        }
        char number[] = new char[n+1];
        number[n] = '\0';
        pp(number,n,0);
   }
   static void pp(char number[] ,int length,int index){
        if(index == length-1){
            System.out.println(number);
            return;
        }
        for(int i=0;i<10;i++){
            number[index+1] = (char) (i+'0');
            pp(number,length,index+1);
        }
   }
}
