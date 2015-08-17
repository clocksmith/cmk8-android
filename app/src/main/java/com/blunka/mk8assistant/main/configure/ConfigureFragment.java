package com.blunka.mk8assistant.main.configure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsFragment;
import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.data.KartConfiguration;
import com.blunka.mk8assistant.data.parts.Part;
import com.blunka.mk8assistant.data.parts.PartData;
import com.blunka.mk8assistant.data.parts.PartGroup;
import com.blunka.mk8assistant.main.adjust.AdjustActivity;
import com.blunka.mk8assistant.shared.ArgKeys;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.preferences.StarredBuildUtils;
import com.blunka.mk8assistant.shared.stats.StatsDragView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

/**
 * Created by clocksmith on 7/4/14.
 */
public class ConfigureFragment extends AnalyticsFragment implements PartGroupChooserView.Listener {
  private static final String TAG = ConfigureFragment.class.getSimpleName();

  public interface Listener {
    /**
     * Called when the build model has been updated.
     */
    public void onBuildModelUpdated(ConfigureModel configureModel);

    /**
     * Called then the starred build library has been updated.
     */
    public void onStarredBuildLibraryUpdated(String buildKeyAdded);
  }

  // Model.
  private ConfigureModel mConfigureModel;

  // Listeners.
  private Listener mListener;
  private BuildSpinnerOnItemSelectedListener mBuildSpinnerListener;
  private StarViewOnClickListener mStarListener;
  private BuildPanelSlideListener mBuildPanelSlideListener;

  // Adapter.
  private ConfigureSpinnerAdapter mConfigureSpinnerAdapter;

  // View.
  private SlidingUpPanelLayout mSlidingUpPanel;
  private Spinner mSpinner;
  private StarView mStarView;
  private PartGroupChooserView mCharacterChooser;
  private PartGroupChooserView mVehicleChooser;
  private PartGroupChooserView mTireChooser;
  private PartGroupChooserView mGliderChooser;
  private LinearLayout mBottomPanel;
  private StatsDragView mDragView;
  private MultiStatsPositiveView mMultiStatsPositiveView;

  public static ConfigureFragment newInstance(ConfigureModel configureModel) {
    ConfigureFragment configureFragment = new ConfigureFragment();

    Bundle args = new Bundle();
    args.putParcelable(ArgKeys.BUILD_MODEL, configureModel);
    configureFragment.setArguments(args);

    return configureFragment;
  }

