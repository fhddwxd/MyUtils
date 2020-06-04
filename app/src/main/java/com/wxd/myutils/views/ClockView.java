package com.wxd.myutils.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2018/3/28.
 *  二进制时钟
 */

public class ClockView extends View {
    private TimerTask timerTask;
    private Timer timer;
    Canvas canvas = null;
    Paint inFill = null;
    Paint backGround = null;
    Paint circleBackGround = null;
    Paint text =null;
    int cWidth = 0;
    int cHeight = 0;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inFill = new Paint();
        backGround = new Paint();
        circleBackGround = new Paint();
        text =new Paint();
        //inFill.setColor(Color.RED);
        inFill.setColor(Color.WHITE);
        circleBackGround.setColor(Color.GRAY);
        backGround.setColor(Color.TRANSPARENT);
        text.setColor(Color.WHITE);
        text.setTextSize(50);
    }
    @Override
    public void onDraw(Canvas canvas) {

        this.canvas = canvas;

        this.cWidth = canvas.getWidth();
        this.cHeight = canvas.getHeight();

        int cWidth = this.cWidth /10;

        int hoursColPos = cWidth;
        int minsColPos = cWidth * 2;
        int secsColPos = cWidth * 3;

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int secs = cal.get(Calendar.SECOND);
        int hour1 =hour/10;
        int hour2 =hour%10;
        int minute1 =minute/10;
        int minute2 =minute%10;
        int secs1 =secs/10;
        int secs2 =secs%10;
        // Colour the Backgound
        this.canvas.drawPaint(this.backGround);

        int circleRadius = this.cWidth / 13;
        this.canvas.drawText("8",cWidth,20+circleRadius,text);
        this.canvas.drawText("4",cWidth,20+(circleRadius+40)*1+circleRadius,text);
        this.canvas.drawText("2",cWidth,20+(circleRadius+40)*2+circleRadius,text);
        this.canvas.drawText("1",cWidth,20+(circleRadius+40)*3+circleRadius,text);
        drawColumn(cWidth*2, 40+(circleRadius+40)*2, hour1, 2, circleRadius);
        drawColumn(cWidth*2+(circleRadius+40), 40, hour2, 4, circleRadius);
        this.canvas.drawText(":",cWidth*2+(circleRadius+40)*2-20,40+(circleRadius+40)*4+circleRadius,text);
        drawColumn(cWidth*2+(circleRadius+40)*2, 40+(circleRadius+40), minute1, 3, circleRadius);
        drawColumn(cWidth*2+(circleRadius+40)*3, 40, minute2, 4, circleRadius);
        this.canvas.drawText(":",cWidth*2+(circleRadius+40)*4-20,40+(circleRadius+40)*4+circleRadius,text);
        drawColumn(cWidth*2+(circleRadius+40)*4, 40+(circleRadius+40), secs1, 3, circleRadius);
        drawColumn(cWidth*2+(circleRadius+40)*5, 40, secs2, 4, circleRadius);

//        drawColumn(hoursColPos, 150, hour, 6, circleRadius);
//        drawColumn(minsColPos,150, minute, 6, circleRadius);
//        drawColumn(secsColPos, 150, secs, 6, circleRadius);

    }
    public void setCalendar() {
        timeStart();
        timerStart();
    }
    private void timerStart() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 250, 250);
    }

    private void timeStart() {
        invalidate();
    }

    public void drawColumn(int xPos, int yPos, int number, int digits, int legth) {



        int[] bits = convertBinary(number, digits);

        //Log.d("INFO", String.valueOf(bits.length));
        for (int i = 0; i < bits.length; i++) {
            int bottom = yPos + legth + (i * (legth + 40));
            if (bits[i] == 1) {
//                this.canvas.drawCircle(xPos,  yPos + (i * (circleRadius * 3)),  circleRadius, this.inFill);
                this.canvas.drawRect(xPos,yPos+(i*(legth+40)),xPos+legth, bottom,this.inFill);
            } else {
//                this.canvas.drawCircle(xPos,  yPos + (i * (circleRadius * 3)),  circleRadius, this.circleBackGround);
                this.canvas.drawRect(xPos,yPos+(i*(legth+40)),xPos+legth, bottom,this.circleBackGround);
            }
        }
        this.canvas.drawText(number+"",xPos+legth/2-10,yPos+(bits.length*(legth+40))+legth,text);
    }

    public void refreshTime() {
        this.postInvalidate();
    }

    public static int[] convertBinary(int no, int numDigits) {
        int i = 0, temp[] = new int[7];
        int binary[];
        while (no > 0) {
            temp[i++] = no % 2;
            no /= 2;
        }
        // i = num of digits without correct
        binary = new int[numDigits];
        int k = 0;
        for (int j = numDigits - 1; j >= 0; j--) {
            if (j > i) {
                binary[k] = 0;
            } else {
                binary[k] = temp[j];
            }
            k++;
        }

        return binary;
    }
}
