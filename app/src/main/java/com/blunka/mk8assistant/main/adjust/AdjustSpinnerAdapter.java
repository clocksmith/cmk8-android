package com.blunka.mk8assistant.main.adjust;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.ui.SpinnerUtils;

/**
 * Created by clocksmith on 7/14/14.
 */
public class AdjustSpinnerAdapter extends ArrayAdapter<ConfigurationChoice> {
  private Context mContext;
  private Spinner mSpinner;
  private ConfigurationChoice[] mConfigurationNames;

  public AdjustSpinnerAdapter(Context context,
      Spinner spinner,
      ConfigurationChoice[] configurationChoices) {
    super(context, 0, configurationChoices);
    mContext = context;
    mSpinner = spinner;
    mConfigurationNames = configurationChoices;
  }

  @Override
  public int getCount() {
    return mConfigurationNames.length;
  }

  @Override
  public ConfigurationChoice getItem(int position) {
    return mConfigurationNames[position];
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
  public View getDropDownView(int position, View convertView,
      ViewGroup parent) {
    if (position == ConfigurationChoice.CUSTOM.ordinal()) {
      TextView emptyTextView = new TextView(mContext);
      emptyTextView.setHeight(0);
      emptyTextView.setVisibility(View.GONE);
      return emptyTextView;
    } else {
      return createDropDownLabel(position);
    }
  }

  private TextView createLabel(int position) {
    boolean isCustom = getItem(position) == ConfigurationChoice.CUSTOM;
    return SpinnerUtils.createLabel(mContext,
        getItem(position).getDisplayName(),
        mContext.getResources().getColor(isCustom ? R.color.mk8_blue : R.color.dark_gray),
        isCustom);
  }

  private TextView createDropDownLabel(int position) {
    boolean isCustom = getItem(position) == ConfigurationChoice.CUSTOM;
    return SpinnerUtils.createDropDownLabel(mContext,
        getItem(position).getDisplayName(),
        mContext.getResources().getColor(isCustom ? R.color.mk8_blue : R.color.dark_gray),
        isCustom || getItem(position).equals(mSpinner.getSelectedItem()));
  }
}
