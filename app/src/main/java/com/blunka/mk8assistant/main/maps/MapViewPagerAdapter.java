package com.blunka.mk8assistant.main.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.blunka.mk8assistant.data.courses.Course;
import com.blunka.mk8assistant.data.courses.CourseData;
import com.blunka.mk8assistant.data.courses.CourseUtils;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.ImageUtils;
import com.blunka.mk8assistant.shared.ui.TouchImageView;

/**
 * Created by clocksmith on 9/7/14.
 */
public class MapViewPagerAdapter extends PagerAdapter {
  private static final String TAG = MapViewPagerAdapter.class.getSimpleName();

  private static final int ACTUAL_DIMENSION_PX = 1024;

  SparseArray<TouchImageView> mRegisteredViews;

  private Context mContext;

  public MapViewPagerAdapter(Context context) {
    mContext = context;
    mRegisteredViews = new SparseArray<TouchImageView>();
  }

  public TouchImageView getRegisteredView(int position) {
    return mRegisteredViews.get(position);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    FilteredLogger.d(TAG, "instantiateItem(position: " + position + ")");
    TouchImageView mapView = new TouchImageView(mContext);
    Bitmap mapBitmap = ImageUtils.decodeSampledBitmapFromResource(mContext,
        CourseUtils.getCourse(position).getMapResId(),
        Bitmap.Config.RGB_565,
        ACTUAL_DIMENSION_PX,
        ACTUAL_DIMENSION_PX);
    mapView.setImageBitmap(mapBitmap);
    mRegisteredViews.put(position, mapView);
    container.addView(mapView, 0);
    return mapView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    FilteredLogger.d(TAG, "destroyItem(position: " + position + ")");
    TouchImageView mapView = (TouchImageView) object;
    mapView.setImageBitmap(null);
    mRegisteredViews.remove(position);
    container.removeView(mapView);
  }

  @Override
  public int getCount() {
    return CourseData.CUPS.size() * Constants.NUM_COURSES_IN_CUP;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }
}
