package com.blunka.mk8assistant.data.parts;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;

/**
 * Created by clocksmith on 8/8/14.
 *
 * There are 29 characters.
 */
public enum Character implements HasDisplayNameAndIcon {
  BABY_MARIO(R.string.baby_mario, R.drawable.wiiu_character_baby_mario),
  BABY_LUIGI(R.string.baby_luigi, R.drawable.wiiu_character_baby_luigi),
  BABY_PEACH(R.string.baby_peach, R.drawable.wiiu_character_baby_peach),
  BABY_DAISY(R.string.baby_daisy, R.drawable.wiiu_character_baby_daisy),
  BABY_ROSALINA(R.string.baby_rosalina, R.drawable.wiiu_character_baby_rosalina),
  LEMMY(R.string.lemmy, R.drawable.wiiu_character_lemmy),

  TOAD(R.string.toad, R.drawable.wiiu_character_toad),
  KOOPA_TROOPA(R.string.koopa_troopa,  R.drawable.wiiu_character_koopa_troopa),
  SHY_GUY(R.string.shy_guy, R.drawable.wiiu_character_shy_guy),
  LAKITU(R.string.lakitu, R.drawable.wiiu_character_lakitu),
  TOADETTE(R.string.toadette, R.drawable.wiiu_character_toadette),
  LARRY(R.string.larry, R.drawable.wiiu_character_larry),
  WENDY(R.string.wendy, R.drawable.wiiu_character_wendy),

  PEACH(R.string.peach, R.drawable.wiiu_character_peach),
  DAISY(R.string.daisy, R.drawable.wiiu_character_daisy),
  YOSHI(R.string.yoshi, R.drawable.wiiu_character_yoshi),

  MARIO(R.string.mario, R.drawable.wiiu_character_mario),
  LUIGI(R.string.luigi, R.drawable.wiiu_character_luigi),
  IGGY(R.string.iggy, R.drawable.wiiu_character_iggy),
  LUDWIG(R.string.ludwig, R.drawable.wiiu_character_ludwig),

  DONKEY_KONG(R.string.donkey_kong, R.drawable.wiiu_character_donkey_kong),
  WALUIGI(R.string.waluigi, R.drawable.wiiu_character_waluigi),
  ROSALINA(R.string.rosalina, R.drawable.wiiu_character_rosalina),
  ROY(R.string.roy, R.drawable.wiiu_character_roy),

  METAL_MARIO(R.string.metal_mario, R.drawable.wiiu_character_metal_mario),
  PINK_GOLD_PEACH(R.string.pink_gold_peach, R.drawable.wiiu_character_pink_gold_peach),

  BOWSER(R.string.bowser, R.drawable.wiiu_character_bowser),
  WARIO(R.string.wario, R.drawable.wiiu_character_wario),
  MORTON(R.string.morton, R.drawable.wiiu_character_morton);

  private int mDisplayNameResourceId;
  private int mIconResourceId;

  Character(int displayNameResourceId, int iconResourceId) {
    mDisplayNameResourceId = displayNameResourceId;
    mIconResourceId = iconResourceId;
  }

  @Override
  public String getDisplayName(Context context) {
    return context.getString(mDisplayNameResourceId);
  }

  @Override
  public int getIconResourceId() {
    return mIconResourceId;
  }
}
