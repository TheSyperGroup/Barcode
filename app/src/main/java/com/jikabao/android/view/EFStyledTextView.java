package com.jikabao.android.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.jikabao.android.R;
import com.jikabao.android.util.EFLogger;
import com.jikabao.android.util.FontManager;

public class EFStyledTextView extends TextView {

    private static final String TAG = EFStyledTextView.class.getName();

    // Minimum text size for this text view
    public static final float MIN_TEXT_SIZE = 12;

    // Interface for resize notifications
    public interface OnTextResizeListener {
        public void onTextResize(TextView textView, float oldSize, float newSize);
    }

    // Our ellipse string
    private static final String mEllipsis = "...";

    // Registered resize listener
    private OnTextResizeListener mTextResizeListener;

    // Flag for text and/or size changes to force a resize
    private boolean mNeedsResize = false;

    // Text size that is set from code. This acts as a starting point for resizing
    private float mTextSize;

    // Temporary upper bounds on the starting text size
    private float mMaxTextSize = 0;

    // Lower bounds for text size
    private float mMinTextSize = MIN_TEXT_SIZE;

    // Text view line spacing multiplier
    private float mSpacingMult = 1.0f;

    // Text view additional line spacing
    private float mSpacingAdd = 0.0f;

    // Add ellipsis to text that overflows at the smallest text size
    private boolean mAddEllipsis = true;

	private TextPaint originalPaint;

	private TextPaint childPaint;

    // Default constructor override
    public EFStyledTextView(Context context) {
        this(context, null);
    }

    // Default constructor when inflating from XML file
    public EFStyledTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // Default constructor override
    public EFStyledTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTextSize = getTextSize();

        if (!isInEditMode()) {
            // Fonts work as a combination of particular family and the style.
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Fonts);
            String family = a.getString(R.styleable.Fonts_fontFamily);
            int style = a.getInt(R.styleable.Fonts_android_textStyle, -1);
            a.recycle();

