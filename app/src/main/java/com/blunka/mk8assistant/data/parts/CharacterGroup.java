package com.blunka.mk8assistant.data.parts;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.data.Stats;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 7/1/14.
 *
 * There are 7 character groups.
 */
public enum CharacterGroup implements PartGroup {
  FLYWEIGHT(R.string.flyweight,
      Stats.newBuilder()
          .withAcceleration(3.25f)
          .withGroundSpeed(2.25f)
          .withAntigravitySpeed(2.5f)
          .withAirSpeed(2.25f)
          .withWaterSpeed(2.75f)
          .withMiniturbo(3f)
          .withGroundHandling(4.75f)
          .withAntigravityHandling(5f)
          .withAirHandling(4.5f)
          .withWaterHandling(4.75f)
          .withTraction(4.5f)
          .withWeight(2.25f)
          .build(),
      Lists.newArrayList(
          Character.BABY_MARIO,
          Character.BABY_LUIGI,
          Character.BABY_PEACH,
          Character.BABY_DAISY,
          Character.BABY_ROSALINA,
          Character.LEMMY)),
  FEATHERWEIGHT(R.string.featherweight,
      Stats.newBuilder()
          .withAcceleration(3f)
          .withGroundSpeed(2.75f)
          .withAntigravitySpeed(3f)
          .withAirSpeed(2.75f)
          .withWaterSpeed(3.25f)
          .withMiniturbo(2.75f)
          .withGroundHandling(4.25f)
          .withAntigravityHandling(4.5f)
          .withAirHandling(4f)
          .withWaterHandling(4.25f)
          .withTraction(4.25f)
          .withWeight(2.75f)
          .build(),
      Lists.newArrayList(
          Character.TOAD,
          Character.KOOPA_TROOPA,
          Character.SHY_GUY,
          Character.LAKITU,
          Character.TOADETTE,
          Character.LARRY,
          Character.WENDY)),
  LIGHTWEIGHT(R.string.lightweight,
      Stats.newBuilder()
          .withAcceleration(2.75f)
          .withGroundSpeed(3.25f)
          .withAntigravitySpeed(3.5f)
          .withAirSpeed(3.25f)
          .withWaterSpeed(3.75f)
          .withMiniturbo(2.5f)
          .withGroundHandling(3.75f)
          .withAntigravityHandling(4f)
          .withAirHandling(3.5f)
          .withWaterHandling(3.75f)
          .withTraction(4f)
          .withWeight(3.25f)
          .build(),
      Lists.newArrayList(
          Character.PEACH,
          Character.DAISY,
          Character.YOSHI)),
  MIDDLEWEIGHT(R.string.middleweight,
      Stats.newBuilder()
          .withAcceleration(2.5f)
          .withGroundSpeed(3.75f)
          .withAntigravitySpeed(4f)
          .withAirSpeed(3.75f)
          .withWaterSpeed(4.25f)
          .withMiniturbo(2.25f)
          .withGroundHandling(3.25f)
          .withAntigravityHandling(3.5f)
          .withAirHandling(3f)
          .withWaterHandling(3.25f)
          .withTraction(3.75f)
          .withWeight(3.75f)
          .build(),
      Lists.newArrayList(
          Character.MARIO,
          Character.LUIGI,
          Character.IGGY,
          Character.LUDWIG)),
  CRUISERWEIGHT(R.string.cruiserweight,
      Stats.newBuilder()
          .withAcceleration(2.25f)
          .withGroundSpeed(4.25f)
          .withAntigravitySpeed(4.5f)
          .withAirSpeed(4.25f)
          .withWaterSpeed(4.75f)
          .withMiniturbo(2f)
          .withGroundHandling(2.75f)
          .withAntigravityHandling(3f)
          .withAirHandling(2.5f)
          .withWaterHandling(2.75f)
          .withTraction(3.5f)
          .withWeight(4.25f)
          .build(),
      Lists.newArrayList(
          Character.DONKEY_KONG,
          Character.WALUIGI,
          Character.ROSALINA,
          Character.ROY)),
  METALWEIGHT(R.string.metalweight,
      Stats.newBuilder()
          .withAcceleration(2f)
          .withGroundSpeed(4.25f)
          .withAntigravitySpeed(4.5f)
          .withAirSpeed(4.25f)
          .withWaterSpeed(4.75f)
          .withMiniturbo(1.75f)
          .withGroundHandling(2.75f)
          .withAntigravityHandling(3f)
          .withAirHandling(2.5f)
          .withWaterHandling(2.75f)
          .withTraction(3.25f)
          .withWeight(4.75f)
          .build(),
      Lists.newArrayList(
          Character.METAL_MARIO,
          Character.PINK_GOLD_PEACH)),
  HEAVYWEIGHT(R.string.heavyweight,
      Stats.newBuilder()
          .withAcceleration(2f)
          .withGroundSpeed(4.75f)
          .withAntigravitySpeed(5f)
          .withAirSpeed(4.75f)
          .withWaterSpeed(5.25f)
          .withMiniturbo(1.75f)
          .withGroundHandling(2.25f)
          .withAntigravityHandling(2.5f)
          .withAirHandling(2f)
          .withWaterHandling(2.25f)
          .withTraction(3.25f)
          .withWeight(4.75f)
          .build(),
      Lists.newArrayList(
          Character.BOWSER,
          Character.WARIO,
          Character.MORTON));

  private int mDisplayGroupNameResourceId;
  private Stats mStats;
  private List<Character> mCharacters;

  CharacterGroup(int displayGroupNameResourceId, Stats stats, List<Character> characters) {
    mDisplayGroupNameResourceId = displayGroupNameResourceId;
    mStats = stats;
    mCharacters = characters;
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
    return Lists.<HasDisplayNameAndIcon>newArrayList(mCharacters);
  }
}
