package com.blunka.mk8assistant.data;

import android.content.Context;

/**
 * Created by clocksmith on 7/23/14.
 */

public interface HasDisplayNameAndIcon {
  public String getDisplayName(Context context);

  public int getIconResourceId();
}

