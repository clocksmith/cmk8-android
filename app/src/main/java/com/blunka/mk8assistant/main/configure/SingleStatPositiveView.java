package com.blunka.mk8assistant.main.configure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.stats.SingleStatSingleValueView;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.blunka.mk8assistant.shared.ui.UiUtils;

/**
 * Created by clocksmith on 7/15/14.
 */
public class SingleStatPositiveView extends SingleStatSingleValueView {
  private static final String TAG = SingleStatPositiveView.class.getSimpleName();

  private FrameLayout mBarContainer;
  private ImageView mBarImage;
  private TextView mValueLabel;

  public SingleStatPositiveView(Context context) {
    super(context);

    mMaxValue = Constants.MAX_TOTAL_STATS_VALUE;

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_single_stat_positive, this, true);

    mBarContainer = (FrameLayout) findViewById(R.id.singleStatPositiveView_barContainer);
    mBarImage = (ImageView) findViewById(R.id.singleStatPositiveView_barImage);
    mValueLabel = (TextView) findViewById(R.id.singleStatPositiveView_valueLabel);

    mBarContainer.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            UiUtils.removeOnGlobalLayoutListener(mBarContainer, this);
            mOnGlobalLayoutListenerCalled = true;
            float maxWidthPx = mBarContainer.getMeasuredWidth();
            updateBar(mBarImage, 0, (int) (mNewValue / mMaxValue * maxWidthPx));
          }
        });

    mBarImage.setPivotX(0);

    mValueLabel.setTypeface(FontLoader.getInstance().getRobotoCondensedNormalTypeface());
  }

  @Override
  public void updateValue(float newValue) {
    float originalValue = mNewValue;
    mNewValue = newValue;
    mValueLabel.setText(mDecimalFormat.format(mNewValue));
    if (mOnGlobalLayoutListenerCalled) {
      float maxWidthPx = mBarContainer.getMeasuredWidth();
      updateBar(mBarImage,
          (int) (originalValue / mMaxValue * maxWidthPx),
          (int) (mNewValue / mMaxValue * maxWidthPx));
    }
  }
}
