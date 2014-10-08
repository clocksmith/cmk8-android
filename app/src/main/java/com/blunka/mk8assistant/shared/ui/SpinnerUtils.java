package com.blunka.mk8assistant.shared.ui;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blunka.mk8assistant.R;

/**
 * Created by clocksmith on 7/17/14.
 */
public class SpinnerUtils {

  public static TextView createLabel(Context context,
      String labelString,
      int color,
      boolean isBold) {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT);
    layoutParams.gravity = Gravity.CENTER;
    TextView label = new TextView(context);
    label.setTextColor(color);
    label.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        context.getResources().getDimension(R.dimen.default_spinner_text_size));
    label.setTypeface(isBold ? FontLoader.getInstance().getRobotoCondensedNormalTypeface() :
        FontLoader.getInstance().getRobotoCondensedLightTypeface());
    label.setText(labelString);
    return label;
  }

  public static TextView createDropDownLabel(Context context,
      String labelString,
      int color,
      boolean isBold) {
    TextView label = createLabel(context, labelString, color, isBold);
    int padding = context.getResources().getDimensionPixelOffset(R.dimen.spinner_padding);
    label.setBackgroundColor(context.getResources().getColor(R.color.spinner_bg));
    label.setPadding(padding, padding, padding, padding);
    return label;
  }
}
