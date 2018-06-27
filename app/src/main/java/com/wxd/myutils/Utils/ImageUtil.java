package com.wxd.myutils.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import java.math.BigDecimal;

/**
 * 图片处理工具类
 */
public class ImageUtil {
    /**
     * 把照片转为圆形
     *
     * @param bitmap 原始照片
     * @return 圆形照片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap) {
       /* int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }
        Bitmap output = null;
        try {
            output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            return null;
        }
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);*/


        int minEdge = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int dx = (bitmap.getWidth() - minEdge) / 2;
        int dy = (bitmap.getHeight() - minEdge) / 2;

        // Init shader
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate(-dx, -dy);   // Move the target area to center of the source bitmap
        shader.setLocalMatrix(matrix);

        // Init paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(shader);

        // Create and draw circle bitmap
        Bitmap output = Bitmap.createBitmap(minEdge, minEdge, bitmap.getConfig());
        Canvas canvas = new Canvas(output);
        canvas.drawOval(new RectF(0, 0, minEdge, minEdge), paint);

        // Recycle the source bitmap, because we already generate a new one
       /* bitmap.recycle();*/
        return output;

    }

    public static Bitmap toRoundCorner2(Bitmap bitmap){
        int width =bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas =new Canvas(output);
        Paint paint =new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(0,0,width,height),10,10,paint);
        return output;
    }

    /**
     * 图片尺寸压缩
     * 压缩根据长宽比例
     *
     * @param bm       原始图片
     * @param longSize 想要的长边尺寸
     * @return 压缩后的图片
     */
    public static Bitmap scaleImg(Bitmap bm, int longSize) {

        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();

        // 设置想要的大小
        int newWidth1 = 0;
        int newHeight1 = 0;
        if (width > height) {
            newWidth1 = longSize;
            BigDecimal bWidth = new BigDecimal(Double.toString(width));
            BigDecimal bHeight = new BigDecimal(Double.toString(height));
            BigDecimal bLongSize = new BigDecimal(Double.toString(longSize));
            newHeight1 = bLongSize.multiply(bWidth.divide(bHeight, 4)).intValue();
        } else {
            newHeight1 = longSize;
            BigDecimal bWidth = new BigDecimal(Double.toString(width));
            BigDecimal bHeight = new BigDecimal(Double.toString(height));
            BigDecimal bLongSize = new BigDecimal(Double.toString(longSize));
            newWidth1 = bLongSize.multiply(bWidth.divide(bHeight, 4)).intValue();
        }
        // 计算缩放比例
        float scaleWidth = ((float) newWidth1) / width;
        float scaleHeight = ((float) newHeight1) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }
}
