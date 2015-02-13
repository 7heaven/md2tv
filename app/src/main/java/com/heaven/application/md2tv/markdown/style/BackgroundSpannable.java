package com.heaven.application.md2tv.markdown.style;

import android.graphics.Canvas;

import com.heaven.application.md2tv.markdown.MDTextView;

/**
 * Created by caifangmao on 15/2/13.
 */
public interface BackgroundSpannable {

    public void setMainTextView(MDTextView tv);

    public void setRange(int start, int end);

    public void updateDrawState(Canvas canvas);
}
