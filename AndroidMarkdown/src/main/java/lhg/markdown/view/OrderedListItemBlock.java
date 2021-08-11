package lhg.markdown.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import lhg.canvasscrollview.CanvasScrollView;
import lhg.canvasscrollview.blocks.VerticalGroupBlock;
import lhg.markdown.MarkDownTheme;

public class OrderedListItemBlock extends VerticalGroupBlock {

    private final MarkDownTheme theme;
    private final String number;
    private final int numberWidth;
    private final int numberHeight;
    private final int indent;

    public OrderedListItemBlock(MarkDownTheme theme, String number) {
        this.theme = theme;
        this.number = number;
        setClipPadding(false);
        numberWidth = (int) theme.getTextPaint().measureText(number);
        Paint.FontMetricsInt fi = theme.getTextPaint().getFontMetricsInt();
        numberHeight = fi.descent - fi.ascent;
        indent = Math.max(numberWithPlusPadding(), theme.getListItemIndent());
    }

    @Override
    public void onMeasure(CanvasScrollView parent, int parentWidth, boolean horizontalScrollable) {
        onMeasure(parent, indent, 0, parentWidth, horizontalScrollable);
    }

    private int numberWithPlusPadding() {
        return numberWidth + numberHeight/4;
    }

    @Override
    public void onDraw(CanvasScrollView parent, Canvas canvas, int left, int top, int right, int bottom) {
        Paint.FontMetricsInt fi = theme.getTextPaint().getFontMetricsInt();
        int baseline = getPaddingTop() - fi.ascent;
        canvas.drawText(number, getPaddingLeft() + indent - numberWithPlusPadding(), baseline, theme.getTextPaint());
        super.onDraw(parent, canvas, left, top, right, bottom);
    }
}
