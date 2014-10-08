package com.blunka.mk8assistant.shared.ui;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.lang.reflect.Field;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by clocksmith on 7/30/14.
 */
public class UiUtils {
  private static final String TAG = UiUtils.class.getSimpleName();

  public static void removeOnGlobalLayoutListener(View view,
      ViewTreeObserver.OnGlobalLayoutListener victim) {
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
      view.getViewTreeObserver().removeGlobalOnLayoutListener(victim);
    } else {
      view.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
    }
  }

  public static void setCustomVerticalViewPagerScroller(VerticalViewPager viewPager,
      int durationMs) {
    try {
      Field originalScrollerField;
      originalScrollerField = VerticalViewPager.class.getDeclaredField("mScroller");
      originalScrollerField.setAccessible(true);
      CustomSpeedScroller customSpeedScroller = new CustomSpeedScroller(viewPager.getContext(),
          new DecelerateInterpolator());
      customSpeedScroller.setDurationMs(durationMs);
      originalScrollerField.set(viewPager, customSpeedScroller);
    } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
      Log.e(TAG, "setViewPagerScrollDurationMs failed: " + e.getMessage());
    }
  }

  public static void unbindDrawables(View view) {
    internalUnbindDrawables(view);
//    System.gc();
  }

private static void internalUnbindDrawables(View view) {
  if (view.getBackground() != null) {
    view.getBackground().setCallback(null);
  }

  if (view instanceof ImageView) {
    ImageView imageView = (ImageView) view;
    imageView.setImageBitmap(null);
  } else if (view instanceof ViewGroup) {
    ViewGroup viewGroup = (ViewGroup) view;
    for (int i = 0; i < viewGroup.getChildCount(); i++) {
      internalUnbindDrawables(viewGroup.getChildAt(i));
    }

    if (!(view instanceof AdapterView)) {
      viewGroup.removeAllViews();
    }
  }
}
}
