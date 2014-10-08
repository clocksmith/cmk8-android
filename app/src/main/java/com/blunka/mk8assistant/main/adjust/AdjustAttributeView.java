package com.blunka.mk8assistant.main.adjust;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.data.Attribute;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.ui.FontLoader;

/**
 * Created by clocksmith on 7/4/14.
 */
public class AdjustAttributeView extends LinearLayout {
  private static final String TAG = AdjustAttributeView.class.getSimpleName();

  public interface Listener {
    void onAttributeValueChanged(Attribute attribute, int value);
  }

  // Model.
  private Attribute mAttribute;

  // Listener.
  private Listener mListener;

  // View.
  private TextView mTitle;
  private AdjustButtonView mDecrementButton;
  private AdjustButtonView mIncrementButton;
  private SeekBar mSeekBar;

  public AdjustAttributeView(Context context) {
    this(context, null);
  }

  public AdjustAttributeView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AdjustAttributeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_adjust_attribute, this, true);

    mTitle = (TextView) findViewById(R.id.adjustAttribute_title);
    mDecrementButton = (AdjustButtonView) findViewById(R.id.adjustAttribute_decrementButton);
    mIncrementButton = (AdjustButtonView) findViewById(R.id.adjustAttribute_incrementButton);
    mSeekBar = (SeekBar) findViewById(R.id.adjustAttribute_seekBar);

    mTitle.setTypeface(FontLoader.getInstance().getRobotoCondensedLightTypeface());

    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          updateProgressButtons(progress);
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        showProgressButtons(seekBar.getProgress());
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        showStandardButtons();
        if (mListener != null) {
          mListener.onAttributeValueChanged(mAttribute, seekBar.getProgress());
        } else {
          Log.e(TAG, "mListener is null!");
        }
      }
    });

    setSymbolButtons();
  }

  public void setListener(Listener listener) {
    mListener = listener;
  }

  public void setAttribute(Attribute attribute) {
    mAttribute = attribute;
    mTitle.setText(attribute.getDisplayName(getContext()));
  }

  public void setProgress(int progress) {
    mSeekBar.setProgress(progress);
  }

  public void animateProgress(int progress) {
    ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mSeekBar,
        "progress",
        mSeekBar.getProgress(),
        progress);
    progressAnimator.setDuration(Constants.DEFAULT_CHANGE_DURATION_MS);
    progressAnimator.start();
  }

  private void setSymbolButtons() {
    mDecrementButton.setDecrementButton();
    mDecrementButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        int progress = mSeekBar.getProgress();
        if (progress > 0) {
          mSeekBar.setProgress(progress - 1);
        }
        if (mListener != null) {
          mListener.onAttributeValueChanged(mAttribute, mSeekBar.getProgress());
        }
      }
    });

    mIncrementButton.setIncrementButton();
    mIncrementButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        int progress = mSeekBar.getProgress();
        if (progress < mSeekBar.getMax()) {
          mSeekBar.setProgress(progress + 1);
        }
        if (mListener != null) {
          mListener.onAttributeValueChanged(mAttribute, mSeekBar.getProgress());
        }
      }
    });
  }

  private void showStandardButtons() {
    mDecrementButton.setStandardMode();
    mIncrementButton.setStandardMode();
  }

  private void showProgressButtons(int progress) {
    mDecrementButton.setProgressMode(progress);
    mIncrementButton.setProgressMode(progress);
  }

  private void updateProgressButtons(int progress) {
    mDecrementButton.updateProgress(progress);
    mIncrementButton.updateProgress(progress);
  }
}
