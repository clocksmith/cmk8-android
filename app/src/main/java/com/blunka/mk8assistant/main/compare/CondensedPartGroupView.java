package com.blunka.mk8assistant.main.compare;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.data.parts.PartGroup;
import com.blunka.mk8assistant.main.configure.PartView;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.UiUtils;

/**
 * Created by clocksmith on 7/22/14.
 */
public class CondensedPartGroupView extends LinearLayout {
  private static final String TAG = CondensedPartGroupView.class.getSimpleName();

  private int mPartHeight;
  private LinearLayout mContainer;

  public CondensedPartGroupView(final Context context, final PartGroup partGroup) {
    super(context);
    FilteredLogger.d(TAG, "CondensedPartGroupView(partGroup: " +
        partGroup.getDisplayGroupName(context) + ")");

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_condensed_part_group, this, true);

    mContainer = (LinearLayout) findViewById(R.id.condensedPartGroupView_container);

    // For some unknown reason, onGlobalLayout is getting called a bunch of times before the
    // listener is actually removed (if ever). Using this flag makes sure the parts are only drawn
    // once which greatly improves the performance of the app.
    final boolean[] onGlobalLayoutCalled = {false};
    if (mContainer.getViewTreeObserver().isAlive()) {
      mContainer.getViewTreeObserver().addOnGlobalLayoutListener(
          new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
              if (mContainer.getViewTreeObserver().isAlive()) {
                // This doesn't seem to do anything.
                UiUtils.removeOnGlobalLayoutListener(mContainer, this);
              }
              if (!onGlobalLayoutCalled[0]) {
                onGlobalLayoutCalled[0] = true;
                mPartHeight = mContainer.getHeight();
                FilteredLogger.d(TAG, "mPartHeight: " + mPartHeight);
                drawParts(context, partGroup, mPartHeight / Constants.MAX_NUMBER_PARTS_PER_GROUP);
              }
            }
          }
      );
    }
  }

  private void drawParts(Context context, PartGroup partGroup, int partDimension) {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(partDimension,
        partDimension);
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
    for (HasDisplayNameAndIcon part : partGroup.getParts()) {
      PartView partView = new PartView(context, part, partDimension, partDimension);
      mContainer.addView(partView, layoutParams);
    }
  }
}
