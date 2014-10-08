package com.blunka.mk8assistant.main.compare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.Attribute;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.stats.AbstractSingleStatView;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.blunka.mk8assistant.shared.ui.UiUtils;

/**
 * Created by clocksmith on 7/18/14.
 */
public class SingleStatCompareView extends AbstractSingleStatView {
  private static final String TAG = SingleStatCompareView.class.getSimpleName();

  private float mNewDifferenceValue;
  private float mMaxWidth;

  private TextView mLeftValueTextView;
  private TextView mLabel;
  private TextView mRightValueTextView;
  private LinearLayout mBarContainer;
  private TextView mLeftDifferenceValueTextView;
  private ImageView mLeftBar;
  private TextView mCenterDifferenceValueTextView;
  private ImageView mRightBar;
  private TextView mRightDifferenceValueTextView;

  public SingleStatCompareView(Context context, Attribute attribute) {
    super(context);

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_single_stat_compare, this, true);

    mLeftValueTextView = (TextView) findViewById(R.id.singleStatCompareView_leftValue);
    mLabel = (TextView) findViewById(R.id.singleStatCompareView_label);
    mRightValueTextView = (TextView) findViewById(R.id.singleStatCompareView_rightValue);
    mBarContainer = (LinearLayout) findViewById(R.id.singleStatsCompareView_barContainer);
    mLeftDifferenceValueTextView = (TextView)
        findViewById(R.id.singleStatCompareView_leftDifferenceValue);
    mLeftBar = (ImageView) findViewById(R.id.singleStatCompareView_leftBar);
    mCenterDifferenceValueTextView = (TextView)
        findViewById(R.id.singleStatCompareView_centerDifferenceValue);
    mRightBar = (ImageView) findViewById(R.id.singleStatCompareView_rightBar);
    mRightDifferenceValueTextView = (TextView)
        findViewById(R.id.singleStatCompareView_rightDifferenceValue);

    mLeftValueTextView.setTypeface(FontLoader.getInstance().getRobotoCondensedNormalTypeface());
    mLabel.setTypeface(FontLoader.getInstance().getRobotoCondensedLightTypeface());
    mLabel.setText(attribute.getDisplayName(context));
    mRightValueTextView.setTypeface(FontLoader.getInstance().getRobotoCondensedNormalTypeface());
    mLeftDifferenceValueTextView.setTypeface(FontLoader.getInstance()
        .getRobotoCondensedNormalTypeface());
    mCenterDifferenceValueTextView.setTypeface(FontLoader.getInstance()
        .getRobotoCondensedNormalTypeface());
    mRightDifferenceValueTextView.setTypeface(FontLoader.getInstance()
        .getRobotoCondensedNormalTypeface());

    mBarContainer.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            mOnGlobalLayoutListenerCalled = true;
            // Each bar will have 2 "0.00" text views when expanded.
            mMaxWidth = (float) (mBarContainer.getWidth() / 2) -
                (getResources().getDimension(R.dimen.stats_difference_width) * 2);
            int newWidth = (int) (Math.abs(mNewDifferenceValue) /
                Constants.MAX_TOTAL_STATS_VALUE * mMaxWidth);
            FilteredLogger.d(TAG, "mBarContainer.getWidth(): " + mBarContainer.getWidth());
            if (mNewDifferenceValue >= 0) {
              updateBar(mLeftBar, 0, newWidth);
            }
            if (mNewDifferenceValue <= 0) {
              updateBar(mRightBar, 0, newWidth);
            }
            UiUtils.removeOnGlobalLayoutListener(mBarContainer, this);
          }
        }
    );

    mLeftBar.setPivotX(0);
    mRightBar.setPivotX(0);
  }

  public void updateValues(float newLeftValue, float newRightValue) {
    // Update the values.
    float originalDifferenceValue = mNewDifferenceValue;
    mNewDifferenceValue = newLeftValue - newRightValue;

    // Update the labels.
    mLeftValueTextView.setText(mDecimalFormat.format(newLeftValue));
    mRightValueTextView.setText(mDecimalFormat.format(newRightValue));
    if (mNewDifferenceValue == 0) {
      showDifferenceLabel(mCenterDifferenceValueTextView);
    } else {
      String differenceLabelString = "+" + mDecimalFormat.format(Math.abs(mNewDifferenceValue));
      if (mNewDifferenceValue > 0) {
        mLeftDifferenceValueTextView.setText(differenceLabelString);
        showDifferenceLabel(mLeftDifferenceValueTextView);
      } else if (mNewDifferenceValue < 0) {
        mRightDifferenceValueTextView.setText(differenceLabelString);
        showDifferenceLabel(mRightDifferenceValueTextView);
      }
    }

    // Update the bars.
    int originalWidth = (int) (Math.abs(originalDifferenceValue) /
        Constants.MAX_TOTAL_STATS_VALUE * mMaxWidth);
    int newWidth =  (int) (Math.abs(mNewDifferenceValue) /
        Constants.MAX_TOTAL_STATS_VALUE * mMaxWidth);
    if (mOnGlobalLayoutListenerCalled) {
      updateBar(mLeftBar,
          originalDifferenceValue <= 0 ? 0 : originalWidth,
          mNewDifferenceValue <= 0 ? 0 : newWidth);
      updateBar(mRightBar,
          originalDifferenceValue >= 0 ? 0 : originalWidth,
          mNewDifferenceValue >= 0 ? 0 : newWidth);
    }
  }

  private void showDifferenceLabel(TextView differenceLabel) {
    mLeftDifferenceValueTextView.setVisibility(differenceLabel.equals(mLeftDifferenceValueTextView)
        ? VISIBLE : GONE);
    mCenterDifferenceValueTextView.setVisibility(differenceLabel.equals(
        mCenterDifferenceValueTextView) ? VISIBLE : GONE);
    mRightDifferenceValueTextView.setVisibility(differenceLabel.equals(
        mRightDifferenceValueTextView) ? VISIBLE : GONE);
  }
}
