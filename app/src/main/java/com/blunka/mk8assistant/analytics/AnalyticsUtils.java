package com.blunka.mk8assistant.analytics;

import android.content.Context;

import com.blunka.mk8assistant.data.AdjustConfiguration;
import com.blunka.mk8assistant.data.Attribute;
import com.blunka.mk8assistant.data.KartConfiguration;
import com.blunka.mk8assistant.data.parts.Part;
import com.blunka.mk8assistant.main.adjust.ConfigurationChoice;
import com.blunka.mk8assistant.shared.preferences.StarredBuildUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by clocksmith on 7/31/14.
 */
public class AnalyticsUtils {
  public static final String PROPERTY_ID = "UA-53388714-1";


  public static final String ADJUST_CONFIGURATION_CATEGORY_CUSTOM = "Adjust Configuration Custom";

  public static final String ADJUST_BUILD_RESULT_CATEGORY_CUSTOM = "Adjust Build Result Custom";

  public static final String ADJUST_BUILD_RESULT_CATEGORY_SIMPLE =
      "Adjust Build Result Simple";

  public static final String PART_VIEW_CATEGORY = "Part View";

  public static final String STAR_VIEW_CATEGORY_CUSTOM = "Star View Custom";

  public static final String STAR_VIEW_CATEGORY_SIMPLE= "Star View Simple";

  public static final String COMPARE_VIEW_CATEGORY_CUSTOM = "Compare View Custom";

  public static final String COMPARE_VIEW_CATEGORY_SIMPLE = "Compare View Simple";

  public static final String BUTTON_CATEGORY = "Button";

  public static final String DONATION_CATEGORY = "Donation";

  public static final String ERROR_CATEGORY = "Error";


  public static final String OK_ACTION = "OK";

  public static final String CANCEL_ACTION = "Cancel";

  public static final String CLICK_ACTION = "Click";

  public static final String STAR_ACTION = "Star";

  public static final String UNSTAR_ACTION = "Unstar";

  public static final String SELECT_ACTION = "Select";

  public static final String DONATE_ACTION = "Donate";


  public static final String RATE_BUTTON_LABEL = "Rate Button";

  public static final String DONATE_BUTTON_LABEL = "Donate Button";


  public static final int CUSTOM_METRIC_ATTRIBUTE_ACCELERATION_INDEX = 1;

  public static final int CUSTOM_METRIC_ATTRIBUTE_AVERAGE_SPEED_INDEX = 2;

  public static final int CUSTOM_METRIC_ATTRIBUTE_AVERAGE_HANDLING_INDEX = 3;

  public static final int CUSTOM_METRIC_ATTRIBUTE_MINITURBO_INDEX = 4;

  public static final int CUSTOM_METRIC_ATTRIBUTE_TRACTION_INDEX = 5;

  public static final int CUSTOM_METRIC_ATTRIBUTE_WEIGHT_INDEX = 6;


  public static final int CUSTOM_DIMENSION_CHARACTER_GROUP = 1;

  public static final int CUSTOM_DIMENSION_VEHICLE_GROUP = 2;

  public static final int CUSTOM_DIMENSION_TIRE_GROUP = 3;

  public static final int CUSTOM_DIMENSION_GLIDER_GROUP = 4;

  public static final int CUSTOM_DIMENSION_PARENT_FRAGMENT = 5;



  public static void sendAdjustConfigurationCancelEvent(Tracker tracker,
      ConfigurationChoice configurationChoice,
      AdjustConfiguration adjustConfiguration) {
    sendAdjustConfigurationEvent(tracker,
        AnalyticsUtils.CANCEL_ACTION,
        configurationChoice,
        adjustConfiguration);
  }

  public static void sendAdjustConfigurationOkEvent(Tracker tracker,
      ConfigurationChoice configurationChoice,
      AdjustConfiguration adjustConfiguration) {
    sendAdjustConfigurationEvent(tracker,
        AnalyticsUtils.OK_ACTION,
        configurationChoice,
        adjustConfiguration);
  }

