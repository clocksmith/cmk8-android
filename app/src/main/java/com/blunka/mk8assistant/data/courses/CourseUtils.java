package com.blunka.mk8assistant.data.courses;

import com.blunka.mk8assistant.shared.Constants;

/**
 * Created by clocksmith on 9/8/14.
 *
 * These are needed because we can't have enums with cyclical references.
 */
public class CourseUtils {
  public static Cup getCup(Course course) {
    return CourseData.CUPS.get(course.getIndex() / Constants.NUM_COURSES_IN_CUP);
  }

  public static Course getCourse(int position) {
    return CourseData.CUPS.get(position / Constants.NUM_COURSES_IN_CUP)
        .getCourses().get(position % Constants.NUM_COURSES_IN_CUP);
  }

}
