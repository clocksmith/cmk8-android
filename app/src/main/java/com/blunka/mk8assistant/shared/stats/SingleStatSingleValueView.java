package com.blunka.mk8assistant.shared.stats;

import android.content.Context;

/**
 * Created by clocksmith on 7/23/14.
 */
public abstract class SingleStatSingleValueView extends AbstractSingleStatView {
  // Model.
  protected double mMaxValue;
  protected double mNewValue;

  public SingleStatSingleValueView(Context context) {
    super(context);
  }

  public void setMaxValue(double maxValue) {
    mMaxValue = maxValue;
  }

  protected abstract void updateValue(double newValue);
}
