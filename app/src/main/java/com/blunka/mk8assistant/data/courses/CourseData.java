package com.blunka.mk8assistant.data.courses;

import android.content.Context;
import android.util.Log;

import com.blunka.mk8assistant.data.JsonUtils;
import com.blunka.mk8assistant.shared.Constants;
import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by clocksmith on 8/16/15.
 */
public class CourseData {
  private static final String TAG = CourseData.class.getSimpleName();

  public static final List<Cup> CUPS = Lists.newArrayList();

  public static void init(Context context) throws JSONException {
    Log.d(TAG, "init");
    JSONObject partsObj = JsonUtils.loadJsonFromAssets(context, "data/courses.json");

    // Just in case...
    CUPS.clear();

    JSONArray cupsJsonArray = partsObj.getJSONArray("cups");
    for (int i = 0; i < cupsJsonArray.length(); i++) {
      Log.d(TAG, "adding cup: " + i);
      JSONObject cupJsonObj = cupsJsonArray.getJSONObject(i);
      List<Course> courses = Lists.newArrayList();

      JSONArray coursesJsonArray = cupJsonObj.getJSONArray("courses");
      for (int j = 0; j < coursesJsonArray.length(); j++) {
        courses.add(new Course(context, coursesJsonArray.getString(i), i * Constants.NUM_COURSES_IN_CUP + j, j));
      }

      CUPS.add(new Cup(context, cupJsonObj.getString("name"), courses, i));
    }
  }
}
