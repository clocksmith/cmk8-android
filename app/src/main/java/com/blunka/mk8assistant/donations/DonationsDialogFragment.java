package com.blunka.mk8assistant.donations;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.shared.AnalyticsLogger;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.CircleWithForegroundDrawable;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.blunka.mk8assistant.shared.ui.TransitionImageDialogFragment;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

/**
 * Created by clocksmith on 7/24/14.
 */
public class DonationsDialogFragment extends TransitionImageDialogFragment {
  private static final String TAG = DonationsDialogFragment.class.getSimpleName();

  private static final String BITCOIN_ADDRESS = "1L3xkhhQh1nWD8sAUDWyHdvnbcpqT1Qe7U";

  private IInAppBillingService mBillingService;
  private ServiceConnection mServiceConnection;

  private TextView mTopMessage;
  private TextView mViaGooglePlayMessage;
  private TextView mViaBitcoinMessage;
  private TextView mThankYouMessage;

  public static DonationsDialogFragment newInstance(List<ImageView> startImageViews) {
    DonationsDialogFragment donationsDialogFragment = new DonationsDialogFragment();

    Bundle args = new Bundle();
    donationsDialogFragment.insertSharedArgs(args, startImageViews);
    donationsDialogFragment.setArguments(args);

    return donationsDialogFragment;
  }

  @Override
  public String getScreenName() {
    return DonationsDialogFragment.class.getName();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    FilteredLogger.d(TAG, "onActivityResult()");
    if (requestCode == Constants.DONATE_REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
        String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
        if (responseCode == Constants.BILLING_RESPONSE_RESULT_OK) {
          try {
            JSONObject purchaseDataJson = new JSONObject(purchaseData);
            String productId = purchaseDataJson.getString("productId");
            String purchaseToken = purchaseDataJson.getString("purchaseToken");
            String packageName = getActivity().getApplicationContext().getPackageName();
            try {
              mBillingService.consumePurchase(Constants.BILLING_API_VERSION,
                  packageName,
                  purchaseToken);
            } catch (RemoteException e) {
              AnalyticsLogger.e(TAG, mTracker,  "Failed to consume purchase: " + purchaseToken);
            }
            AnalyticsUtils.sendDonationComplete(mTracker, productId);
          } catch (JSONException e) {
            AnalyticsLogger.e(TAG, mTracker,  "Failed to parse purchase data.");
          }
        } else {
          AnalyticsLogger.e(TAG, mTracker, "Donate request responseCode: " + resultCode);
        }
      } else {
        AnalyticsLogger.e(TAG, mTracker, "Donate request resultCode: " + resultCode);
      }
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mTracker.setScreenName(DonationsDialogFragment.class.getName());
    mTracker.send(new HitBuilders.AppViewBuilder().build());

    mServiceConnection = new ServiceConnection() {
      @Override
      public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected()");
        mBillingService = null;
      }

      @Override
      public void onServiceConnected(ComponentName name,
          IBinder service) {
        Log.d(TAG, "onServiceConnected()");
        mBillingService = IInAppBillingService.Stub.asInterface(service);
      }
    };

