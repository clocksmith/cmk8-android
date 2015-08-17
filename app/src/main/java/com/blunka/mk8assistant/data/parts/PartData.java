package com.blunka.mk8assistant.data.parts;

import android.content.Context;
import android.util.Log;

import com.blunka.mk8assistant.data.JsonUtils;
import com.blunka.mk8assistant.data.Stats;
import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by clocksmith on 8/16/15.
 */
public class PartData {
  private static final String TAG = PartData.class.getSimpleName();

  public static final List<PartGroup> CHARACTER_GROUPS = Lists.newArrayList();
  public static final List<PartGroup> VEHICLE_GROUPS = Lists.newArrayList();
  public static final List<PartGroup> TIRE_GROUPS = Lists.newArrayList();
  public static final List<PartGroup> GLIDER_GROUPS = Lists.newArrayList();

  public static void init(Context context) throws JSONException {
    Log.d(TAG, "init");
    JSONObject partsObj = JsonUtils.loadJsonFromAssets(context, "data/parts.json");
    initPartGroupList(context, partsObj.getJSONArray("character_groups"), CHARACTER_GROUPS, Part.Type.CHARACTER);
    initPartGroupList(context, partsObj.getJSONArray("vehicle_groups"), VEHICLE_GROUPS, Part.Type.VEHICLE);
    initPartGroupList(context, partsObj.getJSONArray("tire_groups"), TIRE_GROUPS, Part.Type.TIRE);
    initPartGroupList(context, partsObj.getJSONArray("glider_groups"), GLIDER_GROUPS, Part.Type.GLIDER);
  }

  private static void initPartGroupList(Context context, JSONArray in, List<PartGroup> out, Part.Type partType)
      throws JSONException {
    Log.d(TAG, "initPartGroupList: " + partType.name());
    // Just in case...
    out.clear();

    for (int i = 0; i < in.length(); i++) {
      Log.d(TAG, "adding part: " + i);
      JSONObject partGroupJsonObj = in.getJSONObject(i);

      List<Part> parts = Lists.newArrayList();
      JSONArray partsJsonArray = partGroupJsonObj.getJSONArray(partType.name().toLowerCase() + "s");
      for (int j = 0; j < partsJsonArray.length(); j++) {
        parts.add(new Part(context, partsJsonArray.getString(j), partType));
      }

      JSONObject statsJsonObj = partGroupJsonObj.getJSONObject("stats");
      PartGroup partGroup = new PartGroup(
          context,
          partGroupJsonObj.getString("name"),
          Stats.newBuilder()
              .withAcceleration(statsJsonObj.getDouble("acceleration"))
              .withGroundSpeed(statsJsonObj.getDouble("ground_speed"))
              .withAntigravitySpeed(statsJsonObj.getDouble("antigravity_speed"))
              .withAirSpeed(statsJsonObj.getDouble("air_speed"))
              .withWaterSpeed(statsJsonObj.getDouble("water_speed"))
              .withMiniturbo(statsJsonObj.getDouble("miniturbo"))
              .withGroundHandling(statsJsonObj.getDouble("ground_handling"))
              .withAntigravityHandling(statsJsonObj.getDouble("antigravity_handling"))
              .withAirHandling(statsJsonObj.getDouble("air_handling"))
              .withWaterHandling(statsJsonObj.getDouble("water_handling"))
              .withTraction(statsJsonObj.getDouble("traction"))
              .withWeight(statsJsonObj.getDouble("weight"))
              .build(),
          parts,
          partType,
          i);

      out.add(partGroup);
    }
  }
}
