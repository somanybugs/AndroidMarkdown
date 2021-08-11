package lhg.markdown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

public class MarkDownTheme {

    public static MarkDownTheme create(Context context) {
        return builderWithDefaults(context).build();
    }

    
    public static Builder builderWithDefaults(Context context) {

        final Dimens dimens = Dimens.create(context);
        return new Builder()
                .dip(dimens)
                .pageHorizontalMargin(dimens.dp2px(12))
                .codeBlockBackgroundColor(0xffecf0ec)
                .codeBackgroundColor(0xffecf0ec)
                .blockQuoteColor(0xffe0e0e0)
                .tableHeaderRowBackgroundColor(0xfff6f8fa)
                .tableBodyRowBackgroundColor(new int[]{0xffffffff, 0xfffdf5e6})
                .tableCellPadding(dimens.dp2px(4))
                .tableBorderWidth(dimens.dp2px(0.5f))
                .tableBorderColor(0xff888888)
                .codeBlockMargin(dimens.dp2px(8))
                .blockMargin(dimens.dp2px(8))
                .listItemIndent(dimens.dp2px(0))
                .blockQuoteWidth(dimens.dp2px(4))
                .bulletListItemStrokeWidth(dimens.dp2px(1))
                .headingBreakHeight(dimens.dp2px(1))
                .thematicBreakHeight(dimens.dp2px(4))
                .linkColor(0xff006400)
                ;
    }

    protected static final float CODE_DEF_TEXT_SIZE_RATIO = .87F;

    protected static final int HEADING_DEF_BREAK_COLOR_ALPHA = 75;

    private static final float[] HEADING_SIZES = {
            1.8F, 1.4F, 1.17F, 1.F, .83F, .67F,
    };

    protected static final int THEMATIC_BREAK_DEF_ALPHA = 25;

    protected TextPaint textPaint;

    protected int linkColor;

    protected boolean isLinkedUnderlined = true;

    protected int pageHorizontalMargin;

    // used in quote, lists
    protected int blockMargin;
    protected int blockQuoteWidth;
    protected int blockQuoteColor;

    // by default uses text color (applied for un-ordered lists & ordered (bullets & numbers)
    protected int listItemColor;
    protected int listItemIndent;//

    // by default the stroke color of a paint object
    protected int bulletListItemStrokeWidth;
    protected int bulletWidth;
    protected int codeTextColor;
    protected int codeBlockTextColor;
    protected int codeBackgroundColor;
    protected int codeBlockBackgroundColor;
    protected int codeBlockMargin;


    protected Typeface codeTypeface;
    protected Typeface codeBlockTypeface;

    protected int codeTextSize;
    protected int codeBlockTextSize;

    protected int headingBreakHeight;
    protected int headingBreakColor;
    protected Typeface headingTypeface;
    protected float[] headingTextSizeMultipliers;

    protected int thematicBreakColor;
    protected int thematicBreakHeight;
    
    
    ////////table
    protected int tableCellPadding;
    protected int tableBorderColor;
    protected int tableBorderWidth;
    protected int[] tableBodyRowBackgroundColor;
    protected int tableHeaderRowBackgroundColor;
    /////////table


    public int getTableCellPadding() {
        return tableCellPadding;
    }

    public int getTableBorderColor() {
        return tableBorderColor;
    }

    public int getTableBorderWidth() {
        return tableBorderWidth;
    }

    public int getTableHeaderRowBackgroundColor() {
        return tableHeaderRowBackgroundColor;
    }

    public int[] getTableBodyRowBackgroundColor() {
        return tableBodyRowBackgroundColor;
    }

    public int getListItemIndent() {
        return listItemIndent;
    }

    public int getPageHorizontalMargin() {
        return pageHorizontalMargin;
    }


    public void applyLinkStyle(TextPaint paint) {
        paint.setUnderlineText(isLinkedUnderlined);
        if (linkColor != 0) {
            paint.setColor(linkColor);
        } else {
            paint.setColor(paint.linkColor);
        }
    }


    public int getBlockQuoteColor() {
        return blockQuoteColor;
    }

