package com.blunka.mk8assistant.shared.stats;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blunka.mk8assistant.data.Attribute;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by clocksmith on 7/23/14.
 */
public abstract class AbstractMultiStatsView<T extends AbstractSingleStatView>
    extends LinearLayout {
  // Model.
  protected Map<Attribute, T> mSingleStatsViews;

  // View.
  protected ScrollView mScrollView;
  protected LinearLayout mStatViewContainer;

  public AbstractMultiStatsView(Context context) {
    this(context, null);
  }

  public AbstractMultiStatsView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AbstractMultiStatsView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  protected abstract T createSingleStatView(Attribute attribute);

  protected abstract int getIntendedSingleStatViewHeightPx();

  protected void initSingleStatViews() {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        getIntendedSingleStatViewHeightPx());
    layoutParams.gravity = Gravity.CENTER_VERTICAL;

    mSingleStatsViews = Maps.newHashMap();
    for (Attribute attribute : Attribute.getAllValuesInOrder()) {
      T singleStatView = createSingleStatView(attribute);
      mStatViewContainer.addView(singleStatView, layoutParams);
      mSingleStatsViews.put(attribute, singleStatView);
    }
  }

  public ScrollView getScrollView() {
    return mScrollView;
  }
}
