package com.blunka.mk8assistant.data.parts;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;

/**
 * Created by clocksmith on 7/3/14.
 *
 * There are 18 tires.
 */
public enum Tire implements HasDisplayNameAndIcon {
  STANDARD("Standard", R.drawable.wiiu_tire_standard),
  OFF_ROAD("Off-Road", R.drawable.wiiu_tire_off_road),
  BLUE_STANDARD("Blue Standard", R.drawable.wiiu_tire_blue_standard),
  RETRO_OFF_ROAD("Retro Off-Road", R.drawable.wiiu_tire_retro_off_road),
  GLA_TIRES("GLA Tires", R.drawable.wiiu_tire_gla_tires),

  MONSTER("Monster", R.drawable.wiiu_tire_monster),
  HOT_MONSTER("Hot Monster", R.drawable.wiiu_tire_hot_monster),

  ROLLER("Roller", R.drawable.wiiu_tire_roller),
  BUTTON("Button", R.drawable.wiiu_tire_button),
  AZURE_ROLLER("Azure Roller", R.drawable.wiiu_tire_azure_roller),

  SLIM("Slim", R.drawable.wiiu_tire_slim),
  CRIMSON_SLIM("Crimson Slim", R.drawable.wiiu_tire_crimson_slim),

  SLICK("Slick", R.drawable.wiiu_tire_slick),
  CYBER_SLICK("Cyber Slick", R.drawable.wiiu_tire_cyber_slick),

  METAL("Metal", R.drawable.wiiu_tire_metal),
  GOLD_TIRES("Gold Tires", R.drawable.wiiu_tire_gold_tires),

  SPONGE("Sponge", R.drawable.wiiu_tire_sponge),
  WOOD("Wood", R.drawable.wiiu_tire_wood),
  CUSHION("Cushion", R.drawable.wiiu_tire_cushion);

  private String mDisplayName;
  private int mResourceId;

  Tire(String displayName, int resourceId) {
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