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
    JSONObject cupsJsonObj = JsonUtils.loadJsonFromAssets(context, "data/courses.json");

    // Just in case...
    CUPS.clear();

    JSONArray cupsJsonArray = cupsJsonObj.getJSONArray("cups");
    for (int i = 0; i < cupsJsonArray.length(); i++) {
      Log.d(TAG, "adding cup: " + i);
      JSONObject cupJsonObj = cupsJsonArray.getJSONObject(i);
      List<Course> courses = Lists.newArrayList();

      JSONArray coursesJsonArray = cupJsonObj.getJSONArray("courses");
      for (int j = 0; j < coursesJsonArray.length(); j++) {
        Log.d(TAG, "adding course: " + j);
        courses.add(new Course(context, coursesJsonArray.getString(j), i * Constants.NUM_COURSES_IN_CUP + j, j));
      }

      CUPS.add(new Cup(context, cupJsonObj.getString("name"), courses, i));
    }
  }

  public static Course getCourse(int position) {
    return CUPS.get(position / Constants.NUM_COURSES_IN_CUP)
        .getCourses().get(position % Constants.NUM_COURSES_IN_CUP);
  }
}
