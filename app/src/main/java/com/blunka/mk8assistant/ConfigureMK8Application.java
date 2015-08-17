package com.blunka.mk8assistant;

import android.app.Application;
import android.util.Log;

import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.data.courses.CourseData;
import com.blunka.mk8assistant.data.parts.PartData;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;

/**
 * Created by clocksmith on 7/28/14.
 */
public class ConfigureMK8Application extends Application {
  private static final String TAG = ConfigureMK8Application.class.getSimpleName();

  private Tracker mAppTracker;

  @Override
  public void onCreate() {
    super.onCreate();

    // Init the fonts.
    FontLoader.getInstance().init(getAssets());

    // Init the parts and course data.
    try {
      PartData.init(getApplicationContext());
      CourseData.init(getApplicationContext());
    } catch (JSONException e) {
      // Uh oh.
      Log.e(TAG, "Could not init data", e);
    }
  }

  public synchronized Tracker getAppTracker() {
    if (mAppTracker == null) {
      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      analytics.setLocalDispatchPeriod(1800);
      mAppTracker = analytics.newTracker(AnalyticsUtils.PROPERTY_ID);
      mAppTracker.enableExceptionReporting(false);
      mAppTracker.enableAdvertisingIdCollection(true);
      mAppTracker.enableAutoActivityTracking(true);
    }
    return mAppTracker;
  }
}
