package com.blunka.mk8assistant;

import android.app.Application;

import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by clocksmith on 7/28/14.
 */
public class ConfigureMK8Application extends Application {
  private Tracker mAppTracker;

  @Override
  public void onCreate() {
    super.onCreate();

    // Init the fonts.
    FontLoader.getInstance().init(getAssets());
  }

  public synchronized Tracker getAppTracker() {
    if (mAppTracker == null) {
      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      mAppTracker = analytics.newTracker(AnalyticsUtils.PROPERTY_ID);
    }
    return mAppTracker;
  }
}
