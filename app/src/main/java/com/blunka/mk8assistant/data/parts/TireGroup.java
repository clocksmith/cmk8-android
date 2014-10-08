package com.blunka.mk8assistant.data.parts;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.data.Stats;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 7/3/14.
 *
 * There are 7 tire groups.
 */
public enum TireGroup implements PartGroup {
  A(R.string.tire_a,
      Stats.newBuilder()
          .withAcceleration(0f)
          .withGroundSpeed(0f)
          .withAntigravitySpeed(0f)
          .withAirSpeed(0f)
          .withWaterSpeed(0f)
          .withMiniturbo(0f)
          .withGroundHandling(0f)
          .withAntigravityHandling(0f)
          .withAirHandling(0f)
          .withWaterHandling(0f)
          .withTraction(0f)
          .withWeight(0f)
          .build(),
      Lists.newArrayList(
          Tire.STANDARD,
          Tire.OFF_ROAD,
          Tire.BLUE_STANDARD,
          Tire.RETRO_OFF_ROAD,
          Tire.GLA_TIRES)),
  B(R.string.tire_b,
      Stats.newBuilder()
          .withAcceleration(-0.5f)
          .withGroundSpeed(0f)
          .withAntigravitySpeed(0f)
          .withAirSpeed(-0.5f)
          .withWaterSpeed(-0.5f)
          .withMiniturbo(0f)
          .withGroundHandling(-0.75f)
          .withAntigravityHandling(-0.75f)
          .withAirHandling(-0.75f)
          .withWaterHandling(-0.75f)
          .withTraction(0.75f)
          .withWeight(0.5f)
          .build(),
      Lists.newArrayList(
          Tire.MONSTER,
          Tire.HOT_MONSTER)),
  C(R.string.tire_c,
      Stats.newBuilder()
          .withAcceleration(1f)
          .withGroundSpeed(-0.5f)
          .withAntigravitySpeed(-0.5f)
          .withAirSpeed(0.5f)
          .withWaterSpeed(0f)
          .withMiniturbo(1.5f)
          .withGroundHandling(0.25f)
          .withAntigravityHandling(0.25f)
          .withAirHandling(0.25f)
          .withWaterHandling(0.25f)
          .withTraction(-0.25f)
          .withWeight(-0.5f)
          .build(),
      Lists.newArrayList(
          Tire.ROLLER,
          Tire.BUTTON,
          Tire.AZURE_ROLLER)),
  D(R.string.tire_d,
      Stats.newBuilder()
          .withAcceleration(-0.25f)
          .withGroundSpeed(0.25f)
          .withAntigravitySpeed(0.25f)
          .withAirSpeed(0.25f)
          .withWaterSpeed(-0.25f)
          .withMiniturbo(0.25f)
          .withGroundHandling(0.25f)
          .withAntigravityHandling(0.25f)
          .withAirHandling(0.25f)
          .withWaterHandling(0.25f)
          .withTraction(-0.5f)
          .withWeight(0f)
          .build(),
      Lists.newArrayList(
          Tire.SLIM,
          Tire.CRIMSON_SLIM)),
  E(R.string.tire_e,
      Stats.newBuilder()
          .withAcceleration(-0.25f)
          .withGroundSpeed(0.5f)
          .withAntigravitySpeed(0.5f)
          .withAirSpeed(0.5f)
          .withWaterSpeed(-1f)
          .withMiniturbo(0.25f)
          .withGroundHandling(0f)
          .withAntigravityHandling(0f)
          .withAirHandling(0f)
          .withWaterHandling(0f)
          .withTraction(-1f)
          .withWeight(0.25f)
          .build(),
      Lists.newArrayList(
          Tire.SLICK,
          Tire.CYBER_SLICK)),
  F(R.string.tire_f,
      Stats.newBuilder()
          .withAcceleration(-0.5f)
          .withGroundSpeed(0.25f)
          .withAntigravitySpeed(0.25f)
          .withAirSpeed(0.25f)
          .withWaterSpeed(-0.25f)
          .withMiniturbo(0f)
          .withGroundHandling(0f)
          .withAntigravityHandling(0f)
          .withAirHandling(0f)
          .withWaterHandling(0f)
          .withTraction(-0.5f)
          .withWeight(0.5f)
          .build(),
      Lists.newArrayList(
          Tire.METAL,
          Tire.GOLD_TIRES)),
  G(R.string.tire_g,
      Stats.newBuilder()
          .withAcceleration(0.25f)
          .withGroundSpeed(-0.25f)
          .withAntigravitySpeed(-0.25f)
          .withAirSpeed(0.25f)
          .withWaterSpeed(-1f)
          .withMiniturbo(0.75f)
          .withGroundHandling(-0.25f)
          .withAntigravityHandling(-0.25f)
          .withAirHandling(0f)
          .withWaterHandling(-0.5f)
          .withTraction(0.5f)
          .withWeight(-0.25f)
          .build(),
      Lists.newArrayList(
          Tire.SPONGE,
          Tire.WOOD,
          Tire.CUSHION));

  private int mDisplayGroupNameResourceId;
  private Stats mStats;
  private List<Tire> mTires;

  TireGroup(int displayGroupNameResourceId, Stats stats, List<Tire> tires) {
    mDisplayGroupNameResourceId = displayGroupNameResourceId;
    mStats = stats;
    mTires = tires;
  }

  @Override
  public String getDisplayGroupName(Context context) {
    return context.getString(mDisplayGroupNameResourceId);
  }

  @Override
  public Stats getPartStats() {
    return mStats;
  }

  @Override
  public List<HasDisplayNameAndIcon> getParts() {
    return Lists.<HasDisplayNameAndIcon>newArrayList(mTires);
  }
}
