package lhg.markdown.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import lhg.canvasscrollview.CanvasScrollView;
import lhg.canvasscrollview.blocks.VerticalGroupBlock;
import lhg.markdown.MarkDownTheme;

public class MDBlockQuoteBlock extends VerticalGroupBlock {
    static Paint paint = new Paint();
    static {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    final MarkDownTheme theme;

    public MDBlockQuoteBlock(MarkDownTheme theme) {
        this.theme = theme;
    }

    @Override
    public void onMeasure(CanvasScrollView parent, int parentWidth, boolean horizontalScrollable) {
        int leftOffset = theme.getBlockQuoteWidth() + theme.getBlockMargin();
        onMeasure(parent, leftOffset, 0, parentWidth, horizontalScrollable);
    }

    @Override
    public void onDraw(CanvasScrollView parent, Canvas canvas, int left, int top, int right, int bottom) {
        paint.setColor(theme.getBlockQuoteColor());
        canvas.drawRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + theme.getBlockQuoteWidth(), getHeight() - getPaddingBottom(), paint);
        super.onDraw(parent, canvas, left, top, right, bottom);
    }
}
