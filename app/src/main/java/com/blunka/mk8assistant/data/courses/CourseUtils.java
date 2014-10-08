package com.blunka.mk8assistant.data.courses;

/**
 * Created by clocksmith on 9/8/14.
 *
 * These are needed because we can't have enums with cyclical references.
 */
public class CourseUtils {
  public static Cup getCup(Course course) {
    for (Cup cup : Cup.values()) {
      if (cup.getCourses().contains(course)) {
        return cup;
      }
    }
    return null;
  }

  public static int getPositionOfCourseInCup(Course course) {
    for (Cup cup : Cup.values()) {
      if (cup.getCourses().contains(course)) {
        return cup.getCourses().indexOf(course);
      }
    }
    return -1;
  }
}
