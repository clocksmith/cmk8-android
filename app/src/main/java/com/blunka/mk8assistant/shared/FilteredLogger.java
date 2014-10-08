package com.blunka.mk8assistant.shared;

import android.util.Log;

import com.blunka.mk8assistant.main.MainActivity;
import com.blunka.mk8assistant.main.configure.PartGroupChooserView;
import com.blunka.mk8assistant.main.configure.PartGroupViewPagerAdapter;
import com.blunka.mk8assistant.main.maps.MapViewPagerAdapter;
import com.blunka.mk8assistant.main.maps.MapsFragment;
import com.blunka.mk8assistant.main.maps.MapsSpinnerAdapter;
import com.blunka.mk8assistant.main.maps.SpinnerItemWithIconView;
import com.blunka.mk8assistant.shared.preferences.LastUsedModelUtils;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Created by clocksmith on 7/19/14.
 */
public class FilteredLogger {
  private static Set<String> mAcceptedTags = ImmutableSet.of(
      // Activities.
//      MainActivity.class.getSimpleName(),
//      AdjustActivity.class.getSimpleName(),

      // Fragments.
//      AdjustFragment.class.getSimpleName(),
//      BuildFragment.class.getSimpleName(),
//      CompareFragment.class.getSimpleName(),
      MapsFragment.class.getSimpleName(),
//      FaqFragment.class.getSimpleName(),
//      TransitionImageDialogFragment.class.getSimpleName(),
//      PartStatsDialogFragment.class.getSimpleName(),
//      DonationsDialogFragment.class.getSimpleName(),
//
//      Views.
//      PartGroupChooserView.class.getSimpleName(),
//      CondensedKartConfigurationView.class.getSimpleName(),
//      CondensedPartGroupView.class.getSimpleName(),
//      MultiStatsWithLabelView.class.getSimpleName(),
//      SingleStatCompareView.class.getSimpleName(),
//      SpinnerItemWithIconView.class.getSimpleName(),

      // Adapters.
//      PartGroupViewPagerAdapter.class.getSimpleName(),
//      MapViewPagerAdapter.class.getSimpleName(),
//      MapsSpinnerAdapter.class.getSimpleName(),

      // Other
//      LastUsedModelUtils.class.getSimpleName(),


      ""
  );

  public static void d(String tag, String message) {
    if (mAcceptedTags.contains(tag)) {
      Log.d("FILTERED/" + tag, message);
    }
  }
}
