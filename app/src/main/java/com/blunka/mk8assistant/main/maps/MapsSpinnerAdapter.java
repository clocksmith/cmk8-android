package com.blunka.mk8assistant.main.maps;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.shared.FilteredLogger;

import java.util.List;
import java.util.Map;

/**
 * Created by clocksmith on 9/10/14.
 *
 * We cache our own item view since it has non trivial resizing and so we don't need to constantly
 * relayout the view.
 */
public class MapsSpinnerAdapter<T extends HasDisplayNameAndIcon> extends ArrayAdapter<T> {
  private static final String TAG = MapsSpinnerAdapter.class.getSimpleName();

  private Context mContext;
  private List<T> mObjects;
  private int mDropDownViewHeight;
  private int mDropDownViewPaddingTopBottom;
  private SpinnerItemWithIconView mItemView;
  private SpinnerItemWithIconView mItemDropDownView;

  public MapsSpinnerAdapter(Context context,
      List<T> objects,
      int dropDownViewHeight,
      int dropDownViewIconPaddingTopBottom) {
    super(context, 0, objects);
    mContext = context;
    mObjects = objects;
    mDropDownViewHeight = dropDownViewHeight;
    mDropDownViewPaddingTopBottom = dropDownViewIconPaddingTopBottom;
  }

  public void updateObjects(List<T> objects) {
    mObjects = objects;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return mObjects.size();
  }

  @Override
  public T getItem(int position) {
    return mObjects.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    FilteredLogger.d(TAG, "getView() position: " + position);
    if (mItemView == null) {
      mItemView = internalGetView(position, convertView);
    } else {
      mItemView.setIconDrawable(
          mContext.getResources().getDrawable(getItem(position).getIconResourceId()));
      mItemView.setLabelString(getItem(position).getDisplayName(mContext));
    }
    return mItemView;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    FilteredLogger.d(TAG, "getDropDownView() position: " + position);
    SpinnerItemWithIconView view = internalGetView(position, convertView);
    view.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,
        mDropDownViewHeight));
    view.setIconPadding(0, mDropDownViewPaddingTopBottom, 0, mDropDownViewPaddingTopBottom);
    return view;

  }

  private SpinnerItemWithIconView internalGetView(int position, View convertView) {
    SpinnerItemWithIconView view = new SpinnerItemWithIconView(mContext);
    view.setIconDrawable(
        mContext.getResources().getDrawable(getItem(position).getIconResourceId()));
    view.setLabelString(getItem(position).getDisplayName(mContext));
    return view;
  }
}
