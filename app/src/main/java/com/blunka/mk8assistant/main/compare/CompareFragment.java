package com.blunka.mk8assistant.main.compare;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsFragment;
import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.data.KartConfiguration;
import com.blunka.mk8assistant.main.configure.StarView;
import com.blunka.mk8assistant.shared.AnalyticsLogger;
import com.blunka.mk8assistant.shared.ArgKeys;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.preferences.StarredBuildUtils;
import com.blunka.mk8assistant.shared.stats.StatsDragView;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.blunka.mk8assistant.shared.ui.UiUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by clocksmith on 7/18/14.
 */
public class CompareFragment extends AnalyticsFragment {
  private static final String TAG = CompareFragment.class.getSimpleName();

  private static final int MINIMUM_NUMBER_OF_BUILDS = 2;

  public interface Listener {
    void onCompareModelUpdated(CompareModel compareModel);
  }

  // Model
  private CompareModel mCompareModel;

  // View
  private FrameLayout mRootView;
  private SlidingUpPanelLayout mSlidingUpPanel;
  private LinearLayout mLessThanTwoMessage;
  private TextView mHelpMessage;
  private StarView mStarView;
  private Spinner mLeftSpinner;
  private Spinner mRightSpinner;
  private LinearLayout mKartConfigurationContainer;
  private CondensedKartConfigurationView mLeftKartConfigurationView;
  private CondensedKartConfigurationView mRightKartConfigurationView;
  private LinearLayout mBottomPanel;
  private StatsDragView mDragView;
  private MultiStatsCompareView mMultiStatsCompareView;

  // Listeners and Adapters.
  private Listener mListener;
  private CompareSpinnerAdapter mLeftSpinnerAdapter;
  private CompareSpinnerAdapter mRightSpinnerAdapter;
  private SpinnerOnItemSelectedListener mSpinnerListener;
  private ComparePanelSlideListener mComparePanelSlideListener;

  private CountDownLatch mSpinnerCountDownLatch;

  public static CompareFragment newInstance(CompareModel compareModel) {
    CompareFragment compareFragment = new CompareFragment();

    Bundle args = new Bundle();
    args.putParcelable(ArgKeys.COMPARE_MODEL, compareModel);
    compareFragment.setArguments(args);

    return compareFragment;
  }

