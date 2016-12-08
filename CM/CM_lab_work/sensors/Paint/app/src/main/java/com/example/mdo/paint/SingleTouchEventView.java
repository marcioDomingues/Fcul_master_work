package com.example.mdo.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static java.lang.Math.abs;
import static java.lang.Math.round;

/**
 * Created by MDo on 02/11/16.
 */
public class SingleTouchEventView extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, SensorEventListener {

    private Paint paint = new Paint();
    private Path path = new Path();

    private GestureDetectorCompat gm;

    private SensorManager sensorManager;
    private Sensor accel, prox;
    //compare new accel events to old to get how changed
    private float eventOldAccel[] = {0,0,0};
    private int xDiff,yDiff,zDiff;
    private int smallDeltaDiff, bigDeltaDiff;


    private ArrayList<Path> paths = new ArrayList<Path>();

    public SingleTouchEventView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);


        sensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        //sensors registry
        //accel
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel, Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_NORMAL);
        //prox
        prox = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this, prox, SensorManager.SENSOR_DELAY_NORMAL);


        gm = new GestureDetectorCompat(this.getContext(),this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Path path: paths ) {
            canvas.drawPath(path, paint);
        }
    }



    /*
    * TOUCH DETECTION
    **/
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
                paths.add( path1 );

                return true;
            case MotionEvent.ACTION_MOVE:
                //path.lineTo(eventX, eventY);
                paths.get( paths.size() - 1 ).lineTo(eventX,eventY);
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





    /*
    * SENSORS DETECTION
    **/
    @Override
    public void onSensorChanged(SensorEvent event) {


        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            smallDeltaDiff = 10;
            bigDeltaDiff = 20;

            xDiff = abs (round( eventOldAccel[0] - event.values[0] ));
            yDiff = abs (round( eventOldAccel[1] - event.values[1] ));
            zDiff = abs (round( eventOldAccel[2] - event.values[2] ));

            eventOldAccel[0]=event.values[0];
            eventOldAccel[1]=event.values[1];
            eventOldAccel[2]=event.values[2];

            if( paths.size() > 1 ){

                if((xDiff) > (bigDeltaDiff) ||
                        (yDiff) > (bigDeltaDiff) ||
                        (zDiff) > (bigDeltaDiff) ){
                    //Log.v(TAG, "accel: [ left_right x_"+xDiff+"up_Down y_"+yDiff+"front_back z_"+zDiff+"]");
                    for(Path path: paths ) {
                        path.reset();
                    }
                    paths.clear();
                    invalidate();
                    return;

                }

                if( (xDiff) > (smallDeltaDiff)  ||
                        (yDiff) > (smallDeltaDiff)  ||
                        (zDiff) > (smallDeltaDiff)  ){
                    Log.v(TAG, "accel: [ left_right x_"+xDiff+"up_Down y_"+yDiff+"front_back z_"+zDiff+"]___"+(paths.size() - 1));

                    Path path1 = paths.get( paths.size() - 1 );
                    path1.reset();
                    paths.remove( path1 );
                    invalidate();
                    return;
                }

            }








        }else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            //Log.v(TAG, "Proximity: " + String.valueOf(event.values[0]) );
            if ( event.values[0] <= 1 ){
                this.setBackgroundColor(Color.rgb(0,0,0));
            }else{
                this.setBackgroundColor(Color.rgb(255,255,255));
            }
        }


        /*
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if(event.values[0]> 40 || event.values[1]> 40 || event.values[2]> 40){
                paths.remove(paths.size());
                invalidate();
            }
        }*/


    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
