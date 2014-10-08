package com.blunka.mk8assistant.shared.stats;

import android.content.Context;

/**
 * Created by clocksmith on 7/23/14.
 */
public abstract class SingleStatSingleValueView extends AbstractSingleStatView {
  // Model.
  protected float mMaxValue;
  protected float mNewValue;

  public SingleStatSingleValueView(Context context) {
    super(context);
  }

  public void setMaxValue(float maxValue) {
    mMaxValue = maxValue;
  }

  protected abstract void updateValue(float newValue);
}
