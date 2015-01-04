package com.jikabao.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.jikabao.android.R;
import com.jikabao.android.util.FontManager;

public class EFEditText extends EditText {

    // Default constructor when inflating from XML file
    public EFEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            // Fonts work as a combination of particular family and the style.
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Fonts);
            String family = a.getString(R.styleable.Fonts_fontFamily);
            int style = a.getInt(R.styleable.Fonts_android_textStyle, -1);
            a.recycle();

            // Set the typeface based on the family and the style combination.
            setTypeface(FontManager.getInstance().get(family, style));

        }
    }


    // Default constructor override
    public EFEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            // Fonts work as a combination of particular family and the style.
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Fonts);
            String family = a.getString(R.styleable.Fonts_fontFamily);
            int style = a.getInt(R.styleable.Fonts_android_textStyle, -1);
            a.recycle();

            // Set the typeface based on the family and the style combination.
            setTypeface(FontManager.getInstance().get(family, style));

        }
    }
}




