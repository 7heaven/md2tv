package com.heaven.application.md2tv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;

/**
 * Created by caifangmao on 15/3/4.
 */
public class LineEditText extends EditText {

    private int originalLeftPadding = 0;
    private Rect lineBound;
    private Rect lineNumberBound;

    private Paint paint;

    public LineEditText(Context context){
        this(context, null);
    }

    public LineEditText(Context context, AttributeSet attrs){
        super(context, attrs);

        originalLeftPadding = getPaddingLeft();
        setPadding(50, getPaddingTop(), getPaddingRight(), getPaddingBottom());

        lineBound = new Rect();
        lineNumberBound = new Rect();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF888888);
        paint.setTextSize(30);

        setGravity(Gravity.TOP | Gravity.LEFT);
        setBackgroundDrawable(null);
    }

    public LineEditText(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        originalLeftPadding = getPaddingLeft();
        setPadding(50, getPaddingTop(), getPaddingRight(), getPaddingBottom());

        lineBound = new Rect();
        lineNumberBound = new Rect();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF888888);
        paint.setTextSize(30);

        setGravity(Gravity.TOP | Gravity.LEFT);
        setBackgroundDrawable(null);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(getLayout() != null){
            Layout layout = getLayout();

            int paddingTop = getTotalPaddingTop();
            int totalLines = layout.getLineCount();
            int lines = 1;
            for(int i = 0; i < totalLines; i++){
                int baseLine = layout.getLineBounds(i, lineBound);
                String line = (lines) + "";

                paint.getTextBounds(line, 0, line.length(), lineNumberBound);

                canvas.drawText(line, 0, line.length(), 0, lineBound.bottom - (lineBound.height() - lineNumberBound.height()) / 2 + paddingTop, paint);
            }
        }
    }

}
