package com.blunka.mk8assistant.main;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.blunka.mk8assistant.R;

/**
 * Created by clocksmith on 7/11/14.
 */
public enum MainTab {
  CONFIGURE(R.string.configure, R.drawable.configure_icon_128_black),
  COMPARE(R.string.compare, R.drawable.compare_icon_128_black),
  MAPS(R.string.maps, R.drawable.maps_icon_128_black),
  FAQ(R.string.faq, R.drawable.faq_icon_128_black);

  private int mTitleResourceId;
  private int mIconResourceId;

  private MainTab(int titleResourceId, int iconResourceId) {
    mTitleResourceId = titleResourceId;
    mIconResourceId = iconResourceId;
  }

  public String getTitle(Context context) {
    return context.getResources().getString(mTitleResourceId);
  }

  public Drawable getIcon(Context context) {
    return context.getResources().getDrawable(mIconResourceId);
  }
}
