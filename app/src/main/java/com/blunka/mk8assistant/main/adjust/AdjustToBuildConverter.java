package com.blunka.mk8assistant.main.adjust;

import android.util.Log;

import com.blunka.mk8assistant.data.AdjustConfiguration;
import com.blunka.mk8assistant.data.AllKartConfigurations;
import com.blunka.mk8assistant.data.Attribute;
import com.blunka.mk8assistant.data.KartConfiguration;
import com.blunka.mk8assistant.data.Stats;
import com.blunka.mk8assistant.main.configure.ConfigureModel;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.Flags;
import com.blunka.mk8assistant.shared.preferences.StarredBuildUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by clocksmith on 7/14/14.
 */
public class AdjustToBuildConverter {
  private static final String TAG = AdjustToBuildConverter.class.getSimpleName();

  private AllKartConfigurations mAllKartConfigurations;

  public AdjustToBuildConverter() {
    mAllKartConfigurations = new AllKartConfigurations();
  }

  public ConfigureModel convert(AdjustModel adjustModel) {
    KartConfiguration kartConfiguration = getOptimalKartConfiguration(
        adjustModel.getSelectedConfiguration());
    if (kartConfiguration == null) {
      kartConfiguration = new KartConfiguration();
    }
    ConfigureModel configureModel = new ConfigureModel();
    configureModel.setKartConfiguration(kartConfiguration);
    return configureModel;
  }

  private KartConfiguration getOptimalKartConfiguration(
      AdjustConfiguration adjustConfiguration) {
    KartConfiguration optimalKartConfiguration = null;
    float maxScore = 0;
    for (KartConfiguration kartConfiguration : mAllKartConfigurations.getKartConfigurations()) {
      float score = calculateScore(adjustConfiguration, kartConfiguration);
      if (score > maxScore) {
        maxScore = score;
        optimalKartConfiguration = kartConfiguration;
      }
    }
    return optimalKartConfiguration;
  }

  private float calculateScore(AdjustConfiguration adjustConfiguration,
      KartConfiguration kartConfiguration) {
    Stats kartStats = kartConfiguration.getKartStats();
    float score = 0;
    for (Attribute attribute : Attribute.getSimpleValues()) {
      score += getWeightedValue(adjustConfiguration, kartStats, attribute);
    }
    return score;
  }

  private float getWeightedValue(AdjustConfiguration adjustConfiguration,
      Stats kartStats,
      Attribute attribute) {
    double adjustValue = adjustConfiguration.getAttributeValue(attribute);
    double kartStatValue = kartStats.getAttributeValue(attribute);
    if (attribute == Attribute.ACCELERATION) {
      // Floor the acceleration to reflect actual game mechanics.
      kartStatValue = Math.floor(kartStatValue);
    }
    return (float) (adjustValue * kartStatValue);
  }

  public void runTest() {
    FilteredLogger.d(TAG, "Starting test...");
    Map<String, List<String>> items = Maps.newHashMap();
    AdjustConfiguration config = new AdjustModel().getCustomConfiguration();
    float interval = 100f / (Flags.NUM_TEST_INTERVALS - 1);
    int accelerationInt;
    int speedInt;
    int handlingInt;
    int miniturboInt;
    int tractionInt;
    int weightInt;
    for (float acceleration = 0; acceleration <= 100; acceleration += interval) {
      accelerationInt = (int) (acceleration + 0.5f);
      Log.d(TAG, "accelerationInt: " + accelerationInt);
      config.setAttributeValue(Attribute.ACCELERATION, accelerationInt);
      for (float speed = 0; speed <= 100; speed += interval) {
        speedInt = (int) (speed + 0.5f);
        Log.d(TAG, "speedInt: " + speedInt);
        config.setAttributeValue(Attribute.AVERAGE_SPEED, speedInt);
        for (float handling = 0; handling <= 100; handling += interval) {
          handlingInt = (int) (handling + 0.5f);
          config.setAttributeValue(Attribute.AVERAGE_HANDLING, handlingInt);
          for (float miniturbo = 0; miniturbo <= 100; miniturbo += interval) {
            miniturboInt = (int) (miniturbo + 0.5f);
            config.setAttributeValue(Attribute.MINITURBO, miniturboInt);
            for (float traction = 0; traction <= 100; traction += interval) {
              tractionInt = (int) (traction + 0.5f);
              config.setAttributeValue(Attribute.TRACTION, tractionInt);
              for (float weight = 0; weight <= 100; weight += interval) {
                weightInt = (int) (weight + 0.5f);
                config.setAttributeValue(Attribute.WEIGHT, weightInt);
                KartConfiguration optimalKartConfiguration = getOptimalKartConfiguration(config);
                if (optimalKartConfiguration != null) {
                  String kartCombinationString =
                      StarredBuildUtils.getKeyFromConfiguration(optimalKartConfiguration);
                  String modelString = (accelerationInt + "," +
                      speedInt + "," +
                      handlingInt + "," +
                      miniturboInt + "," +
                      tractionInt + "," +
                      weightInt);
                  if (items.containsKey(kartCombinationString)) {
                    items.get(kartCombinationString).add(modelString);
                  } else {
                    items.put(kartCombinationString, Lists.newArrayList(modelString));
                  }
                }
              }
            }
          }
        }
      }
    }

    Random r = new Random();
    for (Map.Entry<String, List<String>> entry : items.entrySet()) {
      Log.d(TAG, entry.getKey() + ": " + entry.getValue().size());
      Log.d(TAG, entry.getValue().get(r.nextInt(entry.getValue().size())));
      Log.d(TAG, entry.getValue().get(r.nextInt(entry.getValue().size())));
      Log.d(TAG, entry.getValue().get(r.nextInt(entry.getValue().size())));
    }
  }
}
