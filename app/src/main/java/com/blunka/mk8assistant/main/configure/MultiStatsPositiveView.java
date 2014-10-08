package com.blunka.mk8assistant.main.configure;

import android.content.Context;
import android.util.AttributeSet;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.Attribute;
import com.blunka.mk8assistant.shared.stats.MultiStatsWithLabelView;

/**
 * Created by clocksmith on 7/14/14.
 */
public class MultiStatsPositiveView extends
    MultiStatsWithLabelView<SingleStatPositiveView> {
  public MultiStatsPositiveView(Context context) {
    this(context, null);
  }

  public MultiStatsPositiveView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MultiStatsPositiveView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    // Bind and init happens in parent class.
  }

  @Override
  protected SingleStatPositiveView createSingleStatView(Attribute attribute) {
    return new SingleStatPositiveView(getContext());
  }

  @Override
  protected int getIntendedSingleStatViewHeightPx() {
    return (int) (getResources().getDimension(R.dimen.single_stat_height) + 0.5);
  }
}
