package com.blunka.mk8assistant.main.adjust;

import com.blunka.mk8assistant.data.AdjustConfiguration;

/**
 * Created by clocksmith on 7/14/14.
 */
public enum ConfigurationChoice {
  CUSTOM("Custom", null),
  // FLYWEIGHT:E:C:B
  NEVER_PLAYED_BEFORE("Never Played Before", AdjustConfiguration.newBuilder()
      .withAcceleration(100)
      .withAverageSpeed(0)
      .withAverageHandling(100)
      .withMiniturbo(0)
      .withTraction(0)
      .withWeight(0)
      .build()),
  // FLYWEIGHT:B:C:B
  NOVICE("Novice", AdjustConfiguration.newBuilder()
      .withAcceleration(0)
      .withAverageSpeed(20)
      .withAverageHandling(100)
      .withMiniturbo(0)
      .withTraction(20)
      .withWeight(0)
      .build()),
  // METALWEIGHT:D:B:A
  TRACTOR("Tractor", AdjustConfiguration.newBuilder()
      .withAcceleration(25)
      .withAverageSpeed(50)
      .withAverageHandling(75)
      .withMiniturbo(25)
      .withTraction(100)
      .withWeight(100)
      .build()),
  // HEAVYWEIGHT:E:C:B
  FLASH("Flash", AdjustConfiguration.newBuilder()
      .withAcceleration(100)
      .withAverageSpeed(60)
      .withAverageHandling(0)
      .withMiniturbo(0)
      .withTraction(0)
      .withWeight(0)
      .build()),
  // HEAVYWEIGHT:C:E:A
  EXPERT("Expert", AdjustConfiguration.newBuilder()
      .withAcceleration(0)
      .withAverageSpeed(100)
      .withAverageHandling(0)
      .withMiniturbo(0)
      .withTraction(0)
      .withWeight(0)
      .build());

  private String mDisplayName;
  private AdjustConfiguration mAdjustConfiguration;

  ConfigurationChoice(String displayName, AdjustConfiguration adjustConfiguration) {
    mDisplayName = displayName;
    mAdjustConfiguration = adjustConfiguration;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  public AdjustConfiguration getAdjustConfiguration() {
    return mAdjustConfiguration;
  }
}
