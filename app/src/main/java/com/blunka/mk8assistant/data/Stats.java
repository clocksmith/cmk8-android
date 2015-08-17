package com.blunka.mk8assistant.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.blunka.mk8assistant.data.parts.PartGroup;

import java.util.List;

/**
 * Created by clocksmith on 7/1/14.
 */
public class Stats extends HasEveryNumericStat<Double> implements Parcelable {
  private boolean mHasInnerDrift;

  public Stats(double acceleration,
      double groundSpeed,
      double antigravitySpeed,
      double airSpeed,
      double waterSpeed,
      double miniturbo,
      double groundHandling,
      double antigravityHandling,
      double airHandling,
      double waterHandling,
      double traction,
      double weight,
      boolean hasInnerDrift) {
    super(acceleration,
        groundSpeed,
        antigravitySpeed,
        airSpeed,
        waterSpeed,
        miniturbo,
        groundHandling,
        antigravityHandling,
        airHandling,
        waterHandling,
        traction,
        weight);
    mHasInnerDrift = hasInnerDrift;
  }

  public boolean hasInnerDrift() {
    return mHasInnerDrift;
  }

  @Override
  protected Double average(List<Double> numbers) {
    if (numbers.size() == 0) {
      return 0d;
    } else {
      double sum = 0;
      for (Double aDouble : numbers) {
        sum += aDouble;
      }
      return sum / numbers.size();
    }
  }


  public Stats(Parcel in) {
    mAcceleration = in.readDouble();
    mGroundSpeed = in.readDouble();
    mAntigravitySpeed = in.readDouble();
    mAirSpeed = in.readDouble();
    mWaterSpeed = in.readDouble();
    mMiniturbo = in.readDouble();
    mGroundHandling = in.readDouble();
    mAntigravityHandling = in.readDouble();
    mAirHandling = in.readDouble();
    mWaterHandling = in.readDouble();
    mTraction = in.readDouble();
    mWeight = in.readDouble();
    boolean[] hasInnerDrift = new boolean[1];
    in.readBooleanArray(hasInnerDrift);
    mHasInnerDrift = hasInnerDrift[0];
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeDouble(mAcceleration);
    out.writeDouble(mGroundSpeed);
    out.writeDouble(mAntigravitySpeed);
    out.writeDouble(mAirSpeed);
    out.writeDouble(mWaterSpeed);
    out.writeDouble(mMiniturbo);
    out.writeDouble(mGroundHandling);
    out.writeDouble(mAntigravityHandling);
    out.writeDouble(mAirHandling);
    out.writeDouble(mWaterHandling);
    out.writeDouble(mTraction);
    out.writeDouble(mWeight);
    boolean[] hasInnerDrift = new boolean[1];
    hasInnerDrift[0] = mHasInnerDrift;
    out.writeBooleanArray(hasInnerDrift);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Stats> CREATOR =
      new Parcelable.Creator<Stats>() {
        public Stats createFromParcel(Parcel in) {
          return new Stats(in);
        }

        public Stats[] newArray(int size) {
          return new Stats[size];
        }
      };

  public static abstract class AbstractBuilder<A extends AbstractBuilder<A>>
      extends HasEveryNumericStat.AbstractBuilder<Double, A> {
    private boolean mHasInnerDrift;

    public A withInnerDrift(boolean innerDrift) {
      mHasInnerDrift = innerDrift;
      return self();
    }

    public Stats build() {
      return new Stats(mAcceleration,
          mGroundSpeed,
          mAntigravitySpeed,
          mAirSpeed,
          mWaterSpeed,
          mMiniturbo,
          mGroundHandling,
          mAntigravityHandling,
          mAirHandling,
          mWaterHandling,
          mTraction,
          mWeight,
          mHasInnerDrift);
    }
  }

  private static class Builder extends AbstractBuilder<Builder> {
    @Override
    protected Builder self() {
      return this;
    }
  }

  public static AbstractBuilder<?> newBuilder() {
    return new Builder();
  }
}
