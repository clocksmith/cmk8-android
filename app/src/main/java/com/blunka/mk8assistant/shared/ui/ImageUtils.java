package com.blunka.mk8assistant.shared.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by clocksmith on 9/8/14.
 */
public class ImageUtils {
  public static Bitmap decodeSampledBitmapFromResource(Context context,
      int resourceId,
      Bitmap.Config bitmapConfig,
      int targetWidth,
      int targetHeight) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);

    options.inPreferredConfig = bitmapConfig;
    options.inJustDecodeBounds = false;
    options.inPreferQualityOverSpeed = false;
    options.inDither = true;
    return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
  }

  private static int calculateInSampleSize(
      BitmapFactory.Options options, int targetWidth, int targetHeight) {
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > targetHeight || width > targetWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      while ((halfHeight / inSampleSize) > targetHeight
          && (halfWidth / inSampleSize) > targetWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }
}
