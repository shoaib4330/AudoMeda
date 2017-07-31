package com.emo.lkplayer.outerlayer.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emo.lkplayer.R;

/**
 * Created by shoaibanwar on 6/13/17.
 */

public class VerticalSliderWrapped extends LinearLayout {

    public interface SliderCallbacks {
        void onValueChanged(int index, float updatedValue);
        void MeasureTakesPlace(int index);
    }

    private View rootView;
    private VerticalSlider verticalSlider;
    private TextView tv_sliderBottomText;

    public VerticalSliderWrapped(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        rootView = layoutInflater.inflate(R.layout.vertical_slider_wrapped, this);
        verticalSlider = (VerticalSlider) findViewById(R.id.owned_verticalSlider);
        tv_sliderBottomText = (TextView) rootView.findViewById(R.id.tv_owned_verticalSlider);
    }

    public void setIndex(int index)
    {
        verticalSlider.setIndex(index);
    }

    public void setPosViaVal(float value)
    {
        verticalSlider.setPosViaVal(value);
    }

    public void setSliderBottomText(String text)
    {
        this.tv_sliderBottomText.setText(text);
    }

    public void registerToSliderCallbacks(SliderCallbacks sliderCallbacks)
    {
        verticalSlider.addCallbacksListener(sliderCallbacks);
    }

    public void setMinMax(int min, int max, boolean careForCenteredNegPosBoundary)
    {
        verticalSlider.setMinMax(min, max, careForCenteredNegPosBoundary);
    }

    public void setToZero()
    {
        verticalSlider.setZeroPosition();
    }

//    public boolean isSliderLayoutDrawn()
//    {
//        return verticalSlider.isDrawn();
//    }

    public static class VerticalSlider extends View implements GestureDetector.OnGestureListener {


        private static final class ComponentPositionHolder {
            private float pointX = 0;
            private float pointY = 0;

            public ComponentPositionHolder(float x, float y)
            {
                this.pointX = x;
                this.pointY = y;
            }

            public float getPointX()
            {
                return pointX;
            }

            public float getPointY()
            {
                return pointY;
            }

            public void setPointX(float x)
            {
                this.pointX = x;
            }

            public void setPointY(float y)
            {
                this.pointY = y;
            }

        }

        private static final class ScreenSizesPlusComponentConfigurationHolder {
            private static final String[] SCREENS_SIZES_NAMES = {"typical phone", "tweener tablet", "a 7inch tablet", "a 10inch tablet"};
            private static final float[] SCREENS_SIZES_DP = {320.0f, 480.0f, 600.0f, 720.0f};

            public static int suggestSliderWidth(Context context)
            {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

                if (dpWidth >= SCREENS_SIZES_DP[0] && dpWidth <= SCREENS_SIZES_DP[1])
                {
                    return (int) (40 * dpWidth / SCREENS_SIZES_DP[0]); // this is in pixels
                } else if (dpWidth >= SCREENS_SIZES_DP[1] && dpWidth <= SCREENS_SIZES_DP[2])
                {
                    return (int) (60 * dpWidth / SCREENS_SIZES_DP[1]); // this is in pixels
                } else if (dpWidth >= SCREENS_SIZES_DP[2] && dpWidth <= SCREENS_SIZES_DP[3])
                {
                    return (int) (80 * dpWidth / SCREENS_SIZES_DP[2]); // this is in pixels
                } else
                {
                    return (int) (100 * dpWidth / SCREENS_SIZES_DP[3]); // this is in pixels
                }
            }

            public static int suggestSliderHeight(Context context)
            {
                return suggestSliderWidth(context); //since our png is square. to keep the aspect ratio
                //it must be resized by keeping size square that is
                //keeping height and widht equal.
            }

            public static int DptoPixels(Context context, int dps)
            {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int px = Math.round(dps * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                return px;
            }

        }

        /*height will be equal to "minusFactoredLineHeight" property i.e. view's height */
        private static final int lineWidthInDps = 10;
        private static final int MUST_LINE_TOP_AND_DOWN_MARGIN = 15; //in dp
        /* height and width that this view has been given by taking paddings into account */
        private int minusFactoredLineWidth = -1;
        private int minusFactoredLineHeight = -1;
        /* mover width and height */
        private final int moverWidth;
        private final int moverHeight;
        /* Rectangle that bounds the coordinates of the line to be drawn */
        private Rect lineBoundsRect;
        /* This holds the location of the mover */
        private ComponentPositionHolder mover_locationHolder;
        /* Bitmaps for the images/bitmaps/drawables */
        private Bitmap mover;
        private Drawable backLine_StateListDrawable;
        /* Gesture detector */
        private GestureDetector mGestureDetector;

        /* Postition and callbacks update listener*/
        private SliderCallbacks sliderCallbacksListener = null;

