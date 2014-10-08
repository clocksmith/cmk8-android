package com.blunka.mk8assistant.data.courses;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 9/7/14.
 */
public enum Cup implements HasDisplayNameAndIcon {
  MUSHROOM(R.string.mushroom_cup,
      R.drawable.wiiu_cup_mushroom,
      Lists.newArrayList(
          Course.MARIO_KART_STADIUM,
          Course.WATER_PARK,
          Course.SWEET_SWEET_CANYON,
          Course.THWOMP_RUINS)),
  FLOWER(R.string.flower_cup,
      R.drawable.wiiu_cup_flower,
      Lists.newArrayList(
          Course.MARIO_CIRCUIT,
          Course.TOAD_HARBOR,
          Course.TWISTED_MANSION,
          Course.SHY_GUY_FALLS)),
  STAR(R.string.star_cup,
      R.drawable.wiiu_cup_star,
      Lists.newArrayList(
          Course.SUNSHINE_AIRPORT,
          Course.DOLPHIN_SHOALS,
          Course.ELECTRODROME,
          Course.MOUNT_WARIO)),
  SPECIAL(R.string.special_cup,
      R.drawable.wiiu_cup_special,
      Lists.newArrayList(
          Course.CLOUDTOP_CRUISE,
          Course.BONE_DRY_DUNES,
          Course.BOWSERS_CASTLE,
          Course.RAINBOW_ROAD)),
  SHELL(R.string.shell_cup,
      R.drawable.wiiu_cup_shell,
      Lists.newArrayList(
          Course.MOO_MOO_MEADOWS,
          Course.MARIO_CIRCUIT_GBA,
          Course.CHEEP_CHEEP_BEACH,
          Course.TOADS_TURNPIKE)),
  BANANA(R.string.banana_cup,
      R.drawable.wiiu_cup_banana,
      Lists.newArrayList(
          Course.DRY_DRY_DESERT,
          Course.DONUT_PLAINS_3,
          Course.ROYAL_RACEWAY,
          Course.DK_JUNGLE)),
  LEAF(R.string.leaf_cup,
      R.drawable.wiiu_cup_leaf,
      Lists.newArrayList(
          Course.WARIO_STADIUM,
          Course.SHERBET_LAND,
          Course.MUSIC_PARK,
          Course.YOSHI_VALLEY)),
  LIGHTNING(R.string.lightning_cup,
      R.drawable.wiiu_cup_lightning,
      Lists.newArrayList(
          Course.TICK_TOCK_CLOCK,
          Course.PIRANHA_PLANT_SLIDE,
          Course.GRUMBLE_VOLCANO,
          Course.RAINBOW_ROAD_N64));

  private int mDisplayNameResourceId;
  private int mIconResourceId;
  private List<Course> mCourses;

  Cup(int displayNameResourceId, int iconResourceId, List<Course> courses) {
    mDisplayNameResourceId = displayNameResourceId;
    mIconResourceId = iconResourceId;
    mCourses = courses;
  }

  public String getDisplayName(Context context) {
    return context.getString(mDisplayNameResourceId);
  }

  public int getDisplayNameResourceId() {
    return mDisplayNameResourceId;
  }

  public int getIconResourceId() {
    return mIconResourceId;
  }

  public List<Course> getCourses() {
    return mCourses;
  }
}
