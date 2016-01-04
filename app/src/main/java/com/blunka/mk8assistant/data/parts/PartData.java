package com.blunka.mk8assistant.data.parts;

import android.content.Context;
import android.util.Log;

import com.blunka.mk8assistant.data.JsonUtils;
import com.blunka.mk8assistant.data.Stats;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by clocksmith on 8/16/15.
 */
public class PartData {
  private static final String TAG = PartData.class.getSimpleName();

  public static final Map<PartType, LinkedHashMap<String, PartGroup>> PART_TYPE_TO_PART_GROUPS_MAP = Maps.newHashMap();
  public static final Map<String, PartGroup> PART_NAME_TO_PART_GROUP_MAP = Maps.newHashMap();

  public static final LinkedHashMap<String, PartGroup> CHARACTER_GROUPS = Maps.newLinkedHashMap();
  public static final LinkedHashMap<String, PartGroup> VEHICLE_GROUPS = Maps.newLinkedHashMap();
  public static final LinkedHashMap<String, PartGroup> TIRE_GROUPS = Maps.newLinkedHashMap();
  public static final LinkedHashMap<String, PartGroup> GLIDER_GROUPS = Maps.newLinkedHashMap();

  public static void init(Context context) throws JSONException {
    Log.d(TAG, "init");
    JSONObject partsObj = JsonUtils.loadJsonFromAssets(context, "data/parts.json");
    initPartGroupList(context, partsObj.getJSONArray("character_groups"), CHARACTER_GROUPS, PartType.CHARACTER);
    initPartGroupList(context, partsObj.getJSONArray("vehicle_groups"), VEHICLE_GROUPS, PartType.VEHICLE);
    initPartGroupList(context, partsObj.getJSONArray("tire_groups"), TIRE_GROUPS, PartType.TIRE);
    initPartGroupList(context, partsObj.getJSONArray("glider_groups"), GLIDER_GROUPS, PartType.GLIDER);

    FilteredLogger.d(TAG, "CHARACTER_GROUPS: " + CHARACTER_GROUPS.toString());
  }

  public static PartGroup getPartGroup(Part part) {
    return PART_NAME_TO_PART_GROUP_MAP.get(part.getName());
  }


  public static PartGroup getPartGroup(PartType partType, String name) {
    return PART_TYPE_TO_PART_GROUPS_MAP.get(partType).get(name);
  }

  private static void initPartGroupList(
      Context context,
      JSONArray in,
      LinkedHashMap<String, PartGroup> out,
      PartType partType) throws JSONException {
    Log.d(TAG, "initPartGroupList: " + partType.name());

    // Just in case this was already called...
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
      String partGroupName = partGroupJsonObj.getString("name");
      PartGroup partGroup = new PartGroup(
          context,
          partGroupName,
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

      out.put(partGroupName, partGroup);

      for (Part part : partGroup.getParts()) {
        PART_NAME_TO_PART_GROUP_MAP.put(part.getName(), partGroup);
      }
    }

    PART_TYPE_TO_PART_GROUPS_MAP.put(partType, out);
  }
}
