package com.blunka.mk8assistant.main.adjust;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.shared.Constants;

/**
 * Created by clocksmith on 7/11/14.
 */
public class AdjustButtonView extends FrameLayout {
  private float mMaxRatio;
  private ProgressDrawableFactory mProgressDrawableFactory;
  private Drawable mOriginalDrawable;
  private Drawable mSymbolDrawable;

  private ImageView mButtonImage;


  public AdjustButtonView(Context context) {
    this(context, null);
  }

  public AdjustButtonView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AdjustButtonView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    mMaxRatio = getResources().getDimension(R.dimen.circle_diameter_with_shadow) /
        getResources().getDimension(R.dimen.circle_diameter);
    mProgressDrawableFactory = new ProgressDrawableFactory(getContext());

    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.view_adjust_button, this, true);

    mButtonImage = (ImageView) findViewById(R.id.adjustButton_buttonImage);
    mOriginalDrawable = getResources().getDrawable(R.drawable.circle_gray);

    // TODO(clocksmith): Make this behavior in a superclass.
    this.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            expandButton(Constants.PULSE_DURATION_MS);
            break;
          case MotionEvent.ACTION_UP:
            contractButton(Constants.PULSE_DURATION_MS, false);
            break;
          default:
            break;
        }
        return false;
      }
    });

    // Use increment button as default.
    setIncrementButton();
  }

  public void setDecrementButton() {
    mSymbolDrawable = getResources().getDrawable(R.drawable.decrement_symbol);
    drawSymbol();
  }

  public void setIncrementButton() {
    mSymbolDrawable = getResources().getDrawable(R.drawable.increment_symbol);
    drawSymbol();
  }

  public void setBlankButton() {
    mSymbolDrawable = null;
    drawSymbol();
  }

  public void setStandardMode() {
    contractButton(Constants.DEFAULT_CHANGE_DURATION_MS, true);
  }

  public void setProgressMode(int progress) {
    expandButton(Constants.DEFAULT_CHANGE_DURATION_MS);
    updateProgress(progress);
  }

  public void updateProgress(int progress) {
    mButtonImage.setImageDrawable(mProgressDrawableFactory.createDrawable(
        getResources().getDrawable(R.drawable.circle_gray),
        progress));
  }

  // TODO(clocksmith): make this into a pulse class/interface
  private void expandButton(long duration) {
    ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mButtonImage,
        "scaleX",
        1f,
        mMaxRatio);
    ObjectAnimator yAnimator = ObjectAnimator.ofFloat(mButtonImage,
        "scaleY",
        1f,
        mMaxRatio);
    AnimatorSet animatorSetXY = new AnimatorSet();
    animatorSetXY.setDuration(duration);
    animatorSetXY.playTogether(xAnimator, yAnimator);
    animatorSetXY.start();
  }

  private void contractButton(long duration, boolean setStandardMode) {
    ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mButtonImage,
        "scaleX",
        mMaxRatio,
        1f);
    ObjectAnimator yAnimator = ObjectAnimator.ofFloat(mButtonImage,
        "scaleY",
        mMaxRatio,
        1f);
    AnimatorSet animatorSetXY = new AnimatorSet();
    if (setStandardMode) {
      animatorSetXY.addListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
            // Do nothing
          }

          @Override
          public void onAnimationEnd(Animator animation) {
            drawSymbol();
          }

          @Override
          public void onAnimationCancel(Animator animation) {
            // Do nothing
          }

          @Override
          public void onAnimationRepeat(Animator animation) {
            // Do nothing
          }
      });
    }
    animatorSetXY.setDuration(duration);
    animatorSetXY.playTogether(xAnimator, yAnimator);
    animatorSetXY.start();
  }

  private void drawSymbol() {
    Bitmap newBitmap = Bitmap.createBitmap(mOriginalDrawable.getIntrinsicWidth(),
        mOriginalDrawable.getIntrinsicHeight(),
        Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);
    mOriginalDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    mOriginalDrawable.draw(canvas);
    if (mSymbolDrawable != null) {
      mSymbolDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      mSymbolDrawable.draw(canvas);
    }
    mButtonImage.setImageDrawable(new BitmapDrawable(getResources(), newBitmap));
  }
}
