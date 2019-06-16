package com.ssaurel.mypaint;

import android.graphics.Path;

/**
 * Create a FingerPath  to represent a path drawn with the finger on the screen.
 *
 * @author Dariusz Wantuch
 * @version 1.0
 * @since 2019-06-16
 *
 */
public class FingerPath {

    public int color;
    public boolean blur;
    public int strokeWidth;
    public Path path;

    /**
     * This method represent a path drawn with the finger on
     * the screen. They are defined here:
     * @param color Color of the path
     * @param blur Blur yes or not
     * @param strokeWidth Stroke width of the path
     * @param path object representing the path drawn
     */
    public FingerPath(int color, boolean blur, int strokeWidth, Path path) {
        this.color = color;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}