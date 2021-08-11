package lhg.markdown.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;

import lhg.canvasscrollview.blocks.TextBlock;

import java.lang.ref.WeakReference;


public abstract class DrawableSpan extends ReplacementSpan implements TextBlock.Invalidateable {

    public static final int ALIGN_BOTTOM = 0;
    public static final int ALIGN_BASELINE = 1;
    public static final int ALIGN_CENTER = 2;

    protected final int mVerticalAlignment;
    protected TextBlock.Invalidator invalidator;


    private final Drawable placeholder = new ColorDrawable(Color.TRANSPARENT);
    private WeakReference<Drawable> mDrawableRef;


    public DrawableSpan() {
        mVerticalAlignment = ALIGN_BOTTOM;
    }

    protected DrawableSpan(int verticalAlignment) {
        mVerticalAlignment = verticalAlignment;
    }

    int getVerticalAlignment() {
        return mVerticalAlignment;
    }

    public abstract Drawable getDrawable();

    public void setInvalidator(TextBlock.Invalidator invalidator) {
        this.invalidator = invalidator;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        if (d == null) {
            d = placeholder;
            Paint.FontMetricsInt fm2 = paint.getFontMetricsInt();
            int size = fm2.descent - fm2.ascent;
            d.setBounds(0, 0, size, size);
        }
        Rect rect = d.getBounds();
        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getCachedDrawable();
        if (b == null) {
            b = placeholder;
        }
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        } else if (mVerticalAlignment == ALIGN_CENTER) {
            transY = (bottom - top) / 2 - b.getBounds().height() / 2;
        }

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null) {
            d = wr.get();
        }

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference(d);
        }

        return d;
    }
    
    public void invalidate() {
        if (invalidator != null) {
            invalidator.invalidate(this);
        }
    }
}