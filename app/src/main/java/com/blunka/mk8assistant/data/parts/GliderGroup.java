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
 * There are 2 glider groups.
 */
public enum GliderGroup implements PartGroup {
  A(R.string.glider_a,
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
          Glider.SUPER_GLIDER,
          Glider.WARIO_WING,
          Glider.WADDLE_WING,
          Glider.PLANE_GLIDER,
          Glider.GOLD_GLIDER)),
  B(R.string.glider_b,
      Stats.newBuilder()
          .withAcceleration(0.25f)
          .withGroundSpeed(0f)
          .withAntigravitySpeed(0f)
          .withAirSpeed(-0.25f)
          .withWaterSpeed(0f)
          .withMiniturbo(0.25f)
          .withGroundHandling(0f)
          .withAntigravityHandling(0f)
          .withAirHandling(0.25f)
          .withWaterHandling(0f)
          .withTraction(0f)
          .withWeight(-0.25f)
          .build(),
      Lists.newArrayList(
          Glider.CLOUD_GLIDER,
          Glider.PEACH_PARASOL,
          Glider.PARACHUTE,
          Glider.PARAFOIL,
          Glider.FLOWER_GLIDER,
          Glider.BOWSER_KITE,
          Glider.MKTV_PARAFOIL));

  private int mDisplayGroupNameResourceId;
  private Stats mStats;
  private List<Glider> mGliders;

  GliderGroup(int displayGroupNameResourceId, Stats stats, List<Glider> gliders) {
    mDisplayGroupNameResourceId = displayGroupNameResourceId  ;
    mStats = stats;
    mGliders = gliders;
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
    return Lists.<HasDisplayNameAndIcon>newArrayList(mGliders);
  }
}