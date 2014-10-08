package com.blunka.mk8assistant.main.adjust;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.ui.FontLoader;

/**
 * Created by clocksmith on 7/12/14.
 */
public class ProgressDrawableFactory {
  private Context mContext;
  private Paint mTextPaint;

  public ProgressDrawableFactory(Context context) {
    mContext = context;
    mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setColor(mContext.getResources().getColor(R.color.text_on_dark_bg));
    mTextPaint.setTextSize(mContext.getResources().getDimension(R.dimen.progress_text_size));
    mTextPaint.setTypeface(FontLoader.getInstance().getRobotoCondensedNormalTypeface());
  }

  public BitmapDrawable createDrawable(Drawable originalDrawable, int progress) {
    Bitmap newBitmap = Bitmap.createBitmap(originalDrawable.getIntrinsicWidth(),
        originalDrawable.getIntrinsicHeight(),
        Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);
    originalDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    originalDrawable.draw(canvas);
    String progressDisplaytext = Integer.toString(progress);

    int width = (int) mTextPaint.measureText(progressDisplaytext);
    int x = (canvas.getWidth() - width) / 2;
    int y = (int) ((canvas.getHeight() / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
    canvas.drawText(progressDisplaytext, x, y, mTextPaint);

    return new BitmapDrawable(mContext.getResources(), newBitmap);
  }
}
