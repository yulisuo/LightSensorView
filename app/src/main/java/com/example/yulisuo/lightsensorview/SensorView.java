package com.example.yulisuo.lightsensorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by yulisuo on 16-11-23.
 */
public class SensorView extends View {

    private static final String TAG = "SensorView";

    private ArrayList<Integer> datas = new ArrayList<>();
//    private ArrayDeque<Integer> datas = new ArrayDeque<>();
    Paint paint = new Paint();
    private int count;
    private int startX;
    private int width;
    private int height;
    private int stepX;
    private int retioY;

    public SensorView(Context context) {
        super(context);
        init(context);
    }


    public SensorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SensorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = (int)(wm.getDefaultDisplay().getHeight() * 0.7);
        startX = (int)(width * 0.05);
        stepX = (int)(width * 0.005);
        retioY = 3;
        count = (width - 2*startX)/stepX;
        Log.d(TAG,"SensorView init,width:"+width+",height:"+height+",startX:"+startX+",stepX:"+stepX+",retioY:"+retioY+",count:"+count);
    }

    public void addDatas(int data){
        Log.d(TAG,"SensorView addDatas:"+data);
        if(datas != null){
            datas.add(data);
            if(datas.size() > count){
                datas.remove(0);
            }
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG,"SensorView onDraw");
        paint.setColor(Color.BLUE);
        paint.setTextSize(20);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("0",startX,height,paint);
        canvas.drawLine(startX,height,width - startX,height,paint);
        canvas.drawText("90",startX,height - 90 * retioY,paint);
        canvas.drawLine(startX,height - 90 * retioY,width - startX,height - 90 * retioY,paint);
        paint.setColor(Color.RED);
        int positionX = startX;       //x position
        for (int i = 0;i < datas.size() - 1;i++){
            int beginX = positionX;
            int beginY = height - datas.get(i) * retioY;
            int endX = positionX + stepX;
            int endY = height - datas.get(i + 1)*retioY;
            canvas.drawLine(beginX,beginY,endX,endY,paint);
            Log.d(TAG,"SensorView drawline,begin:("+beginX+","+beginY+"),end:("+endX+","+endY+")");
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            positionX += stepX;
            if(positionX > width - startX){
                break;
            }
        }
    }
}
