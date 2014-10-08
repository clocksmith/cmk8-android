package com.blunka.mk8assistant.main.configure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.blunka.mk8assistant.ConfigureMK8Application;
import com.blunka.mk8assistant.analytics.AnalyticsFragment;
import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.main.MainActivity;
import com.blunka.mk8assistant.shared.AnalyticsLogger;
import com.blunka.mk8assistant.shared.stats.PartStatsDialogFragment;
import com.blunka.mk8assistant.shared.ui.ImageUtils;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by clocksmith on 7/13/14.
 */
public class PartView extends ImageView {
  private static final String TAG = PartView.class.getSimpleName();

  private Tracker mTracker;

  public PartView(final Context context, final HasDisplayNameAndIcon part, int targetWidth, int targetHeight) {
    super(context);

    mTracker = ((ConfigureMK8Application) ((Activity) context).getApplication()).getAppTracker();

//    Bitmap partBitmap = ImageUtils.decodeSampledBitmapFromResource(context,
//        part.getIconResourceId(),
//        Bitmap.Config.RGB_565,
//        targetWidth,
//        targetHeight);
//    this.setImageBitmap(partBitmap);

    this.setImageDrawable(context.getResources().getDrawable(part.getIconResourceId()));

    this.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          String fragmentScreenName = ((AnalyticsFragment) ((MainActivity) context)
              .getCurrentPagerFragment()).getScreenName();
          AnalyticsUtils.sendPartViewClickedEvent(mTracker, fragmentScreenName, part);
          FragmentManager fragmentManager =
              ((FragmentActivity) context).getSupportFragmentManager();
          Fragment existingFragment = fragmentManager.findFragmentByTag(
              PartStatsDialogFragment.class.getSimpleName());
          if (existingFragment == null) {
            PartStatsDialogFragment partStatsDialogFragment =
                PartStatsDialogFragment.newInstance(part, PartView.this, part.getIconResourceId());
            partStatsDialogFragment.show(fragmentManager.beginTransaction(),
                PartStatsDialogFragment.class.getSimpleName());
          } else {
            ((PartStatsDialogFragment) existingFragment).dismiss();
            Log.w(TAG, "PartStatsDialogFragment already instantiated!");
          }
        } catch (ClassCastException e) {
          AnalyticsLogger.e(TAG, mTracker, "Cannot get fragment manager");
        }
      }
    });
  }
}
