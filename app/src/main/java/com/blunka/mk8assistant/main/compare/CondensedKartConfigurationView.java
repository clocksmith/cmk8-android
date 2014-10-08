package com.blunka.mk8assistant.main.compare;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.KartConfiguration;
import com.blunka.mk8assistant.data.parts.PartGroup;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.preferences.StarredBuildUtils;
import com.blunka.mk8assistant.shared.ui.UiUtils;

/**
 * Created by clocksmith on 7/21/14.
 */
public class CondensedKartConfigurationView extends LinearLayout {
  private static final String TAG = CondensedKartConfigurationView.class.getSimpleName();

  private LinearLayout mContainer;

  public CondensedKartConfigurationView(Context context) {
    this(context, null);
  }

  public CondensedKartConfigurationView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CondensedKartConfigurationView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_condensed_kart_configuration, this, true);

    mContainer = (LinearLayout)
        findViewById(R.id.condensedKartConfigurationView_container);
  }

  public void updateKartConfiguration(final KartConfiguration kartConfiguration) {
    FilteredLogger.d(TAG, "updateKartConfiguration(kartConfiguration: " +
        StarredBuildUtils.getKeyFromConfiguration(kartConfiguration) + ")");
    ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(mContainer, "alpha", 1f, 0f);
    fadeOutAnimator.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            0,
            LayoutParams.MATCH_PARENT,
            1);
        UiUtils.unbindDrawables(mContainer);
        mContainer.removeAllViews();
        for (PartGroup partGroup : kartConfiguration.getParts()) {
          CondensedPartGroupView condensedPartGroupView =
              new CondensedPartGroupView(getContext(), partGroup);
          mContainer.addView(condensedPartGroupView, layoutParams);
        }
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(mContainer, "alpha", 0f, 1f);
        fadeInAnimator.setDuration(Constants.DEFAULT_CHANGE_DURATION_MS);
        fadeInAnimator.start();
      }

      @Override
      public void onAnimationCancel(Animator animation) {
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }
    });
    fadeOutAnimator.setDuration(Constants.DEFAULT_CHANGE_DURATION_MS);
    fadeOutAnimator.start();
  }
}
