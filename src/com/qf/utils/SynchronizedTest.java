package com.qf.utils;

public class SynchronizedTest {

    private static int a = 0;

    private static Integer m = 1000;

    static class MyClass implements Runnable {

        @Override
        public void run() {
            for(int i = 0; i < 10000; i++) {
                synchronized (m) {  // 字符串对象是不可变的的。
                    a++;
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[10];

        Runnable runnable = new MyClass();

        for(int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(runnable);
        }

        for(Thread t : ts) {
            t.start();
            t.join();  // Waits for this thread to die.
        }

//        Thread.sleep(4000);

        System.out.println(a);
    }
}