  public static void sendAdjustConfigurationEvent(Tracker tracker,
      String action,
      ConfigurationChoice configurationChoice,
      AdjustConfiguration adjustConfiguration) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(ADJUST_CONFIGURATION_CATEGORY_CUSTOM)
        .setAction(action)
        .setLabel(configurationChoice.name())
        .setCustomMetric(CUSTOM_METRIC_ATTRIBUTE_ACCELERATION_INDEX,
            adjustConfiguration.getAttributeValue(Attribute.ACCELERATION))
        .setCustomMetric(CUSTOM_METRIC_ATTRIBUTE_AVERAGE_SPEED_INDEX,
            adjustConfiguration.getAttributeValue(Attribute.AVERAGE_SPEED))
        .setCustomMetric(CUSTOM_METRIC_ATTRIBUTE_AVERAGE_HANDLING_INDEX,
            adjustConfiguration.getAttributeValue(Attribute.AVERAGE_HANDLING))
        .setCustomMetric(CUSTOM_METRIC_ATTRIBUTE_MINITURBO_INDEX,
            adjustConfiguration.getAttributeValue(Attribute.MINITURBO))
        .setCustomMetric(CUSTOM_METRIC_ATTRIBUTE_TRACTION_INDEX,
            adjustConfiguration.getAttributeValue(Attribute.TRACTION))
        .setCustomMetric(CUSTOM_METRIC_ATTRIBUTE_WEIGHT_INDEX,
            adjustConfiguration.getAttributeValue(Attribute.WEIGHT))
        .build());
  }

  public static void sendBuildConfigurationEvent(Tracker tracker,
      ConfigurationChoice configurationChoice,
      KartConfiguration kartConfiguration) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(ADJUST_BUILD_RESULT_CATEGORY_CUSTOM)
        .setLabel(configurationChoice.name())
        .setCustomDimension(CUSTOM_DIMENSION_CHARACTER_GROUP,
            kartConfiguration.getCharacterGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_VEHICLE_GROUP,
            kartConfiguration.getVehicleGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_TIRE_GROUP,
            kartConfiguration.getTireGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_GLIDER_GROUP,
            kartConfiguration.getGliderGroup().getName())
        .build());

    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(ADJUST_BUILD_RESULT_CATEGORY_SIMPLE)
        .setLabel(StarredBuildUtils.getNameFromConfiguration(kartConfiguration))
        .build());
  }

  public static void sendPartViewClickedEvent(Tracker tracker, String screenName, Part part) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(PART_VIEW_CATEGORY)
        .setAction(CLICK_ACTION)
        .setCustomDimension(CUSTOM_DIMENSION_PARENT_FRAGMENT, screenName)
        .setLabel(part.getName())
        .build());
  }

  public static void sendStarViewStarredEvent(Tracker tracker,
      KartConfiguration kartConfiguration,
      boolean starred) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(STAR_VIEW_CATEGORY_CUSTOM)
        .setAction(starred ? STAR_ACTION : UNSTAR_ACTION)
        .setCustomDimension(CUSTOM_DIMENSION_CHARACTER_GROUP,
            kartConfiguration.getCharacterGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_VEHICLE_GROUP,
            kartConfiguration.getVehicleGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_TIRE_GROUP,
            kartConfiguration.getTireGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_GLIDER_GROUP,
            kartConfiguration.getGliderGroup().getName())
        .build());

    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(STAR_VIEW_CATEGORY_SIMPLE)
        .setAction(starred ? STAR_ACTION : UNSTAR_ACTION)
        .setLabel(StarredBuildUtils.getNameFromConfiguration(kartConfiguration))
        .build());
  }

  public static void sendCompareViewSelectedEvent(Tracker tracker,
      KartConfiguration kartConfiguration) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(COMPARE_VIEW_CATEGORY_CUSTOM)
        .setAction(SELECT_ACTION)
        .setCustomDimension(CUSTOM_DIMENSION_CHARACTER_GROUP,
            kartConfiguration.getCharacterGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_VEHICLE_GROUP,
            kartConfiguration.getVehicleGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_TIRE_GROUP,
            kartConfiguration.getTireGroup().getName())
        .setCustomDimension(CUSTOM_DIMENSION_GLIDER_GROUP,
            kartConfiguration.getGliderGroup().getName())
        .build());

    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(COMPARE_VIEW_CATEGORY_SIMPLE)
        .setAction(SELECT_ACTION)
        .setLabel(StarredBuildUtils.getNameFromConfiguration(kartConfiguration))
        .build());
  }

  public static void sendRateButtonClicked(Tracker tracker) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(BUTTON_CATEGORY)
        .setAction(CLICK_ACTION)
        .setLabel(RATE_BUTTON_LABEL)
        .build());
  }

  public static void sendDonateButtonClicked(Tracker tracker) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(BUTTON_CATEGORY)
        .setAction(CLICK_ACTION)
        .setLabel(DONATE_BUTTON_LABEL)
        .build());
  }

  public static void sendDonationComplete(Tracker tracker, String productId) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(DONATION_CATEGORY)
        .setAction(DONATE_ACTION)
        .setLabel(productId)
        .build());
  }

  public static void sendErrorLogEvent(Tracker tracker, String errorMessage) {
    tracker.send(new HitBuilders.EventBuilder()
        .setCategory(ERROR_CATEGORY)
        .setLabel(errorMessage)
        .build());
  }
}
