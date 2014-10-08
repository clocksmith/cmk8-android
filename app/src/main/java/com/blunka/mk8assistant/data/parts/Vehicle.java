package com.blunka.mk8assistant.data.parts;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;

/**
 * Created by clocksmith on 7/2/14.
 *
 * There are 26 vehicles.
 */
public enum Vehicle implements HasDisplayNameAndIcon {
  STANDARD_KART("Standard Kart", R.drawable.wiiu_vehicle_standard_kart),
  CAT_CRUISER("Cat Cruiser", R.drawable.wiiu_vehicle_cat_cruiser),
  PRANCER("Prancer", R.drawable.wiiu_vehicle_prancer),
  SNEEKER("Sneeker", R.drawable.wiiu_vehicle_sneeker),
  THE_DUKE("The Duke", R.drawable.wiiu_vehicle_the_duke),
  TEDDY_BUGGY("Teddy Buggy", R.drawable.wiiu_vehicle_teddy_buggy),
  THREE_HUNDRED_SL_ROADSTER("300 SL Roadster", R.drawable.wiiu_vehicle_300_sl_roadster),

  PIPE_FRAME("Pipe Frame", R.drawable.wiiu_vehicle_pipe_frame),
  STANDARD_BIKE("Standard Bike", R.drawable.wiiu_vehicle_standard_bike),
  FLAME_RIDER("Flame Rider", R.drawable.wiiu_vehicle_flame_rider),
  VARMINT("Varmint", R.drawable.wiiu_vehicle_varmint),
  WILD_WIGGLER("Wild Wiggler", R.drawable.wiiu_vehicle_wild_wiggler),
  W_25_SILVER_ARROW("W 25 Silver Arrow", R.drawable.wiiu_vehicle_w_25_silver_arrow),

  MACH_8("Mach 8", R.drawable.wiiu_vehicle_mach_8),
  CIRCUIT_SPECIAL("Circuit Special", R.drawable.wiiu_vehicle_circuit_special),
  SPORTS_COUPE("Sports Coupe", R.drawable.wiiu_vehicle_sports_coupe),
  GOLD_STANDARD("Gold Standard", R.drawable.wiiu_vehicle_gold_standard),

  STEEL_DRIVER("Steel Driver", R.drawable.wiiu_vehicle_steel_driver),
  TRI_SPEEDER("Tri-Speeder", R.drawable.wiiu_vehicle_tri_speeder),
  BADWAGON("Badwagon", R.drawable.wiiu_vehicle_badwagon),
  STANDARD_ATV("Standard ATV", R.drawable.wiiu_vehicle_standard_atv),
  GLA("GLA", R.drawable.wiiu_vehicle_gla),

  BIDDYBUGGY("Biddybuggy", R.drawable.wiiu_vehicle_biddybuggy),
  LANDSHIP("Landship", R.drawable.wiiu_vehicle_landship),
  MR_SCOOTY("Mr. Scooty", R.drawable.wiiu_vehicle_mr_scooty),

  COMET("Comet", R.drawable.wiiu_vehicle_comet),
  SPORT_BIKE("Sport Bike", R.drawable.wiiu_vehicle_sport_bike),
  JET_BIKE("Jet Bike", R.drawable.wiiu_vehicle_jet_bike),
  YOSHI_BIKE("Yoshi Bike", R.drawable.wiiu_vehicle_yoshi_bike);

  private String mDisplayName;
  private int mResourceId;

  Vehicle(String displayName, int resourceId) {
    mDisplayName = displayName;
    mResourceId = resourceId;
  }

  @Override
  public String getDisplayName(Context context) {
    return mDisplayName;
  }

  @Override
  public int getIconResourceId() {
    return mResourceId;
  }
}