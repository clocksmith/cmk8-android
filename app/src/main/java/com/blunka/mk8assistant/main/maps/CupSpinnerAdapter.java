package com.blunka.mk8assistant.main.maps;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.courses.CourseData;
import com.blunka.mk8assistant.data.courses.Cup;
import com.google.common.collect.Lists;

/**
 * Created by clocksmith on 9/8/14.
 */
public class CupSpinnerAdapter extends MapsSpinnerAdapter<Cup> {
  public CupSpinnerAdapter(Context context) {
    super(context,
        Lists.newArrayList(CourseData.CUPS),
        (int) context.getResources().getDimension(
            R.dimen.cup_spinner_item_drop_down_height),
        (int) context.getResources().getDimension(
            R.dimen.cup_spinner_item_drop_down_icon_padding_tb));
  }
}
