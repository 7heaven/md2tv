package com.sevenheaven.shtextextend.style;

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

    /**
     *  当前行的模式
     *
     *  LinePositionStart Span从本行开始
     *  LinePositionMiddle Span的start、end不在本行，但是包括了本行
     *  LinePositionEnd Span到本行结束
     *  LinePositionSingle Span的start、end都在本行
     */
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

    public int getStart(){
        return this.start;
    }

    public int getEnd(){
        return this.end;
    }

    public void updateDrawState(Canvas canvas){
        Layout layout = tv.getLayout();

        if(layout != null){

            int saveBound = canvas.save();

            //计算剪裁矩形，保证绘制内容不会超出文本layout
            int clipLeft = tv.getPaddingLeft();
            int clipTop = tv.getTotalPaddingTop() + tv.getScrollY();
            int clipRight = canvas.getWidth() - tv.getPaddingRight();
            int clipBottom = clipTop + (tv.getHeight() - tv.getTotalPaddingTop() - tv.getTotalPaddingBottom());

            canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);

            //根据start end  获得起始行数和结束行数
            lineStart = layout.getLineForOffset(start);
            lineEnd = layout.getLineForOffset(end);

            if(lineStart != lineEnd){
                lines = new Rect[(lineEnd + 1) - lineStart];

                //计算每一行中包含当前span的矩形大小
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

            //绘制
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
