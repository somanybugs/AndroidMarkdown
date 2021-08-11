package lhg.markdown.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import lhg.canvasscrollview.CanvasScrollView;
import lhg.canvasscrollview.blocks.VerticalGroupBlock;
import lhg.markdown.MarkDownTheme;

public class BulletListItemBlock extends VerticalGroupBlock {

    private final MarkDownTheme theme;
    private final int level;
    private int bulletWidth;
    private final RectF circle =  new RectF();
    private final Rect rectangle = new Rect();
    private static final Paint paint = new Paint();
    private final int indent;
    static {
        paint.setAntiAlias(true);
    }

    public BulletListItemBlock(MarkDownTheme theme, int level) {
        this.theme = theme;
        this.level = level;
        Paint.FontMetricsInt fi = theme.getTextPaint().getFontMetricsInt();
        bulletWidth = (fi.descent - fi.ascent)/2;
        indent = Math.max(bulletWidthPlusPadding(), theme.getListItemIndent());
    }

    @Override
    public void onMeasure(CanvasScrollView parent, int parentWidth, boolean horizontalScrollable) {
        onMeasure(parent, indent, 0, parentWidth, horizontalScrollable);
    }

    private int bulletWidthPlusPadding() {
        return (int) (bulletWidth*1.5f);
    }

    @Override
    public void onDraw(CanvasScrollView parent, Canvas canvas, int left, int top, int right, int bottom) {

        Paint.FontMetricsInt fi = theme.getTextPaint().getFontMetricsInt();
        final int l = getPaddingLeft() + indent - bulletWidthPlusPadding();
        final int t = getPaddingTop() + (fi.bottom - fi.top - bulletWidth) / 2;
        final int b = t + bulletWidth;
        final int r = l + bulletWidth;

        theme.applyListItemStyle(paint);
        if (level == 0 || level == 1) {
            circle.set(l, t, r, b);
            paint.setStyle(level == 0 ? Paint.Style.FILL : Paint.Style.STROKE);
            canvas.drawOval(circle, paint);
        } else {
            rectangle.set(l, t, r, b);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectangle, paint);
        }

        super.onDraw(parent, canvas, left, top, right, bottom);
    }
}
