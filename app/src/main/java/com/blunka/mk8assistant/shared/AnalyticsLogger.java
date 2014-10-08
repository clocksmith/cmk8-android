package com.blunka.mk8assistant.shared;

import android.util.Log;

import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by clocksmith on 8/1/14.
 */
public class AnalyticsLogger {
  public static void e(String tag, Tracker tracker, String message) {
    Log.e(tag, message);
    AnalyticsUtils.sendErrorLogEvent(tracker, tag + ": " + message);
  }
}
