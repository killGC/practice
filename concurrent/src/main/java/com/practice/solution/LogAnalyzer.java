package com.practice.solution;

import java.io.*;
import java.util.Scanner;

/**
 * All rights Reserved
 *
 * @Package com.practice.solution
 * @author: 长风
 * @date: 2018/9/21 下午5:29
 */

public class LogAnalyzer {
    private static String fileName = "o_1ch2jlihs4iamgp10qpmgf1a62a.mov";

    public static void main(String[] args) throws Exception{
        //directRead();
        //readByScanner();
        //String errorType = readByStream();
        //splitFile();

        System.out.println(4%3);


    }

    public  static String directRead() throws Exception{
        char input = (char)System.in.read();

        System.out.println(input);

        return String.valueOf(input);
    }

    public  static String readByScanner() throws Exception{
        Scanner scanner = new Scanner(System.in);
        String data= scanner.nextLine();

        System.out.println(data);

        return data;
    }

    public  static String readByStream() throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String data = reader.readLine();

        System.out.println(data);

        return data;
    }

    public static void splitFile() throws IOException{
        System.out.println(LogAnalyzer.class.getResource("/").getPath());
        String path = LogAnalyzer.class.getResource("/").getPath()+fileName;
        File file = new File(path);
        System.out.println("totalSpace:"+file.getTotalSpace()/(1024*1024));//该分区大小
        System.out.println(file.getName());
        System.out.println(file.getParent());
        long size = file.length();
        System.out.println("size:"+size/(1024*1024));

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader,10*1024*1024);
        while(fileReader.ready()){
            bufferedReader.readLine();
        }

        bufferedReader.readLine();
    }


}
