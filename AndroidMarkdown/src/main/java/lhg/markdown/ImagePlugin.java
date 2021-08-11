package lhg.markdown;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import lhg.canvasscrollview.blocks.TextBlock;
import lhg.markdown.span.DrawableSpan;

import org.commonmark.node.Image;
import org.commonmark.parser.Parser;

public abstract class ImagePlugin implements MarkDownPlugin {

    @Override
    public void onParserBuild(Parser.Builder builder) {

    }

    @Override
    public void onRegisterHandler(MarkDownPlugin.Register register) {
        register.register(Image.class, new MarkdownHandler<Image>() {
            @Override
            public void handler(Image node, MarkDownVisitor visitor) {
                ImageSpan span = createImageSpan();
                span.path = node.getDestination();
                String text = node.getTitle();
                if (TextUtils.isEmpty(text)) {
                    text = "image";
                }
                appendToSpannable(visitor.getSpannableBuilder(), text, span);
            }
        });
    }

    public abstract ImageSpan createImageSpan();

    public abstract static class ImageSpan extends DrawableSpan {

        public Bitmap bitmap;
        private String path;

        public ImageSpan() {
        }

        @Override
        public Drawable getDrawable() {
            if (bitmap != null) {
                Drawable drawable = new BitmapDrawable(bitmap);
                drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                return drawable;
            }
            return null;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            loadBitmap(path);
            return super.getSize(paint, text, start, end, fm);
        }

        protected abstract void loadBitmap(String path);

        protected void updateBitmap(Bitmap bitmap) {
            if (this.bitmap != bitmap) {
                this.bitmap = bitmap;
                invalidate();
            }
        }

    }

    public static void appendToSpannable(SpannableStringBuilder builder, CharSequence text, Object... whats) {
        int start = builder.length();
        builder.append(text);
        if (whats != null) {
            for (Object what : whats) {
                builder.setSpan(what, start, builder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }

}
