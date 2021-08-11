package lhg.markdown.span;


import android.text.TextPaint;
import android.text.style.URLSpan;

import lhg.markdown.MarkDownTheme;

public class LinkSpan extends URLSpan {

    private final MarkDownTheme theme;
    private final String link;

    public LinkSpan(
            MarkDownTheme theme,
            String link) {
        super(link);
        this.theme = theme;
        this.link = link;
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        theme.applyLinkStyle(ds);
    }


    public String getLink() {
        return link;
    }
}
