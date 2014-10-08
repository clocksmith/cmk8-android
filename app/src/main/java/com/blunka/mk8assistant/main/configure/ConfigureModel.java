package com.blunka.mk8assistant.main.configure;

import android.os.Parcel;
import android.os.Parcelable;

import com.blunka.mk8assistant.data.KartConfiguration;
import com.blunka.mk8assistant.shared.Model;

/**
 * Created by clocksmith on 7/14/14.
 */
public class ConfigureModel implements Parcelable, Model {
  private KartConfiguration mKartConfiguration;

  public ConfigureModel() {
    this(new KartConfiguration());
  }

  public ConfigureModel(KartConfiguration kartConfiguration) {
    mKartConfiguration = kartConfiguration;
  }

  public KartConfiguration getKartConfiguration() {
    return mKartConfiguration;
  }

  public void setPlayerConfiguration(KartConfiguration kartConfiguration) {
    mKartConfiguration = kartConfiguration;
  }

  public ConfigureModel(Parcel in) {
    mKartConfiguration = in.readParcelable(KartConfiguration.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeParcelable(mKartConfiguration, flags);
  }

  public static final Parcelable.Creator<ConfigureModel> CREATOR =
      new Parcelable.Creator<ConfigureModel>() {
        public ConfigureModel createFromParcel(Parcel in) {
          return new ConfigureModel(in);
        }

        public ConfigureModel[] newArray(int size) {
          return new ConfigureModel[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }
}
