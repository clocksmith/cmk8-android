package com.blunka.mk8assistant.data.parts;

import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Created by clocksmith on 7/23/14.
 */
public class PartsUtils {
  // These is needed because we can't have enums with cyclical references.
  public static PartGroup getPartGroup(HasDisplayNameAndIcon part) {
    for (PartGroup partGroup : Iterables.concat(Lists.newArrayList(CharacterGroup.values()),
        Lists.newArrayList(VehicleGroup.values()),
        Lists.newArrayList(TireGroup.values()),
        Lists.newArrayList(GliderGroup.values()))) {
      if (partGroup.getParts().contains(part)) {
        return partGroup;
      }
    }
    return null;
  }
}
