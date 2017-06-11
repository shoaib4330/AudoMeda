package com.emo.audomeda.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.emo.audomeda.R;

/**
 * Created by shoaibanwar on 6/10/17.
 */

public final class RotatingKnobControllerUse extends RelativeLayout implements GestureDetector.OnGestureListener {

    private Context context;
    private GestureDetector gestureDetector;
    private float mAngleDown, mAngleUp;

    private ImageView iv_Scaler;
    private ImageView iv_Rotator;

    int count =0;
    //Bitmap scalerBitmap;
    //Bitmap rotatorBitmap;

    public RotatingKnobControllerUse(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.gestureDetector = new GestureDetector(this.context, this);
        setWillNotDraw(false);
        //init();
    }

    private void init(){
        LayoutParams layoutParams = new LayoutParams(getWidth(),getHeight());
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        iv_Scaler = new ImageView(context);
        iv_Rotator = new ImageView(context);

        iv_Rotator.setScaleType(ImageView.ScaleType.FIT_XY);

        Bitmap dotScalerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dots);
        Bitmap rotatingKnobBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotoron);

        dotScalerBitmap    = Bitmap.createScaledBitmap(dotScalerBitmap, getWidth(), getHeight(), true);
        //rotatingKnobBitmap = Bitmap.createScaledBitmap(rotatingKnobBitmap, getWidth(), getHeight(), true);

        iv_Scaler.setImageBitmap(dotScalerBitmap);
        iv_Rotator.setImageBitmap(rotatingKnobBitmap);

        addView(iv_Scaler,layoutParams);
        addView(iv_Rotator,layoutParams);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
    }

    private float cartesianToPolar(float x, float y) {
        return (float) -Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
    }

    public void setRotorPosAngle(float deg) {

        if (deg >= 210 || deg <= 150) {
            if (deg > 180) deg = deg - 360;
            Matrix matrix = new Matrix();
            //matrix.postRotate((float) deg, getWidth() / 2, getHeight() / 2);//getWidth()/2, getHeight()/2);
            //iv_Rotator.setScaleType(ImageView.ScaleType.MATRIX);
            //iv_Rotator.setImageMatrix(matrix);
            RotatingKnobControllerUse.RotateTask rotateTask = new RotateTask(matrix,deg,getWidth()/2,getHeight()/2);
            rotateTask.execute();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event) == true) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        count=0;
        float x = e.getX() / ((float) getWidth());
        float y = e.getY() / ((float) getHeight());
        mAngleDown = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        float x = e.getX() / ((float) getWidth());
        float y = e.getY() / ((float) getHeight());
        mAngleUp = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("HOLA HERE","Count is: "+count);
        count++;
        float x = e2.getX() / ((float) getWidth());
        float y = e2.getY() / ((float) getHeight());
        float rotDegrees = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction

        if (!Float.isNaN(rotDegrees)) {
            // instead of getting 0-> 180, -180 0 , we go for 0 -> 360
            float posDegrees = rotDegrees;
            if (rotDegrees < 0) {
                posDegrees = 360 + rotDegrees;
            }

            // deny full rotation, start start and stop point, and get a linear scale
            if (posDegrees > 210 || posDegrees < 150) {
                // rotate our imageview
                setRotorPosAngle(posDegrees);
                // get a linear scale
                float scaleDegrees = rotDegrees + 150; // given the current parameters, we go from 0 to 300
                // get position percent
                int percent = (int) (scaleDegrees / 3);
                //if (m_listener != null) m_listener.onRotate(percent);
                return true; //consumed
            } else
                return false;
        } else {
            return false; // not consumed
        }

    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private class RotateTask extends AsyncTask<Void,Void,Matrix>{
        //private Bitmap inputImage;
        private Matrix matrix;
        private float degreesToRotate;
        private int xCentre,yCentre;

        public RotateTask(Matrix matrix,float degreesToRotate,int xCentre,int yCentre){
            //this.inputImage = inputBitmap;
            this.matrix = matrix;
            this.degreesToRotate = degreesToRotate;
            this.xCentre = xCentre;
            this.yCentre = yCentre;
        }

        @Override
        protected Matrix doInBackground(Void... params) {
            matrix.postRotate((float) degreesToRotate, xCentre / 2, yCentre / 2);
            //inputImage.s
            //this.inputImage.setScaleType(ImageView.ScaleType.MATRIX);
            //iv_Rotator.setImageMatrix(matrix);
            return null;
        }

        @Override
        protected void onPostExecute(Matrix aVoid) {
            iv_Rotator.setScaleType(ImageView.ScaleType.MATRIX);
            iv_Rotator.setImageMatrix(aVoid);
        }
    }

}
