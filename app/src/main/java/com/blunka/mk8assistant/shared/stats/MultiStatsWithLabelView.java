package com.blunka.mk8assistant.shared.stats;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.Attribute;
import com.blunka.mk8assistant.data.Stats;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.FontLoader;

/**
 * Created by clocksmith on 7/23/14.
 */
public abstract class MultiStatsWithLabelView<T extends SingleStatSingleValueView>
    extends AbstractMultiStatsView<T> {
  private static final String TAG = MultiStatsWithLabelView.class.getSimpleName();

  private long mStartTimeMs;

  protected Context mContext;

  protected LinearLayout mLabelContainer;

  public MultiStatsWithLabelView(Context context) {
    this(context, null);
  }

  public MultiStatsWithLabelView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MultiStatsWithLabelView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mStartTimeMs = System.currentTimeMillis();
    FilteredLogger.d(TAG, "MultiStatsWithLabelView() start: " +
        (System.currentTimeMillis() - mStartTimeMs) + "ms");
    mContext = context;

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_multi_stats_with_label, this, true);

    mScrollView = (ScrollView) findViewById(R.id.multiStatsWithLabelView_scrollView);
    mLabelContainer = (LinearLayout)
        findViewById(R.id.multiStatsWithLabelView_labelContainer);
    mStatViewContainer = (LinearLayout)
        findViewById(R.id.multiStatsWithLabelView_statViewContainer);

    initLabelViews();
    initSingleStatViews();
    FilteredLogger.d(TAG, "MultiStatsWithLabelView() end: " +
        (System.currentTimeMillis() - mStartTimeMs) + "ms");
  }

  private void initLabelViews() {
    FilteredLogger.d(TAG, "initLabelViews(): " +
        (System.currentTimeMillis() - mStartTimeMs) + "ms");
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        getIntendedSingleStatViewHeightPx());
    Typeface typeface = FontLoader.getInstance().getRobotoCondensedLightTypeface();
    for (Attribute attribute : Attribute.getAllValuesInOrder()) {
      TextView labelView = new TextView(mContext);
      labelView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
          getResources().getDimension(R.dimen.stats_attribute_text_size));
      labelView.setTypeface(typeface);
      labelView.setText(attribute.getDisplayName(mContext));
      labelView.setGravity(Gravity.CENTER_VERTICAL);
      mLabelContainer.addView(labelView, layoutParams);
    }
  }

  @Override
  protected void initSingleStatViews() {
    FilteredLogger.d(TAG, "initSingleStatViews(): " +
        (System.currentTimeMillis() - mStartTimeMs) + "ms");
    super.initSingleStatViews();
  }

  public void updateStatViews(Stats stats) {
    for (Attribute attribute : Attribute.values()) {
      mSingleStatsViews.get(attribute).updateValue(stats.getAttributeValue(attribute));
    }
  }

  public void setMaxValue(float maxValue) {
    for (Attribute attribute : Attribute.getAllValuesInOrder()) {
      mSingleStatsViews.get(attribute).setMaxValue(maxValue);
    }
  }
}
