package com.blunka.mk8assistant.main.adjust;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsFragment;
import com.blunka.mk8assistant.data.Attribute;
import com.blunka.mk8assistant.shared.ArgKeys;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by clocksmith on 7/4/14.
 */
public class AdjustFragment extends AnalyticsFragment implements AdjustAttributeView.Listener {
  private static final String TAG = AdjustFragment.class.getSimpleName();

  public interface Listener {
    public void onAdjustModelUpdated(AdjustModel adjustModel);
  }

  // Model.
  private Map<Attribute, AdjustAttributeView> mAttributeViews;
  private AdjustModel mAdjustModel;

  // Listeners.
  private Listener mListener;
  private AdjustSpinnerOnItemSelectedListener mAdjustSpinnerListener;

  // Adapter.
  private AdjustSpinnerAdapter mAdjustSpinnerAdapter;

  private Spinner mSpinner;
  private LinearLayout mAttributesContainer;

  public static AdjustFragment newInstance(AdjustModel adjustModel) {
    AdjustFragment adjustFragment = new AdjustFragment();
    Bundle args = new Bundle();
    args.putParcelable(ArgKeys.ADJUST_MODEL, adjustModel);
    adjustFragment.setArguments(args);
    return adjustFragment;
  }

  @Override
  public String getScreenName() {
    return AdjustFragment.class.getName();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (activity instanceof Listener) {
      mListener = (Listener) activity;
    } else {
      Log.e(TAG, "Parent activity does not implement Listener");
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    sendScreenViewToAnalytics();

    Bundle bundle;
    if (savedInstanceState != null) {
      bundle = savedInstanceState;
    } else {
      bundle = getArguments();
    }
    mAdjustModel = bundle.getParcelable(ArgKeys.ADJUST_MODEL);

    mAdjustSpinnerListener = new AdjustSpinnerOnItemSelectedListener();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_adjust, container, false);

    mSpinner = (Spinner) view.findViewById(R.id.adjustFragment_spinner);
    mAttributesContainer = (LinearLayout) view.findViewById(
        R.id.adjustFragment_attributesContainer);

    mAdjustSpinnerAdapter = new AdjustSpinnerAdapter(getActivity(),
        mSpinner,
        ConfigurationChoice.values());
    mSpinner.setAdapter(mAdjustSpinnerAdapter);

    mAttributesContainer.setWeightSum(Attribute.getSimpleValues().size());
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        0,
        1);
    mAttributeViews = Maps.newHashMap();
    for (Attribute attribute : Attribute.getSimpleValues()) {
      AdjustAttributeView adjustAttributeView = new AdjustAttributeView(getActivity());
      adjustAttributeView.setListener(this);
      adjustAttributeView.setAttribute(attribute);
      // We need to do this because the animation is getting skipped on loading this fragment
      // and it looks bad because they progress buttons jump.
      adjustAttributeView.setProgress(
          mAdjustModel.getSelectedConfiguration().getAttributeValue(attribute));
      mAttributeViews.put(attribute, adjustAttributeView);
      mAttributesContainer.addView(adjustAttributeView, layoutParams);
    }

    updateView();
    return view;
  }

  @Override
  public void onResume() {
    FilteredLogger.d(TAG, "onResume()");
    mSpinner.setOnItemSelectedListener(mAdjustSpinnerListener);
    super.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putParcelable(ArgKeys.ADJUST_MODEL, mAdjustModel);
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public void onPause() {
    mSpinner.setOnItemSelectedListener(null);
    super.onPause();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onAttributeValueChanged(Attribute attribute, int value) {
    if (mAdjustModel.getSelectedConfigurationChoice() != ConfigurationChoice.CUSTOM) {
      mAdjustModel.copySelectedConfigurationToCustomConfiguration();
      setSpinnerSelectionSilently(ConfigurationChoice.CUSTOM);
    }
    mAdjustModel.getCustomConfiguration().setAttributeValue(attribute, value);
    if (mListener != null) {
      mListener.onAdjustModelUpdated(mAdjustModel);
    }
  }

  private synchronized void setSpinnerSelectionSilently(ConfigurationChoice configurationChoice) {
    mSpinner.setOnItemSelectedListener(null);
    mSpinner.setSelection(configurationChoice.ordinal());
    mSpinner.setOnItemSelectedListener(mAdjustSpinnerListener);
  }

  private void updateView() {
    FilteredLogger.d(TAG, "updateView()");
    mSpinner.setSelection(mAdjustModel.getSelectedConfigurationChoice().ordinal());
  }

  private void updateAdjusterViews() {
    FilteredLogger.d(TAG, "updateAdjusterViews() choice: " +
        mAdjustModel.getSelectedConfigurationChoice().name());
    for (Map.Entry<Attribute, AdjustAttributeView> entry : mAttributeViews.entrySet()) {
      entry.getValue().animateProgress(
          mAdjustModel.getSelectedConfiguration().getAttributeValue(entry.getKey()));
    }
  }

  private class AdjustSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      ConfigurationChoice configurationChoice = (ConfigurationChoice)
          parent.getItemAtPosition(position);
      mAdjustModel.setSelectedConfigurationChoice(configurationChoice);
      updateAdjusterViews();

      if (mListener != null) {
        mListener.onAdjustModelUpdated(mAdjustModel);
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
      // Do nothing.
    }
  }
}
