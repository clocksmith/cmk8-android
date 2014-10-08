package com.blunka.mk8assistant.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.blunka.mk8assistant.data.parts.CharacterGroup;
import com.blunka.mk8assistant.data.parts.GliderGroup;
import com.blunka.mk8assistant.data.parts.PartGroup;
import com.blunka.mk8assistant.data.parts.TireGroup;
import com.blunka.mk8assistant.data.parts.VehicleGroup;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by clocksmith on 7/6/14.
 */
public class KartConfiguration implements Parcelable {
  private static final String TAG = KartConfiguration.class.getSimpleName();

  private CharacterGroup mCharacterGroup;
  private VehicleGroup mVehicleGroup;
  private TireGroup mTireGroup;
  private GliderGroup mGliderGroup;

  public KartConfiguration() {
    this(CharacterGroup.values()[0],
        VehicleGroup.values()[0],
        TireGroup.values()[0],
        GliderGroup.values()[0]);
  }

  public KartConfiguration(CharacterGroup characterGroup,
      VehicleGroup vehicleGroup,
      TireGroup tireGroup,
      GliderGroup gliderGroup) {
    mCharacterGroup = characterGroup;
    mVehicleGroup = vehicleGroup;
    mTireGroup = tireGroup;
    mGliderGroup = gliderGroup;
  }

  public List<? extends PartGroup> getParts() {
    return ImmutableList.of(getCharacterGroup(),
        getVehicleGroup(),
        getTireGroup(),
        getGliderGroup());
  }

  public CharacterGroup getCharacterGroup() {
    return mCharacterGroup;
  }

  public VehicleGroup getVehicleGroup() {
    return mVehicleGroup;
  }

  public TireGroup getTireGroup() {
    return mTireGroup;
  }

  public GliderGroup getGliderGroup() {
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

  private float getAttributeSum(Attribute attribute) {
    return mCharacterGroup.getPartStats().getAttributeValue(attribute) +
        mVehicleGroup.getPartStats().getAttributeValue(attribute) +
        mTireGroup.getPartStats().getAttributeValue(attribute) +
        mGliderGroup.getPartStats().getAttributeValue(attribute);
  }

  public void setCharacterGroup(CharacterGroup characterGroup) {
    mCharacterGroup = characterGroup;
  }

  public void setVehicleGroup(VehicleGroup vehicleGroup) {
    mVehicleGroup = vehicleGroup;
  }

  public void setTireGroup(TireGroup tireGroup) {
    mTireGroup = tireGroup;
  }

  public void setGliderGroup(GliderGroup gliderGroup) {
    mGliderGroup = gliderGroup;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private CharacterGroup mCharacterGroup;
    private VehicleGroup mVehicleGroup;
    private TireGroup mTireGroup;
    private GliderGroup mGliderGroup;

    public Builder() {
    }

    public Builder withCharacterGroup(CharacterGroup characterGroup) {
      mCharacterGroup = characterGroup;
      return this;
    }

    public Builder withVehicleGroup(VehicleGroup vehicleGroup) {
      mVehicleGroup = vehicleGroup;
      return this;
    }

    public Builder withTireGroup(TireGroup tireGroup) {
      mTireGroup = tireGroup;
      return this;
    }

    public Builder withGliderGroup(GliderGroup gliderGroup) {
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
    mCharacterGroup = (CharacterGroup) in.readSerializable();
    mVehicleGroup = (VehicleGroup) in.readSerializable();
    mTireGroup = (TireGroup) in.readSerializable();
    mGliderGroup = (GliderGroup) in.readSerializable();
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeSerializable(mCharacterGroup);
    out.writeSerializable(mVehicleGroup);
    out.writeSerializable(mTireGroup);
    out.writeSerializable(mGliderGroup);
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

  @Override
  public int describeContents() {
    return 0;
  }
}
