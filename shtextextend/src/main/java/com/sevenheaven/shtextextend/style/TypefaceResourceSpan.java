package com.sevenheaven.shtextextend.style;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.Log;

/**
 * Created by caifangmao on 15/2/14.
 *
 * from http://stackoverflow.com/questions/8710300/how-to-set-font-style-of-selected-text-using-custom-typeface-through-spannable-m
 *
 */
public class TypefaceResourceSpan extends MetricAffectingSpan implements ParcelableSpan {

    private String resourceName_;
    private Typeface tf_;

    private Context context;

    public TypefaceResourceSpan(Context context, String resourceName) {
        super();

        this.context = context;
        resourceName_=resourceName;
        tf_=createTypeface(resourceName_);
    }

    public TypefaceResourceSpan(Context context, Parcel src) {
        this.context = context;
        resourceName_ = src.readString();
        tf_=createTypeface(resourceName_);
    }

    private Typeface createTypeface(String resourceName) {
        Typeface result=null;
        Context c=context;
        if (c==null) {
            Log.e("TypefaceResourceSpan", "Application context is null!");
        }
        AssetManager am=c.getAssets();
        if (am==null) {
            Log.e("TypefaceResourceSpan", "AssetManager is null!");
        }
        result=Typeface.createFromAsset(am, resourceName);
        return result;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resourceName_);
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        Typeface old=p.getTypeface();
        if ( old != null && !old.isBold() && tf_.isBold() ) {
            p.setFakeBoldText(true);
        }
        if ( old != null && !old.isItalic() && tf_.isItalic() ) {
            p.setTextSkewX(-0.25f);
        }
        p.setTypeface(tf_);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        Typeface old=tp.getTypeface();
        if ( old != null && !old.isBold() && tf_.isBold() ) {
            tp.setFakeBoldText(true);
        }
        if ( old != null && !old.isItalic() && tf_.isItalic() ) {
            tp.setTextSkewX(-0.25f);
        }
        tp.setTypeface(tf_);
    }

    public int getSpanTypeId() {
        // TODO does this work???!?
        return 123456;
    }

    public int describeContents() {
        return 0;
    }
}