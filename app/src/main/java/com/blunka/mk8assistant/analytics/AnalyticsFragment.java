package com.blunka.mk8assistant.analytics;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.blunka.mk8assistant.ConfigureMK8Application;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by clocksmith on 7/31/14.
 */
public abstract class AnalyticsFragment extends Fragment {
  protected Tracker mTracker;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mTracker = ((ConfigureMK8Application) getActivity().getApplication()).getAppTracker();
    mTracker.setScreenName(getScreenName());
  }

  public abstract String getScreenName();

  public void sendScreenViewToAnalytics() {
    mTracker.send(new HitBuilders.AppViewBuilder().build());
  }
}
