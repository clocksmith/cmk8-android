package com.blunka.mk8assistant.main.configure;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.preferences.StarredBuildUtils;
import com.blunka.mk8assistant.shared.ui.SpinnerUtils;

/**
 * Created by clocksmith on 7/17/14.
 */
public class ConfigureSpinnerAdapter extends ArrayAdapter<String> {
  private static final String LOAD_FROM_ADJUST = "New custom configuration...";

  private Context mContext;
  private Spinner mSpinner;
  private String[] mBuildKeys; // the last key should be the key to hide.

  public ConfigureSpinnerAdapter(Context context, Spinner spinner, String[] buildKeys) {
    super(context, 0, buildKeys);
    mContext = context;
    mSpinner = spinner;
    mBuildKeys = buildKeys;
  }

  public int getLastPosition() {
    return getCount() - 1;
  }


  @Override
  public int getCount() {
    // Add 1 for the option to load from the adjust fragment that is at position 0.
    return mBuildKeys.length + 1;
  }

  @Override
  public String getItem(int position) {
    if (position > 0) {
      return mBuildKeys[position - 1];
    } else {
      return LOAD_FROM_ADJUST;
    }
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return createLabel(position);
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    if (position == mBuildKeys.length) {
      TextView emptyTextView = new TextView(mContext);
      emptyTextView.setHeight(0);
      emptyTextView.setVisibility(View.GONE);
      return emptyTextView;
    } else {
      return createDropDownLabel(position);
    }
  }

  private TextView createLabel(int position) {
    boolean isLoadFromAdjust = getItem(position).equals(LOAD_FROM_ADJUST);
    return SpinnerUtils.createLabel(mContext,
        position == 0 ? LOAD_FROM_ADJUST : StarredBuildUtils.getNameFromKey(getItem(position)),
        mContext.getResources().getColor(isLoadFromAdjust ? R.color.mk8_blue : R.color.dark_gray),
        false);
  }

  private TextView createDropDownLabel(int position) {
    boolean isLoadFromAdjust = getItem(position).equals(LOAD_FROM_ADJUST);
    return SpinnerUtils.createDropDownLabel(mContext,
        position == 0 ? LOAD_FROM_ADJUST : StarredBuildUtils.getNameFromKey(getItem(position)),
        mContext.getResources().getColor(isLoadFromAdjust ? R.color.mk8_blue : R.color.dark_gray),
        isLoadFromAdjust || getItem(position).equals(mSpinner.getSelectedItem()));
  }
}
