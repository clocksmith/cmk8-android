package com.blunka.mk8assistant.main.configure;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.Constants;

/**
 * Created by clocksmith on 7/16/14.
 */
public class StarView extends ImageView {
  private boolean mIsFilled;

  public StarView(Context context) {
    this(context, null);
  }

  public StarView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public StarView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

      this.setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
              expandButton(Constants.PULSE_DURATION_MS);
              break;
            case MotionEvent.ACTION_UP:
              contractButton(Constants.PULSE_DURATION_MS);
              break;
            default:
              break;
          }
          return false;
        }
      });

    setFilled(false);
  }

  public void setFilled(boolean isFilled) {
    mIsFilled = isFilled;
    this.setImageDrawable(getResources().getDrawable(isFilled ?
        R.drawable.filled_star :
        R.drawable.hollow_star_b0b0b0));
  }

  public boolean isFilled() {
    return mIsFilled;
  }

  private void expandButton(long duration) {
    ObjectAnimator xAnimator = ObjectAnimator.ofFloat(this,
        "scaleX",
        1f,
        1.4f);
    ObjectAnimator yAnimator = ObjectAnimator.ofFloat(this,
        "scaleY",
        1f,
        1.4f);
    AnimatorSet animatorSetXY = new AnimatorSet();
    animatorSetXY.setDuration(duration);
    animatorSetXY.playTogether(xAnimator, yAnimator);
    animatorSetXY.start();
  }

  private void contractButton(long duration) {
    ObjectAnimator xAnimator = ObjectAnimator.ofFloat(this,
        "scaleX",
        1.4f,
        1f);
    ObjectAnimator yAnimator = ObjectAnimator.ofFloat(this,
        "scaleY",
        1.4f,
        1f);
    AnimatorSet animatorSetXY = new AnimatorSet();
    animatorSetXY.setDuration(duration);
    animatorSetXY.playTogether(xAnimator, yAnimator);
    animatorSetXY.start();
  }
}
