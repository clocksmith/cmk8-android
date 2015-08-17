package com.blunka.mk8assistant.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.blunka.mk8assistant.data.parts.PartData;
import com.blunka.mk8assistant.data.parts.PartGroup;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by clocksmith on 7/6/14.
 */
public class KartConfiguration implements Parcelable {
  private static final String TAG = KartConfiguration.class.getSimpleName();

  private PartGroup mCharacterGroup;
  private PartGroup mVehicleGroup;
  private PartGroup mTireGroup;
  private PartGroup mGliderGroup;

  public KartConfiguration() {
    this(PartData.CHARACTER_GROUPS.get(0),
        PartData.VEHICLE_GROUPS.get(0),
        PartData.TIRE_GROUPS.get(0),
        PartData.GLIDER_GROUPS.get(0));
  }

  public KartConfiguration(PartGroup characterGroup,
      PartGroup vehicleGroup,
      PartGroup tireGroup,
      PartGroup gliderGroup) {
    mCharacterGroup = characterGroup;
    mVehicleGroup = vehicleGroup;
    mTireGroup = tireGroup;
    mGliderGroup = gliderGroup;
  }

  public List<PartGroup> getParts() {
    return ImmutableList.of(mCharacterGroup,
        mVehicleGroup,
        mTireGroup,
        mGliderGroup);
  }

  public PartGroup getCharacterGroup() {
    return mCharacterGroup;
  }

  public PartGroup getVehicleGroup() {
    return mVehicleGroup;
  }

  public PartGroup getTireGroup() {
    return mTireGroup;
  }

  public PartGroup getGliderGroup() {
    return mGliderGroup;
  }

  // A new one is created and recalculated so we do not need to keep track of updates.
  public Stats getKartStats() {
    return Stats.newBuilder()
        .withAcceleration(getAttributeSum(Attribute.ACCELERATION))
        .withGroundSpeed(getAttributeSum(Attribute.GROUND_SPEED))
        .withAntigravitySpeed(getAttributeSum(Attribute.ANTIGRAVITY_SPEED))
        .withAirSpeed(getAttributeSum(Attribute.AIR_SPEED))
        .withWaterSpeed(getAttributeSum(Attribute.WATER_SPEED))
        .withMiniturbo(getAttributeSum(Attribute.MINITURBO))
        .withAverageHandling(getAttributeSum(Attribute.AVERAGE_HANDLING))
        .withGroundHandling(getAttributeSum(Attribute.GROUND_HANDLING))
        .withAntigravityHandling(getAttributeSum(Attribute.ANTIGRAVITY_HANDLING))
        .withAirHandling(getAttributeSum(Attribute.AIR_SPEED))
        .withWaterHandling(getAttributeSum(Attribute.WATER_HANDLING))
        .withTraction(getAttributeSum(Attribute.TRACTION))
        .withWeight(getAttributeSum(Attribute.WEIGHT))
        .build();
  }

  private double getAttributeSum(Attribute attribute) {
    return mCharacterGroup.getStats().getAttributeValue(attribute) +
        mVehicleGroup.getStats().getAttributeValue(attribute) +
        mTireGroup.getStats().getAttributeValue(attribute) +
        mGliderGroup.getStats().getAttributeValue(attribute);
  }

  public void setCharacterGroup(PartGroup characterGroup) {
    mCharacterGroup = characterGroup;
  }

  public void setVehicleGroup(PartGroup vehicleGroup) {
    mVehicleGroup = vehicleGroup;
  }

  public void setTireGroup(PartGroup tireGroup) {
    mTireGroup = tireGroup;
  }

  public void setGliderGroup(PartGroup gliderGroup) {
    mGliderGroup = gliderGroup;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private PartGroup mCharacterGroup;
    private PartGroup mVehicleGroup;
    private PartGroup mTireGroup;
    private PartGroup mGliderGroup;

    public Builder() {
    }

    public Builder withCharacterGroup(PartGroup characterGroup) {
      mCharacterGroup = characterGroup;
      return this;
    }

    public Builder withVehicleGroup(PartGroup vehicleGroup) {
      mVehicleGroup = vehicleGroup;
      return this;
    }

    public Builder withTireGroup(PartGroup tireGroup) {
      mTireGroup = tireGroup;
      return this;
    }

    public Builder withGliderGroup(PartGroup gliderGroup) {
      mGliderGroup = gliderGroup;
      return this;
    }

    public KartConfiguration build() {
      return new KartConfiguration(mCharacterGroup,
          mVehicleGroup,
          mTireGroup,
          mGliderGroup);
    }
  }

  public KartConfiguration(Parcel in) {
    mCharacterGroup = in.readParcelable(PartGroup.class.getClassLoader());
    mVehicleGroup = in.readParcelable(PartGroup.class.getClassLoader());
    mTireGroup = in.readParcelable(PartGroup.class.getClassLoader());
    mGliderGroup = in.readParcelable(PartGroup.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeParcelable(mCharacterGroup, flags);
    out.writeParcelable(mVehicleGroup, flags);
    out.writeParcelable(mTireGroup, flags);
    out.writeParcelable(mGliderGroup, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<KartConfiguration> CREATOR =
      new Parcelable.Creator<KartConfiguration>() {
        public KartConfiguration createFromParcel(Parcel in) {
          return new KartConfiguration(in);
        }

        public KartConfiguration[] newArray(int size) {
          return new KartConfiguration[size];
        }
      };
}
