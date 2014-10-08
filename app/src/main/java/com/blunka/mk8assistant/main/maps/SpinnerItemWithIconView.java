package com.blunka.mk8assistant.main.maps;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.blunka.mk8assistant.shared.ui.UiUtils;

/**
 * Created by clocksmith on 9/8/14.
 */
public class SpinnerItemWithIconView extends LinearLayout {
  private static final String TAG = SpinnerItemWithIconView.class.getSimpleName();

  private int mIconDimensionPx;
  private ImageView mIconImageView;
  private TextView mLabel;

  public SpinnerItemWithIconView(Context context) {
    this(context, null);
  }

  public SpinnerItemWithIconView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SpinnerItemWithIconView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_spinner_item_with_icon, this, true);

    mIconImageView = (ImageView) findViewById(R.id.spinnerItemWithIcon_iconImageView);
    mLabel = (TextView) findViewById(R.id.spinnerItemWithIcon_label);

    this.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        mIconDimensionPx = mIconImageView.getHeight();
        FilteredLogger.d(TAG, "onGlobalLayout() mIconDimensionPx: " + mIconDimensionPx);
        makeIconSquare();
        UiUtils.removeOnGlobalLayoutListener(SpinnerItemWithIconView.this, this);
      }
    });

    mLabel.setTextColor(getResources().getColor(R.color.dark_gray));
    mLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        context.getResources().getDimension(R.dimen.default_spinner_text_size));
    mLabel.setTypeface(FontLoader.getInstance().getRobotoCondensedLightTypeface());
  }

  public void setIconDrawable(Drawable iconImageDrawable) {
    mIconImageView.setImageDrawable(iconImageDrawable);
  }

  public void setLabelString(String labelString) {
    mLabel.setText(labelString);
  }

  public void setIconPadding(int left, int top, int right, int bottom) {
    mIconImageView.setPadding(left, top, right, bottom);
  }

  public void makeIconSquare() {
    FilteredLogger.d(TAG, "makeIconSquare() mIconDimensionPx: " + mIconDimensionPx);
    mIconImageView.getLayoutParams().width = mIconDimensionPx;
    mIconImageView.getLayoutParams().height = mIconDimensionPx;
//    requestLayout();
  }
}
