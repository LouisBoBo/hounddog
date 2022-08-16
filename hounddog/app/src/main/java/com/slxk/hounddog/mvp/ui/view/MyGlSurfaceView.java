package com.slxk.hounddog.mvp.ui.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

public class MyGlSurfaceView extends GLSurfaceView {
    private Renderer mRenderer;
    private static final String TAG = MyGlSurfaceView.class.getName();
    public MyGlSurfaceView(Context context) {
        super(context);
    }

    public MyGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if(mRenderer == null) {
            return;
        }
        super.onWindowVisibilityChanged(visibility);
        Log.d(TAG, "onWindowVisibilityChanged");
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        mRenderer = renderer;
        Log.d(TAG, "setRender");
    }

}
