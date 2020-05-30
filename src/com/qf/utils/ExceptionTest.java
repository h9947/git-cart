package com.qf.utils;

public class ExceptionTest {
    public static void main(String[] args) {

        try {
            test();
            System.out.println("异常后的代码");
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void test() {
        int a = 10 / 0;
    }
}
