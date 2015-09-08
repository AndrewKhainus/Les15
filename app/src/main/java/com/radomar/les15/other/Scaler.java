package com.radomar.les15.other;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by Radomar on 27.08.2015.
 * this is Singleton class with few methods for resize bitmap image
 */
public class Scaler {

    private Activity mActivity;

    private int mScreenWidth;
    private int mScreenHeight;
    private float mScreenRatio;

    private static Scaler instance;

    private Scaler() {
    }

    private Scaler(Activity activity) {
        this.mActivity = activity;
        initializeScreenParameters();
    }

    public static Scaler getInstance(Activity activity) {
        if (instance == null) {
            instance = new Scaler(activity);
        }
        return instance;
    }

    private void initializeScreenParameters() {
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.mScreenWidth = size.x;
        this.mScreenHeight = size.y;
        mScreenRatio = (float)mScreenWidth / mScreenHeight;
    }

    /**
     * adapt image for the screen
     */
    public Bitmap resize(Bitmap image) {
        if (mScreenHeight > 0 && mScreenWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) mScreenWidth / (float) mScreenHeight;

            int finalWidth = mScreenWidth;
            int finalHeight = mScreenHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)mScreenHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)mScreenWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public int getLayoutSize() {
        if( mScreenWidth < mScreenHeight) {
            return mScreenWidth / 2;
        } else {
            return mScreenHeight / 2;
        }
    }

    /**
     * the width of the thumbnail is equal to half the width of the screen
     */
    public Bitmap generateThumbnail(Bitmap image) {
        Bitmap thumbnail = Bitmap.createScaledBitmap(image, getLayoutSize(), getLayoutSize(), false);
        return thumbnail;
    }

}
