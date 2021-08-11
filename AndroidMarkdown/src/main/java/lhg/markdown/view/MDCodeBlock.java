package lhg.markdown.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

import lhg.canvasscrollview.CanvasScrollView;
import lhg.canvasscrollview.blocks.GroupBlock;
import lhg.canvasscrollview.blocks.TextBlock;
import lhg.markdown.MarkDownTheme;

public class MDCodeBlock extends GroupBlock {
    static TextPaint textPaint = new TextPaint();
    static Paint paint = new Paint();
    static {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
    }

    final MarkDownTheme theme;
    final int margin;

    public MDCodeBlock(MarkDownTheme theme) {
        this.theme = theme;
        margin = theme.getCodeBlockMargin();
        textPaint.set(theme.getTextPaint());
        theme.applyCodeBlockTextStyle(textPaint);
    }

    @Override
    public void onMeasure(CanvasScrollView parent, int parentWidth) {
        updateTextPaint(this);
        onMeasure(parent, parentWidth, true);
    }

    @Override
    public void onMeasure(CanvasScrollView parent, int parentWidth, boolean horizontalScrollable) {
        int width = 0;
        int height = getPaddingTop() + margin;
        int ph = getPaddingLeft() + getPaddingRight() + margin * 2;
        for (CanvasScrollView.CanvasBlock b : children) {
            b.setTop(height);
            b.setLeft(getPaddingLeft() + margin);
            b.onMeasure(parent, parentWidth - ph, horizontalScrollable);
            width = Math.max(width, b.getWidth());
            height += b.getHeight();
        }
        setWidth(Math.max(parentWidth, width + ph));
        setHeight(height + getPaddingBottom() + margin);
    }

    @Override
    public void onDraw(CanvasScrollView parent, Canvas canvas, int left, int top, int right, int bottom) {
        updateTextPaint(this);
        paint.setColor(theme.getCodeBlockBackgroundColor());

        left = Math.max(left, getPaddingLeft());
        top = Math.max(top, getPaddingTop());
        right = Math.min(right, getWidth() - getPaddingRight());
        bottom = Math.min(bottom, getHeight() - getPaddingBottom());
        canvas.drawRect(left, top, right, bottom, paint);
        super.onDraw(parent, canvas, left, top, right, bottom);
    }


    private void updateTextPaint(GroupBlock group) {
        for (CanvasScrollView.CanvasBlock block : group.getChildren()) {
            if (block instanceof TextBlock) {
                ((TextBlock) block).setTextPaint(textPaint);
            } else if (block instanceof GroupBlock) {
                updateTextPaint((GroupBlock) block);
            }
        }
    }
}
