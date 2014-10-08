package com.blunka.mk8assistant.shared.ui;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by clocksmith on 9/11/14.
 */
public class CustomSpeedScroller extends Scroller {

  private int mDurationMs;

  public CustomSpeedScroller(Context context) {
    super(context);
  }

  public CustomSpeedScroller(Context context, Interpolator interpolator) {
    super(context, interpolator);
  }

  public CustomSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
    super(context, interpolator, flywheel);
  }

  @Override
  public void startScroll(int startX, int startY, int dx, int dy, int duration) {
    // Ignore received duration, use fixed one instead
    super.startScroll(startX, startY, dx, dy, mDurationMs);
  }

  @Override
  public void startScroll(int startX, int startY, int dx, int dy) {
    // Ignore received duration, use fixed one instead
    super.startScroll(startX, startY, dx, dy, mDurationMs);
  }

  public void setDurationMs(int durationMs) {
    mDurationMs = durationMs;
  }
}