package com.blunka.mk8assistant.shared;

/**
 * Created by clocksmith on 7/16/14.
 */
public class Constants {

  // Request codes.
  public static final int ADJUST_BUILD_REQUEST_CODE = 101;
  public static final int DONATE_REQUEST_CODE = 201;

  // Billing.
  public static final int BILLING_RESPONSE_RESULT_OK = 0;
  public static final int BILLING_API_VERSION = 3;

  // Animations
  public static final int PULSE_DURATION_MS = 75;
  public static final int DEFAULT_CHANGE_DURATION_MS = 175;
  public static final int STATS_BAR_CHANGE_DURATION_MS = 350;
  public static final int VERTICAL_VIEW_PAGER_SCROLL_DURATION_MS = 350;

  public static final String STATS_DECIMAL_FORMAT = "0.00";
  public static final float MAX_TOTAL_STATS_VALUE = 6f;
  public static final float MAX_SMALL_PART_STATS_VALUE = 1.5f;
  public static final int MAX_NUMBER_PARTS_PER_GROUP = 7;
}
