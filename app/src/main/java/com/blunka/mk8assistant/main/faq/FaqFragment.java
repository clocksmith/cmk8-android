package com.blunka.mk8assistant.main.faq;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsFragment;
import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.donations.DonationsDialogFragment;
import com.blunka.mk8assistant.shared.AnalyticsLogger;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.CircleWithForegroundDrawable;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by clocksmith on 7/24/14.
 */
public class FaqFragment extends AnalyticsFragment {
  private static final String TAG = FaqFragment.class.getSimpleName();

  private WebView mWebView;
  private ImageView mRateButton;
  private ImageView mDonateButton;

  public static FaqFragment newInstance() {
    return new FaqFragment();
  }

  @Override
  public String getScreenName() {
    return FaqFragment.class.getName();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_faq, container, false);

    mWebView = (WebView) view.findViewById(R.id.faqFragment_webView);
    mRateButton = (ImageView) view.findViewById(R.id.faqFragment_rateButton);
    mDonateButton = (ImageView) view.findViewById(R.id.faqFragment_donateButton);

    try {
      InputStream htmlInputStream = getResources().openRawResource(R.raw.faq);
      String htmlString =
          CharStreams.toString(new InputStreamReader(htmlInputStream, Charsets.UTF_8));
      Closeables.closeQuietly(htmlInputStream);
//      mWebView.loadData(htmlString, "text/html; charset=UTF-8", null);
      mWebView.loadDataWithBaseURL("file:///android_asset/",
          htmlString,
          "text/html",
          Charsets.UTF_8.displayName(),
          null);
    } catch (IOException e) {
      AnalyticsLogger.e(TAG, mTracker, "Cannot open faq.html");
    }
//    mWebView.loadUrl("file:///android_asset/html/faq.html");

    mRateButton.setImageDrawable(new CircleWithForegroundDrawable(getActivity(),
        mRateButton,
        R.color.mk8_dark_red,
        getString(R.string.rate),
        R.dimen.default_text_size_sp,
        FontLoader.getInstance().getRobotoCondensedNormalTypeface(),
        true));
    mRateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AnalyticsUtils.sendRateButtonClicked(mTracker);
        Uri rateAppViaMarketUri = Uri.parse("market://details?id=" +
            getActivity().getApplicationContext().getPackageName());
        Intent rateAppViaMarketIntent = new Intent(Intent.ACTION_VIEW, rateAppViaMarketUri);
        Uri rateAppViaBroswerUri = Uri.parse("https://play.google.com/store/apps/details?id=" +
            getActivity().getApplicationContext().getPackageName());
        Intent rateAppViaBrowserIntent = new Intent(Intent.ACTION_VIEW, rateAppViaBroswerUri);

        if (getActivity().getPackageManager()
            .queryIntentActivities(rateAppViaMarketIntent, 0).size() > 0) {
          startActivity(rateAppViaMarketIntent);
        } else if (getActivity().getPackageManager().
            queryIntentActivities(rateAppViaBrowserIntent, 0).size() > 0) {
          startActivity(rateAppViaBrowserIntent);
        } else {
          AnalyticsLogger.e(TAG, mTracker,
              "User has no activity that can handle the rate app intent.");
          Toast.makeText(getActivity(),
              getString(R.string.cannot_rate_app),
              Toast.LENGTH_SHORT).show();
        }
      }
    });

    mDonateButton.setImageDrawable(new CircleWithForegroundDrawable(getActivity(),
        mDonateButton,
        R.color.mk8_blue,
        getString(R.string.donate),
        R.dimen.default_text_size_sp,
        FontLoader.getInstance().getRobotoCondensedNormalTypeface(),
        true));
    mDonateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AnalyticsUtils.sendDonateButtonClicked(mTracker);
        try {
          FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
          Fragment existingFragment = fragmentManager.findFragmentByTag(
              DonationsDialogFragment.class.getSimpleName());
          if (existingFragment == null) {
            // The donate button is the source transition image for each of the 4 resulting buttons.
            DonationsDialogFragment donationsDialogFragment =
                DonationsDialogFragment.newInstance(Lists.newArrayList(mDonateButton,
                    mDonateButton,
                    mDonateButton,
                    mDonateButton));
            donationsDialogFragment.show(fragmentManager.beginTransaction(),
                DonationsDialogFragment.class.getSimpleName());
          } else {
            ((DonationsDialogFragment) existingFragment).dismiss();
            FilteredLogger.d(TAG, "DonationsDialogFragment already instantiated!");
          }
        } catch (ClassCastException e) {
          AnalyticsLogger.e(TAG, mTracker, "Cannot get fragment manager");
        }
      }
    });

    return view;
  }
}