    getActivity().bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"),
        mServiceConnection,
        Context.BIND_AUTO_CREATE);

    Bundle bundle;
    if (savedInstanceState != null) {
      bundle = savedInstanceState;
    } else {
      bundle = getArguments();
    }
    unpackSharedArgs(bundle);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_donations_dialog, container, false);

    mShade = view.findViewById(R.id.donationsDialogFragment_shade);
    mMainContent = (LinearLayout) view.findViewById(R.id.donationsDialogFragment_mainContent);
    mTopMessage = (TextView) view.findViewById(R.id.donationsDialogFragment_topMessage);
    mViaGooglePlayMessage = (TextView)
        view.findViewById(R.id.donationsDialogFragment_viaGooglePlay);
    mShadowImageViews.add((ImageView)
        view.findViewById(R.id.donationsDialogFragment_donateButton1));
    mShadowImageViews.add((ImageView)
        view.findViewById(R.id.donationsDialogFragment_donateButton3));
    mShadowImageViews.add((ImageView)
        view.findViewById(R.id.donationsDialogFragment_donateButton5));
    mViaBitcoinMessage = (TextView)
        view.findViewById(R.id.donationsDialogFragment_viaBitcoin);
    mShadowImageViews.add((ImageView)
        view.findViewById(R.id.donationsDialogFragment_donateButtonBtc));
    mThankYouMessage = (TextView) view.findViewById(R.id.donationsDialogFragment_thankYouMessage);

    mShade.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });

    mTopMessage.setTypeface(FontLoader.getInstance().getRobotoLightTypeface());
    mViaGooglePlayMessage.setTypeface(FontLoader.getInstance().getRobotoLightTypeface());

    // Set the click listeners for the donate usd buttons.
    for (int index = 0; index < mShadowImageViews.size() - 1; index++) {
      final DonationAmount donationAmount = DonationAmount.values()[index];
      mShadowImageViews.get(index).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          new DonateTask().execute(donationAmount);
        }
      });
    }

    mViaBitcoinMessage.setTypeface(FontLoader.getInstance().getRobotoLightTypeface());

    // Set the click listeners for the donate bitcoin button.
    mShadowImageViews.get(mShadowImageViews.size() - 1).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String url = "bitcoin:" + BITCOIN_ADDRESS;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            TransitionOutThenDismissDialog dialog =
                (TransitionImageDialogFragment.TransitionOutThenDismissDialog) getDialog();
            try {
              startActivity(intent);
              dialog.setDismissWithoutAnimation(true);
              dialog.dismiss();
            } catch (ActivityNotFoundException e) {
              ClipboardManager clipboard = (ClipboardManager)
                  getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
              ClipData clip = ClipData.newPlainText("bitcoin:", BITCOIN_ADDRESS);
              clipboard.setPrimaryClip(clip);
              Toast.makeText(getActivity(),
                  getString(R.string.bitcoin_address_copied),
                  Toast.LENGTH_SHORT).show();
              dialog.setDismissWithoutAnimation(false);
              dialog.dismiss();
            }
          }
        });

    mThankYouMessage.setTypeface(FontLoader.getInstance().getRobotoLightTypeface());

    return view;
  }

  @Override
  protected Drawable getTransitionDrawable(int imageIndex, ImageView imageView) {
    // If it is a usd donation button.
    if (imageIndex < mShadowImageViews.size() - 1) {
      return new CircleWithForegroundDrawable(getActivity(),
          imageView,
          R.color.mk8_blue,
          DonationAmount.values()[imageIndex].getDisplayName(),
          R.dimen.default_title_text_size_sp,
          FontLoader.getInstance().getRobotoCondensedNormalTypeface(),
          false);
    } else {
      // Else it is a btc donation button.
      return new CircleWithForegroundDrawable(getActivity(),
          imageView,
          R.color.mk8_blue,
          BitmapFactory.decodeResource(getResources(), R.drawable.bitcoin_b_white),
          false);
    }
  }

  @Override
  protected void onTransitionInAnimationStart() {
  }

  @Override
  protected void onTransitionOutAnimationEnd() {
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mBillingService != null) {
      getActivity().unbindService(mServiceConnection);
    }
  }

  private class DonateTask extends AsyncTask<DonationAmount, Void, Void> {
    private String mDeveloperPayload;
    @Override
    protected Void doInBackground(DonationAmount... donationAmounts) {
      Random random = new Random();
      mDeveloperPayload = String.valueOf(random.nextInt());
      String packageName = getActivity().getApplicationContext().getPackageName();
      Bundle buyIntentBundle;
      try {
        buyIntentBundle = mBillingService.getBuyIntent(Constants.BILLING_API_VERSION,
            packageName,
            donationAmounts[0].getPlayStoreProductId(),
            "inapp",
            mDeveloperPayload);
        int responseCode = buyIntentBundle.getInt("RESPONSE_CODE");
        if (responseCode == Constants.BILLING_RESPONSE_RESULT_OK) {
          PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
          getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
              Constants.DONATE_REQUEST_CODE,
              new Intent(),
              0,
              0,
              0);
          TransitionOutThenDismissDialog dialog =
              (TransitionImageDialogFragment.TransitionOutThenDismissDialog) getDialog();
          dialog.setDismissWithoutAnimation(true);
          dialog.dismiss();
        } else {
          AnalyticsLogger.e(TAG, mTracker, "BuyIntentBundle RESPONSE_CODE: " + responseCode);
        }
      } catch (RemoteException e) {
        AnalyticsLogger.e(TAG, mTracker, "Unable to get buyIntentBundle");
      } catch (IntentSender.SendIntentException e) {
        AnalyticsLogger.e(TAG, mTracker, "Unable to start intent for sender.");
      }
      return null;
    }
  }
}