        boolean careForCenteredNegPosBoundary = false;
        private int index;
        private int min, max;
        private float currentSliderPosition = 0;
        private float moverMultiplier = 0;
        //private boolean isDrawn = false;


//        public boolean isDrawn(){
//            return isDrawn;
//        }

        public void setIndex(int index)
        {
            this.index = index;
        }

        public void setPosViaVal(float value)
        {
            float zeroPos = getZeroPos();
            if (careForCenteredNegPosBoundary)
            {
                float posDiff = value / moverMultiplier;
                posDiff = Math.abs(posDiff);
                if (value < 0)
                {
                    this.currentSliderPosition = zeroPos + posDiff;
                    updatePos(this.currentSliderPosition);
                    //mover_locationHolder.setPointY(currentSliderPosition-moverHeight/2);
                } else
                {
                    this.currentSliderPosition = zeroPos - posDiff;
                    updatePos(this.currentSliderPosition);
                    //mover_locationHolder.setPointY(currentSliderPosition-moverHeight/2);
                }
            }
        }

        public void addCallbacksListener(SliderCallbacks sliderCallbacks)
        {
            this.sliderCallbacksListener = sliderCallbacks;
        }

        public void setMinMax(int min, int max, boolean careForCenteredNegPosBoundary)
        {
            this.min = min;
            this.max = max;
            this.careForCenteredNegPosBoundary = careForCenteredNegPosBoundary;
        }

        private void upgradeOutValCalcs()
        {
            float minMaxDiff = Math.abs(max - min);
            float lineLength = getLengthOfLine();
            moverMultiplier = minMaxDiff / lineLength;
        }

        public void setZeroPosition()
        {
            //float zeroPos = getLineTopY() + this.getLengthOfLine() / 2;
            float zeroPos = getZeroPos();
            this.updatePos(zeroPos);
        }

        public float getLengthOfLine()
        {
            return Math.abs(minusFactoredLineHeight);
        }

        public VerticalSlider(Context context, @Nullable AttributeSet attrs)
        {
            super(context, attrs);
            this.mGestureDetector = new GestureDetector(context, this);

            /* get the dimensions for slider bitmap according to device screen size and density */
            moverWidth = ScreenSizesPlusComponentConfigurationHolder.suggestSliderWidth(getContext());
            moverHeight = ScreenSizesPlusComponentConfigurationHolder.suggestSliderHeight(getContext());

            /* let the knob bitmap be created according to its dimensions */
            mover = decodeSampledBitmapFromResource(getResources(), R.drawable.cus_slider, moverWidth, moverHeight);

            //set position for mover/knob
            mover_locationHolder = new ComponentPositionHolder(Math.round(slider_get_X_asCentered()), getZeroPos()-mover.getHeight()/2);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            Log.d("onmeasure","onmeasure called vbar indexed= "+index);
            int desiredHeight = 200;
            int desiredWidth = 80;

            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);

            int width;
            int height;

            //Measure Width
            if (widthMode == MeasureSpec.EXACTLY)
            {
                //Must be this size
                width = widthSize;
            } else if (widthMode == MeasureSpec.AT_MOST)
            {
                //Can't be bigger than...
                width = Math.min(desiredWidth, widthSize);
            } else
            {
                //Be whatever you want
                width = desiredWidth;
            }

            //Measure Height
            if (heightMode == MeasureSpec.EXACTLY)
            {
                //Must be this size
                height = heightSize;
            } else if (heightMode == MeasureSpec.AT_MOST)
            {
                //Can't be bigger than...
                height = Math.min(desiredHeight, heightSize);
            } else
            {
                //Be whatever you want
                height = desiredHeight;
            }

            //MUST CALL THIS
            setMeasuredDimension(width, height);
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);

            //set position for mover/knob
            mover_locationHolder = new ComponentPositionHolder(Math.round(slider_get_X_asCentered()), getZeroPos()-mover.getHeight()/2);

            minusFactoredLineHeight = height - getPaddingTop() - getPaddingBottom() - mover.getHeight();
            minusFactoredLineWidth = width - getPaddingLeft() - getPaddingRight();

            lineBoundsRect = new Rect();
            lineBoundsRect.contains((int) getLineLeftX(), getPaddingTop() + 0 + mover.getHeight() / 2, (int) getLineRightX(), getPaddingTop() + 0 + mover.getHeight() / 2 + minusFactoredLineHeight);


            upgradeOutValCalcs();

