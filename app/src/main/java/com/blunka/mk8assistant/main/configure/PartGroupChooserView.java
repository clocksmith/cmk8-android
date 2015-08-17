package com.blunka.mk8assistant.main.configure;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.parts.Part;
import com.blunka.mk8assistant.data.parts.PartGroup;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.UiUtils;

import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by clocksmith on 7/12/14.
 */
public class PartGroupChooserView extends LinearLayout {
  private static final String TAG = PartGroupChooserView.class.getSimpleName();

  public interface Listener {
    void onGroupSwitched(PartGroup partGroup);
  }

  private Listener mListener;
  private ChooserOnPageChangeListener mChooserOnPageChangeListener;

  private ImageView mUpwardTriangle;
  private VerticalViewPager mViewPager;
  private ImageView mDownwardTriangle;
  private PartGroupViewPagerAdapter mPartGroupViewPagerAdapter;

  public PartGroupChooserView(Context context) {
    this(context, null);
  }

  public PartGroupChooserView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PartGroupChooserView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_part_group_chooser, this, true);

    mUpwardTriangle = (ImageView) findViewById(R.id.partGroupChooserView_upwardTriangle);
    mViewPager = (VerticalViewPager) findViewById(R.id.partGroupChooserView_viewPager);
    mDownwardTriangle = (ImageView) findViewById(R.id.partGroupChooserView_downwardTriangle);

    UiUtils.setCustomVerticalViewPagerScroller(mViewPager,
        Constants.VERTICAL_VIEW_PAGER_SCROLL_DURATION_MS);
    mChooserOnPageChangeListener = new ChooserOnPageChangeListener();
    mViewPager.setOnPageChangeListener(mChooserOnPageChangeListener);
  }

  public void setListener(Listener listener) {
    mListener = listener;
  }

  public void init(List<PartGroup> partGroups) {
    mPartGroupViewPagerAdapter = new PartGroupViewPagerAdapter(this.getContext(), partGroups);
    mViewPager.setAdapter(mPartGroupViewPagerAdapter);

    mUpwardTriangle.setVisibility(mViewPager.getCurrentItem() == 0 ? INVISIBLE : VISIBLE);
    mDownwardTriangle.setVisibility(
        mViewPager.getCurrentItem()  == mPartGroupViewPagerAdapter.getCount() - 1 ?
            INVISIBLE :
            VISIBLE);
  }

  public synchronized void setCurrentItemSilently(PartGroup partGroup) {
    FilteredLogger.d(TAG, "setCurrentItemSilently: " + partGroup.getIndex());
    if (mViewPager.getCurrentItem() != partGroup.getIndex()) {
      mChooserOnPageChangeListener.setNextChangeSilent();
      mViewPager.setCurrentItem(partGroup.getIndex(), true);
    }
  }

  private class ChooserOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private boolean mNextChangeSilent = false;

    public void setNextChangeSilent() {
      mNextChangeSilent = true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      // Do nothing.
    }

    @Override
    public void onPageSelected(int position) {
      FilteredLogger.d(TAG, "onPageSelected(position: " + position + ") mNextChangeSilent: " + mNextChangeSilent );
      if (mListener != null && !mNextChangeSilent) {
        mListener.onGroupSwitched(mPartGroupViewPagerAdapter.getPartGroup(position));
      }

      mNextChangeSilent = false;

      mUpwardTriangle.setVisibility(position == 0 ? INVISIBLE : VISIBLE);
      mDownwardTriangle.setVisibility(position == mPartGroupViewPagerAdapter.getCount() - 1 ?
          INVISIBLE :
          VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
      // Do nothing.
    }
  }
}