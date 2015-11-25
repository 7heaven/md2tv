package com.sevenheaven.shtextextend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;


import com.sevenheaven.shtextextend.style.BackgroundSpannable;

/**
 * Created by caifangmao on 15/2/13.
 */
public class MDEditText extends EditText {

    private BackgroundSpannable[] cacheBackgroundSpannables;

    private Object buffer;

    private Object[] selectionSpans;

    private int defaultHighlightColor;
    private int defaultLeftPadding;

    private boolean shouldBackgroundSpanDraw = true;

    private boolean selectionSpanAtTop = false;

    private Rect lineBound;
    private Rect lineNumberBound;
    private boolean lineNumberEnabled = false;
    private int lineGap;
    private int singleLineCharWidth;

    private Paint paint;

    public MDEditText(Context context) {
        this(context, null);
    }

    public MDEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MDEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init(){
        defaultLeftPadding = getPaddingLeft();


        lineBound = new Rect();
        lineNumberBound = new Rect();
        lineGap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF888888);
        paint.setTextSize(30);

        paint.getTextBounds("0", 0, 1, lineNumberBound);
        singleLineCharWidth = lineNumberBound.width();

        setGravity(Gravity.TOP | Gravity.LEFT);
        setBackgroundDrawable(null);
    }


    public void setSelectionSpanAtTop(boolean atTop){
        this.selectionSpanAtTop = atTop;
        invalidate();
    }

    /**
     * @hide
     * @param text
     */
    public void parseMarkDown(CharSequence text){
        //TODO parse markdown string
    }

    public void showLineNumber(boolean show){
        int start = getSelectionStart();
        int end = getSelectionEnd();


        this.lineNumberEnabled = show;

        if(show){
            defaultLeftPadding = getPaddingLeft();

            int lineWidth = 0;
            Layout layout = getLayout();
            if(layout != null){
                String line = layout.getLineCount() + "";

                lineWidth = singleLineCharWidth * line.length() + lineGap * 2;
            }else{
                lineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            }

            setPadding(lineWidth + defaultLeftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());

        }else{
            setPadding(defaultLeftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }

        if(start != -1 && end != -1){
            setSelection(start, end);
        }

        invalidate();
    }

    /**
     * 设置文本选择时的样式，可多个span重叠
     * @param span
     */
    public void setSelectionSpan(Object... span){

        int start = getSelectionStart();
        int end = getSelectionEnd();

        if(this.selectionSpans != null && this.selectionSpans.length > 0){
            for(Object sSpan : this.selectionSpans) {

                if(start == -1 || end == -1){
                    start = ((Spannable) getText()).getSpanStart(sSpan);
                    end = ((Spannable) getText()).getSpanEnd(sSpan);
                }

                ((Spannable) getText()).removeSpan(sSpan);
            }
        }

        if(span != null && span.length > 0){
            this.setHighlightColor(0x0);
        }else{
            this.setHighlightColor(0xFF0099CC);
        }
        this.selectionSpans = span;

        if(start != -1 && end != -1){
            for(Object sSpan : this.selectionSpans){

                if(sSpan instanceof BackgroundSpannable){
                    ((BackgroundSpannable) sSpan).setRange(start, end);
                }else{
                    ((Spannable) getText()).setSpan(sSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
        }

        invalidate();
    }

    @Override
    public void setText(CharSequence text, BufferType type){
        super.setText(text, type);

        buffer = null;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        Log.d("selStart:" + selStart, "selEnd:" + selEnd + ", length:" + getText().length());
        if(selStart == selEnd && selStart == getText().length()){
            Log.d("ddddddddd", "ddfdfsdf");
            if(this.selectionSpans != null){
                for(Object span : this.selectionSpans){
                    ((Spannable) getText()).removeSpan(span);
                }
            }
            return;
        }

        if(this.getDefaultEditable() && this.selectionSpans != null){
            for(Object span : this.selectionSpans){
                if(span instanceof BackgroundSpannable) continue;
                ((Spannable) getText()).setSpan(span, selStart, selEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

        }
    }

    @Override
    public void onDraw(Canvas canvas){

        if(buffer == null){
            try{
                if(getText() instanceof SpannableStringBuilder){
                    SpannableStringBuilder temp = ((SpannableStringBuilder) getText());

                    cacheBackgroundSpannables = temp.getSpans(0, temp.length() - 1, BackgroundSpannable.class);

                    for(BackgroundSpannable span : cacheBackgroundSpannables){
                        span.setMainTextView(this);
                        span.setRange(temp.getSpanStart(span), temp.getSpanEnd(span));
                    }
                }


            }catch(ClassCastException e){
                e.printStackTrace();
            }
        }

        //span
        if(shouldBackgroundSpanDraw && cacheBackgroundSpannables != null && cacheBackgroundSpannables.length > 0){
            outter: for(BackgroundSpannable span : cacheBackgroundSpannables){
                for(Object selectionSpan : selectionSpans){
                    if(span == selectionSpan){
                        continue outter;
                    }
                }
                span.updateDrawState(canvas);
            }
        }


        //selectionSpan
        if(this.selectionSpans != null && this.selectionSpans.length > 0){

            int start = getSelectionStart();
            int end = getSelectionEnd();

            if(selectionSpanAtTop){
                super.onDraw(canvas);
                for(Object selectionSpan : selectionSpans){
                    if(selectionSpan instanceof BackgroundSpannable){
                        BackgroundSpannable span = (BackgroundSpannable) selectionSpan;
                        span.setMainTextView(this);
                        span.setRange(start, end);
                        span.updateDrawState(canvas);
                    }
                }
            }else{
                for(Object selectionSpan : selectionSpans){
                    if(selectionSpan instanceof BackgroundSpannable){
                        BackgroundSpannable span = (BackgroundSpannable) selectionSpan;
                        span.setMainTextView(this);
                        span.setRange(start, end);
                        span.updateDrawState(canvas);
                    }
                }
                super.onDraw(canvas);
            }
        }else{
            super.onDraw(canvas);
        }

        //lineNumber
        if(getLayout() != null && lineNumberEnabled){

            int clipLeft = 0;
            int clipTop = this.getTotalPaddingTop() + this.getScrollY();
            int clipRight = canvas.getWidth();
            int clipBottom = clipTop + (this.getHeight() - this.getTotalPaddingTop() - this.getTotalPaddingBottom());

            int saveForLineNumber = canvas.save();
            canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);

            Layout layout = getLayout();

            int paddingTop = getTotalPaddingTop();
            int totalLines = layout.getLineCount();

            String lineString = totalLines + "";

            int lineWidth = singleLineCharWidth * lineString.length() + lineGap * 2;

            if(getPaddingLeft() != lineWidth) setPadding(lineWidth + defaultLeftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());

            for(int i = 0; i < totalLines; i++){
                int baseLine = layout.getLineBounds(i, lineBound);
                String line = (i + 1) + "";

                paint.getTextBounds(line, 0, line.length(), lineNumberBound);

                //使用singleLineCharWidth * line.length() 而不是直接用lineNumberBound.width() 以防止部分字符宽度较小(比如1)引起行数对不齐的问题
                canvas.drawText(line, 0, line.length(), getPaddingLeft() - lineGap - singleLineCharWidth * line.length(), lineBound.bottom - (lineBound.height() - lineNumberBound.height()) / 2 + paddingTop, paint);
            }

            canvas.restoreToCount(saveForLineNumber);
        }
    }
}
