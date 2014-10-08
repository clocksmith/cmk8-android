package com.blunka.mk8assistant.data.parts;

import android.content.Context;

import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.data.Stats;

import java.util.List;

/**
 * Created by clocksmith on 8/8/14.
 */
public interface PartGroup {
  public String getDisplayGroupName(Context context);

  public Stats getPartStats();

  public List<HasDisplayNameAndIcon> getParts();
}