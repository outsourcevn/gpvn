package com.collalab.caygiapha.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.collalab.caygiapha.GiaPhaApp;

/**
 * Created by laptop88 on 3/6/2017.
 */

public class AwesomeFontTextView extends TextView {
    public AwesomeFontTextView(Context context) {
        this(context, null, 0);
    }

    public AwesomeFontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AwesomeFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface awesomeFont = GiaPhaApp.getFont("fonts/fontawesome-webfont.ttf", context);
        setTypeface(awesomeFont);
        isInEditMode();
    }

}
