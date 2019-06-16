package com.ssaurel.mypaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

/**
 * Define a custom view to draw paths on the screen.
 *
 * @author Dariusz Wantuch
 * @version 1.0
 * @since 2019-06-16
 *
 */
public class PaintView extends View {

    public static int BRUSH_SIZE = 40;                          /**Default brush size*/
    public static final int DEFAULT_COLOR = Color.RED;          /**Default color*/
    public static final int DEFAULT_BG_COLOR = Color.WHITE;     /**Default background color*/
    private static final float TOUCH_TOLERANCE = 4;             /**Tolerance of touch*/
    private float mX, mY;                                       /**Finger location*/
    private Path mPath;                                         /**Path - geometric paths consisting of straight line segments*/
    private Paint mPaint;                                       /**Paint variable*/
    private ArrayList<FingerPath> paths = new ArrayList<>();    /**Array list for fingers*/
    private int currentColor;                                   /**Current color*/
    private int backgroundColor = DEFAULT_BG_COLOR;             /**Background color*/
    private int strokeWidth;                                    /**Stroke width*/
    private boolean blur;                                       /**Blur yes or not */
    private MaskFilter mBlur;                                   /**Mask Filter for Blur*/
    private Bitmap mBitmap;                                     /**Bitmap*/
    private Canvas mCanvas;                                     /**Canvas - holds the "draw" calls*/
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);  /**Paint flag that enables dithering when blitting.*/


    public PaintView(Context context) {

        this(context, null);
    }

    /**
     * Sets the Paint object its shape, appearance, etc.
     * @param context
     * @param attrs
     */
    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mBlur = new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL);
    }

    /**
     * Display metrics it will be responsible to define the height
     * and width of the Paint View.Canvas use Bitmap to draw path
     * one the screen.
     * @param metrics
     */
    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }
    /**
     * Method for a normal brush
     */
    public void normal() {
        blur = false;
    }
    /**
     * Method for a blur brush
     */
    public void blur() {
        blur = true;
    }
    /**
     * Method for a small brush
     */
    public void small(){
        strokeWidth = 20;
    }
    /**
     * Method for a medium brush
     */
    public void medium(){
        strokeWidth = 40;
    }
    /**
     * Method for a big brush
     */
    public void big(){
        strokeWidth = 60;
    }
    /**
     * Method for a red color brush
     */
    public void reedColor(){
        currentColor=Color.RED;
    }
    /**
     * Method for a blue color brush
     */
    public void blueColor(){
        currentColor=Color.BLUE;
    }
    /**
     * Method for a green color brush
     */
    public void greenColor(){
        currentColor=Color.GREEN;
    }
    /**
     * Method for a yellow color brush
     */
    public void yellowColor(){
        currentColor=Color.YELLOW;
    }
    /**
     * Method for a black color brush
     */
    public void blackColor(){
        currentColor=Color.BLACK;
    }
    /**
     * Method for a eraser
     */
    public void eraser(){
        currentColor=Color.WHITE;
        strokeWidth = 60;
    }
    /**
     * Method for a very big brush
     */
    public void veryBig(){
        strokeWidth = 100;
    }
    /**
     * method for cleaning the painting
     */
    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
        normal();
        invalidate();
    }

    /**
     * Method of the View class. Save the current state of the Canvas
     * before to draw the background color of the Paint View.Draw all
     * elements on the Canvas of the Paint View and we restore the
     * current Canvas.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);


            if (fp.blur)
                mPaint.setMaskFilter(mBlur);

            mCanvas.drawPath(fp.path, mPaint);

        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }
    /**
     * Create new Path and a new Finger Path Objects. Call the
     * moveTo method of the Path object.
     * @param x
     * @param y
     */
    private void touchStart(float x, float y) {
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor,blur, strokeWidth, mPath);
        paths.add(fp);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    /**
     * Check if the move on the screen if greater than the touch tolerance
     * defined as a constant previously.If yes call the quadTo method
     * of the Paint Object starting from the last point touched and going
     * to the averange position and the current position.
     * @param x
     * @param y
     */
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    /**
     * End the line by calling the Line to of the Path object
     * with the last position and the current screen.
     */
    private void touchUp()
    {
        mPath.lineTo(mX, mY);
    }

    /**
     * This is the touch behavior we take x and y and in the
     * switch construction we execute the methods depending on
     * the event detection.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }

        return true;
    }
}