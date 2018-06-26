package com.wxd.myutils.Utils.Utils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/16.
 * 判断是非在封闭图形内(边界不可平行于y轴) 多边形有点问题
 */

public class xixi {
    private static float x1,y1,x2,y2,x3,y3,X1,Y1,X2,Y2;
    private static int num=0;
    public static void main(String[] str){
        mPoint point1=new mPoint(1,0);
        mPoint point2=new mPoint(2,1);
        mPoint point3=new mPoint(3,0);
        mPoint point4=new mPoint(4,2);
        mPoint point5=new mPoint(3,3);
        mPoint point6=new mPoint(0,2);

        mPoint mpoint=new mPoint(3.5f,2.5f);
        List<mPoint> lists =new ArrayList<mPoint>();
        lists.add(point1);
        lists.add(point2);
        lists.add(point3);
        lists.add(point4);
        lists.add(point5);
        lists.add(point6);
        System.out.println("("+mpoint.x+","+mpoint.y+")"+xixi(lists,mpoint));

    }
    public static boolean xixi(List<mPoint> lists, mPoint mPoint){
        Y1 =(mPoint.y/mPoint.x)*X1;//(x1<mPoint.x&&y1<mPoint.y)
        if(lists.size()>3){
            for (int i=1;i<lists.size();i++){
                x2 = lists.get(i-1).x;
                y2 = lists.get(i-1).y;
                x3 = lists.get(i).x;
                y3 = lists.get(i).y;
                if(x2<=mPoint.x && x3<=mPoint.x){
//                    Min(x2,x3)<X1<Max(x2,x3)
                    if(!(compare(line1(Min(x2,x3),mPoint.x,mPoint.y),line2(Min(x2,x3),x2,y2,x3,y3))
                            ==compare(line1(Max(x2,x3),mPoint.x,mPoint.y),line2(Max(x2,x3),x2,y2,x3,y3)))){
                        num++;
                    }
                }else if (x2<=mPoint.x && x3>=mPoint.x){
//                    x2<X1<mPoint.x
                    if(!(compare(line1(x2,mPoint.x,mPoint.y),line2(x2,x2,y2,x3,y3))
                            ==compare(line1(mPoint.x,mPoint.x,mPoint.y),line2(mPoint.x,x2,y2,x3,y3)))){
                        num++;
                    }
                }else if (x3<=mPoint.x && x2>=mPoint.x){
//                    x3<X1<mPoint.x
                    if(!(compare(line1(x3,mPoint.x,mPoint.y),line2(x3,x2,y2,x3,y3))
                            ==compare(line1(mPoint.x,mPoint.x,mPoint.y),line2(mPoint.x,x2,y2,x3,y3)))){
                        num++;
                    }
                }
            }
            x2 = lists.get(lists.size()-1).x;
            y2 = lists.get(lists.size()-1).y;
            x3 = lists.get(0).x;
            y3 = lists.get(0).y;
            if(x2<=mPoint.x && x3<=mPoint.x){
//                    Min(x2,x3)<X1<Max(x2,x3)
                if(!(compare(line1(Min(x2,x3),mPoint.x,mPoint.y),line2(Min(x2,x3),x2,y2,x3,y3))
                        ==compare(line1(Max(x2,x3),mPoint.x,mPoint.y),line2(Max(x2,x3),x2,y2,x3,y3)))){
                    num++;
                }
            }else if (x2<=mPoint.x && x3>=mPoint.x){
//                    x2<X1<mPoint.x
                if(!(compare(line1(x2,mPoint.x,mPoint.y),line2(x2,x2,y2,x3,y3))
                        ==compare(line1(mPoint.x,mPoint.x,mPoint.y),line2(mPoint.x,x2,y2,x3,y3)))){
                    num++;
                }
            }else if (x3<=mPoint.x && x2>=mPoint.x){
//                    x3<X1<mPoint.x
                if(!(compare(line1(x3,mPoint.x,mPoint.y),line2(x3,x2,y2,x3,y3))
                        ==compare(line1(mPoint.x,mPoint.x,mPoint.y),line2(mPoint.x,x2,y2,x3,y3)))){
                    num++;
                }
            }
            if(num%2==0){
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }
    public  static float Min(float x ,float y){
        if(x<=y){
            return x;
        }else return y;
    }
    public  static float Max(float x,float y){
        if(x<=y){
            return y;
        }else return x;
    }
    public static boolean compare(float x,float y){
        if(x<y){
            return true;
        }else {
            return false;
        }
    }
    public static float line2(float x ,float x2, float y2 ,float x3,float y3){
        float y;
        if(x2-x3==0){
            y=y3;
        }else {
            y =((x-x3)*(y2-y3))/(x2-x3)+y3;
        }
        return y;
    }
    public static float line1(float x,float x1,float y1){
        float y;
        if(x1==0){
            y=0;
        }else {
            y =(y1/x1)*x;
        }
        return y;
    }
}
