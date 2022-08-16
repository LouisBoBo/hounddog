package com.slxk.hounddog.mvp.ui.view.googlemap;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by neu on 2018/9/19.
 */

public abstract class OnInfoWindowElemTouchListener implements View.OnTouchListener  {

    private final View view;
    //private final Drawable bgDrawableNormal;
    //private final Drawable bgDrawablePressed;
    private final Handler handler = new Handler();

    private Marker marker;
    private boolean pressed = false;

    public OnInfoWindowElemTouchListener(View view) {
        this.view = view;
        //this.bgDrawableNormal = bgDrawableNormal;
        //this.bgDrawablePressed = bgDrawablePressed;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        if (0 <= x && x <= view.getWidth() && 0 <= y && y <= view.getHeight()) {

            switch (event.getAction()) {
                // We need to delay releasing of the view a little so it shows the
                // pressed state on the
                // screen
                case MotionEvent.ACTION_DOWN:
                    startPress();
                    handler.postDelayed(performClickRunnable, ViewConfiguration.getTapTimeout());
                    break;

                case MotionEvent.ACTION_UP:
                    handler.postDelayed(performClickRunnable, ViewConfiguration.getTapTimeout());
                    break;
                case MotionEvent.ACTION_CANCEL:
                    endPress();
                    break;

                default:
                    break;
            }
        } else {
            // If the touch goes outside of the view's area
            // (like when moving finger out of the pressed button)
            // just release the press
            endPress();
        }
        return false;
    }

    private void startPress() {
        if (!pressed) {
            pressed = true;
            handler.removeCallbacks(performClickRunnable);
            //view.setBackgroundDrawable(bgDrawablePressed);
            if (marker != null)
                marker.showInfoWindow();
        }
    }

    private boolean endPress() {
        if (pressed) {
            this.pressed = false;
            handler.removeCallbacks(performClickRunnable);
            //view.setBackgroundDrawable(bgDrawableNormal);
            if (marker != null)
                marker.showInfoWindow();
            return true;
        } else
            return false;
    }

    private final Runnable performClickRunnable = new Runnable() {
        public void run() {
            if (endPress()) {
                onClickConfirmed(view, marker);
            }
        }
    };

    /**
     * This is called after a successful click
     */
    protected abstract void onClickConfirmed(View v, Marker marker);
}