    public int getBlockMargin() {
        return blockMargin;
    }

    public int getBlockQuoteWidth() {
        return blockQuoteWidth;
    }

    public void applyListItemStyle(Paint paint) {
        final int color;
        if (listItemColor != 0) {
            color = listItemColor;
        } else {
            color = paint.getColor();
        }
        paint.setColor(color);

        if (bulletListItemStrokeWidth != 0) {
            paint.setStrokeWidth(bulletListItemStrokeWidth);
        }
    }

    public void applyCodeTextStyle(Paint paint) {
        if (codeTextColor != 0) {
            paint.setColor(codeTextColor);
        }
        paint.setTypeface(codeTypeface != null ? codeTypeface : Typeface.MONOSPACE);
        if (codeTextSize > 0) {
            paint.setTextSize(codeTextSize);
        } else {
            paint.setTextSize(paint.getTextSize() * CODE_DEF_TEXT_SIZE_RATIO);
        }
    }

    public void applyCodeBlockTextStyle(Paint paint) {
        if (codeBlockTextColor != 0) {
            paint.setColor(codeBlockTextColor);
        }
        paint.setTypeface(codeBlockTypeface != null ? codeBlockTypeface : Typeface.MONOSPACE);
        if (codeBlockTextSize > 0) {
            paint.setTextSize(codeBlockTextSize);
        } else {
            paint.setTextSize(paint.getTextSize() * CODE_DEF_TEXT_SIZE_RATIO);
        }
    }

    public int getCodeBlockMargin() {
        return codeBlockMargin;
    }

    public int getCodeBackgroundColor() {
        return codeBackgroundColor;
    }

    public int getCodeBlockBackgroundColor() {
        return codeBlockBackgroundColor;
    }

    //level 1 to  6
    public void applyHeadingTextStyle(Paint paint, int level) {
        if (headingTypeface == null) {
            paint.setFakeBoldText(true);
        } else {
            paint.setTypeface(headingTypeface);
        }
        final float[] textSizes = headingTextSizeMultipliers != null
                ? headingTextSizeMultipliers
                : HEADING_SIZES;

        if (textSizes != null && textSizes.length >= level) {
            paint.setTextSize(paint.getTextSize() * textSizes[level - 1]);
        } else {

        }
    }

    public void applyHeadingBreakStyle(Paint paint) {
        final int color;
        if (headingBreakColor != 0) {
            color = headingBreakColor;
        } else {
            color = ColorUtils.applyAlpha(paint.getColor(), HEADING_DEF_BREAK_COLOR_ALPHA);
        }
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        if (headingBreakHeight >= 0) {
            //noinspection SuspiciousNameCombination
            paint.setStrokeWidth(headingBreakHeight);
        }
    }

