package com.cc;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by 小白菜又菜 on 2017/12/10.
 */

abstract class Lotty{
    abstract void generate(); //生成彩票号码
    abstract int[] check(int[] userScan); //核对彩票号码 返回[0]表示奇数为猜对的位数，[1]表示偶数位猜对的位数，
    // [3]表示第一个位置是否猜对，[4]表示最后一个位置是否猜对
}

/**
 * 体育彩票
 *
 */
class Sport_Lotty extends Lotty{
    private int[] num;
    public static int SLbits = 7; //体育彩票位数


    //构造函数
    public Sport_Lotty() {
        super();
        generate();
    }

    // 为生成的彩票提供get方法
    public int[] getNum() {
        return num;
    }


    //彩票生成
    protected void generate(){
        this.num = new int[SLbits];
        Random random = new Random();
        for(int i = 0; i < SLbits; i++){
            this.num[i] = random.nextInt(9)%(10);
        }
    }

    //核对用户输入的号码 是否 中奖
    public int[] check(int[] userScan){
        int[] res = new int[4];
        if(userScan.length != SLbits) return res; //位数不相等 默认全部押错

        for(int i = 0; i < SLbits; i++){
            if(userScan[i] == num[i]){
                if(i % 2 == 1){ //i为奇数，用户输入的偶数位
                    res[1]++;  //偶数位正确的个数+1
                }else{
                    res[0]++; //奇数位正确的个数+1
                }
                if(i == 0) res[2] = 1;
                if(i == SLbits-1) res[3] = 1;
            }
        }

        return res;
    }
}

/**
 * 福利彩票
 *
 */
class Welfare_Lotty extends Lotty{
    private int[] num;
    public static int WLbits = 6;


    //构造函数
    public Welfare_Lotty() {
        super();
        generate();
    }

    //为生成的彩票提供get方法
    public int[] getNum() {
        return num;
    }


    //彩票生成
    protected void generate(){
        this.num = new int[WLbits];
        Random random = new Random();
        for(int i = 0; i < WLbits; i++){
            this.num[i] = random.nextInt(19)%(20);
        }

    }

    //核对用户输入的号码 是否 中奖
    public int[] check(int[] userScan){
        int[] res = new int[4];
        if(userScan.length != WLbits) return res; //位数不相等 默认全部押错

        for(int i = 0; i < WLbits; i++){
            if(userScan[i] == num[i]){
                if(i % 2 == 1){ //i为奇数，用户输入的偶
                    res[1]++;
                }else{
                    res[0]++; //奇数位正确的个数+1
                }
                if(i == 0) res[2] = 1;
                if(i == WLbits-1) res[3] = 1;
            }
        }

        return res;
    }

}


/**
 * 游戏接口
 *
 */
interface Game{
    void Start(); //游戏开始
    String printResult(int[] condi, int[] lotty); //打印结果
}

/**
 * 彩票游戏类
 *
 */
public class LottyGame implements Game{

    private Scanner in; //输入流
    private int[] uScan; //用户输入

    //构造函数
    public LottyGame() {
        System.out.println("\t\t\t------欢迎开始彩票游戏------\n \t\t\t 1)体育彩票  \t 2)福利彩票");
        in = new Scanner(System.in);
    }

    //输入
    public void SCAN(int bits){
        uScan = new int[bits];
        for(int i = 0; i < bits; i++)
            uScan[i] = in.nextInt();
    }

    //输出开奖情况
    public String printResult(int[] condi, int[] lotty){
        System.out.println("激动人心的时刻到了！彩票开奖：");
        int bits = lotty.length;

        for(int i = 0; i < bits; i++){
            System.out.printf(i == bits-1 ? "%d\n":"%d ", lotty[i]);
        }

        String s = "";
        if(condi[0] + condi[1] == bits){
            s = "特等奖";
            System.out.println("恭喜！您中了特等奖！");
        }else if(condi[0] + condi[1] == bits-1){
            s = "一等奖";
            System.out.println("恭喜您中了一等奖！");
        }else if(condi[0] == (bits+1)/2){
            s = "二等奖";
            System.out.println("恭喜您中了二等奖！");
        }else if(condi[1] == bits/2){
            s = "三等奖";
            System.out.println("恭喜您中了三等奖！");
        }else if(condi[2] + condi[3] == 2){
            s = "四等奖";
            System.out.println("恭喜您中了四等奖！");
        }else if(condi[3] ==1){
            s = "五等奖";
            System.out.println("恭喜您中了五等奖！");
        }else{
            System.out.println("很遗憾！您没有中奖，请再接再厉！");
            s = "无中奖";
        }

        return s;
    }