            // Set the typeface based on the family and the style combination.
            setTypeface(FontManager.getInstance().get(family, style));

        }
        
        originalPaint = getPaint();
        childPaint = new TextPaint(originalPaint);
    }

    /**
     * When text changes, set the force resize flag to true and reset the text size.
     */
    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        mNeedsResize = true;
        // Since this view may be reused, it is good to reset the text size
        resetTextSize();
    }

    /**
     * If the text view size changed, set the force resize flag to true
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mNeedsResize = true;
        }
    }

    /**
     * Register listener to receive resize notifications
     *
     * @param listener
     */
    public void setOnResizeListener(OnTextResizeListener listener) {
        mTextResizeListener = listener;
    }

    public void setTextWithVisible(CharSequence text) {
        setVisibility(VISIBLE);
        setText(text);
    }

    /**
     * Override the set text size to update our internal reference values
     */
    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        mTextSize = getTextSize();
    }

    /**
     * Override the set text size to update our internal reference values
     */
    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        mTextSize = getTextSize();
    }

    /**
     * Override the set line spacing to update our internal reference values
     */
    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        mSpacingMult = mult;
        mSpacingAdd = add;
    }

    /**
     * Set the upper text size limit and invalidate the view
     *
     * @param maxTextSize
     */
    public void setMaxTextSize(float maxTextSize) {
        mMaxTextSize = maxTextSize;
        requestLayout();
        invalidate();
    }

    /**
     * Return upper text size limit
     *
     * @return
     */
    public float getMaxTextSize() {
        return mMaxTextSize;
    }

    /**
     * Set the lower text size limit and invalidate the view
     *
     * @param minTextSize
     */
    public void setMinTextSize(float minTextSize) {
        mMinTextSize = minTextSize;
        requestLayout();
        invalidate();
    }

    /**
     * Return lower text size limit
     *
     * @return
     */
    public float getMinTextSize() {
        return mMinTextSize;
    }

    /**
     * Set flag to add ellipsis to text that overflows at the smallest text size
     *
     * @param addEllipsis
     */
    public void setAddEllipsis(boolean addEllipsis) {
        mAddEllipsis = addEllipsis;
    }

    /**
     * Return flag to add ellipsis to text that overflows at the smallest text size
     *
     * @return
     */
    public boolean getAddEllipsis() {
        return mAddEllipsis;
    }

    /**
     * Reset the text to the original size
     */
    public void resetTextSize() {
        if (mTextSize > 0) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mMaxTextSize = mTextSize;
        }
    }

    /**
     * Resize text after measuring
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed || mNeedsResize) {
            int widthLimit = (right - left) - getCompoundPaddingLeft() - getCompoundPaddingRight();
            int heightLimit = (bottom - top) - getCompoundPaddingBottom() - getCompoundPaddingTop();
            resizeText(widthLimit, heightLimit);
        }
        super.onLayout(changed, left, top, right, bottom);
    }


    /**
     * Resize the text size with default width and height
     */
    public void resizeText() {
        int heightLimit = getHeight() - getPaddingBottom() - getPaddingTop();
        int widthLimit = getWidth() - getPaddingLeft() - getPaddingRight();
        resizeText(widthLimit, heightLimit);
    }

    /**
     * Resize the text size with specified width and height
     *
     * @param width
     * @param height
     */
    public void resizeText(int width, int height) {


        CharSequence text = getText();

        // Do not resize if the view does not have dimensions or there is no text
        if (text == null || text.length() == 0 || height <= 0 || width <= 0 || mTextSize == 0) {
            return;
        }



        // Store the current text size
        float oldTextSize = originalPaint.getTextSize();
        // If there is a max text size set, use the lesser of that and the default text size
        float targetTextSize = mMaxTextSize > 0 ? Math.min(mTextSize, mMaxTextSize) : mTextSize;

        // Get the required text height
        int textHeight = getTextHeight(text, originalPaint, width, targetTextSize);

        // Until we either fit within our text view or we had reached our min text size, incrementally try smaller sizes
        while (textHeight > height && targetTextSize > mMinTextSize) {
            targetTextSize = Math.max(targetTextSize - 1, mMinTextSize);
            textHeight = getTextHeight(text, originalPaint, width, targetTextSize);
        }


        if (!isInEditMode() && targetTextSize < mTextSize) {
            EFLogger.d(TAG, "Resize text: " + text + " from Size: " + mTextSize + "to Size: " + targetTextSize);
        }

        // If we had reached our minimum text size and still don't fit, append an ellipsis
        if (mAddEllipsis && targetTextSize == mMinTextSize && textHeight > height) {
            TextPaint paint = childPaint;
            paint.set(originalPaint);
            // Draw using a static layout
            StaticLayout layout = new StaticLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, false);
            // Check that we have a least one line of rendered text
            if (layout.getLineCount() > 0) {
                // Since the line at the specific vertical position would be cut off,
                // we must trim up to the previous line
                int lastLine = layout.getLineForVertical(height) - 1;
                // If the text would not even fit on a single line, clear it
                if (lastLine < 0) {
                    setText("");
                }
                // Otherwise, trim to the previous line and add an ellipsis
                else {
                    int start = layout.getLineStart(lastLine);
                    int end = layout.getLineEnd(lastLine);
                    float lineWidth = layout.getLineWidth(lastLine);
                    float ellipseWidth = paint.measureText(mEllipsis);

                    // Trim characters off until we have enough room to draw the ellipsis
                    while (width < lineWidth + ellipseWidth && end > 0) {
                        lineWidth = paint.measureText(text.subSequence(start, --end + 1).toString());
                    }
                    setText(text.subSequence(0, end) + mEllipsis);
                }
            }
        }
        // Some devices try to auto adjust line spacing, so force default line spacing
        // and invalidate the layout as a side effect


        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize);
        setLineSpacing(mSpacingAdd, mSpacingMult);


        // Notify the listener if registered
        if (mTextResizeListener != null) {
            mTextResizeListener.onTextResize(this, oldTextSize, targetTextSize);
        }

        // Reset force resize flag
        mNeedsResize = false;
    }

    // Set the text size of the text paint object and use a static layout to render text off screen before measuring
    private int getTextHeight(CharSequence source, TextPaint originalPaint, int width, float textSize) {
        
    	childPaint.reset();
    	childPaint.set(originalPaint);
        // Update the text paint object
        childPaint.setTextSize(textSize);
        // Measure using a static layout
        StaticLayout layout = new StaticLayout(source.toString().trim(), childPaint, width, Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true);
        return layout.getHeight();
    }

    @Override
    public void setTextAppearance(Context context, int resid) {
        super.setTextAppearance(context, resid);

        // If the text is restyled, this may set a new lower bound on the text size.
        mTextSize = super.getTextSize();
    }
}
