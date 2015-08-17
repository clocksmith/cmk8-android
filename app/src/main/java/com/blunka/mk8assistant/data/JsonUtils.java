package com.blunka.mk8assistant.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by clocksmith on 8/16/15.
 */
public class JsonUtils {
  public static final String TAG = JsonUtils.class.getSimpleName();

  public static JSONObject loadJsonFromAssets(Context context, String filename) {
    try {
      InputStream stream = context.getAssets().open(filename);
      int size = stream.available();
      byte[] buffer = new byte[size];
      stream.read(buffer);
      stream.close();
      String json = new String(buffer, "UTF-8");
      return new JSONObject(json);
    } catch (IOException | JSONException e) {
      Log.e(TAG, "Could not read json file: " + filename);
      return null;
    }
  }
}
