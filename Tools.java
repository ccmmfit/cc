package com.cc;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by 小白菜又菜 on 2017/12/10.
 */
public class Tools
{
    /**
     * 写入文件
     * @param filename
     * @param content
     */
    public static void Writer(String filename, String content){

        FileWriter fw = null;

        try  {

            //以追加写入的方式
            fw = new FileWriter(filename, true);
            fw.write(content + "\r\n");

            fw.flush();   //清空缓冲区
            fw.close();   //关闭

        }catch(IOException e){
            System.out.println("写入文件失败！");
        }

    }

    /**
     * 写入二进制文件
     * @param filename
     * @param content
     */
    public static void BinaryWriter(String filename, byte[] content){
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(filename,true);

            fos.write(content); //把content写入文件

            fos.flush();
            fos.close(); //关闭
        }catch(IOException e) {
            System.out.println("写入二进制文件失败！");
        }
    }

    /**
     * 整型转byte
     * @param n
     * @return
     */
    public static byte[] int2bytes(int n) {
        byte[] res = new byte[4];

        for(int i = 0; i < 4; i++){
            res[3-i] = (byte)((n >> i*8) & 0xff);
        }
        return res;//从高位到低位
    }

    /**
     * byte转整型
     * @param b
     * @return
     */
    public static int bytes2int(byte[] b){
        int res = 0;
        for(int i = b.length-1; i != -1; i--){
            res |= (b[i] & 0xff) << 8*(3-i);
        }
        return res;
    }

    /**
     * 读取文件
     * @param filename
     * @return
     */
    public static String[] Reader(String filename){
        File file = new File(filename);
        ArrayList<String> lines = new ArrayList<String>();

        if(!file.exists()) {
            //System.out.println("文件不存在！");
            return lines.toArray(new String[lines.size()]);
        }

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filename));
            String temp = null;
            while ((temp = reader.readLine()) != null) { //一行一行读入
                lines.add(temp);
            }
            reader.close();

        }catch(IOException e){
            System.out.println("读取文件失败！");
        }
        return lines.toArray(new String[lines.size()]);
    }

    /**
     * 读取二进制文件
     * @param filename
     * @return
     */
    public static Integer[] BinaryReader(String filename){
        File file = new File(filename);
        ArrayList<Integer> res = new ArrayList<Integer>();

        if(!file.exists()) {
            //System.out.println("文件不存在！");
            return res.toArray(new Integer[res.size()]);
        }

        InputStream in = null;

        try {
            in = new FileInputStream(file);
            byte[] temp = new byte[4];

            while (in.read(temp) != -1) { //4个字节4个字节的读
                res.add(bytes2int(temp));  //将字节转成int并存入
            }
            in.close();

        }catch(IOException e){
            System.out.println("读取文件失败！");
        }

        return res.toArray(new Integer[res.size()]);
    }

}
