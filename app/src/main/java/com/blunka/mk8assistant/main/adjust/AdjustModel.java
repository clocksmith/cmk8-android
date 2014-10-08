package com.blunka.mk8assistant.main.adjust;

import android.os.Parcel;
import android.os.Parcelable;

import com.blunka.mk8assistant.data.AdjustConfiguration;
import com.blunka.mk8assistant.shared.Model;

/**
 * Created by clocksmith on 7/14/14.
 */
public class AdjustModel implements Model {
  private ConfigurationChoice mSelectedConfigurationChoice;
  private AdjustConfiguration mCustomConfiguration;

  public AdjustModel() {
    this(ConfigurationChoice.CUSTOM,
        AdjustConfiguration.newBuilder()
            .withAcceleration(0)
            .withAverageSpeed(0)
            .withAverageHandling(0)
            .withMiniturbo(0)
            .withTraction(0)
            .withWeight(0)
            .build());
  }

  public AdjustModel(ConfigurationChoice selectedConfigurationChoice,
      AdjustConfiguration customConfiguration) {
    mSelectedConfigurationChoice = selectedConfigurationChoice;
    mCustomConfiguration = customConfiguration;
  }

  public ConfigurationChoice getSelectedConfigurationChoice() {
    return mSelectedConfigurationChoice;
  }

  public AdjustConfiguration getSelectedConfiguration() {
    return mSelectedConfigurationChoice == ConfigurationChoice.CUSTOM ? mCustomConfiguration :
        mSelectedConfigurationChoice.getAdjustConfiguration();
  }

  public AdjustConfiguration getCustomConfiguration() {
    return mCustomConfiguration;
  }

  public void setSelectedConfigurationChoice(
      ConfigurationChoice selectedConfigurationChoice) {
    mSelectedConfigurationChoice = selectedConfigurationChoice;
  }

  public void copySelectedConfigurationToCustomConfiguration() {
    mCustomConfiguration = getSelectedConfiguration();
  }

  public AdjustModel(Parcel in) {
    mSelectedConfigurationChoice = (ConfigurationChoice) in.readSerializable();
    mCustomConfiguration = in.readParcelable(AdjustConfiguration.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeSerializable(mSelectedConfigurationChoice);
    out.writeParcelable(mCustomConfiguration, flags);
  }

  public static final Parcelable.Creator<AdjustModel> CREATOR =
      new Parcelable.Creator<AdjustModel>() {
        public AdjustModel createFromParcel(Parcel in) {
          return new AdjustModel(in);
        }

        public AdjustModel[] newArray(int size) {
          return new AdjustModel[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }
}
