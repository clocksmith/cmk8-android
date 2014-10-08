package com.blunka.mk8assistant.main.adjust;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.blunka.mk8assistant.R;

/**
 * Created by clocksmith on 7/8/14.
 */
public class ProgressDisplayingSeekBar extends SeekBar {
  private static final String TAG = ProgressDisplayingSeekBar.class.getSimpleName();

  private ProgressDrawableFactory mProgressDrawableFactory;
  private Drawable mOriginalThumbDrawable;

  private static OnSeekBarChangeListener DUMMY_CHANGE_LISTENER =
      new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
          // Dummy
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
          // Dummy
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
          // Dummy
        }
      };

  public ProgressDisplayingSeekBar(Context context) {
    this(context, null);
  }

  public ProgressDisplayingSeekBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ProgressDisplayingSeekBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    mProgressDrawableFactory = new ProgressDrawableFactory(getContext());
    setEnabled(true);
    setOnSeekBarChangeListener(DUMMY_CHANGE_LISTENER);
  }

  @Override
  public void setEnabled(boolean isEnabled) {
    super.setEnabled(isEnabled);

    mOriginalThumbDrawable = getResources().getDrawable(isEnabled ?
        R.drawable.circle_mk8_blue : R.drawable.circle_gray).getConstantState().newDrawable();
    this.setProgressDrawable(getResources().getDrawable(isEnabled ?
        R.drawable.horizontal_progress_mk8_blue : R.drawable.horizontal_progress_gray));
    updateThumbWithProgressDisplay();
  }

  @Override
  public void setOnSeekBarChangeListener(final OnSeekBarChangeListener onSeekBarChangeListener) {
    OnSeekBarChangeListener progressDisplayingOnSeekBarChangeListener =
        new OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
            updateThumbWithProgressDisplay();
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {
            onSeekBarChangeListener.onStartTrackingTouch(seekBar);
          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
            onSeekBarChangeListener.onStopTrackingTouch(seekBar);
          }
        };

    super.setOnSeekBarChangeListener(progressDisplayingOnSeekBarChangeListener);
  }

  private void updateThumbWithProgressDisplay() {
    this.setThumb(mProgressDrawableFactory.createDrawable(mOriginalThumbDrawable, getProgress()));
  }
}
