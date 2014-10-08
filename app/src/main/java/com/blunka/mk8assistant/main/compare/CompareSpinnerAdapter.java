package com.blunka.mk8assistant.main.compare;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.preferences.StarredBuildUtils;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.blunka.mk8assistant.shared.ui.SpinnerUtils;

/**
 * Created by clocksmith on 7/18/14.
 */
public class CompareSpinnerAdapter extends ArrayAdapter<String> {
  private Context mContext;
  private Spinner mSpinner;
  private String[] mBuildKeys;

  public CompareSpinnerAdapter(Context context, Spinner spinner, String[] buildKeys) {
    super(context, 0, buildKeys);
    mContext = context;
    mSpinner = spinner;
    mBuildKeys = buildKeys;
  }

  @Override
  public int getCount() {
    return mBuildKeys.length;
  }

  @Override
  public String getItem(int position) {
    return mBuildKeys[position];
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
      return createDropDownLabel(position);
  }

  private TextView createLabel(int position) {
    TextView label = SpinnerUtils.createLabel(mContext,
        StarredBuildUtils.getNameFromKey(mContext, getItem(position)),
        mContext.getResources().getColor(R.color.dark_gray),
        false);
    label.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        mContext.getResources().getDimension(R.dimen.compare_spinner_text_size));
    label.setTypeface(FontLoader.getInstance().getRobotoCondensedLightTypeface());
    return label;
  }

  private TextView createDropDownLabel(int position) {
    return SpinnerUtils.createDropDownLabel(mContext,
        StarredBuildUtils.getNameFromKey(mContext, getItem(position)),
        mContext.getResources().getColor(R.color.dark_gray),
        getItem(position).equals(mSpinner.getSelectedItem()));
  }
}
