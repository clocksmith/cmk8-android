package com.blunka.mk8assistant.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.android.vending.billing.IInAppBillingService;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsFragment;
import com.blunka.mk8assistant.analytics.AnalyticsFragmentActivity;
import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.main.adjust.AdjustToBuildConverter;
import com.blunka.mk8assistant.main.configure.ConfigureModel;
import com.blunka.mk8assistant.main.configure.ConfigureFragment;
import com.blunka.mk8assistant.main.compare.CompareFragment;
import com.blunka.mk8assistant.main.compare.CompareModel;
import com.blunka.mk8assistant.main.faq.FaqFragment;
import com.blunka.mk8assistant.main.maps.MapsFragment;
import com.blunka.mk8assistant.main.maps.MapsModel;
import com.blunka.mk8assistant.shared.AnalyticsLogger;
import com.blunka.mk8assistant.shared.ArgKeys;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.Flags;
import com.blunka.mk8assistant.shared.preferences.LastUsedModelUtils;
import com.google.common.collect.Lists;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by clocksmith on 7/6/14.
 */
public class MainActivity extends AnalyticsFragmentActivity implements ConfigureFragment.Listener,
    CompareFragment.Listener,
    MapsFragment.Listener {
  private static final String TAG = MainActivity.class.getSimpleName();

  // Model.
  private ConfigureModel mConfigureModel;
  private CompareModel mCompareModel;
  private MapsModel mMapsModel;

  // View.
  private PagerSlidingTabStrip mTabs;
  private ViewPager mViewPager;

  private MainPagerAdapter mMainPagerAdapter;

  @Override
  protected String getScreenName() {
    return MainActivity.class.getName();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    FilteredLogger.d(TAG, "onActivityResult()");
    if (requestCode == Constants.DONATE_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
        if (responseCode == Constants.BILLING_RESPONSE_RESULT_OK) {
          try {
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            JSONObject purchaseDataJson = new JSONObject(purchaseData);
            String productId = purchaseDataJson.getString("productId");
            AnalyticsUtils.sendDonationComplete(mTracker, productId);
            String purchaseToken = purchaseDataJson.getString("purchaseToken");
            new ConsumePurchasesTask().execute(purchaseToken);
          } catch (JSONException e) {
            AnalyticsLogger.e(TAG, mTracker,  "Failed to parse purchase data.");
          }
        } else {
          AnalyticsLogger.e(TAG, mTracker, "Donate request responseCode: " + responseCode);
        }
      } else {
        AnalyticsLogger.e(TAG, mTracker, "Donate request resultCode: " + resultCode);
      }
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (Flags.RUN_TEST) {
      new AdjustToBuildConverter().runTest();
    }

    if (savedInstanceState != null) {
      mConfigureModel = savedInstanceState.getParcelable(ArgKeys.BUILD_MODEL);
      mCompareModel = savedInstanceState.getParcelable(ArgKeys.COMPARE_MODEL);
      mMapsModel = savedInstanceState.getParcelable(ArgKeys.MAPS_MODEL);
    }

    if (mConfigureModel == null) {
      ConfigureModel lastUsedConfigureModel = LastUsedModelUtils.getLastUsedBuildModel(this);
      mConfigureModel = lastUsedConfigureModel == null ? new ConfigureModel() : lastUsedConfigureModel;
    }

    if (mCompareModel == null) {
      CompareModel lastUsedCompareModel = LastUsedModelUtils.getLastUsedCompareModel(this);
      mCompareModel = lastUsedCompareModel == null ? new CompareModel() : lastUsedCompareModel;
    }

    if (mMapsModel == null) {
      MapsModel lastUsedMapsModel = LastUsedModelUtils.getLastUsedMapsModel(this);
      mMapsModel = lastUsedMapsModel == null ? new MapsModel() : lastUsedMapsModel;
    }

    setContentView(R.layout.activity_main);

    mTabs = (PagerSlidingTabStrip) findViewById(R.id.main_tabs);
    mViewPager = (ViewPager) findViewById(R.id.main_viewPager);

    mTabs.setShouldExpand(true);
    mTabs.setTabPaddingLeftRight(0);
    mTabs.setDividerColor(getResources().getColor(android.R.color.transparent));
    mTabs.setIndicatorColor(getResources().getColor(R.color.indicator_color));
    mTabs.setIndicatorHeight((int) (getResources().getDimension(R.dimen.main_tab_bar_indicator_height) + 0.5));
    mTabs.setUnderlineColor(getResources().getColor(R.color.tab_underline_color));
    mTabs.setUnderlineHeight(
        (int) (getResources().getDimension(R.dimen.main_tab_bar_bottom_border_height) + 0.5));

    mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);
    mMainPagerAdapter.setTabWidth(metrics.widthPixels / MainTab.values().length);
    mViewPager.setAdapter(mMainPagerAdapter);
    mTabs.setViewPager(mViewPager);

    mViewPager.setCurrentItem(MainTab.CONFIGURE.ordinal());
    // WARNING! If this is not set the way it is, there is an unsolved memory leak.
    mViewPager.setOffscreenPageLimit(MainTab.values().length - 1);


    mTabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        FilteredLogger.d(TAG, "onPageSelected: " + position);
        sendFragmentHit(position);
        for (int tabIndex = 0; tabIndex < MainTab.values().length; tabIndex++) {
          MainTabView mainTabView = (MainTabView) mMainPagerAdapter.getPageTabCustomView(position);
          mainTabView.setSelected(tabIndex == position);
        }
      }
    });

    new ConsumePurchasesTask().execute((String) null);
  }

  @Override
  public void onResume() {
    FilteredLogger.d(TAG, "onResume()");
    sendFragmentHit(mViewPager.getCurrentItem());
    super.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putParcelable(ArgKeys.BUILD_MODEL, mConfigureModel);
    savedInstanceState.putParcelable(ArgKeys.COMPARE_MODEL, mCompareModel);

    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public void onPause() {
    FilteredLogger.d(TAG, "onPause()");
    super.onPause();
  }

  @Override
  public void onBackPressed() {
    FilteredLogger.d(TAG, "onBackPressed()");
    if (mViewPager.getCurrentItem() != MainTab.CONFIGURE.ordinal()) {
      mViewPager.setCurrentItem(MainTab.CONFIGURE.ordinal());
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onBuildModelUpdated(ConfigureModel configureModel) {
    FilteredLogger.d(TAG, "onBuildModelUpdated()");
    mConfigureModel = configureModel;
    LastUsedModelUtils.setLastUsedBuildModel(this, mConfigureModel);
  }

  @Override
  public void onStarredBuildLibraryUpdated(String buildKeyAdded) {
    FilteredLogger.d(TAG, "onStarredBuildLibraryUpdated()");
    CompareFragment compareFragment = (CompareFragment)
        mMainPagerAdapter.getRegisteredFragment(MainTab.COMPARE.ordinal());
    if (compareFragment != null) {
      compareFragment.updateSpinners(buildKeyAdded);
    }
  }

  @Override
  public void onCompareModelUpdated(CompareModel compareModel) {
    FilteredLogger.d(TAG, "onCompareModelUpdated()");
    mCompareModel = compareModel;
    LastUsedModelUtils.setLastUsedCompareModel(this, mCompareModel);
  }

  @Override
  public void onMapsModelUpdated(MapsModel mapsModel) {
    FilteredLogger.d(TAG, "onMapsModelUpdated()");
    mMapsModel = mapsModel;
    LastUsedModelUtils.setLastUsedMapsModel(this, mMapsModel);
  }

  private void sendFragmentHit(int position) {
    AnalyticsFragment analyticsFragment = (AnalyticsFragment)
        mMainPagerAdapter.getRegisteredFragment(position);
    if (analyticsFragment != null) {
      analyticsFragment.sendScreenViewToAnalytics();
    }
  }

  public Fragment getCurrentPagerFragment() {
    return mMainPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
  }

  private class MainPagerAdapter extends FragmentPagerAdapter
      implements PagerSlidingTabStrip.TabCustomViewProvider {
    SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();
    private int mTabWidth;

    public MainPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    public void setTabWidth(int tabWidth) {
      mTabWidth = tabWidth;
    }

    @Override
    public View getPageTabCustomView(int position) {
      MainTabView mainTabView = new MainTabView(MainActivity.this);
      mainTabView.setIcon(MainTab.values()[position].getIcon(MainActivity.this));
      mainTabView.setTitle(MainTab.values()[position].getTitle(MainActivity.this));
      return mainTabView;
    }

    @Override
    public Fragment getItem(int i) {
      switch (MainTab.values()[i]) {
        case CONFIGURE:
          return ConfigureFragment.newInstance(mConfigureModel);
        case COMPARE:
          return CompareFragment.newInstance(mCompareModel);
        case MAPS:
          return MapsFragment.newInstance(mMapsModel);
        case FAQ:
          return FaqFragment.newInstance();
        default:
          AnalyticsLogger.e(TAG, mTracker, "Unsupported MainTab");
          return null;
      }
    }

    @Override
    public int getCount() {
      return MainTab.values().length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      Fragment fragment = (Fragment) super.instantiateItem(container, position);
      mRegisteredFragments.put(position, fragment);
      return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      mRegisteredFragments.remove(position);
      super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
      return mRegisteredFragments.get(position);
    }
  }

  private class ConsumePurchasesTask extends AsyncTask<String, Void, Void> {
    private final CountDownLatch mBillingServiceCountDownLatch;
    private IInAppBillingService mBillingService;
    private ServiceConnection mServiceConnection;
    private String mPackageName;

    public ConsumePurchasesTask() {
      mBillingServiceCountDownLatch = new CountDownLatch(1);

      mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
          FilteredLogger.d(TAG, "ConsumePurchasesTask onServiceDisconnected()");
          mBillingService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
            IBinder service) {
          FilteredLogger.d(TAG, "ConsumePurchasesTask onServiceConnected()");
          mBillingService = IInAppBillingService.Stub.asInterface(service);
          mBillingServiceCountDownLatch.countDown();
        }
      };

      mPackageName = MainActivity.this.getApplicationContext().getPackageName();

      MainActivity.this.bindService(
          new Intent("com.android.vending.billing.InAppBillingService.BIND"),
          mServiceConnection,
          Context.BIND_AUTO_CREATE);
    }

    @Override
    protected Void doInBackground(String[] purchaseTokens) {
      FilteredLogger.d(TAG, "ConsumePurchasesTask doInBackground()");

      try {
        mBillingServiceCountDownLatch.await();
        List<String> purchaseTokensToConsume = Lists.newArrayList();
        if (purchaseTokens[0] != null) {
          purchaseTokensToConsume.add(purchaseTokens[0]);
        } else {
          Bundle items = mBillingService.getPurchases(Constants.BILLING_API_VERSION,
              mPackageName,
              "inapp",
              null);
          int responseCode = items.getInt("RESPONSE_CODE");
          if (responseCode == Constants.BILLING_RESPONSE_RESULT_OK) {
            List<String> purchaseDataJsonsStrings =
                items.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            for (String purchaseDataJsonString : purchaseDataJsonsStrings) {
              try {
                JSONObject purchaseDataJson = new JSONObject(purchaseDataJsonString);
                String purchaseToken = purchaseDataJson.getString("purchaseToken");
                purchaseTokensToConsume.add(purchaseToken);
              } catch (JSONException e) {
                AnalyticsLogger.e(TAG,
                    MainActivity.this.getTracker(),
                    "Could not parse purchase data json.");
              }
            }
          } else {
            AnalyticsLogger.e(TAG,
                MainActivity.this.getTracker(),
                "Get purchases request responseCode: " + responseCode);
          }
        }
        for (String purchaseToken : purchaseTokensToConsume) {
          consume(purchaseToken);
        }
      } catch (RemoteException e) {
        AnalyticsLogger.e(TAG, MainActivity.this.getTracker(), "Could not get purchases.");
      } catch (InterruptedException e) {
        AnalyticsLogger.e(TAG,
            MainActivity.this.getTracker(),
            "Billing Service countdown latch interrupted!");
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      FilteredLogger.d(TAG, "ConsumePurchasesTask onPostExecute()");
      if (mBillingService != null) {
        MainActivity.this.unbindService(mServiceConnection);
      }
    }

    private void consume(String purchaseToken) {
      String packageName = MainActivity.this.getApplicationContext().getPackageName();
      try {
        mBillingService.consumePurchase(Constants.BILLING_API_VERSION,
            packageName,
            purchaseToken);
      } catch (RemoteException e) {
        AnalyticsLogger.e(TAG,
            MainActivity.this.getTracker(),
            "Failed to consume purchase: " + purchaseToken);
      }
    }
  }
}