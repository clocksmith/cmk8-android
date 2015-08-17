package com.blunka.mk8assistant.shared.stats;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.Stats;
import com.blunka.mk8assistant.data.parts.Part;
import com.blunka.mk8assistant.data.parts.PartUtils;
import com.blunka.mk8assistant.main.configure.MultiStatsPositiveView;
import com.blunka.mk8assistant.shared.ArgKeys;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.blunka.mk8assistant.shared.ui.TransitionImageDialogFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.common.collect.Lists;

public class PartStatsDialogFragment extends TransitionImageDialogFragment {
  private static final String TAG = PartStatsDialogFragment.class.getSimpleName();

  private long mStartTimeMs;

  // Model.
  private Part mPart;
  private int mTransitionResourceId;

  // View
  private TextView mTitle;
  private FrameLayout mStatsContainer;
  private MultiStatsWithLabelView mMultiStatsWithLabelView;
  private View mDummyViewForClickDismiss;

  public static PartStatsDialogFragment newInstance(Part part,
      ImageView startImageView,
      int resourceId) {
    PartStatsDialogFragment partStatsDialogFragment = new PartStatsDialogFragment();

    Bundle args = new Bundle();
    args.putParcelable(ArgKeys.PART, part);
    args.putInt(ArgKeys.RESOURCE_ID, resourceId);
    partStatsDialogFragment.insertSharedArgs(args, Lists.newArrayList(startImageView));
    partStatsDialogFragment.setArguments(args);

    return partStatsDialogFragment;
  }

  @Override
  public String getScreenName() {
    return PartStatsDialogFragment.class.getName();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    mStartTimeMs = System.currentTimeMillis();
    FilteredLogger.d(TAG, "onCreate(): " + (System.currentTimeMillis() - mStartTimeMs) + "ms");
    super.onCreate(savedInstanceState);

    mTracker.setScreenName(PartStatsDialogFragment.class.getName());
    mTracker.send(new HitBuilders.AppViewBuilder().build());

    Bundle bundle;
    if (savedInstanceState != null) {
      bundle = savedInstanceState;
    } else {
      bundle = getArguments();
    }
    mPart = bundle.getParcelable(ArgKeys.PART);
    mTransitionResourceId = bundle.getInt(ArgKeys.RESOURCE_ID);
    unpackSharedArgs(bundle);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    FilteredLogger.d(TAG, "onCreateView(): " + (System.currentTimeMillis() - mStartTimeMs) + "ms");
    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

    View view = inflater.inflate(R.layout.fragment_part_stats_dialog, container, false);

    // from super
    mShade = view.findViewById(R.id.partStatsDialogFragment_shade);
    mMainContent = (LinearLayout) view.findViewById(R.id.partStatsDialogFragment_mainContent);
    mShadowImageViews.add((ImageView) view.findViewById(R.id.partStatsDialogFragment_imageView));

    mTitle = (TextView) view.findViewById(R.id.partStatsDialogFragment_title);
    mStatsContainer = (FrameLayout) view.findViewById(R.id.partStatsDialogFragment_statsContainer);
    mDummyViewForClickDismiss =
        view.findViewById(R.id.partStatsDialogFragment_dummyViewForClickDismiss);

    mTitle.setTypeface(FontLoader.getInstance().getRobotoCondensedLightTypeface());
    mTitle.setText(mPart.getDisplayName());

    mDummyViewForClickDismiss.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FilteredLogger.d(TAG, "mDummyViewForClickDismiss.OnClickListener.onClick");
        dismiss();
      }
    });

    addStatsView();

    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FilteredLogger.d(TAG, "onViewCreated(): " + (System.currentTimeMillis() - mStartTimeMs) + "ms");
  }

  @Override
  public void onResume() {
    super.onResume();
    FilteredLogger.d(TAG, "onResume()");
  }

  @Override
  protected void setupViewForTransitionOut() {
    if (mMultiStatsWithLabelView != null) {
      mMultiStatsWithLabelView.updateStatViews(Stats.newBuilder()
          .withAcceleration(0d)
          .withAverageSpeed(0d)
          .withAverageHandling(0d)
          .withMiniturbo(0d)
          .withTraction(0d)
          .withWeight(0d)
          .build());
    }
  }

  @Override
  protected Drawable getTransitionDrawable(int imageIndex, ImageView imageView) {
    return getResources().getDrawable(mTransitionResourceId);
  }

  @Override
  protected void onTransitionInAnimationStart() {
  }

  @Override
  protected void onTransitionOutAnimationEnd() {
  }

  private void addStatsView() {
    FilteredLogger.d(TAG, "addStatsView(): " + (System.currentTimeMillis() - mStartTimeMs) + "ms");
    switch (mPart.getType()) {
      case CHARACTER:
        mMultiStatsWithLabelView = new MultiStatsPositiveView(getActivity());
        break;
      case VEHICLE:
      case TIRE:
      case GLIDER:
        mMultiStatsWithLabelView = new MultiStatsMaybeNegativeView(getActivity());
        break;
    }

    FilteredLogger.d(TAG, "addStatsView() mMultiStatsWithLabelView created: " +
        (System.currentTimeMillis() - mStartTimeMs) + "ms");
    mStatsContainer.addView(mMultiStatsWithLabelView, new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT));
    FilteredLogger.d(TAG, "addStatsView() mMultiStatsWithLabelView added to layout: " +
        (System.currentTimeMillis() - mStartTimeMs) + "ms");
    mMultiStatsWithLabelView.updateStatViews(PartUtils.getPartGroup(mPart).getStats());
  }
}