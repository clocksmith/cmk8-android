package com.blunka.mk8assistant.data.parts;

import android.util.Log;

import com.google.common.collect.Iterables;

import java.util.List;

/**
 * Created by clocksmith on 8/16/15.
 */
public class PartUtils {
  private static final String TAG = PartUtils.class.getSimpleName();

  public static PartGroup getPartGroup(Part part) {
    for (PartGroup aPartGroup : Iterables.concat(
        PartData.CHARACTER_GROUPS,
        PartData.VEHICLE_GROUPS,
        PartData.TIRE_GROUPS,
        PartData.GLIDER_GROUPS)) {
      for (Part aPart : aPartGroup.getParts()) {
        if (aPart.getIconResId() == part.getIconResId()) {
          return aPartGroup;
        }
      }
    }
    return null;
  }

  public static PartGroup getPartGroup(Part.Type partType, String name) {
    switch (partType) {
      case CHARACTER:
        return getPartGroup(PartData.CHARACTER_GROUPS, name);
      case VEHICLE:
        return getPartGroup(PartData.VEHICLE_GROUPS, name);
      case TIRE:
        return getPartGroup(PartData.TIRE_GROUPS, name);
      case GLIDER:
        return getPartGroup(PartData.GLIDER_GROUPS, name);
      default:
        Log.e(TAG, "Could not get part group for partType: " + partType.name() + " and name: " + name);
        return null;
    }
  }

  private static PartGroup getPartGroup(List<PartGroup> partGroups, String name) {
    for (PartGroup partGroup : partGroups) {
      if (partGroup.getName().equals(name)) {
        return partGroup;
      }
    }
    Log.e(TAG, "Could not get part group for name: " + name);
    return null;
  }
}
