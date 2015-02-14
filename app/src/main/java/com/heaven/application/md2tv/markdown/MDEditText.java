package com.heaven.application.md2tv.markdown;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.heaven.application.md2tv.markdown.style.BackgroundSpannable;

/**
 * Created by caifangmao on 15/2/13.
 */
public class MDEditText extends EditText {

    private BackgroundSpannable[] cacheBackgroundSpannables;

    private Object buffer;

    private Object[] selectionSpans;

    private int defaultHighlightColor;

    private boolean shouldBackgroundSpanDraw = true;

    private boolean selectionSpanAtTop = false;

    public MDEditText(Context context) {
        this(context, null);
    }

    public MDEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MDEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setSelectionSpanAtTop(boolean atTop){
        this.selectionSpanAtTop = atTop;
        invalidate();
    }

    public void parseMarkDown(CharSequence text){
        //TODO parse markdown string
    }

    public void setSelectionSpan(Object... span){
        if(span != null && span.length > 0){
            this.setHighlightColor(0x0);
        }else{
            this.setHighlightColor(0xFF0099CC);
        }
        this.selectionSpans = span;
    }

    @Override
    public void setText(CharSequence text, BufferType type){
        super.setText(text, type);

        buffer = null;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if(this.getDefaultEditable() && this.selectionSpans != null){
            for(Object span : this.selectionSpans){
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


        if(selectionSpanAtTop){
            super.onDraw(canvas);
            for(Object selectionSpan : selectionSpans){
                if(selectionSpan instanceof BackgroundSpannable){
                    BackgroundSpannable span = (BackgroundSpannable) selectionSpan;
                    span.setMainTextView(this);
                    span.updateDrawState(canvas);
                }
            }
        }else{
            for(Object selectionSpan : selectionSpans){
                if(selectionSpan instanceof BackgroundSpannable){
                    ((BackgroundSpannable) selectionSpan).updateDrawState(canvas);
                }
            }
            super.onDraw(canvas);
        }
    }
}
