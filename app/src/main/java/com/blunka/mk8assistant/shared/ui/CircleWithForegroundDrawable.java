package com.blunka.mk8assistant.shared.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.blunka.mk8assistant.R;

/**
 * Created by clocksmith on 7/25/14.
 */
public class CircleWithForegroundDrawable extends Drawable {
  private static final String TAG = CircleWithForegroundDrawable.class.getSimpleName();

  private static final float SHADOW_RADIUS = 12f;

  private Context mContext;
  private ImageView mImageView;
  private int mBackgroundColor;
  private String mText;
  private Bitmap mForegroundBitmap;
  private int mFontSizeDimen;
  private Typeface mTypeface;
  private boolean mDrawShadow;
  private Paint mPaint;

  public CircleWithForegroundDrawable(Context context,
      ImageView imageView,
      int backgroundColor,
      boolean drawShadow) {
    mContext = context;
    mImageView = imageView;
    mBackgroundColor = backgroundColor;
    mDrawShadow = drawShadow;
    mPaint = new Paint();
  }

  public CircleWithForegroundDrawable(Context context,
      ImageView imageView,
      int backgroundColor,
      String text,
      int fontSizeDimen,
      Typeface typeface,
      boolean drawShadow) {
    this(context, imageView, backgroundColor, drawShadow);
    mText = text;
    mFontSizeDimen = fontSizeDimen;
    mTypeface = typeface;
  }

  public CircleWithForegroundDrawable(Context context,
      ImageView imageView,
      int backgroundColor,
      Bitmap bitmapForeground,
      boolean drawShadow) {
    this(context, imageView, backgroundColor, drawShadow);
    mForegroundBitmap = bitmapForeground;
  }

  @Override
  public void draw(Canvas canvas) {
    mImageView.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
    if (mDrawShadow) {
      mPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, Color.GRAY);
    }

    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(mContext.getResources().getColor(mBackgroundColor));
    canvas.drawCircle(canvas.getWidth() / 2,
        canvas.getHeight() / 2,
        canvas.getWidth() / 2 - SHADOW_RADIUS,
        mPaint);

    if (mText != null) {
      mPaint.setColor(mContext.getResources().getColor(R.color.text_on_dark_bg));
      mPaint.setTextSize(mContext.getResources().getDimension(mFontSizeDimen));
      mPaint.setTypeface(mTypeface);
      mPaint.clearShadowLayer();

      int width = (int) mPaint.measureText(mText);
      int x = (canvas.getWidth() - width) / 2;
      int y = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
      canvas.drawText(mText, x, y, mPaint);
    }

    if (mForegroundBitmap != null) {
      mPaint.clearShadowLayer();

      Bitmap scaledForegroundBitmap = Bitmap.createScaledBitmap(mForegroundBitmap,
          canvas.getWidth() / 3,
          canvas.getHeight() / 3,
          false);

      int x = (canvas.getWidth() - scaledForegroundBitmap.getWidth()) / 2;
      int y = (canvas.getWidth() - scaledForegroundBitmap.getHeight()) / 2;
      canvas.drawBitmap(scaledForegroundBitmap, x, y, mPaint);
    }
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