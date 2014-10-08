package com.blunka.mk8assistant.shared.stats;

import android.content.Context;
import android.util.AttributeSet;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.Attribute;

/**
 * Created by clocksmith on 7/23/14.
 */
public class MultiStatsMaybeNegativeView extends
    MultiStatsWithLabelView<SingleStatMaybeNegativeView> {
  public MultiStatsMaybeNegativeView(Context context) {
    super(context);
  }

  public MultiStatsMaybeNegativeView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MultiStatsMaybeNegativeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected SingleStatMaybeNegativeView createSingleStatView(Attribute attribute) {
    return new SingleStatMaybeNegativeView(getContext());
  }

  @Override
  protected int getIntendedSingleStatViewHeightPx() {
    return (int) (getResources().getDimension(R.dimen.single_stat_height) + 0.5);
  }
}
