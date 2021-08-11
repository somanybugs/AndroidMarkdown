package lhg.markdown;

import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lhg.canvasscrollview.CanvasScrollView;
import lhg.canvasscrollview.DimenUtils;
import lhg.canvasscrollview.SelectableAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    CanvasScrollView scrollView;
    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
        scrollView.setAdapter(adapter = new MyAdapter(this));

        Single.fromCallable(() -> {
            try (InputStream is = getAssets().open("test.md")) {
                String text = inputstream2text(is);
                return text;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(node ->  {
                    TextPaint textpaint = new TextPaint();
                    textpaint.setTextSize(DimenUtils.sp2px(getApplication(), 18));
                    MarkDownTheme theme = MarkDownTheme.builderWithDefaults(getApplicationContext())
                            .textPaint(textpaint)
                            .build();
                    adapter.setDatas(new MarkDownParser(theme)
                            .usePlugin(new TablePlugin())
                            .render(node));
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }



    static class MyAdapter extends SelectableAdapter {

        List<CanvasScrollView.CanvasBlock> datas;

        public MyAdapter(Context context) {
            super(context);
        }

        public void setDatas(List<CanvasScrollView.CanvasBlock> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return datas != null ? datas.size() : 0;
        }

        @Override
        public CanvasScrollView.CanvasBlock getItem(@NonNull CanvasScrollView parent, int position) {
            return datas.get(position);
        }
    }

    public static String inputstream2text(InputStream is) {
        return inputstream2text(is, "utf-8");
    }

    public static String inputstream2text(InputStream is, String charset) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] chs = new byte[2048];
            int count = 0;
            while ((count = is.read(chs)) > 0) {
                baos.write(chs, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        try {
            return new String(baos.toByteArray(), charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}