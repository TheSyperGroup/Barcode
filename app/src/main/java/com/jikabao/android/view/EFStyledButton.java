package com.jikabao.android.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.jikabao.android.R;
import com.jikabao.android.util.FontManager;

public class EFStyledButton extends Button {

    public EFStyledButton(Context context) {
        super(context);
    }

    public EFStyledButton(Context context, AttributeSet attrs) {
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

    public EFStyledButton(Context context, AttributeSet attrs, int defStyle) {
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

    @Override
    public void setBackgroundDrawable(Drawable d) {
        // Replace the original background drawable (e.g. image) with a LayerDrawable that
        // contains the original drawable.
        EFStyledButtonBackgroundDrawable layer = new EFStyledButtonBackgroundDrawable(d);
        super.setBackgroundDrawable(layer);
    }

    /**
     * The stateful LayerDrawable used by this button.
     */
    protected class EFStyledButtonBackgroundDrawable extends LayerDrawable {

        // The color filter to apply when the button is pressed
        protected ColorFilter _pressedFilter = new LightingColorFilter(Color.LTGRAY, 1);
        // Alpha value when the button is disabled
        protected int _disabledAlpha = 100;
        // Alpha value when the button is enabled
        protected int _fullAlpha = 255;

        public EFStyledButtonBackgroundDrawable(Drawable d) {
            super(new Drawable[] { d });
        }

        @Override
        protected boolean onStateChange(int[] states) {
            boolean enabled = false;
            boolean pressed = false;

            for (int state : states) {
                if (state == android.R.attr.state_enabled)
                    enabled = true;
                else if (state == android.R.attr.state_pressed)
                    pressed = true;
            }

            mutate();
            if (enabled && pressed) {
                setColorFilter(_pressedFilter);
            } else if (!enabled) {
                setColorFilter(null);
                setAlpha(_disabledAlpha);
            } else {
                setColorFilter(null);
                setAlpha(_fullAlpha);
            }

            invalidateSelf();

            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }

    public void setTextWithVisible(CharSequence text) {
        setVisibility(VISIBLE);
        setText(text);
    }

}
