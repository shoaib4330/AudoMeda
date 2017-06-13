package com.emo.audomeda.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.emo.audomeda.R;

/**
 * Created by shoaibanwar on 6/13/17.
 */

public class VerticalSlider extends View implements GestureDetector.OnGestureListener {

    private static final class ComponentPositionHolder{
        private float pointX=0;
        private float pointY=0;

        public ComponentPositionHolder(float x,float y){
            this.pointX=x;
            this.pointY=y;
        }

        public float getPointX() {
            return pointX;
        }

        public float getPointY() {
            return pointY;
        }

        public void setPointX(float x){
            this.pointX = x;
        }

        public void setPointY(float y){
            this.pointY=y;
        }

    }

    private static final float LINE_WIDTH_DIVISION_FACTOR = 4.7f;
    private static final float MOVER_WIDTH_DIVISION_FACTOR = 4.0f;

    private int minusFactoredWidth=-1;
    private int minusFactoredHeight=-1;

    private ComponentPositionHolder mover_locationHolder;
    private ComponentPositionHolder line_locationHolder;

    private Bitmap mover;
    private Bitmap line;

    private GestureDetector mGestureDetector;



    public VerticalSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.mGestureDetector = new GestureDetector(context, this);
        //mover   = BitmapFactory.decodeResource(context.getResources(), R.drawable.mover);
        //line    = BitmapFactory.decodeResource(context.getResources(), R.drawable.line);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (minusFactoredWidth==-1 || minusFactoredHeight==-1)
        {
            minusFactoredWidth  = getWidth()-getPaddingLeft()-getPaddingRight();
            minusFactoredHeight = getHeight()-getPaddingTop()-getPaddingBottom();
        }

        if (mover==null || line==null){
            if (mover==null){
                float knobSize = minusFactoredWidth/MOVER_WIDTH_DIVISION_FACTOR;
                mover = decodeSampledBitmapFromResource(getResources(),R.drawable.mover,(int)knobSize,(int)knobSize);
            }
            if (line==null){
                double lineWidth = (double) minusFactoredWidth/LINE_WIDTH_DIVISION_FACTOR;
                line = decodeSampledBitmapFromResource(getResources(),R.drawable.lineo,(int)lineWidth,minusFactoredHeight);
            }
        }

        //minusFactoredWidth is being used in both ORed conditions on purpose
        if (minusFactoredWidth/MOVER_WIDTH_DIVISION_FACTOR != mover.getWidth() || minusFactoredWidth/MOVER_WIDTH_DIVISION_FACTOR != mover.getHeight()){

            float knobSize = minusFactoredWidth/MOVER_WIDTH_DIVISION_FACTOR;
            mover = Bitmap.createScaledBitmap(mover,(int)knobSize,(int)knobSize,true);
        }

        if ((float)minusFactoredWidth/LINE_WIDTH_DIVISION_FACTOR != line.getWidth() || (float)minusFactoredHeight!=line.getHeight())
        {
            double lineWidth = (double) minusFactoredWidth/LINE_WIDTH_DIVISION_FACTOR;
            line = Bitmap.createScaledBitmap(line,(int)lineWidth,minusFactoredHeight,true);
        }

        if (line_locationHolder==null){
            //calculate initial position for line
            float yPoint=0+getPaddingTop();
            float xPoint= (int)((double)getWidth()/2.0 - (double) line.getWidth()/2.0);
            //set position for line
            line_locationHolder = new ComponentPositionHolder(calculateXPositionForLine(),calculateStart_Y_PositionForLine());
        }

        if (mover_locationHolder==null){
            //calculate initial position for mover/knob
            float mover_yPoint = getHeight()-getWidth()/MOVER_WIDTH_DIVISION_FACTOR;
            float mover_xPoint = (int)((double)getWidth()/2.0 - (double) mover.getWidth()/2.0);
            //set position for mover/knob
            mover_locationHolder = new ComponentPositionHolder(mover_xPoint,mover_yPoint);
        }
        canvas.drawBitmap(line,line_locationHolder.getPointX(),line_locationHolder.getPointY(),null);
        canvas.drawBitmap(mover,mover_locationHolder.getPointX(),mover_locationHolder.getPointY(),null);
    }

    private float calculateXPositionForLine(){
        return (float)((double)getWidth()/2.0 - (double) line.getWidth()/2.0);
    }

    private float calculateStart_Y_PositionForLine(){
        return 0+getPaddingTop();
    }

    private float calculateEnd_Y_PositionForLine(){
        return calculateStart_Y_PositionForLine()+minusFactoredHeight;
    }

    private void updatePos(float yPos){
        if (yPos > calculateStart_Y_PositionForLine() && yPos < calculateEnd_Y_PositionForLine()){
            mover_locationHolder.setPointY(yPos);
            this.invalidate();
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event) == true) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        float xLoc = e.getX();
        float yLoc = e.getY();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //doing nothing, just saying that its consumed by returning true
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float yLoc = e2.getY();
        updatePos(yLoc);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
