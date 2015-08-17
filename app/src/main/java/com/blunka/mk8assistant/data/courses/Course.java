package com.blunka.mk8assistant.data.courses;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;

/**
 * Created by clocksmith on 9/7/14.
 */
public class Course implements Parcelable, HasDisplayNameAndIcon {
  private static final String TAG = Course.class.getSimpleName();

  private String mName;
  private String mDisplayName;
  private int mIconResId;
  private int mMapResId;
  private int mIndex;
  private int mIndexInCup;

  Course(Context context, String name, int index, int indexInCup) {
    mName = name;
    mDisplayName =
        context.getString(context.getResources().getIdentifier(name.toLowerCase(), "string", context.getPackageName()));
    mIconResId = context.getResources().getIdentifier(
        "wiiu_map_icon_" + name.toLowerCase(),
        "drawable",
        context.getPackageName());
    int mapResId = context.getResources().getIdentifier(
        "prima_map_" + name.toLowerCase(),
        "drawable",
        context.getPackageName());
    mMapResId = mapResId == 0 ? R.drawable.default_map : mapResId;
    mIndex = index;
    mIndexInCup = indexInCup;
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

  public int getMapResId() {
    return mMapResId;
  }

  public int getIndex() {
    return mIndex;
  }

  public int getIndexInCup() {
    return mIndexInCup;
  }

  public Course(Parcel in) {
    mName = in.readString();
    mDisplayName = in.readString();
    mIconResId = in.readInt();
    mMapResId = in.readInt();
    mIndex = in.readInt();
    mIndexInCup = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeString(mName);
    out.writeString(mDisplayName);
    out.writeInt(mIconResId);
    out.writeSerializable(mMapResId);
    out.writeInt(mIndex);
    out.writeInt(mIndexInCup);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Course> CREATOR =
      new Parcelable.Creator<Course>() {
        public Course createFromParcel(Parcel in) {
          return new Course(in);
        }

        public Course[] newArray(int size) {
          return new Course[size];
        }
      };
}
