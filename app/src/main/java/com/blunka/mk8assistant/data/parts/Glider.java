package com.blunka.mk8assistant.data.parts;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;

/**
 * Created by clocksmith on 7/3/14.
 *
 * There are 12 gliders.
 */
public enum Glider implements HasDisplayNameAndIcon {
  SUPER_GLIDER("Super Glider", R.drawable.wiiu_glider_super_glider),
  WARIO_WING("Wario Wing", R.drawable.wiiu_glider_wario_wing),
  WADDLE_WING("Waddle Wing", R.drawable.wiiu_glider_waddle_wing),
  PLANE_GLIDER("Plane Glider", R.drawable.wiiu_glider_plane_glider),
  GOLD_GLIDER("Gold Glider", R.drawable.wiiu_glider_gold_glider),

  CLOUD_GLIDER("Cloud Glider", R.drawable.wiiu_glider_cloud_glider),
  PEACH_PARASOL("Peach Parasol", R.drawable.wiiu_glider_peach_parasol),
  PARACHUTE("Parachute", R.drawable.wiiu_glider_parachute),
  PARAFOIL("Parafoil", R.drawable.wiiu_glider_parafoil),
  FLOWER_GLIDER("Flower Glider", R.drawable.wiiu_glider_flower_glider),
  BOWSER_KITE("Bowser Kite", R.drawable.wiiu_glider_bowser_kite),
  MKTV_PARAFOIL("MKTV Parafoil", R.drawable.wiiu_glider_mktv_parafoil);

  private String mDisplayName;
  private int mResourceId;

  Glider(String displayName, int resourceId) {
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
