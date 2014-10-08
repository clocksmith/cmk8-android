package com.blunka.mk8assistant.donations;

/**
 * Created by clocksmith on 7/24/14.
 */
public enum DonationAmount {
  ONE("$1", "donation.one.usd", 1.00f),
  THREE("$3", "donation.three.usd", 3.00f),
  FIVE("$5", "donation.five.usd", 5.00f);

  private String mDisplayName;
  private String mPlayStoreProductId;
  private float mAmountDollars;

  DonationAmount(String displayName, String playStoreProductId, float amountDollars) {
    mDisplayName = displayName;
    mPlayStoreProductId = playStoreProductId;
    mAmountDollars = amountDollars;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  public String getPlayStoreProductId() {
    return mPlayStoreProductId;
  }

  public float getAmountDollars() {
    return mAmountDollars;
  }
}
