package com.blunka.mk8assistant.shared.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsDialogFragment;
import com.blunka.mk8assistant.shared.AnalyticsLogger;
import com.blunka.mk8assistant.shared.ArgKeys;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by clocksmith on 7/25/14.
 */
public abstract class TransitionImageDialogFragment extends AnalyticsDialogFragment {
  private static final String TAG = TransitionImageDialogFragment.class.getSimpleName();

  private static final float SHADE_ALPHA = 0.5f;

  private static final int IMAGE_TRANSITION_DURATION_MS = 350;

  private static final int MAIN_CONTENT_FADE_IN_DURAION_MS = 475;

  private static final int SHADE_FADE_IN_DURATION_MS = 600;

  private List<Integer> mStartXs;
  private List<Integer> mStartYs;
  private List<Integer> mStartWidths;
  private List<Integer> mStartHeights;

  private List<Integer> mTopMargins;
  private List<Integer> mLeftMargins;
  private List<ImageView> mTransitionImageViews;

  private boolean mIsShowing;
  private CountDownLatch mOnGlobalLayoutCountdownLatch;
  private AnimatorSet mTransitionInAnimatorSet;

  private long mStartTimeMs;

  // Shared View
  protected View mShade;
  protected LinearLayout mMainContent;
  // The blank image views that the transition image views will eventually overlay.
  protected List<ImageView> mShadowImageViews;

  public TransitionImageDialogFragment() {
  }

  protected void insertSharedArgs(Bundle args, List<ImageView> startImageViews) {
    List<Integer> startXs = Lists.newArrayList();
    List<Integer> startYs = Lists.newArrayList();
    List<Integer> startWidths = Lists.newArrayList();
    List<Integer> startHeights = Lists.newArrayList();
    for (ImageView startImageView : startImageViews) {
      int[] startLocation = new int[2];
      startImageView.getLocationInWindow(startLocation);
      startXs.add(startLocation[0]);
      startYs.add(startLocation[1]);
      startWidths.add(startImageView.getWidth());
      startHeights.add(startImageView.getHeight());
    }
    args.putIntegerArrayList(ArgKeys.START_XS, (ArrayList<Integer>) startXs);
    args.putIntegerArrayList(ArgKeys.START_YS, (ArrayList<Integer>) startYs);
    args.putIntegerArrayList(ArgKeys.START_WIDTHS, (ArrayList<Integer>) startWidths);
    args.putIntegerArrayList(ArgKeys.START_HEIGHTS, (ArrayList<Integer>) startHeights);
  }