  @Override
  public String getScreenName() {
    return CompareFragment.class.getName();
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
    mCompareModel = bundle.getParcelable(ArgKeys.COMPARE_MODEL);

    mSpinnerListener = new SpinnerOnItemSelectedListener();
    mComparePanelSlideListener = new ComparePanelSlideListener();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    FilteredLogger.d(TAG, "onCreateView()");
    View view = inflater.inflate(R.layout.fragment_compare, container, false);

    mRootView = (FrameLayout) view.findViewById(R.id.compareFragment_rootView);
    mSlidingUpPanel = (SlidingUpPanelLayout) view.findViewById(R.id.compareFragment_slidingUpPanel);
    mLessThanTwoMessage = (LinearLayout) view.findViewById(R.id.compareFragment_lessThanTwoMessage);
    mHelpMessage = (TextView) view.findViewById(R.id.compareFragment_helpMessage);
    mStarView = (StarView) view.findViewById(R.id.compareFragment_starView);
    mLeftSpinner = (Spinner) view.findViewById(R.id.compareFragment_leftSpinner);
    mRightSpinner = (Spinner) view.findViewById(R.id.compareFragment_rightSpinner);
    mKartConfigurationContainer = (LinearLayout)
        view.findViewById(R.id.compareFragment_kartConfigurationContainer);
    mLeftKartConfigurationView = (CondensedKartConfigurationView)
        view.findViewById(R.id.compareFragment_leftKartConfiguration);
    mRightKartConfigurationView = (CondensedKartConfigurationView)
        view.findViewById(R.id.compareFragment_rightKartConfiguration);
    mBottomPanel = (LinearLayout) view.findViewById(R.id.compareFragment_bottomPanel);
    mDragView = (StatsDragView) view.findViewById(R.id.compareFragment_dragView);
    mMultiStatsCompareView = (MultiStatsCompareView)
        view.findViewById(R.id.compareFragment_multiCompareStatsView);

    mSlidingUpPanel.setDragView(mDragView);
    mSlidingUpPanel.setPanelSlideListener(mComparePanelSlideListener);

    mLeftSpinner.setOnItemSelectedListener(mSpinnerListener);
    mRightSpinner.setOnItemSelectedListener(mSpinnerListener);

    mHelpMessage.setTypeface(FontLoader.getInstance().getRobotoLightTypeface());
    mStarView.setOnTouchListener(null);

    mKartConfigurationContainer.getViewTreeObserver().addOnGlobalLayoutListener (
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            FilteredLogger.d(TAG, "onGlobalLayout");
            UiUtils.removeOnGlobalLayoutListener(mKartConfigurationContainer, this);
            mSlidingUpPanel.setPanelHeight(mKartConfigurationContainer.getMeasuredHeight() / 2);
            mBottomPanel.getLayoutParams().height = mKartConfigurationContainer.getMeasuredHeight();
            mBottomPanel.requestLayout();
            mComparePanelSlideListener.onPanelCollapsed(mSlidingUpPanel);
          }
        }
    );

    updateSpinners(null);

    return view;
  }

  @Override
  public void onResume() {
    FilteredLogger.d(TAG, "onResume()");
    mLeftSpinner.setOnItemSelectedListener(mSpinnerListener);
    mRightSpinner.setOnItemSelectedListener(mSpinnerListener);
    super.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putParcelable(ArgKeys.COMPARE_MODEL, mCompareModel);
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public void onPause() {
    FilteredLogger.d(TAG, "onPause()");
    mLeftSpinner.setOnItemSelectedListener(null);
    mRightSpinner.setOnItemSelectedListener(null);
    super.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    UiUtils.unbindDrawables(mRootView);
  }

  private void showMainView(boolean show) {
    FilteredLogger.d(TAG, "show(show: " + show + ")");
    mLessThanTwoMessage.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    mSlidingUpPanel.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    if (!show) {
      mSlidingUpPanel.collapsePanel();
    }
  }

  public void updateSpinners(String rightBuildKey) {
    FilteredLogger.d(TAG, "updateSpinners()");

    FilteredLogger.d(TAG, "mCompareModel.getLeftBuildKey(): " + mCompareModel.getLeftBuildKey());
    FilteredLogger.d(TAG, "mCompareModel.getRightBuildKey(): " + mCompareModel.getRightBuildKey());

    List<String> buildKeys = StarredBuildUtils.getAllStarredKeysSortedByEnum(getActivity());
    FilteredLogger.d(TAG, "buildKeys.size(): " + buildKeys.size());
    for (String buildKey : buildKeys) {
      FilteredLogger.d(TAG, "buildKey: " + buildKey);
    }

    if (buildKeys.size() >= MINIMUM_NUMBER_OF_BUILDS) {
      showMainView(true);
      if (mCompareModel.getLeftBuildKey() == null) {
        setBuildKey(mLeftSpinner, buildKeys.get(0));
      }
      if (mCompareModel.getRightBuildKey() == null) {
        setBuildKey(mRightSpinner, buildKeys.get(1));
      }
      if (rightBuildKey != null) {
        setBuildKey(mRightSpinner, rightBuildKey);
      }
      String currentLeftKey = mCompareModel.getLeftBuildKey();
      FilteredLogger.d(TAG, "currentLeftKey: " + currentLeftKey);
      String currentRightKey = mCompareModel.getRightBuildKey();
      FilteredLogger.d(TAG, "currentRightKey: " + currentRightKey);
      int currentLeftPosition = 0; // default left position
      int currentRightPosition = 1; // default right position
      // If we have both keys, get the positions to restore the last state.
      if (buildKeys.contains(currentLeftKey) && buildKeys.contains(currentRightKey)) {
        for (int keyIndex = 0; keyIndex < buildKeys.size(); keyIndex++) {
          if (currentLeftKey.equals(buildKeys.get(keyIndex))) {
            currentLeftPosition = keyIndex;
          }
          if (currentRightKey.equals(buildKeys.get(keyIndex))) {
            currentRightPosition = keyIndex;
          }
        }
      }
      String[] buildKeysArray = buildKeys.toArray(new String[buildKeys.size()]);
      mLeftSpinnerAdapter = new CompareSpinnerAdapter(getActivity(), mLeftSpinner, buildKeysArray);
      mRightSpinnerAdapter = new CompareSpinnerAdapter(getActivity(),
          mRightSpinner,
          buildKeysArray);

      updateBothKartConfigurationViewsAndStatsView();

      mSpinnerCountDownLatch = new CountDownLatch(2);
      mSpinnerListener.setUpdateViews(false);
      updateSpinner(mLeftSpinner, mLeftSpinnerAdapter, currentLeftPosition);
      updateSpinner(mRightSpinner, mRightSpinnerAdapter, currentRightPosition);
      new SpinnerTask().execute((Void) null);
    } else {
      showMainView(false);
      // We must set the first 2 items manually since the spinner listeners won't do it for us.
      if (buildKeys.size() == 1) {
        setBuildKey(mLeftSpinner, buildKeys.get(0));
      }
    }
  }

  private void updateSpinner(Spinner spinner,
      CompareSpinnerAdapter spinnerAdapter,
      int currentPosition) {
    spinner.setAdapter(spinnerAdapter);
    spinner.setSelection(currentPosition);
  }

  private void updateKartConfiguration(CondensedKartConfigurationView kartConfigurationView,
      KartConfiguration kartConfiguration) {
    FilteredLogger.d(TAG, "updateKartConfiguration()");
    kartConfigurationView.updateKartConfiguration(kartConfiguration);
  }

  private void updateStatsView(KartConfiguration leftKartConfiguration,
      KartConfiguration rightKartConfiguration) {
    FilteredLogger.d(TAG, "updateStatsView()");
    mMultiStatsCompareView.updateStatViews(leftKartConfiguration.getKartStats(),
        rightKartConfiguration.getKartStats());
  }

  private void setBuildKey(Spinner spinner, String key) {
    if (spinner.equals(mLeftSpinner)) {
      FilteredLogger.d(TAG, "setBuildKey() mLeftSpinner key: " + key);
      mCompareModel.setLeftBuildKey(key);
    } else if (spinner.equals(mRightSpinner)) {
      FilteredLogger.d(TAG, "setBuildKey() mRightSpinner key: " + key);
      mCompareModel.setRightBuildKey(key);
    } else {
      AnalyticsLogger.e(TAG, mTracker,  "setBuildKey() spinner not found!");
    }
    if (mListener != null) {
      mListener.onCompareModelUpdated(mCompareModel);
    }
  }

  private void updateBothKartConfigurationViewsAndStatsView() {
    FilteredLogger.d(TAG, "updateBothKartConfigurationViewsAndStatsView()");
    KartConfiguration leftKartConfiguration = StarredBuildUtils.getKartConfigurationFromKey(
        mCompareModel.getLeftBuildKey());
    KartConfiguration rightKartConfiguration = StarredBuildUtils.getKartConfigurationFromKey(
        mCompareModel.getRightBuildKey());
    updateKartConfiguration(mLeftKartConfigurationView, leftKartConfiguration);
    updateKartConfiguration(mRightKartConfigurationView, rightKartConfiguration);
    updateStatsView(leftKartConfiguration, rightKartConfiguration);
  }

  private class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private boolean mUpdateViews = true;

    public void setUpdateViews(boolean updateViews) {
      FilteredLogger.d(TAG, "setUpdateViews(): " + updateViews);
      mUpdateViews = updateViews;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      FilteredLogger.d(TAG, "SpinnerOnItemSelectedListener.onItemSelected()");
      setBuildKey((Spinner) parent, (String) parent.getItemAtPosition(position));

      if (mSpinnerCountDownLatch != null) {
        mSpinnerCountDownLatch.countDown();
      }

      if (mUpdateViews) {
        if (mLeftSpinnerAdapter.getCount() >= MINIMUM_NUMBER_OF_BUILDS ||
            mRightSpinnerAdapter.getCount() >= MINIMUM_NUMBER_OF_BUILDS) {
          KartConfiguration leftKartConfiguration = StarredBuildUtils.getKartConfigurationFromKey(
              mCompareModel.getLeftBuildKey());
          KartConfiguration rightKartConfiguration = StarredBuildUtils.getKartConfigurationFromKey(
              mCompareModel.getRightBuildKey());
          if (parent.equals(mLeftSpinner)) {
            updateKartConfiguration(mLeftKartConfigurationView, leftKartConfiguration);
            AnalyticsUtils.sendCompareViewSelectedEvent(mTracker,
                getActivity(),
                leftKartConfiguration);
          } else if (parent.equals(mRightSpinner)) {
            updateKartConfiguration(mRightKartConfigurationView, rightKartConfiguration);
            AnalyticsUtils.sendCompareViewSelectedEvent(mTracker,
                getActivity(),
                rightKartConfiguration);
          }
          updateStatsView(leftKartConfiguration, rightKartConfiguration);
        }
      } else {
        FilteredLogger.d(TAG, "onItemSelected() updateViews skipped");
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
      // Do nothing.
    }
  }

  // Make general class ScrollingPanelSlideListener, see BuildPanelSlideListener.
  private class ComparePanelSlideListener implements SlidingUpPanelLayout.PanelSlideListener {
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
      FilteredLogger.d(TAG, "resizeVisiblePanelArea(slideOffset: " + slideOffset);
      mMultiStatsCompareView.getScrollView().getLayoutParams().height =
          mSlidingUpPanel.getPanelHeight() -
          mDragView.getHeight() +
          (int) (slideOffset * (mSlidingUpPanel.getHeight() - mSlidingUpPanel.getPanelHeight()));
      mMultiStatsCompareView.getScrollView().requestLayout();
    }
  }

  private class SpinnerTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
      try {
        mSpinnerCountDownLatch.await();
      } catch (InterruptedException e) {
        AnalyticsLogger.e(TAG, mTracker, "mSpinnerCountDownLatch interrupted!");
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      mSpinnerCountDownLatch = null;
      mSpinnerListener.setUpdateViews(true);
    }
  }
}
