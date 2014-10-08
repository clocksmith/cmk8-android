package com.blunka.mk8assistant.data;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 7/6/14.
 */
public enum Attribute {
  ACCELERATION(R.string.acceleration),
  AVERAGE_SPEED(R.string.average_speed),
  GROUND_SPEED(R.string.ground_speed),
  ANTIGRAVITY_SPEED(R.string.antigravity_speed),
  AIR_SPEED(R.string.air_speed),
  WATER_SPEED(R.string.water_speed),
  AVERAGE_HANDLING(R.string.average_handling),
  GROUND_HANDLING(R.string.ground_handling),
  ANTIGRAVITY_HANDLING(R.string.antigravity_handling),
  AIR_HANDLING(R.string.air_handling),
  WATER_HANDLING(R.string.water_handling),
  MINITURBO(R.string.miniturbo),
  TRACTION(R.string.traction),
  WEIGHT(R.string.weight);

  private int mResourceId;

  Attribute(int resourceId) {
    mResourceId = resourceId;
  }

  public String getDisplayName(Context context) {
    return context.getString(mResourceId);
  }

  public static List<Attribute> getSimpleValues() {
    return Lists.newArrayList(ACCELERATION, AVERAGE_SPEED, AVERAGE_HANDLING, MINITURBO, TRACTION, WEIGHT);
  }

  public static List<Attribute> getAdvancedValues() {
    return Lists.newArrayList(GROUND_SPEED,
        ANTIGRAVITY_SPEED,
        AIR_SPEED,
        WATER_SPEED,
        GROUND_HANDLING,
        ANTIGRAVITY_HANDLING,
        AIR_HANDLING,
        WATER_HANDLING);
  }

  public static List<Attribute> getAllValuesInOrder() {
    return Lists.newArrayList(Iterables.concat(getSimpleValues(), getAdvancedValues()));
  }
}