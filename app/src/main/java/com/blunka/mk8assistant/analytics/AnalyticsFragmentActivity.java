package com.blunka.mk8assistant.analytics;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.blunka.mk8assistant.ConfigureMK8Application;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by clocksmith on 8/3/14.
 */
public abstract class AnalyticsFragmentActivity extends FragmentActivity {
  protected Tracker mTracker;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mTracker = ((ConfigureMK8Application) getApplication()).getAppTracker();
    mTracker.setScreenName(getScreenName());
    mTracker.send(new HitBuilders.AppViewBuilder().build());
  }

  protected abstract String getScreenName();

  public Tracker getTracker() {
    return mTracker;
  }
}
