package com.blunka.mk8assistant.shared.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.blunka.mk8assistant.data.KartConfiguration;
import com.blunka.mk8assistant.data.parts.Part;
import com.blunka.mk8assistant.data.parts.PartUtils;
import com.blunka.mk8assistant.main.configure.ConfigureModel;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by clocksmith on 7/16/14.
 */
public class StarredBuildUtils {
  private static final String BUILD_NAMESPACE = "namespace$starredBuildLibrary";

  private static final String KEY_SEPERATOR = ":";

  private static final String NAME_SEPERATOR = "-";

  private static SharedPreferences getPreferences(Context context) {
    return context.getSharedPreferences(BUILD_NAMESPACE, Context.MODE_PRIVATE);
  }

  public static void starBuild(Context context, ConfigureModel configureModel) {
    getPreferences(context).edit().putString(getKeyFromBuild(configureModel),
        getNameFromBuild(context, configureModel)).commit();
  }

  public static void unstarBuild(Context context, ConfigureModel configureModel) {
    getPreferences(context).edit().remove(getKeyFromBuild(configureModel)).commit();
  }

  public static boolean isBuildStarred(Context context, ConfigureModel configureModel) {
    return getPreferences(context).contains(getKeyFromBuild(configureModel));
  }

  public static Set<String> getAllStarredKeys(Context context) {
    return getPreferences(context).getAll().keySet();
  }

  public static List<String> getAllStarredKeysSortedByEnum(Context context) {
    List<KartConfiguration> allStarredConfigurations = Lists.newArrayList(Lists.transform(
        Lists.newArrayList(getAllStarredKeys(context)),
        new Function<String, KartConfiguration>() {
          @Override
          public KartConfiguration apply(String key) {
            return getKartConfigurationFromKey(key);
          }
        }
    ));

    Collections.sort(allStarredConfigurations, new Comparator<KartConfiguration>() {
      @Override
      public int compare(KartConfiguration lhs, KartConfiguration rhs) {
        return ComparisonChain.start()
            .compare(lhs.getCharacterGroup().getIndex(), rhs.getCharacterGroup().getIndex())
            .compare(lhs.getVehicleGroup().getIndex(), rhs.getVehicleGroup().getIndex())
            .compare(lhs.getTireGroup().getIndex(), rhs.getTireGroup().getIndex())
            .compare(lhs.getGliderGroup().getIndex(), rhs.getGliderGroup().getIndex())
            .result();
      }
    });

    return Lists.newArrayList(Lists.transform(allStarredConfigurations,
        new Function<KartConfiguration, String>() {
          @Override
          public String apply(KartConfiguration configuration) {
            return getKeyFromConfiguration(configuration);
          }
        }
    ));
  }

  public static String getKeyFromBuild(ConfigureModel configureModel) {
    return getKeyFromConfiguration(configureModel.getKartConfiguration());
  }

  public static String getKeyFromConfiguration(KartConfiguration configuration) {
    if (configuration == null) {
      return null;
    } else {
      return Joiner.on(KEY_SEPERATOR).join(configuration.getCharacterGroup().getName(),
          configuration.getVehicleGroup().getName(),
          configuration.getTireGroup().getName(),
          configuration.getGliderGroup().getName());
    }
  }

  public static String getNameFromBuild(Context context, ConfigureModel configureModel) {
    return getNameFromConfiguration(context, configureModel.getKartConfiguration());
  }

  public static String getNameFromKey(Context context, String key) {
    return getNameFromConfiguration(context, getKartConfigurationFromKey(key));
  }

  public static String getNameFromConfiguration(Context context,
      KartConfiguration configuration) {
    // It is intentional that character is get display name while the rest are get name
    return Joiner.on(NAME_SEPERATOR).join(
        configuration.getCharacterGroup().getDisplayName(),
        configuration.getVehicleGroup().getName(),
        configuration.getTireGroup().getName(),
        configuration.getGliderGroup().getName());
  }

  public static KartConfiguration getKartConfigurationFromKey(String key) {
    if (key == null) {
      return null;
    } else {
      List<String> keyFragments = Lists.newArrayList(Splitter.on(KEY_SEPERATOR).split(key));
      return KartConfiguration.newBuilder()
          .withCharacterGroup(PartUtils.getPartGroup(Part.Type.CHARACTER, keyFragments.get(0)))
          .withVehicleGroup(PartUtils.getPartGroup(Part.Type.VEHICLE, keyFragments.get(1)))
          .withTireGroup(PartUtils.getPartGroup(Part.Type.TIRE, keyFragments.get(2)))
          .withGliderGroup(PartUtils.getPartGroup(Part.Type.GLIDER, keyFragments.get(3)))
          .build();
    }
  }

  public static ConfigureModel getModelFromKey(String key) {
    return new ConfigureModel(getKartConfigurationFromKey(key));
  }
}
