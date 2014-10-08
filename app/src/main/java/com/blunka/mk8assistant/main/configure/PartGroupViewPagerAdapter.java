package com.blunka.mk8assistant.main.configure;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.blunka.mk8assistant.data.parts.PartGroup;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.UiUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 7/13/14.
 */
public class PartGroupViewPagerAdapter extends PagerAdapter {
  private static final String TAG = PartGroupViewPagerAdapter.class.getSimpleName();

  private Context mContext;
  private List<PartGroup> mPartGroupValues;

  public PartGroupViewPagerAdapter(Context context,
      Class<? extends PartGroup> partGroupClass) {
    mContext = context;
    mPartGroupValues = Lists.newArrayList((PartGroup[]) partGroupClass.getEnumConstants());
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    FilteredLogger.d(TAG, "instantiateItem(position: " + position + ")");
    PartGroupView partGroupView = new PartGroupView(mContext,
        mPartGroupValues.get(position),
        false);
    container.addView(partGroupView);
    return partGroupView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    FilteredLogger.d(TAG, "destroyItem(position: " + position + ")");
    UiUtils.unbindDrawables((PartGroupView) object);
    container.removeView((PartGroupView) object);
  }

  @Override
  public int getCount() {
    return mPartGroupValues.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  public PartGroup getPartGroup(int position) {
    return mPartGroupValues.get(position);
  }
}