  @Override
  public String getScreenName() {
    return ConfigureFragment.class.getName();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == Constants.ADJUST_BUILD_REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        ConfigureModel configureModel = data.getParcelableExtra(ArgKeys.BUILD_MODEL);
        if (configureModel != null) {
          updateModel(configureModel);

          updateSpinnerSilentlyAndRestoreListeners();
          updateStar();
          updateChoosersSilently();
          updateStatsView();
        }
      }
    }
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

    Bundle bundle;
    if (savedInstanceState != null) {
      bundle = savedInstanceState;
    } else {
      bundle = getArguments();
    }
    mConfigureModel = bundle.getParcelable(ArgKeys.BUILD_MODEL);

    mBuildSpinnerListener = new BuildSpinnerOnItemSelectedListener();
    mStarListener = new StarViewOnClickListener();
    mBuildPanelSlideListener = new BuildPanelSlideListener();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    FilteredLogger.d(TAG, "onCreateView()");
     View view = inflater.inflate(R.layout.fragment_configure, container, false);

    mSlidingUpPanel = (SlidingUpPanelLayout) view.findViewById(R.id.buildFragment_slidingUpPanel);
    mSpinner = (Spinner) view.findViewById(R.id.configureFragment_spinner);
    mStarView = (StarView) view.findViewById(R.id.configureFragment_starView);
    mCharacterChooser = (PartGroupChooserView)
        view.findViewById(R.id.configureFragment_characterChooser);
    mVehicleChooser = (PartGroupChooserView)
        view.findViewById(R.id.configureFragment_vehicleChooser);
    mTireChooser = (PartGroupChooserView)
        view.findViewById(R.id.configureFragment_tireChooser);
    mGliderChooser = (PartGroupChooserView)
        view.findViewById(R.id.configureFragment_gliderChooser);
    mBottomPanel = (LinearLayout) view.findViewById(R.id.configureFragment_bottomPanel);
    mDragView = (StatsDragView) view.findViewById(R.id.configureFragment_dragView);
    mMultiStatsPositiveView = (MultiStatsPositiveView)
        view.findViewById(R.id.configureFragment_totalStatsView);

    mStarView.setOnClickListener(mStarListener);
    mSlidingUpPanel.setDragView(mDragView);
    mSlidingUpPanel.setPanelSlideListener(mBuildPanelSlideListener);
    mSlidingUpPanel.setPanelHeight(0);
    mSlidingUpPanel.setShadowHeight(0);
    mSlidingUpPanel.setCoveredFadeColor(android.R.color.transparent);

    mCharacterChooser.setListener(this);
    mVehicleChooser.setListener(this);
    mTireChooser.setListener(this);
    mGliderChooser.setListener(this);

    mCharacterChooser.getViewTreeObserver().addOnPreDrawListener(
        new ViewTreeObserver.OnPreDrawListener() {
          @Override
          public boolean onPreDraw() {
            mCharacterChooser.getViewTreeObserver().removeOnPreDrawListener(this);
            mSlidingUpPanel.setPanelHeight(mCharacterChooser.getMeasuredHeight());
            mBottomPanel.getLayoutParams().height = 2 * mCharacterChooser.getMeasuredHeight();
            mBottomPanel.requestLayout();
            mBuildPanelSlideListener.onPanelCollapsed(mSlidingUpPanel);
            return true;
          }
        }
    );

    initChoosers();

    return view;
  }

  @Override
  public void onResume() {
    FilteredLogger.d(TAG, "onResume()");

    updateSpinnerSilentlyAndRestoreListeners();
    updateStar();
    updateChoosersSilently();
    updateStatsView();

    super.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putParcelable(ArgKeys.BUILD_MODEL, mConfigureModel);
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onGroupSwitched(PartGroup partGroup) {
    FilteredLogger.d(TAG, "onGroupSwitched(partGroup: " + partGroup.getDisplayName() + ")");
    switch(partGroup.getPartType()) {
      case CHARACTER:
        mConfigureModel.getKartConfiguration().setCharacterGroup(partGroup);
      case VEHICLE:
        mConfigureModel.getKartConfiguration().setVehicleGroup(partGroup);
      case TIRE:
        mConfigureModel.getKartConfiguration().setTireGroup(partGroup);
      case GLIDER:
        mConfigureModel.getKartConfiguration().setGliderGroup(partGroup);
    }

    updateModel(mConfigureModel);

    updateSpinnerSilentlyAndRestoreListeners();
    updateStar();
    updateStatsView();
  }

  private void initChoosers() {
    mCharacterChooser.init(PartData.CHARACTER_GROUPS);
    mVehicleChooser.init(PartData.VEHICLE_GROUPS);
    mTireChooser.init(PartData.TIRE_GROUPS);
    mGliderChooser.init(PartData.GLIDER_GROUPS);
  }

  private void updateModel(ConfigureModel configureModel) {
    mConfigureModel = configureModel;
    if (mListener != null) {
      mListener.onBuildModelUpdated(mConfigureModel);
    }
  }

  private void updateSpinner() {
    FilteredLogger.d(TAG, "updateSpinner()");
    List<String> buildKeys = StarredBuildUtils.getAllStarredKeysSortedByEnum(getActivity());
    String currentKey = StarredBuildUtils.getKeyFromBuild(mConfigureModel);
    buildKeys.add(currentKey);
    mConfigureSpinnerAdapter = new ConfigureSpinnerAdapter(getActivity(),
        mSpinner,
        buildKeys.toArray(new String[buildKeys.size()]));
    mSpinner.setAdapter(mConfigureSpinnerAdapter);
    mSpinner.setSelection(mConfigureSpinnerAdapter.getLastPosition());
  }

  private void updateStar() {
    FilteredLogger.d(TAG, "updateStar()");
    mStarView.setFilled(StarredBuildUtils.isBuildStarred(ConfigureFragment.this.getActivity(),
        mConfigureModel));
  }

  private void updateChoosersSilently() {
    FilteredLogger.d(TAG, "updateChoosersSilently()");
    KartConfiguration kartConfiguration = mConfigureModel.getKartConfiguration();
    mCharacterChooser.setCurrentItemSilently(kartConfiguration.getCharacterGroup());
    mVehicleChooser.setCurrentItemSilently(kartConfiguration.getVehicleGroup());
    mTireChooser.setCurrentItemSilently(kartConfiguration.getTireGroup());
    mGliderChooser.setCurrentItemSilently(kartConfiguration.getGliderGroup());
  }

  private void updateStatsView() {
    FilteredLogger.d(TAG, "updateStatsView()");
    mMultiStatsPositiveView.updateStatViews(mConfigureModel.getKartConfiguration().getKartStats());
  }

  private synchronized void updateSpinnerSilentlyAndRestoreListeners() {
    mSpinner.setOnItemSelectedListener(null);
    updateSpinner();
    mSpinner.setOnItemSelectedListener(mBuildSpinnerListener);
  }

  private void launchAdjustActivity() {
    Intent intent = new Intent(getActivity(), AdjustActivity.class);
    startActivityForResult(intent, Constants.ADJUST_BUILD_REQUEST_CODE);
  }

  private class BuildSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      if (position == 0) {
        mSpinner.setSelection(mConfigureSpinnerAdapter.getLastPosition());
        launchAdjustActivity();
      } else {
        mConfigureModel.setPlayerConfiguration(StarredBuildUtils.getKartConfigurationFromKey(
            (String) parent.getItemAtPosition(position)));
        if (position != mConfigureSpinnerAdapter.getLastPosition()) {
          updateStar();
          updateChoosersSilently();
          updateStatsView();
        }
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
      // Do nothing.
    }
  }

  private class StarViewOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      if (mStarView.isFilled()) {
        StarredBuildUtils.unstarBuild(ConfigureFragment.this.getActivity(), mConfigureModel);
        if (mListener != null) {
          mListener.onStarredBuildLibraryUpdated(null);
        }
        AnalyticsUtils.sendStarViewStarredEvent(mTracker,
            getActivity(),
            mConfigureModel.getKartConfiguration(),
            false);
        updateSpinner();
        updateStar();
      } else {
        mStarView.setFilled(true);
        StarredBuildUtils.starBuild(ConfigureFragment.this.getActivity(), mConfigureModel);
        if (mListener != null) {
          mListener.onStarredBuildLibraryUpdated(StarredBuildUtils.getKeyFromBuild(mConfigureModel));
        }
        AnalyticsUtils.sendStarViewStarredEvent(mTracker,
            getActivity(),
            mConfigureModel.getKartConfiguration(),
            true);
        updateSpinner();
        updateStar();
      }
    }
  }

  private class BuildPanelSlideListener implements SlidingUpPanelLayout.PanelSlideListener {
    @Override
    public void onPanelSlide(View panel, float slideOffset) {
      resizeVisiblePanelArea(slideOffset);
    }

    @Override
    public void onPanelExpanded(View panel) {
      resizeVisiblePanelArea(1f);
    }

    @Override
    public void onPanelCollapsed(View panel) {
      resizeVisiblePanelArea(0f);
    }

    @Override
    public void onPanelAnchored(View panel) {
    }

    @Override
    public void onPanelHidden(View view) {
    }

    private void resizeVisiblePanelArea(float slideOffset) {
      mMultiStatsPositiveView.getScrollView().getLayoutParams().height = mSlidingUpPanel.getPanelHeight() -
          mDragView.getHeight() +
          (int) (slideOffset * (mSlidingUpPanel.getHeight() - mSlidingUpPanel.getPanelHeight()));
      mMultiStatsPositiveView.getScrollView().requestLayout();
    }
  }
}