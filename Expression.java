package com.cc;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by 小白菜又菜 on 2017/12/10.
 */
public class Expression {
    private int err = 0; //错误计数
    private String exp; //输入的表达式
    private double ans = 0;  //计算结果

    public int getErr() {
        return err;
    }

    public double getAns() {
        return ans;
    }

    //构造函数
    public Expression(String exp) {
        this.exp = exp;
    }

    /**
     * 运算操作
     * @param a
     * @param b
     * @param op
     * @return
     */
    private double operate(double a, double b, char op) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/': {
                if (b == 0) {
                    System.out.println("错误提示：除数不能为0");
                    err += 1; //错误+1
                    return 0;
                }
                return a / b;
            }
        }
        return 0;
    }

    /**
     * 比较优先级
     * @param cur
     * @param top
     * @return cur > top
     */
    private boolean cmpPriority(char cur, char top){
        boolean res = false;
        if(((cur == '*' || cur == '/') && (top == '+' || top == '-')) || top =='(') {
            res = true;
        }
        //当top = '(' , cur = +-*/之一 时， cur > top (cur在top右边)

        return res;
    }

    /**
     * 取得后缀表达式
     * @return
     */
    private String[] getPostOrder() {

        String tmpNum = "";
        Stack<Character> op = new Stack<Character>(); //符号栈
        ArrayList<String> ret = new ArrayList<String>(); //存放后缀表达式

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c == ' ') continue; //忽略空格
            if ((c >= '0' && c <= '9') || c == '.') { //为数字
                tmpNum += c;
            }else{
                if(false == tmpNum.equals("")) { //不为空则存入
                    ret.add(tmpNum);
                    tmpNum = "";
                }
                if(c == '(')  //遇到左括号则进栈
                    op.push(c);
                else if(c == ')'){ //遇到右括号则出栈
                    while(!op.empty() && op.peek() != '('){
                        ret.add(op.pop().toString()); //存入要返回的列表中
                    }
                    if(!op.empty())
                        op.pop(); //左括号不存入
                    else
                        err += 1; //错误+1
                }
                else{
                    while(!op.empty() && !cmpPriority(c, op.peek())){ //如果栈不空且当前操作符优先级比前一个小
                        ret.add(op.pop().toString()); //存入要返回的列表中
                    }
                    op.push(c); //存入符号栈
                }
            }
        }

        if(exp != null && false == tmpNum.equals("")) //不为空则存入
            ret.add(tmpNum);

        while(!op.empty()){
            ret.add(op.pop().toString()); //存入要返回的列表中
        }
        return ret.toArray(new String[ret.size()]);
    }

    /**
     * 计算
     */
    public void calc(){
        String[] postExp = this.getPostOrder(); //先得到后缀表达式

        System.out.printf("\n后缀表达式: ");
        for(String t:postExp){
            System.out.print(t + "  ");
        }
        System.out.println("\n");

        if(err != 0) return; //如果错误则返回

        Stack<Double> sd = new Stack<Double>(); //Double型的数值栈

        for(int i = 0; i < postExp.length; i++){
            char c = postExp[i].charAt(0); //判断第一个字符
            if(c >= '0' && c <= '9'){
                double tmp = Double.valueOf(postExp[i]); //获取数值
                sd.push(tmp);
            }else{ //否则为+-*/
                double b = (!sd.empty()) ? sd.pop() : err++; //不为空则取出, 为空则错误数+1
                double a = (!sd.empty()) ? sd.pop() : err++;
                sd.push(this.operate(a, b, c)); //将计算结果存回sd
                if(err != 0) {
                    System.out.println("错误提示：表达式输入不完整");
                    return; //有错误则返回

                }
            }
        }

        ans = (sd.size() == 1) ? sd.pop() : 0; //将结果存入ans

    }

    public static void  main(String[] args){
        Expression exp = null;
        Scanner in = new Scanner(System.in);
        System.out.print("请输入合法的表达式：");

        while (in.hasNext()) { //还有输入时继续执行
            String s = in.nextLine(); //获取一行
            if("0".equals(s)){
                break;
            }
            exp = new Expression(s); //new一个表达式对象

            exp.calc(); //计算

            System.out.printf("计算结果: %.2f , 总共: %d 处错误。\n\n\n" ,exp.getAns() ,exp.getErr());

            try {
                Thread.sleep(1000); //停顿1s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print("请输入合法的表达式：");
        }
        System.out.println("程序结束！");
    }


}
