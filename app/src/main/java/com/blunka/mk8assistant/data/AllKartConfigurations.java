package com.blunka.mk8assistant.data;

import com.blunka.mk8assistant.data.parts.CharacterGroup;
import com.blunka.mk8assistant.data.parts.GliderGroup;
import com.blunka.mk8assistant.data.parts.TireGroup;
import com.blunka.mk8assistant.data.parts.VehicleGroup;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 7/6/14.
 */
public class AllKartConfigurations {
  private List<KartConfiguration> mKartConfigurations;

  public AllKartConfigurations() {
    mKartConfigurations = Lists.newArrayList();
    for (CharacterGroup characterGroup : CharacterGroup.values()) {
      for (VehicleGroup vehicleGroup : VehicleGroup.values()) {
        for (TireGroup tireGroup : TireGroup.values()) {
          for (GliderGroup gliderGroup : GliderGroup.values()) {
            mKartConfigurations.add(KartConfiguration.newBuilder()
                .withCharacterGroup(characterGroup)
                .withVehicleGroup(vehicleGroup)
                .withTireGroup(tireGroup)
                .withGliderGroup(gliderGroup)
                .build());
          }
        }
      }
    }
  }

  public List<KartConfiguration> getKartConfigurations() {
    return mKartConfigurations;
  }
}
