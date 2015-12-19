package com.example.younghokim.thebalance;

/**
 * Created by younghokim on 15. 12. 18.
 */
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class ClickedBackground {
    public static final Drawable background = new Drawable() {
        private Paint paint;
        {
            paint = new Paint();
            paint.setColor(Color.rgb(196, 196, 196));
        }
        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(canvas.getWidth() / 2,
                    canvas.getHeight() / 2,
                    canvas.getHeight() / 3,
                    paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    };
}
