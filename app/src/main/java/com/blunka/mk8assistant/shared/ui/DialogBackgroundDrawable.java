package com.blunka.mk8assistant.shared.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by clocksmith on 7/26/14.
 */
public class DialogBackgroundDrawable extends Drawable {
  private static final float ROUNDED_RECT_RADIUS = 10f;
  private static final float SHADOW_RADIUS = 12f;
  private Paint mPaint;

  public DialogBackgroundDrawable(View view) {
    view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    mPaint = new Paint();
    mPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, Color.BLACK);
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(Color.WHITE);
  }

  @Override
  public void draw(Canvas canvas) {
    RectF rectf = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());

    canvas.drawRoundRect(rectf, ROUNDED_RECT_RADIUS, ROUNDED_RECT_RADIUS, mPaint);
  }

  @Override
  public void setAlpha(int alpha) {
    mPaint.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    mPaint.setColorFilter(cf);
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }
}
