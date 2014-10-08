package com.blunka.mk8assistant.data.courses;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;

/**
 * Created by clocksmith on 9/7/14.
 */
public enum Course implements HasDisplayNameAndIcon {
  // Mushroom Cup.
  MARIO_KART_STADIUM(R.string.mario_kart_stadium,
      R.drawable.wiiu_map_icon_mario_kart_stadium,
      R.drawable.prima_map_mario_kart_stadium),
  WATER_PARK(R.string.water_park,
      R.drawable.wiiu_map_icon_water_park,
      R.drawable.prima_map_water_park),
  SWEET_SWEET_CANYON(R.string.sweet_sweet_canyon,
      R.drawable.wiiu_map_icon_sweet_sweet_canyon,
      R.drawable.prima_map_sweet_sweet_canyon),
  THWOMP_RUINS(R.string.thwomp_ruins,
      R.drawable.wiiu_map_icon_thwomp_ruins,
      R.drawable.prima_map_thwomp_ruins),
  // Flower Cup.
  MARIO_CIRCUIT(R.string.mario_circuit,
      R.drawable.wiiu_map_icon_mario_circuit,
      R.drawable.prima_map_mario_circuit),
  TOAD_HARBOR(R.string.toad_harbor,
      R.drawable.wiiu_map_icon_toad_harbor,
      R.drawable.prima_map_toad_harbor),
  TWISTED_MANSION(R.string.twisted_mansion,
      R.drawable.wiiu_map_icon_twisted_mansion,
      R.drawable.prima_map_twisted_mansion),
  SHY_GUY_FALLS(R.string.shy_guy_falls,
      R.drawable.wiiu_map_icon_shy_guy_falls,
      R.drawable.prima_map_shy_guy_falls),
  // Star Cup.
  SUNSHINE_AIRPORT(R.string.sunshine_airport,
      R.drawable.wiiu_map_icon_sunshine_airport,
      R.drawable.prima_map_sunshine_airport),
  DOLPHIN_SHOALS(R.string.dolphin_shoals,
      R.drawable.wiiu_map_icon_dolphin_shoals,
      R.drawable.prima_map_dolphin_shoals),
  ELECTRODROME(R.string.electrodrome,
      R.drawable.wiiu_map_icon_electrodrome,
      R.drawable.prima_map_electrodrome),
  MOUNT_WARIO(R.string.mount_wario,
      R.drawable.wiiu_map_icon_mount_wario,
      R.drawable.prima_map_mount_wario),
  // Special Cup.
  CLOUDTOP_CRUISE(R.string.cloudtop_cruise,
      R.drawable.wiiu_map_icon_cloudtop_cruise,
      R.drawable.prima_map_cloudtop_cruise),
  BONE_DRY_DUNES(R.string.bone_dry_dunes,
      R.drawable.wiiu_map_icon_bone_dry_dunes,
      R.drawable.prima_map_bone_dry_dunes),
  BOWSERS_CASTLE(R.string.bowsers_castle,
      R.drawable.wiiu_map_icon_bowsers_castle,
      R.drawable.prima_map_bowsers_castle),
  RAINBOW_ROAD(R.string.rainbow_road,
      R.drawable.wiiu_map_icon_rainbow_road,
      R.drawable.prima_map_rainbow_road),
  // Shell Cup.
  MOO_MOO_MEADOWS(R.string.moo_moo_meadows,
      R.drawable.wiiu_map_icon_moo_moo_meadows,
      R.drawable.prima_map_moo_moo_meadows),
  MARIO_CIRCUIT_GBA(R.string.mario_circuit_gba,
      R.drawable.wiiu_map_icon_mario_circuit_gba,
      R.drawable.prima_map_mario_circuit_gba),
  CHEEP_CHEEP_BEACH(R.string.cheep_cheep_beach,
      R.drawable.wiiu_map_icon_cheep_cheep_beach,
      R.drawable.prima_map_cheep_cheep_beach),
  TOADS_TURNPIKE(R.string.toads_turnpike,
      R.drawable.wiiu_map_icon_toads_turnpike,
      R.drawable.prima_map_toads_turnpike),
  // Banana Cup.
  DRY_DRY_DESERT(R.string.dry_dry_desert,
      R.drawable.wiiu_map_icon_dry_dry_desert,
      R.drawable.prima_map_dry_dry_desert),
  DONUT_PLAINS_3(R.string.donut_plains_3,
      R.drawable.wiiu_map_icon_donut_plains_3,
      R.drawable.prima_map_donut_plains_3),
  ROYAL_RACEWAY(R.string.royal_raceway,
      R.drawable.wiiu_map_icon_royal_raceway,
      R.drawable.prima_map_royal_raceway),
  DK_JUNGLE(R.string.dk_jungle,
      R.drawable.wiiu_map_icon_dk_jungle,
      R.drawable.prima_map_dk_jungle),
  // Leaf Cup.
  WARIO_STADIUM(R.string.wario_stadium,
      R.drawable.wiiu_map_icon_wario_stadium,
      R.drawable.prima_map_wario_stadium),
  SHERBET_LAND(R.string.sherbet_land,
      R.drawable.wiiu_map_icon_sherbet_land,
      R.drawable.prima_map_sherbet_land),
  MUSIC_PARK(R.string.music_park,
      R.drawable.wiiu_map_icon_music_park,
      R.drawable.prima_map_music_park),
  YOSHI_VALLEY(R.string.yoshi_valley,
      R.drawable.wiiu_map_icon_yoshi_valley,
      R.drawable.prima_map_yoshi_valley),
  // Lightning Cup.
  TICK_TOCK_CLOCK(R.string.tick_tock_clock,
      R.drawable.wiiu_map_icon_tick_tock_clock,
      R.drawable.prima_map_tick_tock_clock),
  PIRANHA_PLANT_SLIDE(R.string.piranha_plant_slide,
      R.drawable.wiiu_map_icon_piranha_plant_slide,
      R.drawable.prima_map_piranha_plant_slide),
  GRUMBLE_VOLCANO(R.string.grumble_volcano,
      R.drawable.wiiu_map_icon_grumble_volcano,
      R.drawable.prima_map_grumble_volcano),
  RAINBOW_ROAD_N64(R.string.rainbow_road,
      R.drawable.wiiu_map_icon_rainbow_road_n64,
      R.drawable.prima_map_rainbow_road_n64);

  private int mDisplayNameResourceId;
  private int mIconResourceId;
  private int mMapResourceId;

  Course(int displayNameResourceId, int iconResourceId, int mapResourceId) {
    mDisplayNameResourceId = displayNameResourceId;
    mIconResourceId = iconResourceId;
    mMapResourceId = mapResourceId;
  }

  public String getDisplayName(Context context) {
    return context.getString(mDisplayNameResourceId);
  }

  public int getDisplayNameResourceId() {
    return mDisplayNameResourceId;
  }

  public int getIconResourceId() {
    return  mIconResourceId;
  }

  public int getMapResourceId() {
    return mMapResourceId;
  }
}
