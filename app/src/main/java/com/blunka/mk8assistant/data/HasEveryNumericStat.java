package com.blunka.mk8assistant.data;

import android.util.Log;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by clocksmith on 7/19/14.
 */
public abstract class HasEveryNumericStat<N extends Number> {
  private static final String TAG = HasEveryNumericStat.class.getSimpleName();

  protected N mAcceleration;
  protected N mGroundSpeed;
  protected N mAntigravitySpeed;
  protected N mAirSpeed;
  protected N mWaterSpeed;
  protected N mMiniturbo;
  protected N mGroundHandling;
  protected N mAntigravityHandling;
  protected N mAirHandling;
  protected N mWaterHandling;
  protected N mTraction;
  protected N mWeight;

  public HasEveryNumericStat() {
  }

  public HasEveryNumericStat(N acceleration,
      N groundSpeed,
      N antigravitySpeed,
      N airSpeed,
      N waterSpeed,
      N miniturbo,
      N groundHandling,
      N antigravityHandling,
      N airHandling,
      N waterHandling,
      N traction,
      N weight) {
    mAcceleration = acceleration;
    mGroundSpeed = groundSpeed;
    mAntigravitySpeed = antigravitySpeed;
    mAirSpeed = airSpeed;
    mWaterSpeed = waterSpeed;
    mMiniturbo = miniturbo;
    mGroundHandling = groundHandling;
    mAntigravityHandling = antigravityHandling;
    mAirHandling = airHandling;
    mWaterHandling = waterHandling;
    mTraction = traction;
    mWeight = weight;
  }

  protected abstract N average(List<N> numbers);

  public N getAttributeValue(Attribute attribute) {
    switch (attribute) {
      case ACCELERATION:
        return mAcceleration;
      case AVERAGE_SPEED:
        return average(ImmutableList.of(mGroundSpeed, mAntigravitySpeed, mAirSpeed, mWaterSpeed));
      case GROUND_SPEED:
        return mGroundSpeed;
      case ANTIGRAVITY_SPEED:
        return mAntigravitySpeed;
      case AIR_SPEED:
        return mAirSpeed;
      case WATER_SPEED:
        return mWaterSpeed;
      case AVERAGE_HANDLING:
        return average(ImmutableList.of(mGroundHandling,
            mAntigravityHandling,
            mAirHandling,
            mWaterHandling));
      case GROUND_HANDLING:
        return mGroundHandling;
      case ANTIGRAVITY_HANDLING:
        return mAntigravityHandling;
      case AIR_HANDLING:
        return mAirHandling;
      case WATER_HANDLING:
        return mWaterHandling;
      case MINITURBO:
        return mMiniturbo;
      case TRACTION:
        return mTraction;
      case WEIGHT:
        return mWeight;
      default:
        Log.e(TAG, "getAttributeValue(attribute: " + attribute.name() + ") not defined!");
        return null;
    }
  }

  public void setAttributeValue(Attribute attribute, N value) {
    switch (attribute) {
      case ACCELERATION:
        mAcceleration = value;
        break;
      case AVERAGE_SPEED:
        mGroundSpeed = value;
        mAntigravitySpeed = value;
        mAirSpeed = value;
        mWaterSpeed = value;
        break;
      case GROUND_SPEED:
        mGroundSpeed = value;
        break;
      case ANTIGRAVITY_SPEED:
        mAntigravitySpeed = value;
        break;
      case AIR_SPEED:
        mAirSpeed = value;
        break;
      case WATER_SPEED:
        mWaterSpeed = value;
        break;
      case AVERAGE_HANDLING:
        mGroundHandling = value;
        mAntigravityHandling = value;
        mAirHandling = value;
        mWaterHandling = value;
        break;
      case GROUND_HANDLING:
        mGroundHandling = value;
        break;
      case ANTIGRAVITY_HANDLING:
        mAntigravityHandling = value;
        break;
      case AIR_HANDLING:
        mAirHandling = value;
        break;
      case WATER_HANDLING:
        mWaterHandling = value;
        break;
      case MINITURBO:
        mMiniturbo = value;
        break;
      case TRACTION:
        mTraction = value;
        break;
      case WEIGHT:
        mWeight = value;
        break;
      default:
        Log.e(TAG, "setAttributeValue(attribute: " + attribute.name() + "...) not defined!");
    }
  }

  public static abstract class AbstractBuilder<N extends Number, A extends AbstractBuilder<N, A>> {
    protected N mAcceleration;
    protected N mGroundSpeed;
    protected N mAntigravitySpeed;
    protected N mAirSpeed;
    protected N mWaterSpeed;
    protected N mMiniturbo;
    protected N mGroundHandling;
    protected N mAntigravityHandling;
    protected N mAirHandling;
    protected N mWaterHandling;
    protected N mTraction;
    protected N mWeight;

    protected abstract A self();

    public A withAcceleration(N acceleration) {
      mAcceleration = acceleration;
      return self();
    }

    public A withGroundSpeed(N groundSpeed) {
      mGroundSpeed = groundSpeed;
      return self();
    }

    public A withAntigravitySpeed(N antigravitySpeed) {
      mAntigravitySpeed = antigravitySpeed;
      return self();
    }

    public A withAirSpeed(N airSpeed) {
      mAirSpeed = airSpeed;
      return self();
    }

    public A withWaterSpeed(N waterSpeed) {
      mWaterSpeed = waterSpeed;
      return self();
    }

    public A withMiniturbo(N miniturbo) {
      mMiniturbo = miniturbo;
      return self();
    }

    public A withGroundHandling(N groundHandling) {
      mGroundHandling = groundHandling;
      return self();
    }

    public A withAntigravityHandling(N antigravityHandling) {
      mAntigravityHandling = antigravityHandling;
      return self();
    }

    public A withAirHandling(N airHandling) {
      mAirHandling = airHandling;
      return self();
    }

    public A withWaterHandling(N waterHandling) {
      mWaterHandling = waterHandling;
      return self();
    }

    public A withTraction(N traction) {
      mTraction = traction;
      return self();
    }

    public A withWeight(N weight) {
      mWeight = weight;
      return self();
    }

    public A withAverageSpeed(N speed) {
      mGroundSpeed = speed;
      mAntigravitySpeed = speed;
      mAirSpeed = speed;
      mWaterSpeed = speed;
      return self();
    }

    public A withAverageHandling(N handling) {
      mGroundHandling = handling;
      mAntigravityHandling = handling;
      mAirHandling = handling;
      mWaterHandling = handling;
      return self();
    }
  }
}