            if (this.sliderCallbacksListener!=null)
                this.sliderCallbacksListener.MeasureTakesPlace(index);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            if (backLine_StateListDrawable == null)
            {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    backLine_StateListDrawable = getContext().getApplicationContext().getResources().getDrawable(R.drawable.vertical_slider_back_line, getContext().getTheme());
                } else
                {
                    backLine_StateListDrawable = getContext().getApplicationContext().getResources().getDrawable(R.drawable.vertical_slider_back_line);
                }
            }
            backLine_StateListDrawable.setBounds((int) getLineLeftX(), (int) getLineTopY(), (int) getLineRightX(), (int) getLineEndY());
            backLine_StateListDrawable.draw(canvas);
            if (mover!=null)
                canvas.drawBitmap(mover, mover_locationHolder.getPointX(), mover_locationHolder.getPointY(), null);
            //this.isDrawn = true;
        }

        @Override
        public void invalidate()
        {
            super.invalidate();
        }

        private double slider_get_X_asCentered()
        {
            double mover_xPoint = getMeasuredWidth() - getMeasuredWidth() / 2.0 - mover.getWidth() / 2.0;
            return mover_xPoint;
        }

        private double slider_get_Y_forDownEndLimit()
        {
            /* Slider-knob cannot scroll below this y-coordinate */
            return getLineEndY();
        }

        private double slider_get_Y_forTopEndLimit()
        {
            return getLineTopY();
        }

        private double getLineLeftX()
        {
            double xPoint = getWidth() - getWidth() / 2.0 - ScreenSizesPlusComponentConfigurationHolder.DptoPixels(getContext(), lineWidthInDps) / 2.0;
            return xPoint;
        }

        private double getLineRightX()
        {
            double xPoint = getLineLeftX() + ScreenSizesPlusComponentConfigurationHolder.DptoPixels(getContext(), lineWidthInDps);
            return xPoint;
        }

        private float getLineTopY()
        {
            return getPaddingTop() + 0 + moverHeight / 2;
        }

        private float getLineEndY()
        {
            return getPaddingTop() + 0 + moverHeight / 2 + minusFactoredLineHeight;
        }

        private float getZeroPos()
        {
            float zeroPos = (getLineTopY()+getLineEndY())/2;
            return zeroPos;
        }

        private void updatePos(float yPos)
        {
            Log.d("((","Line Top   : "+getLineTopY());
            Log.d("((","Line Bottom: "+getLineEndY());
            Log.d("((","Zero Pos   : "+getZeroPos());
            if (yPos > slider_get_Y_forTopEndLimit() && yPos < slider_get_Y_forDownEndLimit())
            {
                Log.d("((","Y Pos   : "+yPos);
                mover_locationHolder.setPointY(yPos-mover.getHeight()/2);
                this.invalidate();
//                if (yPos > slider_get_Y_forTopEndLimit())
//                {
//                    mover_locationHolder.setPointY(yPos-mover.getHeight()/2);
//                } else
//                {
//                    mover_locationHolder.setPointY(yPos-mover.getHeight()/2);
//                }
                if (this.sliderCallbacksListener != null)
                {
                    if (careForCenteredNegPosBoundary)
                    {
                        float zeroPos = getZeroPos();
                        if (mover_locationHolder.getPointY()+mover.getHeight()/2 - zeroPos <= 0)
                        {
                        /* condition when slider is above centre point*/
                            float posDiff = Math.abs(zeroPos - mover_locationHolder.getPointY()-mover.getHeight()/2);
                            this.sliderCallbacksListener.onValueChanged(index, posDiff * moverMultiplier);
                        } else
                        {
                        /* condition when slider is below centre point*/
                            float posDiff = Math.abs(mover_locationHolder.getPointY()+mover.getHeight()/2 - zeroPos);
                            this.sliderCallbacksListener.onValueChanged(index, posDiff * moverMultiplier * -1);
                        }
                    } else
                    {
                        float diff = Math.abs(mover_locationHolder.getPointY() - getLineTopY());
                        this.sliderCallbacksListener.onValueChanged(index, (getLengthOfLine() - diff) * moverMultiplier);
                    }
                }
            }
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
        {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth)
            {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth)
                {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        private Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
        {

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
        public boolean onTouchEvent(MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    backLine_StateListDrawable = getContext().getApplicationContext().getResources().getDrawable(R.drawable.vertical_slider_back_line_activated, getContext().getTheme());
                } else
                {
                    backLine_StateListDrawable = getContext().getApplicationContext().getResources().getDrawable(R.drawable.vertical_slider_back_line_activated);
                }
                this.invalidate();
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_OUTSIDE)
            {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    backLine_StateListDrawable = getContext().getApplicationContext().getResources().getDrawable(R.drawable.vertical_slider_back_line, getContext().getTheme());
                } else
                {
                    backLine_StateListDrawable = getContext().getApplicationContext().getResources().getDrawable(R.drawable.vertical_slider_back_line);
                }
                this.invalidate();
            }
            if (mGestureDetector.onTouchEvent(event) == true)
            {
                return true;
            } else
            {
                return super.onTouchEvent(event);
            }
        }

        @Override
        public boolean onDown(MotionEvent e)
        {
            float xLoc = e.getX();
            float yLoc = e.getY();
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e)
        {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            //doing nothing, just saying that its consumed by returning true
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            float yLoc = e2.getY();
            updatePos(yLoc);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e)
        {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            float yLoc = e2.getY();
            updatePos(yLoc);
            return true;
        }
    }
}

