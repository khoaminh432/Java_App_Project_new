package com.demo.app;
import java.util.Scanner;
public class testJDBC {
    private static final Scanner scanner = new Scanner(System.in);
    private void outtonghieutichthuong(int a,int b){
        
        System.out.println("tong:" + (a + b));
        System.out.println("hieu:" + (a - b));
        System.out.println("tich:"+(a*b));
        System.out.println("thuong:"+(a/(b)));
    }
    private int nhapsonguyen(){
        return scanner.nextInt();
    }
    private double nhapsothuc(){
        return  scanner.nextDouble();
    }
    private void  outcvdt(int a,int b){
        System.out.println("chuvi: "+(a+b)*2);
        System.out.println("dientich: "+(a*b));

    }
    private void outcvdtht(double r){
        System.out.println("chuvi: "+Math.PI*r*r);
        System.out.println("dientich:"+Math.PI*2*r);
    }
    private void outchanle(int n){
        System.out.println(n+" la so: "+(n%2==0?"chan":"le"));
    }
    private void outsonguyen(int n){
        if (n<0)
            System.out.println(n+" la so am");
        else if(n==0)
            System.out.println(n+" la so 0");
        else System.out.println(n+" la so duong");
    }
    private boolean prime(int a){
        if(a<2)
            return false;
        for (int i = 2;i<=Math.sqrt(a);i++)
            if (a%i==0)
                return false;
        return true;
    }
    private  void outsnt(int a){
        System.out.println(a+(prime(a)?" chinh":" khong")+" la so nguyen to: ");
    }
    public void out1(){
        int a = nhapsonguyen();
        int b = nhapsonguyen();
        outtonghieutichthuong(a, b);
    }
    public void out2(){
        int dai = nhapsonguyen();
        int rong = nhapsonguyen();
        outcvdt(dai, rong);
    }
    
    public void out3(){
        double bankinh = nhapsothuc();
        outcvdtht(bankinh);
    }
    public void out4(){
        int soN = nhapsonguyen();
        outchanle(soN);
    }
    public  void out5(){
        int soN = nhapsonguyen();
        outsonguyen(soN);
    }
    public void out6(){
        int soN = nhapsonguyen();
        outsnt(soN);
    }
    private void caua(int n){
        int s = 0;
        for (int i=1;i<=n;i++){
            s+=i;
        }
        System.out.println("Tong cua 1 den "+n+" la: "+s);
    }
    private void  caub(int n){
        int s = 0;
        for (int i=1;i<=n;i++){
            if(i%2==0)
                {s+=i;
                    System.out.print(i+" ");
                } 
        }
        System.out.println("tong chan cua "+n+" la: "+s);
    }
    private void  cauc(int n){
        int s = 0;
        for (int i=1;i<=n;i++){
            if(i%2!=0)
               { s+=i;
                System.out.print(i+" ");
               } 
        }
        System.out.println("tong le cua "+n+" la: "+s);
    }
    private void  caud(int n){
        int s = 0;
        for (int i=1;i<=n;i++){
            if(prime(i))
                {System.out.print(i+" ");
                    s+=i;}
        }
        System.out.println("tong snt cua "+n+" la: "+s);
    }
    private void  caue(int n){
        int s = 0;
        int i = 2;
        int count = 0;
        while(count<n){
            if(prime(i))
                {s+=i;
                count++;
            System.out.print(i+" ");}
            i++;
        }
        System.out.println("tong "+n+" snt dau tien la: "+s);
    }
    public void out7(){
        int soN = nhapsonguyen();
        caua(soN);
        caub(soN);
        cauc(soN);
        caud(soN);
        caue(soN);
    }
    public static void main(String[] args) {
        
        System.out.println("xin chao:");
        testJDBC test = new testJDBC();
        System.out.println("cau 1:");
        test.out1();
        System.out.println("cau 2: ");
        test.out2();
        System.out.println("cau 3:");
        test.out3();
        System.out.println("cau 4:");
        test.out4();
        System.out.println("cau 5:");
        test.out5();
        System.out.println("cau 6:");
        test.out6();
        System.out.println("cau 7:");
        test.out7();
        scanner.close();
    }
}