  protected void unpackSharedArgs(Bundle bundle) {
    mStartXs = bundle.getIntegerArrayList(ArgKeys.START_XS);
    mStartYs = bundle.getIntegerArrayList(ArgKeys.START_YS);
    mStartWidths = bundle.getIntegerArrayList(ArgKeys.START_WIDTHS);
    mStartHeights = bundle.getIntegerArrayList(ArgKeys.START_HEIGHTS);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    mStartTimeMs = System.currentTimeMillis();
    FilteredLogger.d(TAG, "onCreate(): " + (System.currentTimeMillis() - mStartTimeMs) + "ms");
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MK8DialogStyle);
    mIsShowing = false;
    mShadowImageViews = Lists.newArrayList();
    mTopMargins = Lists.newArrayList();
    mLeftMargins = Lists.newArrayList();
    mTransitionImageViews = Lists.newArrayList();
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    FilteredLogger.d(TAG, "onCreateDialog(): " + (System.currentTimeMillis() - mStartTimeMs) + "ms");
    return new TransitionOutThenDismissDialog(getActivity(), getTheme());
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    FilteredLogger.d(TAG, "onViewCreated(): " + (System.currentTimeMillis() - mStartTimeMs) + "ms");
    super.onViewCreated(view, savedInstanceState);
    mOnGlobalLayoutCountdownLatch = new CountDownLatch(mShadowImageViews.size());
    for (final ImageView shadowImageView : mShadowImageViews) {
      shadowImageView.getViewTreeObserver().addOnGlobalLayoutListener(
          new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
              UiUtils.removeOnGlobalLayoutListener(shadowImageView, this);
              mOnGlobalLayoutCountdownLatch.countDown();
            }
          }
      );
    }
  }

  @Override
  public void onResume() {
    FilteredLogger.d(TAG, "onResume(): " + +(System.currentTimeMillis() - mStartTimeMs) + "ms");
    super.onResume();
    if (!mIsShowing) {
      setupViewForTransitionIn();
      // Animate the shade alpha here so the user gets some feedback before waiting for the countdown
      // latch to hit 0.
      ObjectAnimator shadeAlphaAnimator = ObjectAnimator.ofFloat(mShade, "alpha", 0f, SHADE_ALPHA);
      shadeAlphaAnimator.setDuration(SHADE_FADE_IN_DURATION_MS);
      shadeAlphaAnimator.start();
      new TransitionInAsyncTask().execute((Void) null);
    }
  }

  protected abstract Drawable getTransitionDrawable(int imageIndex, ImageView imageView);

  protected abstract void onTransitionInAnimationStart();

  protected abstract void onTransitionOutAnimationEnd();

  protected void setupViewForTransitionIn() {
    FilteredLogger.d(TAG, "setupViewForTransitionIn(): " +
        (System.currentTimeMillis() - mStartTimeMs) + "ms");
    // Set width, height, and gravity of dialog window.
    View mainDecorView = getActivity().getWindow().getDecorView().findViewById(
        android.R.id.content);
    int width = mainDecorView.getWidth();
    int height = mainDecorView.getHeight();
    getDialog().getWindow().setLayout(width, height);
    getDialog().getWindow().setGravity(Gravity.CENTER);

    // Hide the shade and main content.
    mShade.setAlpha(0f);
    mMainContent.setAlpha(0f);

    // Status bar height.
    int statusBarHeight = 0;
    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      statusBarHeight = getResources().getDimensionPixelSize(resourceId);
    }

    mOnGlobalLayoutCountdownLatch = new CountDownLatch(mStartXs.size());
    for (int imageIndex = 0; imageIndex < mStartXs.size(); imageIndex++) {
      // Margins of transition image.
      mTopMargins.add(mStartYs.get(imageIndex) - statusBarHeight);
      mLeftMargins.add(mStartXs.get(imageIndex));
      // Create the image views.
      final ImageView transitionImageView = new ImageView(getActivity());
      mTransitionImageViews.add(transitionImageView);
      FrameLayout decorView = (FrameLayout) getDialog().getWindow().getDecorView();
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
          mStartWidths.get(imageIndex),
          mStartHeights.get(imageIndex));
      final int topMargin = mTopMargins.get(imageIndex);
      final int leftMargin = mLeftMargins.get(imageIndex);
      layoutParams.topMargin = topMargin;
      layoutParams.leftMargin = leftMargin;
      transitionImageView.setImageDrawable(getTransitionDrawable(imageIndex, transitionImageView));
      transitionImageView.setLayoutParams(layoutParams);
      transitionImageView.setPivotX(0);
      transitionImageView.setPivotY(0);
      decorView.addView(transitionImageView);
    }
  }

  protected void setupViewForTransitionOut() {
  }

  private void transitionIn() {
    FilteredLogger.d(TAG, "transitionIn(): " + (System.currentTimeMillis() - mStartTimeMs) + "ms");

    List<Animator> animators = Lists.newArrayList();
    for (int imageIndex = 0; imageIndex < mShadowImageViews.size(); imageIndex++) {
      final int currentImageIndex = imageIndex;

      // Shadow image view location.
      int[] shadowImageViewLocation = new int[2];
      mShadowImageViews.get(imageIndex).getLocationInWindow(shadowImageViewLocation);

      // Animate the x position.
      ObjectAnimator xAnimator = ObjectAnimator.ofFloat(
          mTransitionImageViews.get(currentImageIndex),
          "x",
          mLeftMargins.get(imageIndex),
          shadowImageViewLocation[0]);
      xAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
      animators.add(xAnimator);

      // Animate the y position.
      ObjectAnimator yAnimator = ObjectAnimator.ofFloat(
          mTransitionImageViews.get(currentImageIndex),
          "y",
          mTopMargins.get(imageIndex),
          shadowImageViewLocation[1]);
      yAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
      animators.add(yAnimator);

      int finalTransitionImageViewWidth = mShadowImageViews.get(currentImageIndex).getWidth();
      int finalTransitionImageViewHeight = mShadowImageViews.get(currentImageIndex).getHeight();

      // Animate the width.
      if (mStartWidths.get(currentImageIndex) != finalTransitionImageViewWidth) {
        ValueAnimator widthAnimator = ValueAnimator.ofInt(mStartWidths.get(currentImageIndex),
            finalTransitionImageViewWidth);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            mTransitionImageViews.get(currentImageIndex).getLayoutParams().width =
                ((Integer) animation.getAnimatedValue());
            mTransitionImageViews.get(currentImageIndex).requestLayout();
          }
        });
        widthAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
        animators.add(widthAnimator);
      }

      // Animate the height.
      if (mStartHeights.get(currentImageIndex) != finalTransitionImageViewHeight) {
        ValueAnimator heightAnimator = ValueAnimator.ofInt(mStartHeights.get(imageIndex),
            finalTransitionImageViewHeight);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            mTransitionImageViews.get(currentImageIndex).getLayoutParams().height =
                ((Integer) animation.getAnimatedValue());
            mTransitionImageViews.get(currentImageIndex).requestLayout();
          }
        });
        heightAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
        animators.add(heightAnimator);
      }

      // Animate the main content fade.
      ObjectAnimator mainContentFadeAnimator = ObjectAnimator.ofFloat(mMainContent,
          "alpha",
          0f,
          1f);
      mainContentFadeAnimator.setDuration(MAIN_CONTENT_FADE_IN_DURAION_MS);
      animators.add(mainContentFadeAnimator);
    }

    mTransitionInAnimatorSet = new AnimatorSet();
    mTransitionInAnimatorSet.playTogether(animators);
    mTransitionInAnimatorSet.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {
        onTransitionInAnimationStart();
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mIsShowing = true;
      }

      @Override
      public void onAnimationCancel(Animator animation) {
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }
    });
    mTransitionInAnimatorSet.start();
  }

  private void transitionOut(final Runnable dismissSuper) {
    FilteredLogger.d(TAG, "transitionOut()");
    setupViewForTransitionOut();
    List<Animator> animators = Lists.newArrayList();
    for (int imageIndex = 0; imageIndex < mShadowImageViews.size(); imageIndex++) {
      final int currentImageIndex = imageIndex;

      // TODO(clocksmith): pick one.
      // Shadow image view location.
      int[] shadowImageViewLocation = new int[2];
      mShadowImageViews.get(imageIndex).getLocationInWindow(shadowImageViewLocation);
      // Transition image view location.
      int[] transitionImageViewLocation = new int[2];
      mTransitionImageViews.get(imageIndex).getLocationInWindow(shadowImageViewLocation);

      // Animate the x position.
      ObjectAnimator xAnimator = ObjectAnimator.ofFloat(
          mTransitionImageViews.get(currentImageIndex),
          "x",
          shadowImageViewLocation[0],
          mLeftMargins.get(imageIndex));
      xAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
      animators.add(xAnimator);

      // Animate the y position.
      ObjectAnimator yAnimator = ObjectAnimator.ofFloat(
          mTransitionImageViews.get(currentImageIndex),
          "y",
          shadowImageViewLocation[1],
          mTopMargins.get(imageIndex));
      yAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
      animators.add(yAnimator);

      // Get the starting transition size.
      int startTransitionImageViewWidth = mShadowImageViews.get(currentImageIndex).getWidth();
      int startTransitionImageViewHeight = mShadowImageViews.get(currentImageIndex).getHeight();

      // Animate the width.
      if (mStartWidths.get(currentImageIndex) != startTransitionImageViewWidth) {
        ValueAnimator widthAnimator = ValueAnimator.ofInt(startTransitionImageViewWidth,
            mStartWidths.get(currentImageIndex));
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            mTransitionImageViews.get(currentImageIndex).getLayoutParams().width =
                ((Integer) animation.getAnimatedValue());
            mTransitionImageViews.get(currentImageIndex).requestLayout();
          }
        });
        widthAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
        animators.add(widthAnimator);
      }

      // Animate the height.
      if (mStartHeights.get(currentImageIndex) != startTransitionImageViewHeight) {
        ValueAnimator heightAnimator = ValueAnimator.ofInt(startTransitionImageViewHeight,
            mStartHeights.get(imageIndex));
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            mTransitionImageViews.get(currentImageIndex).getLayoutParams().height =
                ((Integer) animation.getAnimatedValue());
            mTransitionImageViews.get(currentImageIndex).requestLayout();
          }
        });
        heightAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
        animators.add(heightAnimator);
      }

      // Animate the shade alpha.
      ObjectAnimator shadeAlphaAnimator = ObjectAnimator.ofFloat(mShade, "alpha", SHADE_ALPHA, 0f);
      shadeAlphaAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
      animators.add(shadeAlphaAnimator);

      // Animate the main content fade.
      ObjectAnimator mainContentFadeAnimator = ObjectAnimator.ofFloat(mMainContent, "alpha", 1f, 0f);
      mainContentFadeAnimator.setDuration(IMAGE_TRANSITION_DURATION_MS);
      animators.add(mainContentFadeAnimator);
    }

    if (mTransitionInAnimatorSet != null && mTransitionInAnimatorSet.isRunning()) {
      mTransitionInAnimatorSet.cancel();
    }
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(animators);
    animatorSet.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        handleDone();
      }

      @Override
      public void onAnimationCancel(Animator animation) {
        handleDone();
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }

      private void handleDone() {
        onTransitionOutAnimationEnd();
        mIsShowing = false;
        dismissSuper.run();
      }
    });
    animatorSet.start();
  }

  protected class TransitionOutThenDismissDialog extends Dialog {
    private boolean mDismissWithoutAnimation;

    public TransitionOutThenDismissDialog(Context context, int theme) {
      super(context, theme);
      mDismissWithoutAnimation = false;
    }

    public void setDismissWithoutAnimation(boolean dismissWithoutAnimation) {
      mDismissWithoutAnimation = dismissWithoutAnimation;
    }

    @Override
    public void dismiss() {
      FilteredLogger.d(TAG, "TransitionOutThenDismissDialog.dismiss()");
      if (mDismissWithoutAnimation) {
        super.dismiss();
      } else {
        Runnable dialogDismissSuper = new Runnable() {
          @Override
          public void run() {
            TransitionOutThenDismissDialog.super.dismiss();
          }
        };
        transitionOut(dialogDismissSuper);
      }
    }
  }

  private class TransitionInAsyncTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... inputs) {
      try {
        mOnGlobalLayoutCountdownLatch.await();
      } catch (InterruptedException e) {
        AnalyticsLogger.e(TAG, mTracker, "mOnGlobalLayoutCountdownLatch interrupted!");
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      transitionIn();
    }
  }
}
