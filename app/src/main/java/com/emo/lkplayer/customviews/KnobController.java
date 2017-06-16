package com.emo.lkplayer.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emo.lkplayer.R;

/**
 * Created by shoaibanwar on 6/12/17.
 */

public class KnobController extends RelativeLayout {

    public interface KnobControllerEventsListener{
        void onRotate(int percentage);
    }

    private View rootView;
    private RotatingKnobController rotatingKnobController;
    private TextView leftTop,leftBottom,rightTop,rightBottom;

    public KnobController(Context context, AttributeSet attrs) {
        super(context, attrs);

        rootView                = LayoutInflater.from(context).inflate(R.layout.knob_controller,this);
        rotatingKnobController  = (RotatingKnobController) rootView.findViewById(R.id.cv_KnobController);
        leftTop                 = (TextView) rootView.findViewById(R.id.tv_KnobText1);
        leftBottom              = (TextView) rootView.findViewById(R.id.tv_KnobText3);
        rightTop                = (TextView) rootView.findViewById(R.id.tv_KnobText2);
        rightBottom             = (TextView) rootView.findViewById(R.id.tv_KnobText4);
    }

    public void setTopLeftText(String text){
        this.leftTop.setText(text);
        this.leftTop.setVisibility(VISIBLE);
    }

    public void setTopRightText(String text){
        this.rightTop.setText(text);
        this.rightTop.setVisibility(VISIBLE);
    }

    public void setBottomLeftText(String text){
        this.leftBottom.setText(text);
        this.leftBottom.setVisibility(VISIBLE);
    }

    public void setBottomRightText(String text){
        this.rightBottom.setText(text);
        this.rightBottom.setVisibility(VISIBLE);
    }

    public void setMinLevel(int minPercentage){
        this.rotatingKnobController.setMinPercent(minPercentage);
    }

    public void setMaxLevel(int maxPercentage){
        this.rotatingKnobController.setMaxPercent(maxPercentage);
    }

    public void setMinMaxLevel(int minPercentage,int maxPercentage){
        this.rotatingKnobController.setMinPercent(minPercentage);
        this.rotatingKnobController.setMaxPercent(maxPercentage);
    }

    public void setKnobControllerEventsListener(KnobControllerEventsListener knobControllerEventsListener){
        this.rotatingKnobController.setKnobControllerEventsListener(knobControllerEventsListener);
    }

    public void setCurrentLevel(int currentLevel){
        this.rotatingKnobController.setCurrentPercent(currentLevel);
    }

    public static class RotatingKnobController extends View implements GestureDetector.OnGestureListener {

        private static final int NUM_OF_DEGREES_STANDING_FOR_ONE_PERCENT = 3;
        private Context context;
        private GestureDetector gestureDetector;
        private Matrix rotationMatrix = new Matrix();
        private Bitmap scalerBitmap;
        private Bitmap rotatorBitmap;
        int imageWidth=-1,imageHeight=-1,minPercent,maxPercent,currentPercent;
        //private float mAngleDown, mAngleUp;

        private KnobControllerEventsListener mListener;

        public RotatingKnobController(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);

            this.context = context;
            this.gestureDetector = new GestureDetector(this.context, this);
            scalerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cus_stator);
            rotatorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cus_rotator);
        }

        public void setMinPercent(int minPercent){
            this.minPercent = minPercent;
        }

        public void setMaxPercent(int maxPercent){
            this.maxPercent = maxPercent;
        }

        public int getMinPercent(){
            return minPercent;
        }

        public int getMaxPercent(){
            return maxPercent;
        }

        public void setCurrentPercent(int currentPercent){
            this.currentPercent = currentPercent;
            this.setRotorPosAngle((float)this.currentPercent*NUM_OF_DEGREES_STANDING_FOR_ONE_PERCENT);
        }

        public void setKnobControllerEventsListener(KnobControllerEventsListener rotatingKnobEventsListener){
            this.mListener = rotatingKnobEventsListener;
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

                scalerBitmap = Bitmap.createScaledBitmap(scalerBitmap, imageWidth, imageHeight, true);
                rotatorBitmap = Bitmap.createScaledBitmap(rotatorBitmap, imageWidth, imageHeight, true);
            }


            canvas.drawBitmap(scalerBitmap, 0.0f,0.0f, null);
            canvas.drawBitmap(rotatorBitmap, rotationMatrix, null);
        }

        private float cartesianToPolar(float x, float y) {
            return (float) -Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
        }

        private void setRotorPosAngle(float deg) {

            if (deg >= 210 || deg <= 150) {
                //if (deg > 180) deg = deg - 360;
                rotationMatrix.setRotate((float) deg, (float)imageWidth/2, (float)imageHeight/2);//getWidth()/2, getHeight()/2);
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
            //--mAngleDown = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float x = e.getX() / ((float) getWidth());
            float y = e.getY() / ((float) getHeight());
            //--mAngleUp = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
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
                    if (mListener != null)
                    {
                        mListener.onRotate(percent);
                    }
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

    }
}