    public void applyThematicBreakStyle(Paint paint) {
        final int color;
        if (thematicBreakColor != 0) {
            color = thematicBreakColor;
        } else {
            color = ColorUtils.applyAlpha(paint.getColor(), THEMATIC_BREAK_DEF_ALPHA);
        }
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        if (thematicBreakHeight >= 0) {
            //noinspection SuspiciousNameCombination
            paint.setStrokeWidth(thematicBreakHeight);
        }
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    public static class Builder extends MarkDownTheme {

        public Builder textPaint(TextPaint textPaint) {
            this.textPaint = textPaint;
            return this;
        }

        
        public Builder linkColor(int linkColor) {
            this.linkColor = linkColor;
            return this;
        }

        
        public Builder isLinkUnderlined(boolean isLinkUnderlined) {
            this.isLinkedUnderlined = isLinkUnderlined;
            return this;
        }

        
        public Builder blockMargin(int blockMargin) {
            this.blockMargin = blockMargin;
            return this;
        }

        
        public Builder blockQuoteWidth(int blockQuoteWidth) {
            this.blockQuoteWidth = blockQuoteWidth;
            return this;
        }

        
        public Builder blockQuoteColor(int blockQuoteColor) {
            this.blockQuoteColor = blockQuoteColor;
            return this;
        }

        
        public Builder listItemColor(int listItemColor) {
            this.listItemColor = listItemColor;
            return this;
        }

        
        public Builder bulletListItemStrokeWidth(int bulletListItemStrokeWidth) {
            this.bulletListItemStrokeWidth = bulletListItemStrokeWidth;
            return this;
        }

        
        public Builder bulletWidth(int bulletWidth) {
            this.bulletWidth = bulletWidth;
            return this;
        }

        
        public Builder codeTextColor(int codeTextColor) {
            this.codeTextColor = codeTextColor;
            return this;
        }

        
        public Builder codeBlockTextColor(int codeBlockTextColor) {
            this.codeBlockTextColor = codeBlockTextColor;
            return this;
        }

        
        public Builder codeBackgroundColor(int codeBackgroundColor) {
            this.codeBackgroundColor = codeBackgroundColor;
            return this;
        }

        
        public Builder codeBlockBackgroundColor(int codeBlockBackgroundColor) {
            this.codeBlockBackgroundColor = codeBlockBackgroundColor;
            return this;
        }

        
        public Builder codeBlockMargin(int codeBlockMargin) {
            this.codeBlockMargin = codeBlockMargin;
            return this;
        }

        
        public Builder codeTypeface(Typeface codeTypeface) {
            this.codeTypeface = codeTypeface;
            return this;
        }

        
        public Builder codeBlockTypeface(Typeface typeface) {
            this.codeBlockTypeface = typeface;
            return this;
        }

        
        public Builder codeTextSize(int codeTextSize) {
            this.codeTextSize = codeTextSize;
            return this;
        }

        
        public Builder codeBlockTextSize(int codeTextSize) {
            this.codeBlockTextSize = codeTextSize;
            return this;
        }

        
        public Builder headingBreakHeight(int headingBreakHeight) {
            this.headingBreakHeight = headingBreakHeight;
            return this;
        }

        
        public Builder headingBreakColor(int headingBreakColor) {
            this.headingBreakColor = headingBreakColor;
            return this;
        }

        
        public Builder headingTypeface(Typeface headingTypeface) {
            this.headingTypeface = headingTypeface;
            return this;
        }

        
        public Builder headingTextSizeMultipliers(float[] headingTextSizeMultipliers) {
            this.headingTextSizeMultipliers = headingTextSizeMultipliers;
            return this;
        }

        
        public Builder thematicBreakColor(int thematicBreakColor) {
            this.thematicBreakColor = thematicBreakColor;
            return this;
        }

        
        public Builder thematicBreakHeight(int thematicBreakHeight) {
            this.thematicBreakHeight = thematicBreakHeight;
            return this;
        }

        
        public MarkDownTheme build() {
            if (textPaint == null) {
                textPaint = new TextPaint();
                textPaint.setTextSize(dimens.dp2px(16));
                textPaint.setColor(Color.BLACK);
            }
            textPaint.setAntiAlias(true);
            return this;
        }


        
        public Builder tableCellPadding(int tableCellPadding) {
            this.tableCellPadding = tableCellPadding;
            return this;
        }

        
        public Builder tableBorderColor(int tableBorderColor) {
            this.tableBorderColor = tableBorderColor;
            return this;
        }

        
        public Builder tableBorderWidth(int tableBorderWidth) {
            this.tableBorderWidth = tableBorderWidth;
            return this;
        }

        
        public Builder tableBodyRowBackgroundColor(int[] tableBodyRowBackgroundColor) {
            this.tableBodyRowBackgroundColor = tableBodyRowBackgroundColor;
            return this;
        }

        
        public Builder tableHeaderRowBackgroundColor(int tableHeaderRowBackgroundColor) {
            this.tableHeaderRowBackgroundColor = tableHeaderRowBackgroundColor;
            return this;
        }

        public Builder listItemIndent(int indent) {
            this.listItemIndent = indent;
            return this;
        }


        public Builder pageHorizontalMargin(int pageHorizontalMargin) {
            this.pageHorizontalMargin = pageHorizontalMargin;
            return this;
        }


        private Dimens dimens;
        public Builder dip(Dimens dimens) {
            this.dimens = dimens;
            return this;
        }
    }

}
