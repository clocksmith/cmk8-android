package com.blunka.mk8assistant.main.maps;

import android.content.Context;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.courses.Course;
import com.blunka.mk8assistant.data.courses.Cup;

/**
 * Created by clocksmith on 9/8/14.
 */
public class CourseSpinnerAdapter extends MapsSpinnerAdapter<Course> {
  public CourseSpinnerAdapter(Context context, Cup cup) {
    super(context,
        cup.getCourses(),
        (int) context.getResources().getDimension(
            R.dimen.course_spinner_item_drop_down_height),
        (int) context.getResources().getDimension(
            R.dimen.course_spinner_item_drop_down_icon_padding_tb));
  }
}