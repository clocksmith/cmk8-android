package com.blunka.mk8assistant.data.courses;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.data.parts.Part;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 9/7/14.
 *
 * This is really a "CourseGroup"
 */
public class Cup implements Parcelable, HasDisplayNameAndIcon {
  private static final String TAG = Cup.class.getSimpleName();

  private String mName;
  private String mDisplayName;
  private int mIconResId;
  private List<Course> mCourses;
  private int mIndex;

  public Cup(Context context, String name, List<Course> courses, int index) {
    mName = name;
    mDisplayName = context.getString(context.getResources().getIdentifier(
        name.toLowerCase() + "_cup",
        "string",
        context.getPackageName()));
    mIconResId = context.getResources().getIdentifier(
        "wiiu_cup_" + name.toLowerCase(),
        "drawable",
        context.getPackageName());
    mCourses = courses;
    mIndex = index;
  }

  public String getName() {
    return mName;
  }

  @Override
  public String getDisplayName() {
    return mDisplayName;
  }

  @Override
  public int getIconResId() {
    return mIconResId;
  }

  public List<Course> getCourses() {
    return mCourses;
  }

  public int getIndex() {
    return mIndex;
  }

  public Cup(Parcel in) {
    mName = in.readString();
    mDisplayName = in.readString();
    mIconResId = in.readInt();
    mCourses = Lists.newArrayList(in.createTypedArray(Course.CREATOR));
    mIndex = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeString(mName);
    out.writeString(mDisplayName);
    out.writeInt(mIconResId);
    out.writeTypedArray(mCourses.toArray(new Course[mCourses.size()]), flags);
    out.writeInt(mIndex);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Cup> CREATOR =
      new Parcelable.Creator<Cup>() {
        public Cup createFromParcel(Parcel in) {
          return new Cup(in);
        }

        public Cup[] newArray(int size) {
          return new Cup[size];
        }
      };
}
