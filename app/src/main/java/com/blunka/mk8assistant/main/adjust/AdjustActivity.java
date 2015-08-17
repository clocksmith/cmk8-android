package com.blunka.mk8assistant.main.adjust;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.blunka.mk8assistant.ConfigureMK8Application;
import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsUtils;
import com.blunka.mk8assistant.main.configure.ConfigureModel;
import com.blunka.mk8assistant.shared.AnalyticsLogger;
import com.blunka.mk8assistant.shared.ArgKeys;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.preferences.LastUsedModelUtils;
import com.blunka.mk8assistant.shared.ui.FontLoader;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by clocksmith on 7/17/14.
 */
public class AdjustActivity extends FragmentActivity implements AdjustFragment.Listener {
  private static final String TAG = AdjustActivity.class.getSimpleName();

  // Analaytics Tracker.
  private Tracker mTracker;

  // Model.
  private AdjustModel mAdjustModel;

  // View.
  private Button mCancelButton;
  private Button mOkButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    FilteredLogger.d(TAG, "onCreate()");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_adjust);

    mTracker = ((ConfigureMK8Application) getApplication()).getAppTracker();
    mTracker.setScreenName(AdjustActivity.class.getName());
    mTracker.send(new HitBuilders.AppViewBuilder().build());

    mCancelButton = (Button) findViewById(R.id.adjustActivity_cancel);
    mOkButton = (Button) findViewById(R.id.adjustActivity_ok);

    if (savedInstanceState != null) {
      mAdjustModel = savedInstanceState.getParcelable(ArgKeys.ADJUST_MODEL);
    }
    if (mAdjustModel == null) {
      AdjustModel lastUsedAdjustModel = LastUsedModelUtils.getLastUsedAdjustModel(this);
      mAdjustModel = lastUsedAdjustModel == null ? new AdjustModel() : lastUsedAdjustModel;
    }

    getSupportFragmentManager().beginTransaction().add(R.id.adjustActivity_fragmentContainer,
        AdjustFragment.newInstance(mAdjustModel)).commit();

    mCancelButton.setTypeface(FontLoader.getInstance().getRobotoNormalTypeface());
    mCancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AnalyticsUtils.sendAdjustConfigurationCancelEvent(mTracker,
            mAdjustModel.getSelectedConfigurationChoice(),
            mAdjustModel.getSelectedConfiguration());
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
      }
    });

    mOkButton.setTypeface(FontLoader.getInstance().getRobotoNormalTypeface());
    mOkButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AnalyticsUtils.sendAdjustConfigurationOkEvent(mTracker,
            mAdjustModel.getSelectedConfigurationChoice(),
            mAdjustModel.getSelectedConfiguration());
        GetBuildTask getBuildTask = new GetBuildTask();
        getBuildTask.execute(mAdjustModel);
      }
    });
  }

  @Override
  public void onResume() {
    FilteredLogger.d(TAG, "onResume()");
    super.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putParcelable(ArgKeys.ADJUST_MODEL, mAdjustModel);

    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public void onPause() {
    FilteredLogger.d(TAG, "onPause()");
    super.onPause();
  }

  @Override
  public void onAdjustModelUpdated(AdjustModel adjustModel) {
    FilteredLogger.d(TAG, "onAdjustModelUpdated()");
    mAdjustModel = adjustModel;
  }

  private class GetBuildTask extends AsyncTask<AdjustModel, Void, ConfigureModel> {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onPreExecute() {
      mProgressDialog = new ProgressDialog(AdjustActivity.this);
      mProgressDialog.show();
    }

    @Override
    protected ConfigureModel doInBackground(AdjustModel... adjustModels) {
      LastUsedModelUtils.setLastUsedAdjustModel(AdjustActivity.this, mAdjustModel);
      AdjustToBuildConverter converter = new AdjustToBuildConverter();
      return converter.convert(adjustModels[0]);
    }

    @Override
    protected void onPostExecute(ConfigureModel configureModel) {
      if (mProgressDialog != null && mProgressDialog.isShowing()) {
        mProgressDialog.dismiss();
      }

      Intent returnIntent = new Intent();
      if (configureModel != null) {
        AnalyticsUtils.sendBuildConfigurationEvent(mTracker,
            mAdjustModel.getSelectedConfigurationChoice(),
            configureModel.getKartConfiguration());
        returnIntent.putExtra(ArgKeys.BUILD_MODEL, configureModel);
      } else {
        AnalyticsLogger.e(TAG, mTracker, "buildModel is null!");
      }
      setResult(RESULT_OK, returnIntent);
      finish();
    }
  }
}

