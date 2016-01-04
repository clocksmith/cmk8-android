package com.blunka.mk8assistant.data.parts;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.blunka.mk8assistant.data.Stats;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 8/8/14.
 */
public class PartGroup implements Parcelable, Comparable<PartGroup> {
  private static final String TAG = PartGroup.class.getSimpleName();

  private String mName;
  private String mDisplayName;
  private Stats mStats;
  private List<Part> mParts;
  private PartType mPartType;
  private int mIndex;

  public PartGroup(Context context, String name, Stats stats, List<Part> parts, PartType partType, int index) {
    mName = name;
    mDisplayName = context.getString(context.getResources().getIdentifier(
        partType == PartType.CHARACTER ? name.toLowerCase() : partType.name().toLowerCase() + "_" + name.toLowerCase(),
        "string",
        context.getPackageName()));
    mStats = stats;
    mParts = parts;
    mPartType = partType;
    mIndex = index;
  }

  public String getName() {
    return mName;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  public Stats getStats() {
    return mStats;
  }

  public List<Part> getParts() {
    return mParts;
  }

  public PartType getPartType() {
    return mPartType;
  }

  public int getIndex() {
    return mIndex;
  }

  public PartGroup(Parcel in) {
    mName = in.readString();
    mDisplayName = in.readString();
    mStats = in.readParcelable(Stats.class.getClassLoader());
    mParts = Lists.newArrayList(in.createTypedArray(Part.CREATOR));
    mPartType = (PartType) in.readSerializable();
    mIndex = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeString(mName);
    out.writeString(mDisplayName);
    out.writeParcelable(mStats, flags);
    out.writeTypedArray(mParts.toArray(new Part[mParts.size()]), flags);
    out.writeSerializable(mPartType);
    out.writeInt(mIndex);
  }

  public static final Parcelable.Creator<PartGroup> CREATOR =
      new Parcelable.Creator<PartGroup>() {
        public PartGroup createFromParcel(Parcel in) {
          return new PartGroup(in);
        }

        public PartGroup[] newArray(int size) {
          return new PartGroup[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public int compareTo(PartGroup otherPartGroup) {
    if (this.getPartType() == PartType.CHARACTER) {
      return this.getIndex() - otherPartGroup.getIndex();
    } else {
      return ComparisonChain.start().compare(this.getName(), otherPartGroup.getName()).result();
    }
  }
}