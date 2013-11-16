/*
 * 捷利商业进销存管理系统
 * @(#)VCodeGenerator.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-14
 */
package cn.jely.cd.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-9-14 下午12:07:34
 */
public class VCodeGenerator {

    final private char[] chars = "2345678ABCDEFGHJKLMPQRSTUVWXYabcdefhkmnqrstuvwx"
            .toCharArray();
    private static String[] fontNames = new String[] { "Courier", "Arial",
            "Verdana", "Georgia","Gisha", "Times", "Tahoma" };
    private static int[] fontStyle = new int[] { Font.PLAIN, Font.BOLD,
            Font.ITALIC, Font.BOLD | Font.ITALIC };

    private int width = 160;
    private int height = 60;
    private int charCnt = 4;
    private int disturbLineNum = 10;

    private OutputStream os;

    public VCodeGenerator(OutputStream os) {
        this.os = os;
    }

    public VCodeGenerator(OutputStream os, int width, int height, int charCnt) {
        this.width = width;
        this.height = height;
        this.charCnt = charCnt;
        this.os = os;
    }

    public String drawCode() throws IOException {
        BufferedImage bi = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(new Color(245, 245, 245));
        g.fillRect(0, 0, this.width, this.height);

        drawDisturbLine(g);
        BufferedImage[] bis = new BufferedImage[charCnt];
        char[] codes = generateCode();
        for (int i = 0; i < charCnt; i++) {
            bis[i] = generateBuffImg(codes[i]);
            g.drawImage(bis[i], null, (int) (this.width / this.charCnt * 0.85) * i, 0);
        }
        drawFilter(g);
        g.dispose();

        ImageIO.write(bi, "gif", os);
        return new String(codes);
    }


	private BufferedImage generateBuffImg(char c) {
        String tmp = Character.toString(c);
        Color forecolor = getRandomColor();
        Color backcolor = new Color(255, 255, 255, 0);
        String fontName = getRandomFontName();
        int fontStyle = getRandomStyle();
        int fontSize = getRandomSize();
        int strX = (this.height - fontSize) / 2;
        int strY = (this.height - fontSize) / 2 + fontSize;
        double arch = getRandomArch();

        BufferedImage ret = new BufferedImage(this.height, this.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ret.createGraphics();
        g.setColor(backcolor);
        g.fillRect(0, 0, this.height, this.height);

        g.setColor(forecolor);
        g.setFont(new Font(fontName, fontStyle, fontSize));
        g.rotate(arch, this.height / 2, this.height / 2);
        g.drawString(tmp, strX, strY);

        g.dispose();
        return ret;
    }

    private double getRandomArch() {
        return ((int) (Math.random() * 1000) % 2 == 0 ? -1 : 1) * Math.random();
    }

    private Color getRandomColor() {
        int r = (int) (Math.random() * 10000) % 200;
        int g = (int) (Math.random() * 10000) % 200;
        int b = (int) (Math.random() * 10000) % 200;
        return new Color(r, g, b);
    }

    private String getRandomFontName() {
        int pos = (int) (Math.random() * 10000) % (fontNames.length);
        return fontNames[pos];
    }

    private int getRandomStyle() {
        int pos = (int) (Math.random() * 10000) % (fontStyle.length);
        return fontStyle[pos];
    }

    private int getRandomSize() {
        int max = (int) (this.height * 0.95);
        int min = (int) (this.height * 0.70);
        return (int) (Math.random() * 10000) % (max - min + 1) + min;
    }

    private char[] generateCode() {
        char[] ret = new char[charCnt];
        for (int i = 0; i < charCnt; i++) {
            int letterPos = (int) (Math.random() * 10000) % (chars.length);
            ret[i] = chars[letterPos];
        }
        return ret;
    }

    private void drawDisturbLine(Graphics2D graphics) {
        for (int i = 0; i < disturbLineNum; i++) {
            graphics.setColor(getRandomColor());
            int x = (int) (Math.random() * 10000) % (this.width + 1) + 1;
            int x1 = (int) (Math.random() * 10000) % (this.width + 1) + 1;
            int y = (int) (Math.random() * 10000) % (this.height + 1) + 1;
            int y1 = (int) (Math.random() * 10000) % (this.height + 1) + 1;
            graphics.drawLine(x, y, x1, y1);
        }

    }

    /**
	 * TODO:对图像加滤镜
	 * @param g void
	 */
	private void drawFilter(Graphics2D g) {
		int filterw = (int) (this.width*Math.random()+1);
		int filterh = (int) (this.height*Math.random()+1);
		BufferedImage img=new BufferedImage(filterw, filterh,
                BufferedImage.TYPE_INT_ARGB);
		
		float[] elements=new float[]{0.0f,-1.0f,0.0f,-1.0f,4.0f,-1.0f,0.0f,-1.0f,0.0f};
		BufferedImageOp op= new ConvolveOp(new Kernel(3, 3, elements));
		g.drawImage(img, op, 0, 0);
	}

    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("d:/tmp.gif");
        VCodeGenerator vg = new VCodeGenerator(fos);
        vg.drawCode();
    }
}
