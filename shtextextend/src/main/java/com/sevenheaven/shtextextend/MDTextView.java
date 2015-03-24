package com.sevenheaven.shtextextend;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.sevenheaven.shtextextend.style.BackgroundSpannable;


/**
 * Created by caifangmao on 15/2/13.
 */
public class MDTextView extends TextView {

    private BackgroundSpannable[] cacheBackgroundSpannables;

    private Object buffer;

    private BackgroundSpannable selectionSpan;

    private boolean shouldBackgroundSpanDraw = true;

    public MDTextView(Context context){
        this(context, null);
    }

    public MDTextView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public MDTextView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    public void parseMarkDown(CharSequence text){
        //TODO parse markdown string
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        if(getText() instanceof SpannableString && handleMotionEvent(event) && getMovementMethod() != null){
//            return super.onTouchEvent(event);
//        }else{
//            return false;
//        }
//    }

    public void setSelectionSpan(BackgroundSpannable span){
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
                }else if(getText() instanceof Spanned){
                    SpannedString temp = (SpannedString) getText();

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

        if(this.getDefaultEditable() && this.selectionSpan != null){
            ((Spannable) getText()).setSpan(this.selectionSpan, this.getSelectionStart(), this.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if(shouldBackgroundSpanDraw && cacheBackgroundSpannables != null && cacheBackgroundSpannables.length > 0){
            for(int i = 0; i < cacheBackgroundSpannables.length; i++){
                cacheBackgroundSpannables[i].updateDrawState(canvas);
            }
        }


        super.onDraw(canvas);
    }

}
