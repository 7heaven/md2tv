package com.heaven.application.md2tv;

import android.app.Activity;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.heaven.application.md2tv.markdown.MDEditText;
import com.heaven.application.md2tv.markdown.MDTextView;
import com.heaven.application.md2tv.markdown.style.ArrowBackgroundSpan;
import com.heaven.application.md2tv.markdown.style.CornerBackgroundSpan;
import com.heaven.application.md2tv.markdown.style.DrawableBackgroundSpan;
import com.heaven.application.md2tv.markdown.style.ShapeBackgroundSpan;
import com.heaven.application.md2tv.markdown.style.TypefaceResourceSpan;

public class MainActivity extends Activity {

    MDEditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (MDEditText) findViewById(R.id.tv);
        SpannableString ss = new SpannableString("I see a beautiful city and a brilliant people rising from this abyss, and, in their struggles to be truly free, in their triumphs and defeats, through long years to come, I see the evil of this time and of the previous time of which this is the natural birth, gradually making expiation for itself and wearing out.\n" +
                "I see the lives for which I lay down my life, peaceful, useful, prosperous and happy, in that England which I shall see no more.\n" +
                "I see that I hold a sanctuary in their hearts, and in the hearts of their descendants, generations hence. I see her, an old woman, weeping for me on the anniversary of this day. I see her and her husband, their course done, lying side by side in their last earthly bed, and I know that each was not more honoured and held sacred in the other's soul, than I was in the souls of both.\n" +
                "I see that child who lay upon her bosom and who bore my name, a man winning his way up in that path of life which once was mine. I see him winning it so well, that my name is made illustrious there by the light of his. I see the blots I threw upon it, faded away. I see him, foremost of just judges and honoured men, bringing a boy of my name, with a forehead that I know and golden hair, to this place— then fair to look upon, with not a trace of this day's disfigurement— and I hear him tell the child my story, with a tender and a faltering voice.\n" +
                "Crush humanity out of shape once more, under similar hammers, and it will twist itself into the same tortured forms. Sow the same seed of rapacious license and oppression over again, and it will surely yield the same fruit according to its kind.\n" +
                "It is a far, far better thing that I do, than I have ever done; it is a far, far better rest that I go to than I have ever known.");
        ss.setSpan(new CornerBackgroundSpan(0xFF0099CC, 5), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(0xFFFFFFFF), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CornerBackgroundSpan(0xFFFF0000, 10), 6, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ShapeBackgroundSpan(0xFF22FF00, new OvalShape(), false), 71, 75, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(0xFFFFFFFF), 30, 70, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Drawable drawable = getResources().getDrawable(R.drawable.paint_streak);
//        textView.setSelectionSpan(new CornerBackgroundSpan(0xFF0099CC, 10));

        textView.setSelectionSpan(new ArrowBackgroundSpan(0xFFFF7373, 0x22000000));
        textView.setSelectionSpanAtTop(true);
//        textView.setSelectionSpan(new TypefaceResourceSpan(this.getApplicationContext(), "PillGothic300mgRegular.ttf"), new ForegroundColorSpan(0xFF555555));
        ss.setSpan(new CornerBackgroundSpan(0xFF0099CC, 10), 30, 70, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CornerBackgroundSpan(0xFF0099CC, 10), 250, 450, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(0xFFFFFFFF), 250, 450, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Path path = new Path();
        path.moveTo(0, 12);
        path.lineTo(100, 88);

        ShapeBackgroundSpan shapeSpan = new ShapeBackgroundSpan(0xFF000000, new PathShape(path, 100, 100), true);
        shapeSpan.setStrokeWidth(6);

        ss.setSpan(shapeSpan, 700, 890, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        textView.setSelectionSpan(new CornerBackgroundSpan(0xFF0099CC, 10));

        textView.setText(ss);
        textView.setFocusable(true);
        textView.setEnabled(true);
        textView.requestFocus();
    }

}
