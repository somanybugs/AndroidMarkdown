package lhg.markdown.view;

import android.text.TextPaint;

import lhg.canvasscrollview.CanvasScrollView;
import lhg.canvasscrollview.blocks.GroupBlock;
import lhg.canvasscrollview.blocks.TextBlock;
import lhg.canvasscrollview.blocks.VerticalGroupBlock;
import lhg.markdown.MarkDownTheme;

public class MDHeadBlock extends VerticalGroupBlock {

    private TextPaint textPaint2 = new TextPaint();
    private final MarkDownTheme theme;
    private final int level;

    public MDHeadBlock(MarkDownTheme theme, int level) {
        this.theme = theme;
        this.level = level;
        textPaint2.set(theme.getTextPaint());
        theme.applyHeadingTextStyle(textPaint2, level);
    }

    @Override
    public void onMeasure(CanvasScrollView parent, int parentWidth) {
        updateTextPaint(this);
        super.onMeasure(parent, parentWidth);
    }

    private void updateTextPaint(GroupBlock group) {
        for (CanvasScrollView.CanvasBlock block : group.getChildren()) {
            if (block instanceof TextBlock) {
                ((TextBlock) block).setTextPaint(textPaint2);
            } else if (block instanceof GroupBlock) {
                updateTextPaint((GroupBlock) block);
            }
        }
    }
}
