package com.blunka.mk8assistant.data.parts;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.data.Stats;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 7/2/14.
 *
 * There are 6 vehicle groups.
 */
public enum VehicleGroup implements PartGroup {
  A(R.string.vehicle_a,
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
          Vehicle.STANDARD_KART,
          Vehicle.CAT_CRUISER,
          Vehicle.PRANCER,
          Vehicle.SNEEKER,
          Vehicle.THE_DUKE,
          Vehicle.TEDDY_BUGGY,
          Vehicle.THREE_HUNDRED_SL_ROADSTER)),
  B(R.string.vehicle_b,
      Stats.newBuilder()
          .withAcceleration(0.25f)
          .withGroundSpeed(0f)
          .withAntigravitySpeed(-0.25f)
          .withAirSpeed(0.25f)
          .withWaterSpeed(0.25f)
          .withMiniturbo(0.25f)
          .withGroundHandling(0.5f)
          .withAntigravityHandling(0.25f)
          .withAirHandling(0.5f)
          .withWaterHandling(0.5f)
          .withTraction(-0.5f)
          .withWeight(-0.25f)
          .build(),
      Lists.newArrayList(
          Vehicle.PIPE_FRAME,
          Vehicle.STANDARD_BIKE,
          Vehicle.FLAME_RIDER,
          Vehicle.VARMINT,
          Vehicle.WILD_WIGGLER,
          Vehicle.W_25_SILVER_ARROW)),
  C(R.string.vehicle_c,
      Stats.newBuilder()
          .withAcceleration(-0.25f)
          .withGroundSpeed(0.5f)
          .withAntigravitySpeed(-0.25f)
          .withAirSpeed(0.5f)
          .withWaterSpeed(0.25f)
          .withMiniturbo(-0.5f)
          .withGroundHandling(0f)
          .withAntigravityHandling(-0.25f)
          .withAirHandling(0f)
          .withWaterHandling(0f)
          .withTraction(-1f)
          .withWeight(0.25f)
          .build(),
      Lists.newArrayList(
          Vehicle.MACH_8,
          Vehicle.CIRCUIT_SPECIAL,
          Vehicle.SPORTS_COUPE,
          Vehicle.GOLD_STANDARD)),
  D(R.string.vehicle_d,
      Stats.newBuilder()
          .withAcceleration(-0.5f)
          .withGroundSpeed(0f)
          .withAntigravitySpeed(-0.25f)
          .withAirSpeed(0f)
          .withWaterSpeed(0.5f)
          .withMiniturbo(-0.75f)
          .withGroundHandling(-0.5f)
          .withAntigravityHandling(-0.75f)
          .withAirHandling(-0.25f)
          .withWaterHandling(0.75f)
          .withTraction(0.5f)
          .withWeight(0.5f)
          .build(),
      Lists.newArrayList(
          Vehicle.STEEL_DRIVER,
          Vehicle.TRI_SPEEDER,
          Vehicle.BADWAGON,
          Vehicle.STANDARD_ATV,
          Vehicle.GLA)),
  E(R.string.vehicle_e,
      Stats.newBuilder()
          .withAcceleration(1.25f)
          .withGroundSpeed(-0.75f)
          .withAntigravitySpeed(-1f)
          .withAirSpeed(0.5f)
          .withWaterSpeed(0.5f)
          .withMiniturbo(1f)
          .withGroundHandling(-0.5f)
          .withAntigravityHandling(0.75f)
          .withAirHandling(0.75f)
          .withWaterHandling(0.5f)
          .withTraction(-0.25f)
          .withWeight(-0.5f)
          .build(),
      Lists.newArrayList(
          Vehicle.BIDDYBUGGY,
          Vehicle.LANDSHIP,
          Vehicle.MR_SCOOTY)),
  F(R.string.vehicle_f,
      Stats.newBuilder()
          .withAcceleration(0.75f)
          .withGroundSpeed(0f)
          .withAntigravitySpeed(-0.25f)
          .withAirSpeed(0f)
          .withWaterSpeed(0f)
          .withMiniturbo(0.5f)
          .withGroundHandling(0.75f)
          .withAntigravityHandling(0.5f)
          .withAirHandling(0.75f)
          .withWaterHandling(0.75f)
          .withTraction(-1.25f)
          .withWeight(-0.25f)
          .withInnerDrift(true)
          .build(),
      Lists.newArrayList(
          Vehicle.COMET,
          Vehicle.SPORT_BIKE,
          Vehicle.JET_BIKE,
          Vehicle.YOSHI_BIKE));

  private int mDisplayGroupNameResourceId;
  private Stats mStats;
  private List<Vehicle> mVehicles;

  VehicleGroup(int displayGroupNameResourceId, Stats stats, List<Vehicle> vehicles) {
    mDisplayGroupNameResourceId = displayGroupNameResourceId;
    mStats = stats;
    mVehicles = vehicles;
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
    return Lists.<HasDisplayNameAndIcon>newArrayList(mVehicles);
  }
}