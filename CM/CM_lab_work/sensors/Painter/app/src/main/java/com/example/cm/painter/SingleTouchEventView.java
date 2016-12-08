package com.example.cm.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Random;

import android.hardware.Sensor;


public class SingleTouchEventView extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, SensorEventListener
        {
    private Paint paint = new Paint();
    private Path path = new Path();

    private GestureDetectorCompat gm;

    private SensorManager sensorManager;
    private Sensor accel;


    private HashMap<Integer, Path> caminhos = new HashMap<Integer, Path>();

    public SingleTouchEventView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);


        sensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel, Sensor.TYPE_ACCELEROMETER);

        caminhos.put(caminhos.size() + 1, path);


        gm = new GestureDetectorCompat(this.getContext(),this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Path caminho: caminhos.values()) {
            canvas.drawPath(caminho, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gm.onTouchEvent(event);
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //path.moveTo(eventX, eventY);
                Path path1 = new Path();
                path1.moveTo(eventX, eventY);
                caminhos.put(caminhos.size()+1 ,path1);
                return true;
            case MotionEvent.ACTION_MOVE:
                //path.lineTo(eventX, eventY);
                caminhos.get(caminhos.size()).lineTo(eventX,eventY);
                break;
            case MotionEvent.ACTION_UP:
                // nothing to do
                break;
            default:
                return false;
        }

        // Schedules a repaint.
        invalidate();
        return true;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Random rnd = new Random();
        paint.setColor(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        Random rnd = new Random();
        this.setBackgroundColor(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if(event.values[0]> 40 || event.values[1]> 40 || event.values[2]> 40){
                caminhos.remove(caminhos.size());
                invalidate();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
