package com.heaven.application.md2tv.markdown.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.widget.TextView;

/**
 * Created by caifangmao on 15/2/13.
 */
public abstract class BackgroundSpannable {

    private int lineStart;
    private int lineEnd;

    private Rect[] lines;

    protected Paint paint;

    private TextView tv;

    private int start;
    private int end;

    public enum LinePosition{
        LinePositionStart, LinePositionMiddle, LinePositionEnd, LinePositionSingle;
    }

    public void setMainTextView(TextView tv){
        this.tv = tv;
    }

    public void setRange(int start, int end){
        this.start = start;
        this.end = end;
    }

    public void updateDrawState(Canvas canvas){
        Layout layout = tv.getLayout();

        if(layout != null){

            int saveBound = canvas.save();
            canvas.clipRect(tv.getPaddingLeft(), 0, canvas.getWidth() - tv.getPaddingRight(), layout.getHeight());

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

            int saveTranslate = canvas.save();

            canvas.translate(x, y);

            int length = lineEnd - lineStart;

            for(int i = 0; i <= lineEnd - lineStart; i++){
                Rect rect = lines[i];

                if(i == 0){
                    canvas.translate(rect.left, rect.top);
                }else{
                    canvas.translate(-lines[i - 1].left, -lines[i - 1].top);
                    canvas.translate(rect.left, rect.top);
                }

                LinePosition linePosition;

                if(length != 0){
                    if(i == 0){
                        linePosition = LinePosition.LinePositionStart;
                    }else if(i == length){
                        linePosition = LinePosition.LinePositionEnd;
                    }else{
                        linePosition = LinePosition.LinePositionMiddle;
                    }

                }else{
                    linePosition = LinePosition.LinePositionSingle;
                }

                drawLine(canvas, rect.width(), rect.height(), linePosition);

            }

            canvas.restoreToCount(saveTranslate);
            canvas.restoreToCount(saveBound);
        }
    }

    protected abstract void drawLine(Canvas canvas, int width, int height, LinePosition linePosition);
}
