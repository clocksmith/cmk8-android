package com.blunka.mk8assistant.data.parts;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by clocksmith on 8/16/15.
 */
public class Part implements Parcelable {
  private static final String TAG = Part.class.getSimpleName();

  public enum Type {
    CHARACTER,
    VEHICLE,
    TIRE,
    GLIDER
  }

  private String mName;
  private String mDisplayName;
  private int mIconResId;
  private Type mType;

  public Part(Context context, String name, Type type) {
    mName = name;
    mDisplayName = context.getString(context.getResources().getIdentifier(
        name.toLowerCase(),
        "string",
        context.getPackageName()));
    mIconResId = context.getResources().getIdentifier(
        "wiiu_" + type.name().toLowerCase() + "_" + name.toLowerCase(),
        "drawable",
        context.getPackageName());
    mType = type;
  }

  public String getName() {
    return mName;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  public int getIconResId() {
    return mIconResId;
  }

  public Type getType() {
    return mType;
  }

  public Part(Parcel in) {
    mName = in.readString();
    mDisplayName = in.readString();
    mIconResId = in.readInt();
    mType = (Type) in.readSerializable();
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeString(mName);
    out.writeString(mDisplayName);
    out.writeInt(mIconResId);
    out.writeSerializable(mType);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Part> CREATOR =
      new Parcelable.Creator<Part>() {
        public Part createFromParcel(Parcel in) {
          return new Part(in);
        }

        public Part[] newArray(int size) {
          return new Part[size];
        }
      };
}
