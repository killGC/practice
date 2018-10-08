package com.practice.solution;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * All rights Reserved
 *
 * @Package com.practice.solution
 * @author: 长风
 * @date: 2018/10/8 下午6:50
 */
public class ImageMerge {

    public static void main(String[] args) throws Exception{
        mergeImage("https://img.maihaoche.com/0770BC82-2636-4655-950F-294BB54AA90C.jpg","https://img.maihaoche.com/45FBBC4B-BF54-44D6-887A-F6FD4CDBF912.jpg");
    }

    public static void mergeImage(String url1,String url2) throws Exception{
        BufferedImage image1 = ImageIO.read(new URL(url1));
        BufferedImage image2 = ImageIO.read(new URL(url2));

        //BufferedImage image3 = new BufferedImage(image1.getWidth()*2,image1.getHeight(),BufferedImage.TYPE_INT_RGB);
        BufferedImage image3 = new BufferedImage(image1.getWidth(),image1.getHeight()*2,BufferedImage.TYPE_INT_RGB);

        Graphics g = image3.getGraphics();

        g.drawImage(image1,0,0,null);
        g.drawImage(image2,0,image1.getHeight(),null);
        ImageIO.write(image3,"JPG",new File("/Users/user","6.jpg"));
    }
}
