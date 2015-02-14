package com.heaven.application.md2tv.markdown.style;

import android.graphics.Canvas;
import android.widget.TextView;

import com.heaven.application.md2tv.markdown.MDTextView;

/**
 * Created by caifangmao on 15/2/13.
 */
public interface BackgroundSpannable {

    public void setMainTextView(TextView tv);

    public void setRange(int start, int end);

    public void updateDrawState(Canvas canvas);
}
