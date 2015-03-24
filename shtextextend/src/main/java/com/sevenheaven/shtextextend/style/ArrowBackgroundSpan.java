package com.sevenheaven.shtextextend.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

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

        this.arrowColor = this.arrowColor >> 24 == 0 ? 0xFF000000 | this.arrowColor : this.arrowColor;
        this.backgroundColor = this.backgroundColor >> 24 == 0 ? 0xFF000000 | this.backgroundColor : this.backgroundColor;

        startArrow = new Path();
        endArrow = new Path();
    }

    @Override
    protected void drawLine(Canvas canvas, int width, int height, LinePosition linePosition){
        if(linePosition != LinePosition.LinePositionMiddle){
            startArrow.reset();
            endArrow.reset();
//
            int halfHeight = height / 2;
//
//            float startP = (float) (-Math.PI / 2);
//            float perP = (float) ((Math.PI * 2) / 10);
//
//            PointF correctPoint = centerRadiusPoint(-halfHeight, halfHeight, startP, halfHeight);
//            startArrow.moveTo(correctPoint.x, correctPoint.y);
//            correctPoint = centerRadiusPoint(width + halfHeight, halfHeight, startP, halfHeight);
//            endArrow.moveTo(correctPoint.x, correctPoint.y);
//            for(int i = 1; i < 10; i++){
//                if(i % 2 == 0){
//                    correctPoint = centerRadiusPoint(-halfHeight, halfHeight, startP + (i * perP), halfHeight);
//                    startArrow.lineTo(correctPoint.x, correctPoint.y);
//                    correctPoint = centerRadiusPoint(width + halfHeight, halfHeight, startP + (i * perP), halfHeight);
//                    endArrow.lineTo(correctPoint.x, correctPoint.y);
//                }else{
//                    correctPoint = centerRadiusPoint(-halfHeight, halfHeight, startP + (i * perP), halfHeight / 1.8F);
//                    startArrow.lineTo(correctPoint.x, correctPoint.y);
//                    correctPoint = centerRadiusPoint(width + halfHeight, halfHeight, startP + (i * perP), halfHeight / 1.8F);
//                    endArrow.lineTo(correctPoint.x, correctPoint.y);
//                }
//            }
//
//            startArrow.close();
//            endArrow.close();

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

    private PointF centerRadiusPoint(int centerX, int centerY, double angle, double radius){
        float x = (float) (radius * Math.cos(angle) + centerX);
        float y = (float) (radius * Math.sin(angle) + centerY);

        return new PointF(x, y);
    }
}
