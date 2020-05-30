package com.qf.utils;

import org.apache.commons.text.RandomStringGenerator;

public class RandomStringTest {
    public static void main(String[] args) {

        /**
         * 随机生成字符串
         */
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                // withinRange() 指定生成的字符串的区间
                //.withinRange(new char[]{'a', 'z'}, new char[]{'A', 'Z'}, new char[]{'0', '9'}).build();
                // 这是指中文区间
                .withinRange(new char[]{'\u4e00', '\u9fa5'}).build();

        // 生成长度为10的随机字符串
        System.out.println(generator.generate(100));
    }
}
