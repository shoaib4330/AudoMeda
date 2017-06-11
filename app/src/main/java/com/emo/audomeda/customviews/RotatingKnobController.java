package com.emo.audomeda.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emo.audomeda.R;

import java.lang.ref.WeakReference;

/**
 * Created by shoaibanwar on 6/10/17.
 */

public final class RotatingKnobController extends View implements GestureDetector.OnGestureListener {

    private Context context;
    private GestureDetector gestureDetector;
    private float mAngleDown, mAngleUp;

    //private ImageView iv_Scaler;
    //private ImageView iv_Rotator;
    Bitmap scalerBitmap;
    Bitmap rotatorBitmap;
    int imageWidth=-1;
    int imageHeight=-1;

    Matrix ttmatrix = new Matrix();


    public RotatingKnobController(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.gestureDetector = new GestureDetector(this.context, this);
        scalerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dots);
        rotatorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.knob);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /** will see later if this needs explicit implementation
         *
         */
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (imageHeight==-1 && imageWidth==-1){
            imageWidth = getWidth();
            imageHeight = getHeight();
        }
        scalerBitmap = Bitmap.createScaledBitmap(scalerBitmap, imageWidth, imageHeight, true);
        rotatorBitmap = Bitmap.createScaledBitmap(rotatorBitmap, imageWidth, imageHeight, true);

        canvas.drawBitmap(scalerBitmap, 0.0f,0.0f, null);
        canvas.drawBitmap(rotatorBitmap, ttmatrix, null);
    }

    private float cartesianToPolar(float x, float y) {
        return (float) -Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
    }

    public void setRotorPosAngle(float deg) {

        if (deg >= 210 || deg <= 150) {
            if (deg > 180) deg = deg - 360;
            ttmatrix.setRotate((float) deg, (float)imageWidth/2, (float)imageHeight/2);//getWidth()/2, getHeight()/2);
        }
        invalidate();
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
        float x = e.getX() / ((float) getWidth());
        float y = e.getY() / ((float) getHeight());
        Log.d("ME, X and Y: "," "+e.getX()+" and "+e.getY());
        Log.d("ME, Xba and Yba: "," "+x+" and "+y);
        mAngleDown = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
        //setRotorPosAngle(33);
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
        float x = e2.getX() / ((float) getWidth());
        float y = e2.getY() / ((float) getHeight());
        float rotDegrees = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction

        if (!Float.isNaN(rotDegrees))
        {
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
        }
        else
        {
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

//    public class RotateTask extends AsyncTask<Void, Void, Bitmap> {
//        private Bitmap inputBitmap;
//        private Matrix rotMatrix;
//        private int imageWidth;
//        private int imageHeight;
//
//        public RotateTask(Bitmap inputBitmap, Matrix rotMatrix,int imageWidth,int imageHeight) {
//            this.inputBitmap = inputBitmap;
//            this.rotMatrix = rotMatrix;
//            this.imageWidth = imageWidth;
//            this.imageHeight = imageHeight;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            //if you want to show progress dialog
//        }
//
//        @Override
//        protected Bitmap doInBackground(Void... params) {
//            Bitmap rotateBitmap = Bitmap.createBitmap(inputBitmap, 0, 0, this.imageWidth, this.imageHeight, rotMatrix, false);
//            return rotateBitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            //dismiss progress dialog
//            rotatorBitmap = result;
//            postInvalidate();
//        }
//    }
}
