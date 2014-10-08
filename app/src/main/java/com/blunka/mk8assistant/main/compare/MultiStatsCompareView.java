package com.blunka.mk8assistant.main.compare;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.Attribute;
import com.blunka.mk8assistant.data.Stats;
import com.blunka.mk8assistant.shared.stats.AbstractMultiStatsView;

/**
 * Created by clocksmith on 7/20/14.
 */
public class MultiStatsCompareView extends AbstractMultiStatsView<SingleStatCompareView> {
  public MultiStatsCompareView(Context context) {
    this(context, null);
  }

  public MultiStatsCompareView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MultiStatsCompareView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_multi_stats_compare, this, true);

    mScrollView = (ScrollView) findViewById(R.id.multiStatsCompareView_scrollView);
    mStatViewContainer = (LinearLayout) findViewById(R.id.multiStatsCompareView_statViewContainer);

    initSingleStatViews();
  }

  @Override
  protected SingleStatCompareView createSingleStatView(Attribute attribute) {
    return new SingleStatCompareView(getContext(), attribute);
  }

  @Override
  protected int getIntendedSingleStatViewHeightPx() {
    return (int)
        (getContext().getResources().getDimension(R.dimen.single_stat_compare_height) + 0.5);
  }

  public void updateStatViews(Stats leftStats, Stats rightStats) {
    for (Attribute attribute : Attribute.values()) {
      mSingleStatsViews.get(attribute).updateValues(leftStats.getAttributeValue(attribute),
          rightStats.getAttributeValue(attribute));
    }
  }
}
