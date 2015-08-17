package com.blunka.mk8assistant.shared.stats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.blunka.mk8assistant.shared.ui.UiUtils;

/**
 * Created by clocksmith on 7/23/14.
 */
public class SingleStatMaybeNegativeView extends SingleStatSingleValueView {
  private static final String TAG = SingleStatMaybeNegativeView.class.getSimpleName();

  private FrameLayout mNegativeBarContainer;
  private ImageView mNegativeBarImage;
  private TextView mPositiveValueLabel;
  private TextView mZeroLabel;
  private FrameLayout mPositiveBarContainer;
  private ImageView mPositiveBarImage;
  private TextView mNegativeValueLabel;

  public SingleStatMaybeNegativeView(Context context) {
    super(context);

    mMaxValue = Constants.MAX_SMALL_PART_STATS_VALUE;

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_single_stat_maybe_negative, this, true);

    mNegativeBarContainer = (FrameLayout)
        findViewById(R.id.singleStatMaybeNegativeView_negativeBarContainer);
    mNegativeBarImage = (ImageView) findViewById(R.id.singleStatMaybeNegativeView_negativeBarImage);
    mPositiveValueLabel = (TextView)
        findViewById(R.id.singleStatMaybeNegativeView_positiveValueLabel);
    mZeroLabel = (TextView) findViewById(R.id.singleStatMaybeNegativeView_zeroLabel);
    mPositiveBarContainer = (FrameLayout)
        findViewById(R.id.singleStatMaybeNegativeView_positiveBarContainer);
    mPositiveBarImage = (ImageView) findViewById(R.id.singleStatMaybeNegativeView_positiveBarImage);
    mNegativeValueLabel = (TextView)
        findViewById(R.id.singleStatMaybeNegativeView_negativeValueLabel);

    mPositiveValueLabel.setTypeface(FontLoader.getInstance().getRobotoCondensedNormalTypeface());
    mZeroLabel.setTypeface(FontLoader.getInstance().getRobotoCondensedNormalTypeface());
    mNegativeValueLabel.setTypeface(FontLoader.getInstance().getRobotoCondensedNormalTypeface());

    mNegativeBarContainer.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            UiUtils.removeOnGlobalLayoutListener(mNegativeBarContainer, this);
            mOnGlobalLayoutListenerCalled = true;
            double maxWidthPx = mNegativeBarContainer.getMeasuredWidth();
            double negativeBarValue = mNewValue < 0 ? -mNewValue : 0;
            double positiveBarValue = mNewValue >= 0 ? mNewValue : 0;
            updateBar(mNegativeBarImage, 0, (int) (negativeBarValue / mMaxValue * maxWidthPx));
            updateBar(mPositiveBarImage, 0, (int) (positiveBarValue / mMaxValue * maxWidthPx));
          }
        }
    );

    mNegativeBarImage.setPivotX(0);
    mPositiveBarImage.setPivotX(0);
  }

  @Override
  public void updateValue(double newValue) {
    double originalValue = mNewValue;
    mNewValue = newValue;
    if (mNewValue < 0) {
      showValueLabel(mNegativeValueLabel);
      mNegativeValueLabel.setText(mDecimalFormat.format(mNewValue));
    } else if (newValue > 0) {
      showValueLabel(mPositiveValueLabel);
      mPositiveValueLabel.setText("+" + mDecimalFormat.format(mNewValue));
    } else {
      showValueLabel(mZeroLabel);
    }
    if (mOnGlobalLayoutListenerCalled) {
      double maxNegativeWidthPx = mNegativeBarContainer.getMeasuredWidth();
      double maxPositiveWidthPx = mPositiveBarContainer.getMeasuredWidth();
      FilteredLogger.d(TAG, "maxNegativeWidthPx: " + maxNegativeWidthPx + " maxPositiveWidthPx: " + maxPositiveWidthPx);
      updateBar(mNegativeBarImage,
          (int) (originalValue < 0 ? (-originalValue / mMaxValue * maxNegativeWidthPx) : 0),
          (int) (mNewValue < 0 ? (-mNewValue / mMaxValue * maxPositiveWidthPx) : 0));
      updateBar(mPositiveBarImage,
          (int) (originalValue >= 0 ? (originalValue / mMaxValue * maxNegativeWidthPx) : 0),
          (int) (mNewValue >= 0 ? (mNewValue / mMaxValue * maxPositiveWidthPx) : 0));
    }
  }

  public void showValueLabel(TextView valueLabel) {
    mNegativeValueLabel.setVisibility(mNegativeValueLabel.equals(valueLabel) ? VISIBLE : INVISIBLE);
    mZeroLabel.setVisibility(mZeroLabel.equals(valueLabel) ? VISIBLE : INVISIBLE);
    mPositiveValueLabel.setVisibility(mPositiveValueLabel.equals(valueLabel) ? VISIBLE : INVISIBLE);
  }
}