    //保存中奖结果
    public void Save(int[] num, String result){
        if(num.length == Sport_Lotty.SLbits){
            String line = "";
            //保存系统生成的彩票号码
            for(int n:num){
                line += String.valueOf(n) + " ";
            }

            //保存用户输入的号码
            for(int u:uScan){
                line += String.valueOf(u) + " ";
            }
            line += result; //保存中奖结果
            Tools.Writer("D:/Sport_Lotty.dat", line);

        }else if(num.length == Welfare_Lotty.WLbits){
            //int count = Welfare_Lotty.WLbits*2*4 + 1; //约定99作为分隔
            //byte[] b = new byte[count];
            ArrayList<Byte> b = new ArrayList<Byte>();
            int j = 0;
            //保存系统生成的彩票号码
            for(int n:num){
                byte[] tmp = Tools.int2bytes(n);
                for(byte t:tmp){
                    b.add(t);
                }
            }
            //保存用户输入的号码
            for(int u:uScan){
                byte[] tmp = Tools.int2bytes(u);
                for(byte t:tmp){
                    b.add(t);
                }
            }

            //保存几等奖
            byte[] temp = result.getBytes(); //假二进制
            for(byte tmp:temp){
                b.add(tmp);
            }

            //分隔符
            byte[] tmp = Tools.int2bytes(99);
            for(byte t:tmp){
                b.add(t);
            }

            //将Byte类型转成byte类型
            byte[] bt = new byte[b.size()];
            for(int i = 0; i < b.size(); i++){
                bt[i] = b.get(i);
            }

            Tools.BinaryWriter("D:/Welfare_Lotty.dat", bt);
        }
    }

    //游戏开始
    public void Start(){
        while(in.hasNext()){
            int select = in.nextInt(); //选择

            if(select == 1){
                Sport_Lotty s = new Sport_Lotty(); //体育彩票类
                String[] lines = Tools.Reader("D:/Sport_Lotty.dat");

                while(true){
                    boolean flag = false;  //flag标记当前生成的号码是否与文件里存放的之前已生成的号码重复
                    for(String t:lines){
                        String[] tt = t.split(" ");
                        int[] tmp = new int[Sport_Lotty.SLbits];
                        for(int i = 0; i < tmp.length; i++){
                            tmp[i] = Integer.valueOf(tt[i]); //将字符串转回整型
                        }
                        int[] judge = s.check(tmp);
                        if(judge[0] + judge[1] == Sport_Lotty.SLbits){
                            flag = true; //已找到与记录里相等的一组彩票 跳出for循环
                            break;
                        }

                    }
                    if(!flag) break; //当前生成的彩票号码与文件里记录的之前生成的彩票号码全部不一样 则退出循环
                }

                System.out.println("请下注(" + s.SLbits + "个号码<每位范围0-9>)");
                SCAN(s.SLbits); //输入

                int[] condition =  s.check(uScan); //核对中奖情况
                String result = printResult(condition, s.getNum()); //输出中奖结果
                Save(s.getNum(),result); //保存中奖结果

            }else if(select == 2){
                Welfare_Lotty w = new Welfare_Lotty(); //福利彩票类
                Integer[] lines = Tools.BinaryReader("D:/Welfare_Lotty.dat");

                while(true){
                    boolean flag = false;  //flag标记当前生成的号码是否与文件里存放的之前已生成的号码重复
                    int[] tmp = new int[Welfare_Lotty.WLbits];

                    for(int i = 0; i < lines.length; i++){
                        for(int j = 0; i < lines.length && j < tmp.length; i++,j++){
                            tmp[j] = lines[i];
                        }
                        int[] judge = w.check(tmp); //核对相等的数目
                        if(judge[0] + judge[1] == Welfare_Lotty.WLbits){
                            flag = true; //已找到与记录里相等的一组彩票 跳出for循环
                            break;
                        }

                        i += Welfare_Lotty.WLbits; //跳过用户输入的数
                        int t = lines[i];
                        while(i < lines.length && t != 99){ //直到标记数
                            i++;
                            if(i < lines.length)
                                t = lines[i];
                        }
                    }
                    if(!flag) break; //当前生成的彩票号码与文件里记录的之前生成的彩票号码全部不一样 则退出循环
                }

                System.out.println("请下注(" + w.WLbits + "个号码<每位范围0-19>)");
                SCAN(w.WLbits); //输入

                int[] condition =  w.check(uScan); //核对中奖情况
                String result = printResult(condition, w.getNum()); //输出中奖结果
                Save(w.getNum(),result); //保存中奖结果

            }
            System.out.println("请选择彩票类型：< 1)体育彩票   2)福利彩票 >");
        }

    }


    //main函数
    public static void main(String[] args) {

        LottyGame game = new LottyGame();
        game.Start();

    }

}
