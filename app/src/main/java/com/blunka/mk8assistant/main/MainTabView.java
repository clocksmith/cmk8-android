package com.blunka.mk8assistant.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.ui.FontLoader;

/**
 * Created by clocksmith on 7/11/14.
 */
public class MainTabView extends LinearLayout {
  private Context mContext;

  private ImageView mIcon;
  private TextView mTitle;

  public MainTabView(Context context) {
    this(context, null);
  }

  public MainTabView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MainTabView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;

    LayoutInflater layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_main_tab, this);

    mIcon = (ImageView) findViewById(R.id.mainTab_icon);
    mTitle = (TextView) findViewById(R.id.mainTab_title);

    setSelected(false);
  }

  public void setIcon(Drawable iconDrawable) {
    mIcon.setImageDrawable(iconDrawable);
  }
  public void setTitle(String title) {
    mTitle.setText(title);
  }

  @Override
  public void setSelected(boolean selected) {
    super.setSelected(selected);
    int color = selected ? mContext.getResources().getColor(R.color.mk8_blue)
        : mContext.getResources().getColor(R.color.dark_gray);
    mIcon.setColorFilter(color);
    mTitle.setTextColor(color);
    mTitle.setTypeface(selected ? FontLoader.getInstance().getRobotoCondensedNormalTypeface()
        : FontLoader.getInstance().getRobotoCondensedLightTypeface());
  }
}