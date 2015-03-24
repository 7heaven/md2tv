package com.sevenheaven.shtextextend.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by caifangmao on 15/2/13.
 */
public class ShapeBackgroundSpan extends BackgroundSpannable{

    private Shape shape;

    public ShapeBackgroundSpan(int color, Shape shape, boolean stroke){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        this.shape = shape;

        if(stroke){
            paint.setStyle(Paint.Style.STROKE);
        }else{
            paint.setStyle(Paint.Style.FILL);
        }

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
        shape.resize(width, height);
        shape.draw(canvas, paint);
    }

}