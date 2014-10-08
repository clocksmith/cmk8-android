package com.blunka.mk8assistant.shared.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.blunka.mk8assistant.main.adjust.AdjustModel;
import com.blunka.mk8assistant.main.configure.ConfigureModel;
import com.blunka.mk8assistant.main.compare.CompareModel;
import com.blunka.mk8assistant.main.maps.MapsModel;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.Model;
import com.google.gson.GsonBuilder;

/**
 * Created by clocksmith on 7/18/14.
 */
public class LastUsedModelUtils {
  private static final String TAG = LastUsedModelUtils.class.getSimpleName();

  private static final String LAST_USED_MODEL_NAMESPACE = "namespace$lastUsedModel";

  private static final String LAST_USED_ADJUST_MODEL_KEY = "lastUsedAdjustModelKey";

  private static final String LAST_USED_BUILD_MODEL_KEY = "lastUsedBuildModelKey";

  private static final String LAST_USED_COMPARE_MODEL_KEY = "lastUsedCompareModelKey";

  private static final String LAST_USED_MAPS_MODEL_KEY = "lastUsedMapsModelKey";

  public static AdjustModel getLastUsedAdjustModel(Context context) {
    return (AdjustModel) getLastUsedModel(context, LAST_USED_ADJUST_MODEL_KEY);
  }

  public static ConfigureModel getLastUsedBuildModel(Context context) {
    return (ConfigureModel) getLastUsedModel(context, LAST_USED_BUILD_MODEL_KEY);
  }

  public static CompareModel getLastUsedCompareModel(Context context) {
    return (CompareModel) getLastUsedModel(context, LAST_USED_COMPARE_MODEL_KEY);
  }

  public static MapsModel getLastUsedMapsModel(Context context) {
    MapsModel mapsModel = (MapsModel) getLastUsedModel(context, LAST_USED_MAPS_MODEL_KEY);
    if (mapsModel == null) {
      FilteredLogger.d(TAG, "mapsModel is null");
    } else {
      FilteredLogger.d(TAG, "getLastUsedMapsModel cup: " + mapsModel.getCup().name() + " course: " + mapsModel.getCourse().name());
    }
    return (MapsModel) getLastUsedModel(context, LAST_USED_MAPS_MODEL_KEY);
  }

  public static void setLastUsedAdjustModel(Context context, AdjustModel adjustModel) {
    setLastUsedModel(context, adjustModel);
  }

  public static void setLastUsedBuildModel(Context context, ConfigureModel configureModel) {
    setLastUsedModel(context, configureModel);
  }

  public static void setLastUsedCompareModel(Context context, CompareModel compareModel) {
    setLastUsedModel(context, compareModel);
  }

  public static void setLastUsedMapsModel(Context context, MapsModel mapsModel) {
    FilteredLogger.d(TAG, "setLastUsedMapsModel cup: " + mapsModel.getCup().name() + " course: " + mapsModel.getCourse().name());
    setLastUsedModel(context, mapsModel);
  }

  private static Model getLastUsedModel(Context context, String modelKey) {
    String jsonString = getPreferences(context).getString(modelKey, "");
    return jsonString.equals("") ? null :
        (Model) new GsonBuilder().create().fromJson(jsonString, getClassForKey(modelKey));
  }

  private static void setLastUsedModel(Context context, Model model) {
    getPreferences(context).edit().putString(getKeyForModel(model), model == null ?
        "" : new GsonBuilder().create().toJson(model)).commit();
  }

  private static String getKeyForModel(Model model) {
    if (model instanceof AdjustModel) {
      return LAST_USED_ADJUST_MODEL_KEY;
    } else if (model instanceof ConfigureModel) {
      return LAST_USED_BUILD_MODEL_KEY;
    } else if (model instanceof CompareModel) {
      return LAST_USED_COMPARE_MODEL_KEY;
    } else if (model instanceof MapsModel) {
      return LAST_USED_MAPS_MODEL_KEY;
    } else {
      return null;
    }
  }

  private static Class getClassForKey(String modelKey) {
    if (modelKey.equals(LAST_USED_ADJUST_MODEL_KEY)) {
      return AdjustModel.class;
    } else if (modelKey.equals(LAST_USED_BUILD_MODEL_KEY)) {
      return ConfigureModel.class;
    } else if (modelKey.equals(LAST_USED_COMPARE_MODEL_KEY)) {
      return CompareModel.class;
    } else if (modelKey.equals(LAST_USED_MAPS_MODEL_KEY)) {
      return MapsModel.class;
    } else {
      return null;
    }
  }

  private static SharedPreferences getPreferences(Context context) {
    return context.getSharedPreferences(LAST_USED_MODEL_NAMESPACE, Context.MODE_PRIVATE);
  }
}
