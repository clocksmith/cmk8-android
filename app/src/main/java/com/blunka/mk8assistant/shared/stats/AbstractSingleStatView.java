package com.blunka.mk8assistant.shared.stats;

import android.animation.ValueAnimator;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blunka.mk8assistant.shared.Constants;

import java.text.DecimalFormat;

/**
 * Created by clocksmith on 7/23/14.
 */
public abstract class AbstractSingleStatView extends LinearLayout {
  private static final String TAG = AbstractSingleStatView.class.getSimpleName();

  protected DecimalFormat mDecimalFormat;
  protected boolean mOnGlobalLayoutListenerCalled;

  public AbstractSingleStatView(Context context) {
    super(context);

    mDecimalFormat = new DecimalFormat(Constants.STATS_DECIMAL_FORMAT);
    mOnGlobalLayoutListenerCalled = false;
  }

  protected void updateBar(final ImageView bar, int originalWidth, int newWidth) {
    ValueAnimator barAnimator = ValueAnimator.ofInt(originalWidth, newWidth);
    barAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        bar.getLayoutParams().width = ((Integer) animation.getAnimatedValue());
        bar.requestLayout();
      }
    });
    barAnimator.setDuration(Constants.STATS_BAR_CHANGE_DURATION_MS);
    barAnimator.start();
  }
}
