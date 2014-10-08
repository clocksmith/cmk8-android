package com.blunka.mk8assistant.main.compare;

import android.os.Parcel;
import android.os.Parcelable;

import com.blunka.mk8assistant.shared.Model;

/**
 * Created by clocksmith on 7/18/14.
 */
public class CompareModel implements Model {
  private String mLeftBuildKey;
  private String mRightBuildKey;

  public CompareModel() {
    this(null, null);
  }

  public CompareModel(String leftBuildKey, String rightBuildKey) {
    mLeftBuildKey = leftBuildKey;
    mRightBuildKey = rightBuildKey;
  }

  public String getLeftBuildKey() {
    return mLeftBuildKey;
  }

  public void setLeftBuildKey(String leftBuildKey) {
    mLeftBuildKey = leftBuildKey;
  }

  public String getRightBuildKey() {
    return mRightBuildKey;
  }

  public void setRightBuildKey(String rightBuildKey) {
    mRightBuildKey = rightBuildKey;
  }

  public CompareModel(Parcel in) {
    mLeftBuildKey = in.readString();
    mRightBuildKey = in.readString();
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeString(mLeftBuildKey);
    out.writeString(mRightBuildKey);
  }

  public static final Parcelable.Creator<CompareModel> CREATOR =
      new Parcelable.Creator<CompareModel>() {
        public CompareModel createFromParcel(Parcel in) {
          return new CompareModel(in);
        }

        public CompareModel[] newArray(int size) {
          return new CompareModel[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }
}
