package com.blunka.mk8assistant.shared.stats;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.blunka.mk8assistant.R;

/**
 * Created by clocksmith on 7/22/14.
 */
public class StatsDragView extends LinearLayout {
  public StatsDragView(Context context) {
    this(context, null);
  }

  public StatsDragView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public StatsDragView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_stats_drag_view, this, true);
  }
}
