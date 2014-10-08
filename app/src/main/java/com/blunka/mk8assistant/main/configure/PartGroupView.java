package com.blunka.mk8assistant.main.configure;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.HasDisplayNameAndIcon;
import com.blunka.mk8assistant.data.parts.PartGroup;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 7/13/14.
 */
public class PartGroupView extends LinearLayout {
  private static int MAX_NUM_PARTS_PER_ROW = 4;
  private static int MAX_NUM_ROWS = 2;

  private TextView mGroupLabel;
  private View mSingleRowSpacer;
  private LinearLayout mTopRow;
  private LinearLayout mBottomRow;
  private int mViewWidth;
  private int mViewHeight;

  public PartGroupView(Context context, final PartGroup partGroup, boolean showLabels) {
    super(context);

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_part_group, this, true);

    mGroupLabel = (TextView) findViewById(R.id.partGroupView_groupLabel);
    mSingleRowSpacer = findViewById(R.id.partGroupView_singleRowSpacer);
    mTopRow = (LinearLayout) findViewById(R.id.partGroupView_topRow);
    mBottomRow = (LinearLayout) findViewById(R.id.partGroupView_bottomRow);

    mGroupLabel.setTypeface(FontLoader.getInstance().getRobotoCondensedLightTypeface());
    mGroupLabel.setText(partGroup.getDisplayGroupName(context));
    this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      public boolean onPreDraw() {
        PartGroupView.this.getViewTreeObserver().removeOnPreDrawListener(this);
        mViewWidth = PartGroupView.this.getMeasuredWidth();
        mViewHeight = PartGroupView.this.getMeasuredHeight();
        fillRows(partGroup.getParts());
        return true;
      }
    });
  }

  private void fillRows(List<HasDisplayNameAndIcon> parts) {
    if (parts.size() < MAX_NUM_PARTS_PER_ROW) {
      fillRow(mTopRow, parts);
      mBottomRow.setVisibility(View.GONE);
      mSingleRowSpacer.setVisibility(View.VISIBLE);
    } else {
      List<List<HasDisplayNameAndIcon>> sublists = Lists.partition(parts,
          (int) Math.ceil((float) parts.size() / 2));
      fillRow(mTopRow, sublists.get(0));
      fillRow(mBottomRow, sublists.get(1));
    }
  }

  private void fillRow(LinearLayout row, List<HasDisplayNameAndIcon> parts) {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        mViewWidth / MAX_NUM_PARTS_PER_ROW,
        LayoutParams.MATCH_PARENT);
    layoutParams.gravity = Gravity.CENTER;
    for (HasDisplayNameAndIcon part : parts) {
      PartView partView = new PartView(getContext(),
          part,
          mViewWidth / MAX_NUM_PARTS_PER_ROW,
          mViewHeight / MAX_NUM_ROWS);
      partView.setLayoutParams(layoutParams);
      row.addView(partView);
    }
  }
}
