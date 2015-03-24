package com.sevenheaven.shtextextend.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * Created by caifangmao on 15/2/13.
 */
public class CornerBackgroundSpan extends BackgroundSpannable {


    private RoundRectShape shape;

    private int corner;


    public CornerBackgroundSpan(int color, int corner){
        this.corner = corner;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        shape = new RoundRectShape(new float[]{corner, corner, corner, corner,
                corner, corner, corner, corner}, null, null);
    }

    public void setStroke(boolean stroke){
        if(stroke){
            paint.setStyle(Paint.Style.STROKE);
        }else{
            paint.setStyle(Paint.Style.FILL);
        }
    }

    public void setStrokeWidth(int width){
        paint.setStrokeWidth(width);
    }


    @Override
    protected void drawLine(Canvas canvas, int width, int height, LinePosition linePosition){
        switch(linePosition){
            case LinePositionStart:
                shape = new RoundRectShape(new float[]{corner, corner, 0, 0,
                        0, 0, corner, corner}, null, null);
                break;
            case LinePositionMiddle:
                shape = new RoundRectShape(new float[]{0, 0, 0, 0,
                        0, 0, 0, 0}, null, null);
                break;
            case LinePositionEnd:
                shape = new RoundRectShape(new float[]{0, 0, corner, corner,
                        corner, corner, 0, 0}, null, null);
                break;
            case LinePositionSingle:
                shape = new RoundRectShape(new float[]{corner, corner, corner, corner, corner, corner, corner, corner}, null, null);
                break;
        }

        shape.resize(width, height - 2);

        shape.draw(canvas, paint);
    }


}