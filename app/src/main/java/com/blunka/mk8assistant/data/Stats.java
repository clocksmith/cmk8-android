package com.blunka.mk8assistant.data;

import java.util.List;

/**
 * Created by clocksmith on 7/1/14.
 */
public class Stats extends HasEveryNumericStat<Float> {
  private boolean mHasInnerDrift;

  public Stats(float acceleration,
      float groundSpeed,
      float antigravitySpeed,
      float airSpeed,
      float waterSpeed,
      float miniturbo,
      float groundHandling,
      float antigravityHandling,
      float airHandling,
      float waterHandling,
      float traction,
      float weight,
      boolean hasInnerDrift) {
    super(acceleration,
        groundSpeed,
        antigravitySpeed,
        airSpeed,
        waterSpeed,
        miniturbo,groundHandling,
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
  protected Float average(List<Float> numbers) {
    if (numbers.size() == 0) {
      return 0f;
    } else {
      float sum = 0f;
      for (Float aFloat : numbers) {
        sum += aFloat;
      }
      return sum / numbers.size();
    }
  }

  public static abstract class AbstractBuilder<A extends AbstractBuilder<A>>
      extends HasEveryNumericStat.AbstractBuilder<Float, A> {
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
