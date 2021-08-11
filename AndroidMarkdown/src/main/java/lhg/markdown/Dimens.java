package lhg.markdown;

import android.content.Context;
import android.util.DisplayMetrics;

public class Dimens {

    
    public static Dimens create(Context context) {
        DisplayMetrics m = context.getResources().getDisplayMetrics();
        return new Dimens(m.density, m.scaledDensity);
    }

    
    public static Dimens create(float density, float scaledDensity) {
        return new Dimens(density, scaledDensity);
    }

    private final float scaledDensity;
    private final float density;

    public Dimens(float density, float scaledDensity) {
        this.density = density;
        this.scaledDensity = scaledDensity;
    }

    public int dp2px(float dp) {
        return (int) (dp * density + .5F);
    }

    public int sp2px( float spValue) {
        return (int) (spValue * scaledDensity + 0.5f);
    }

    public float px2sp( float pxValue) {
        return (pxValue / scaledDensity + 0.5f);
    }

    public  float px2dp( float pxValue) {
        return (pxValue / density + 0.5f);
    }
}
