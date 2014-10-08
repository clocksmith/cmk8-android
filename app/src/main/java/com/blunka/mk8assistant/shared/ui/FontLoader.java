package com.blunka.mk8assistant.shared.ui;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.blunka.mk8assistant.shared.FilteredLogger;

/**
 * Created by clocksmith on 7/28/14.
 */
public class FontLoader {
  private static final String TAG = FontLoader.class.getSimpleName();

  private static final String PATH_PREFIX = "fonts/";

  private static final String ROBOTO_THIN = PATH_PREFIX + "Roboto-Thin.ttf";

  private static final String ROBOTO_LIGHT = PATH_PREFIX + "Roboto-Light.ttf";

  private static final String ROBOTO_NORMAL = PATH_PREFIX + "Roboto-Regular.ttf";

  private static final String ROBOTO_CONDENSED_LIGHT = PATH_PREFIX + "RobotoCondensed-Light.ttf";

  private static final String ROBOTO_CONDENSED_NORMAL = PATH_PREFIX + "RobotoCondensed-Regular.ttf";

  private volatile static FontLoader instance;

  private Typeface mRobotoThin;
  private Typeface mRobotoLight;
  private Typeface mRobotoNormal;
  private Typeface mRobotoCondensedLight;
  private Typeface mRobotoCondensedNormal;

  public static FontLoader getInstance() {
    if (instance == null) {
      synchronized (FontLoader.class) {
        if (instance == null) {
          instance = new FontLoader();
        }
      }
    }
    return instance;
  }

  public synchronized void init(AssetManager assetsManager) {
    FilteredLogger.d(TAG, "init");
    mRobotoThin = Typeface.createFromAsset(assetsManager, ROBOTO_THIN);
    mRobotoLight = Typeface.createFromAsset(assetsManager, ROBOTO_LIGHT);
    mRobotoNormal = Typeface.createFromAsset(assetsManager, ROBOTO_NORMAL);
    mRobotoCondensedLight = Typeface.createFromAsset(assetsManager, ROBOTO_CONDENSED_LIGHT);
    mRobotoCondensedNormal = Typeface.createFromAsset(assetsManager, ROBOTO_CONDENSED_NORMAL);
  }

  public Typeface getRobotoThinTypeface() {
    return mRobotoThin;
  }

  public Typeface getRobotoLightTypeface() {
    return mRobotoLight;
  }

  public Typeface getRobotoNormalTypeface() {
    return mRobotoNormal;
  }

  public Typeface getRobotoCondensedLightTypeface() {
    return mRobotoCondensedLight;
  }

  public Typeface getRobotoCondensedNormalTypeface() {
    return mRobotoCondensedNormal;
  }
}
