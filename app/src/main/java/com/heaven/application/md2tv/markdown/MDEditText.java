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

    private Object[] selectionSpan;

    private int defaultHighlightColor;

    private boolean shouldBackgroundSpanDraw = true;

    public MDEditText(Context context) {
        this(context, null);
    }

    public MDEditText(Context context, AttributeSet attrs) {
        super(context, attrs);


        this.setHighlightColor(0x0);
    }

    public MDEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.setHighlightColor(0x0);
    }



    public void parseMarkDown(CharSequence text){
        //TODO parse markdown string
    }

    public void setSelectionSpan(Object... span){
        this.selectionSpan = span;
    }

    private boolean handleMotionEvent(MotionEvent event){
        int action = event.getAction();
        SpannableString buffer = (SpannableString) getText();

        //TODO handle span with Click event
        return true;
    }

    @Override
    public void setText(CharSequence text, BufferType type){
        super.setText(text, type);

        buffer = null;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if(this.getDefaultEditable() && this.selectionSpan != null){
            for(Object span : this.selectionSpan){
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
            for(int i = 0; i < cacheBackgroundSpannables.length; i++){
//                if(cacheBackgroundSpannables[i] == this.selectionSpan) continue;
                cacheBackgroundSpannables[i].updateDrawState(canvas);
            }
        }


        super.onDraw(canvas);
    }
}
