package com.blunka.mk8assistant.main.maps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by clocksmith on 7/12/14.
 */
public class MapViewPager extends VerticalViewPager {
  private static final String TAG = MapViewPager.class.getSimpleName();

  public MapViewPager(Context context) {
    super(context);
  }

  public MapViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    // Ignore multi figure swipes to allow uninterrupted pinch zooming for subviews.
    if (event.getPointerCount() == 1) {
      try {
        MapViewPagerAdapter adapter = (MapViewPagerAdapter) getAdapter();
        if (adapter.getRegisteredView(getCurrentItem()) == null ||
            adapter.getRegisteredView(getCurrentItem()).getCurrentZoom() == 1) {
          return super.onInterceptTouchEvent(event);
        }
      } catch (IllegalArgumentException e) {
        return false;
      }
    }
    return false;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getPointerCount() == 1) {
      try {
        MapViewPagerAdapter adapter = (MapViewPagerAdapter) getAdapter();
        if (adapter.getRegisteredView(getCurrentItem()).getCurrentZoom() == 1) {
          return super.onTouchEvent(event);
        }
      } catch (IllegalArgumentException e) {
        return false;
      }
    }
    return false;
  }
}