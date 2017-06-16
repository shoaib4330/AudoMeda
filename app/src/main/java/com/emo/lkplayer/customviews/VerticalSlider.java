package com.emo.lkplayer.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.emo.lkplayer.R;

import java.util.Map;

/**
 * Created by shoaibanwar on 6/13/17.
 */

public class VerticalSlider extends View implements GestureDetector.OnGestureListener {

    private static final class ComponentPositionHolder {
        private float pointX = 0;
        private float pointY = 0;

        public ComponentPositionHolder(float x, float y) {
            this.pointX = x;
            this.pointY = y;
        }

        public float getPointX() {
            return pointX;
        }

        public float getPointY() {
            return pointY;
        }

        public void setPointX(float x) {
            this.pointX = x;
        }

        public void setPointY(float y) {
            this.pointY = y;
        }

    }

    private static final class ScreenSizesPlusComponentConfigurationHolder {
        private static final String[] SCREENS_SIZES_NAMES = {"typical phone", "tweener tablet", "a 7inch tablet", "a 10inch tablet"};
        private static final float[] SCREENS_SIZES_DP = {320.0f, 480.0f, 600.0f, 720.0f};

        public static int suggestSliderWidth(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

            if (dpWidth >= SCREENS_SIZES_DP[0] && dpWidth <= SCREENS_SIZES_DP[1]) {
                return (int) (40 * dpWidth/SCREENS_SIZES_DP[0]); // this is in pixels
            } else if (dpWidth >= SCREENS_SIZES_DP[1] && dpWidth <= SCREENS_SIZES_DP[2]) {
                return (int) ( 60* dpWidth/SCREENS_SIZES_DP[1]); // this is in pixels
            } else if (dpWidth >= SCREENS_SIZES_DP[2] && dpWidth <= SCREENS_SIZES_DP[3]) {
                return (int) (80 * dpWidth/SCREENS_SIZES_DP[2]); // this is in pixels
            } else {
                return (int) (100 * dpWidth/SCREENS_SIZES_DP[3]); // this is in pixels
            }
        }

        public static int suggestSliderHeight(Context context) {
            return suggestSliderWidth(context); //since our png is square. to keep the aspect ratio
            //it must be resized by keeping size square that is
            //keeping height and widht equal.
        }

        public static int DptoPixels(Context context, int dps) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int px = Math.round(dps * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

    }


    private static final int lineWidthInDps = 10; /*height will be equal to "minusFactoredViewHeight" property
                                        i.e. view's height */

    private static final int MUST_LINE_TOP_AND_DOWN_MARGIN = 15; //in dp

    /* height and width that this view has been given by taking paddings into account */
    private int minusFactoredViewWidth = -1;
    private int minusFactoredViewHeight = -1;

    private final int moverWidth = ScreenSizesPlusComponentConfigurationHolder.suggestSliderWidth(getContext());
    private final int moverHeight = ScreenSizesPlusComponentConfigurationHolder.suggestSliderHeight(getContext());


    private ComponentPositionHolder mover_locationHolder;
    //private ComponentPositionHolder line_locationHolder;

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
        setPadding(getPaddingLeft(),MUST_LINE_TOP_AND_DOWN_MARGIN,getPaddingRight(),MUST_LINE_TOP_AND_DOWN_MARGIN);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (minusFactoredViewWidth == -1 || minusFactoredViewHeight == -1) {
            minusFactoredViewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            minusFactoredViewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        }

        if (mover == null || line == null) {
            if (mover == null) {
                mover = decodeSampledBitmapFromResource(getResources(), R.drawable.cus_slider, moverWidth, moverHeight);
            }
            if (line == null) {
                int lineWidth = Math.round(lineWidthInDps);
                line = decodeSampledBitmapFromResource(getResources(), R.drawable.lineo, lineWidth, minusFactoredViewHeight);
            }
        }

        if (mover_locationHolder == null) {
            //set position for mover/knob
            mover_locationHolder = new ComponentPositionHolder(Math.round(slider_get_X_asCentered()), Math.round(slider_get_Y_forDownEndLimit()));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable sld = getContext().getApplicationContext().getResources().getDrawable(R.drawable.testverslider, getContext().getTheme());
            sld.setBounds((int) getLineLeftX(), (int) getLineTopY(), (int) getLineRightX(), (int) getLineEndY());
            sld.draw(canvas);
        }
        canvas.drawBitmap(mover, mover_locationHolder.getPointX(), mover_locationHolder.getPointY(), null);
    }

    private double slider_get_X_asCentered() {
        double mover_xPoint = getWidth() - getWidth() / 2.0 - mover.getWidth() / 2.0;
        return mover_xPoint;
    }

    private double slider_get_Y_forDownEndLimit() {
        /* Slider-knob cannot scroll below this y-coordinate */
        double mover_yPoint = (getPaddingTop() + minusFactoredViewHeight - mover.getHeight() + mover.getHeight()/2.0);
        return mover_yPoint;
    }

    private double slider_get_Y_forTopEndLimit() {
        double mover_yPoint = 0.0f + getPaddingTop() - mover.getHeight()/2.0;
        return mover_yPoint;
    }

    private double getLineLeftX() {
        double xPoint =  getWidth()-getWidth() / 2.0 - ScreenSizesPlusComponentConfigurationHolder.DptoPixels(getContext(), lineWidthInDps)/2.0;
        return xPoint;
    }

    private double getLineRightX() {
        double xPoint = getLineLeftX() + ScreenSizesPlusComponentConfigurationHolder.DptoPixels(getContext(), lineWidthInDps);
        return xPoint;
    }

    private float getLineTopY() {
        float yPoint = 0 + getPaddingTop() + ScreenSizesPlusComponentConfigurationHolder.DptoPixels(getContext(), MUST_LINE_TOP_AND_DOWN_MARGIN);
        return yPoint;
    }

    private float getLineEndY() {
        float yPoint = getLineTopY() + minusFactoredViewHeight - ScreenSizesPlusComponentConfigurationHolder.DptoPixels(getContext(), MUST_LINE_TOP_AND_DOWN_MARGIN);
        return yPoint;
    }

    private void updatePos(float yPos) {
        if (yPos > slider_get_Y_forTopEndLimit() && yPos < slider_get_Y_forDownEndLimit()) {
            if (yPos > slider_get_Y_forTopEndLimit()) {
                //-mover.getHeight()/2.0
                mover_locationHolder.setPointY(Math.round(yPos));
            } else {
                //+mover.getHeight()/2.0
                mover_locationHolder.setPointY(Math.round(yPos));
            }
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
        //this.updatePos(yLoc);
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
