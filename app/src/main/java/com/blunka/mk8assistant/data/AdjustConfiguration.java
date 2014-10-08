package com.blunka.mk8assistant.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by clocksmith on 7/6/14.
 */
public class AdjustConfiguration extends HasEveryNumericStat<Integer> implements Parcelable {
  public AdjustConfiguration(int acceleration,
      int groundSpeed,
      int antigravitySpeed,
      int airSpeed,
      int waterSpeed,
      int miniturbo,
      int groundHandling,
      int antigravityHandling,
      int airHandling,
      int waterHandling,
      int traction,
      int weight) {
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
  }

  @Override
  protected Integer average(List<Integer> numbers) {
    if (numbers.size() == 0) {
      return 0;
    } else {
      int sum = 0;
      for (Integer integer : numbers) {
        sum += integer;
      }
      return sum / numbers.size();
    }
  }

  public static abstract class AbstractBuilder<A extends AbstractBuilder<A>>
      extends HasEveryNumericStat.AbstractBuilder<Integer, A> {
    private boolean mHasInnerDrift;

    public AdjustConfiguration build() {
      return new AdjustConfiguration(mAcceleration,
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
          mWeight);
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

  public AdjustConfiguration(Parcel in) {
    mAcceleration = in.readInt();
    mGroundSpeed = in.readInt();
    mAntigravitySpeed = in.readInt();
    mAirSpeed = in.readInt();
    mWaterSpeed = in.readInt();
    mMiniturbo = in.readInt();
    mGroundHandling = in.readInt();
    mAntigravityHandling = in.readInt();
    mAirHandling = in.readInt();
    mWaterHandling = in.readInt();
    mTraction = in.readInt();
    mWeight = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeInt(mAcceleration);
    out.writeInt(mGroundSpeed);
    out.writeInt(mAntigravitySpeed);
    out.writeInt(mAirSpeed);
    out.writeInt(mWaterSpeed);
    out.writeInt(mMiniturbo);
    out.writeInt(mGroundHandling);
    out.writeInt(mAntigravityHandling);
    out.writeInt(mAirHandling);
    out.writeInt(mWaterHandling);
    out.writeInt(mTraction);
    out.writeInt(mWeight);
  }

  public static final Parcelable.Creator<AdjustConfiguration> CREATOR =
      new Parcelable.Creator<AdjustConfiguration>() {
    public AdjustConfiguration createFromParcel(Parcel in) {
      return new AdjustConfiguration(in);
    }

    public AdjustConfiguration[] newArray(int size) {
      return new AdjustConfiguration[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }
}
