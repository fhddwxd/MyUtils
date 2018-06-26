package com.wxd.myutils.Views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;


public class SectionDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "SectionDecoration";
    private DecorationCallback callback;
    private Context context;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private int leftGap;
    private int bottomGap;
    private Paint.FontMetrics fontMetrics;
    /**
     * 滚动列表的时候是否一直显示悬浮头部
     */
    private boolean showFloatingHeaderOnScrolling=true;

    public SectionDecoration(Context context, DecorationCallback decorationCallback) {
        Resources res = context.getResources();
        this.context =context;
        this.callback = decorationCallback;
        paint = new Paint();
        paint.setColor(Color.parseColor("#E5E5E5"));
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(45);
        textPaint.setColor(Color.BLACK);
        textPaint.getFontMetrics(fontMetrics);
        textPaint.setTextAlign(Paint.Align.LEFT);
        fontMetrics = new Paint.FontMetrics();
        topGap = 60;//32dp
        leftGap = 60;
        bottomGap = 20;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        Log.i(TAG, "getItemOffsets：" + pos);
        long groupId = callback.getGroupId(pos);
        if (groupId < 0) return;
        if (pos== 0 || isFirstInGroup(pos)) {//同组的第一个才添加padding
            outRect.bottom = topGap;
        } else {
            outRect.bottom = 0;
        }
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft()+leftGap;
        int right = parent.getWidth() - parent.getPaddingRight()-leftGap;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            long groupId = callback.getGroupId(position);
            if (groupId < 0) return;
            String textLine = callback.getGroupFirstLine(position);
            if (position == 0 || isFirstInGroup(position)) {
                float top = view.getBottom() ;
                float bottom = view.getBottom()+topGap;
                c.drawRect(left-leftGap, top, right+leftGap, bottom+bottomGap, paint);
                c.drawText(textLine, left, bottom, textPaint);//绘制文本
            }
        }

    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if(!showFloatingHeaderOnScrolling){
            return;
        }
        int firstVisiblePos=((LinearLayoutManager)parent.getLayoutManager()).findFirstVisibleItemPosition()-1;
        if(firstVisiblePos==RecyclerView.NO_POSITION){
            return;
        }else {
            long groupId = callback.getGroupId(firstVisiblePos);
            String title = callback.getGroupFirstLine(firstVisiblePos);
            if (title == null) {
                return;
            }
            boolean flag = false;
            if (callback.getGroupId(firstVisiblePos + 1) != -1 && groupId != callback.getGroupId(firstVisiblePos + 1)) {
                //说明是当前组最后一个元素，但不一定碰撞了
//            Log.e(TAG, "onDrawOver: "+"==============" +firstVisiblePos);
                View child = parent.findViewHolderForAdapterPosition(firstVisiblePos + 1).itemView;
                if (child.getTop() + child.getMeasuredHeight() < topGap) {
                    //进一步检测碰撞
//                Log.e(TAG, "onDrawOver: "+child.getTop()+"$"+firstVisiblePos );
                    c.save();//保存画布当前的状态
                    flag = true;
                    c.translate(0, child.getTop() + child.getMeasuredHeight() - topGap);//负的代表向上
                }
            }
            int left = parent.getPaddingLeft() + leftGap;
            int right = parent.getWidth() - parent.getPaddingRight() - leftGap;
            int top = parent.getPaddingTop();
            int bottom = top + topGap;
            c.drawRect(left - leftGap, top, right + leftGap, bottom + bottomGap, paint);
            c.drawText(title, left, bottom, textPaint);
            if (flag) {
                //还原画布为初始状态
                c.restore();
            }
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos <= 0) {
            return true;
        } else {
            long prevGroupId = callback.getGroupId(pos - 1);
            long groupId = callback.getGroupId(pos);
            return prevGroupId != groupId;
        }
    }

    public void setTopGap(int topGap) {
        this.topGap = topGap;
    }
    public void setLeftGap(int leftGap){this.leftGap = leftGap; }
    public void setBottomGap(int bottomGap){this.bottomGap = bottomGap;}
    public void setTopColor(int color){
        paint.setColor(color);
    }
    public void setTopTextColor(int color){
        textPaint.setColor(color);
    }
    public interface DecorationCallback {
        long getGroupId(int position);
        String getGroupFirstLine(int position);
    }
}

