package com.heaven.application.md2tv.markdown.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by caifangmao on 15/2/14.
 */
public class ArrowBackgroundSpan extends BackgroundSpannable {

    private Path startArrow;
    private Path endArrow;

    private int arrowColor;
    private int backgroundColor;

    public ArrowBackgroundSpan(int arrowColor, int backgroundColor){

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        this.arrowColor = arrowColor;
        this.backgroundColor = backgroundColor;

        startArrow = new Path();
        endArrow = new Path();
    }

    @Override
    protected void drawLine(Canvas canvas, int width, int height, LinePosition linePosition){
        if(linePosition != LinePosition.LinePositionMiddle){
            startArrow.reset();
            endArrow.reset();

            int halfHeight = height / 2;

            startArrow.moveTo(0, 0);
            startArrow.lineTo(halfHeight, 0);
            startArrow.lineTo(0, halfHeight);
            startArrow.close();

            endArrow.moveTo(width, height);
            endArrow.lineTo(width, height - halfHeight);
            endArrow.lineTo(width - halfHeight, height);
            endArrow.close();
        }

        paint.setColor(backgroundColor);
        canvas.drawRect(0, 0, width, height, paint);

        paint.setColor(arrowColor);
        switch(linePosition){
            case LinePositionStart:
                canvas.drawPath(startArrow, paint);
                break;
            case LinePositionEnd:
                canvas.drawPath(endArrow, paint);
                break;
            case LinePositionSingle:
                canvas.drawPath(startArrow, paint);
                canvas.drawPath(endArrow, paint);
                break;
        }
    }
}
