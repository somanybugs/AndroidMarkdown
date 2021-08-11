package lhg.markdown.view;

import lhg.canvasscrollview.blocks.TextBlock;
import lhg.markdown.Linkify;
import lhg.markdown.MarkDownTheme;
import lhg.markdown.span.LinkSpan;

public class MDTextBlock extends TextBlock {

    private final MarkDownTheme theme;

    public MDTextBlock(MarkDownTheme theme, CharSequence text) {
        super(text);
//        Linkify.addLinks(this.text, Linkify.ALL, s -> new LinkSpan(theme, s));
        textPaint = theme.getTextPaint();
        this.theme= theme;
    }

}
