package com.heaven.application.md2tv.markdown.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.text.Layout;

import com.heaven.application.md2tv.markdown.MDTextView;

/**
 * Created by caifangmao on 15/2/13.
 */
public class CornerBackgroundSpan implements BackgroundSpannable {


    private RoundRectShape shape;

    private int lineStart;
    private int lineEnd;

    private Rect[] lines;

    private Paint paint;

    private MDTextView tv;

    private int start;
    private int end;

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
    public void setMainTextView(MDTextView tv){
        this.tv = tv;
    }

    @Override
    public void setRange(int start, int end){
        this.start = start;
        this.end = end;
    }

    @Override
    public void updateDrawState(Canvas canvas){
        Layout layout = tv.getLayout();

        if(layout != null){
            lineStart = layout.getLineForOffset(start);
            lineEnd = layout.getLineForOffset(end);

            if(lineStart != lineEnd){
                lines = new Rect[(lineEnd + 1) - lineStart];

                for(int i = lineStart; i <= lineEnd; i++){
                    Rect rect = new Rect();
                    layout.getLineBounds(i, rect);

                    if(i == lineStart){
                        rect.left = (int) layout.getPrimaryHorizontal(start);
                    }

                    if(i == lineEnd){
                        rect.right = (int) layout.getSecondaryHorizontal(end);
                    }else{
                        float[] width = new float[1];
                        String t = layout.getText().subSequence(layout.getLineEnd(i) - 1, layout.getLineEnd(i)).toString();
                        layout.getPaint().getTextWidths(t, width);
                        rect.right = (int) (layout.getSecondaryHorizontal(layout.getLineEnd(i) - 1) + width[0]);
                    }

                    lines[i - lineStart] = rect;
                }
            }else{
                Rect rect = new Rect();
                lines = new Rect[1];
                layout.getLineBounds(lineStart, rect);
                rect.left = (int) layout.getPrimaryHorizontal(start);
                rect.right = (int) layout.getSecondaryHorizontal(end);

                lines[0] = rect;
            }

            //calculate x & y for padding
            int x = tv.getCompoundPaddingLeft();
            int y = tv.getTotalPaddingTop();

            canvas.save();

            canvas.translate(x, y);

            int length = lineEnd - lineStart;

            for(int i = 0; i <= length; i++){
                Rect rect = lines[i];

                if(length != 0){
                    if(i == 0){
                        shape = new RoundRectShape(new float[]{corner, corner, 0, 0,
                                0, 0, corner, corner}, null, null);
                    }else if(i == length){
                        shape = new RoundRectShape(new float[]{0, 0, corner, corner,
                                corner, corner, 0, 0}, null, null);
                    }else{
                        shape = new RoundRectShape(new float[]{0, 0, 0, 0,
                                0, 0, 0, 0}, null, null);
                    }

                }

                shape.resize(rect.width(), rect.height());

                if(i == 0){
                    canvas.translate(rect.left, rect.top);
                }else{
                    canvas.translate(-lines[i - 1].left, -lines[i - 1].top);
                    canvas.translate(rect.left, rect.top);
                }

                shape.draw(canvas, paint);

            }

            canvas.restore();
        }
    }

}