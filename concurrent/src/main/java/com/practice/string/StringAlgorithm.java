package com.practice.string;

import com.sun.deploy.util.StringUtils;

import java.util.Arrays;

/**
 * All rights Reserved
 *
 * @Package com.practice.string
 * @author: 长风
 * @date: 2018/9/21 上午9:41
 */
public class StringAlgorithm {
    public static String reverseString(String s) {
        String[] t = s.split("");

        int len = s.length();
        int i = len / 2 + 1;
        for (int k = 0; k < i; k++) {
            t[k] = t[len - k - 1];
        }

        return String.join("",t);
    }

    public static void main(String[] args){
        String s = "A man, a plan, a canal: Panama";
        String res = reverseString(s);
        System.out.println(res);
    }
}
