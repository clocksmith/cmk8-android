package com.blunka.mk8assistant.main.maps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.blunka.mk8assistant.R;
import com.blunka.mk8assistant.analytics.AnalyticsFragment;
import com.blunka.mk8assistant.data.courses.Course;
import com.blunka.mk8assistant.data.courses.CourseData;
import com.blunka.mk8assistant.data.courses.CourseUtils;
import com.blunka.mk8assistant.data.courses.Cup;
import com.blunka.mk8assistant.shared.ArgKeys;
import com.blunka.mk8assistant.shared.Constants;
import com.blunka.mk8assistant.shared.FilteredLogger;
import com.blunka.mk8assistant.shared.ui.UiUtils;

/**
 * Created by clocksmith on 9/7/14.
 */
public class MapsFragment extends AnalyticsFragment {
  private static final String TAG = MapsFragment.class.getSimpleName();

  private static final int NUM_COURSES_PER_CUP = 4;

  public interface Listener {
    void onMapsModelUpdated(MapsModel mapsModel);
  }

  private MapsModel mMapsModel;

  private LinearLayout mRootView;
  private Spinner mCupSpinner;
  private Spinner mCourseSpinner;
  private ImageView mUpwardTriangle;
  private MapViewPager mMapViewPager;
  private ImageView mDownwardTriangle;

  private Listener mListener;
  private CupSpinnerAdapter mCupSpinnerAdapter;
  private CourseSpinnerAdapter mCourseSpinnerAdapter;
  private MapViewPagerAdapter mMapViewPagerAdapter;
  private CupSpinnerSelectListener mCupSpinnerSelectListener;
  private CourseSpinnerSelectListener mCourseSpinnerSelectListener;
  private MapViewPageChangeListener mMapViewPageChangeListener;

  public static MapsFragment newInstance(MapsModel mapsModel) {
    MapsFragment mapsFragment = new MapsFragment();

    Bundle args = new Bundle();
    args.putParcelable(ArgKeys.MAPS_MODEL, mapsModel);
    mapsFragment.setArguments(args);

    return mapsFragment;
  }

  @Override
  public String getScreenName() {
    return MapsFragment.class.getName();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (activity instanceof Listener) {
      mListener = (Listener) activity;
    } else {
      Log.e(TAG, "Parent activity does not implement Listener");
    }
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle bundle;
    if (savedInstanceState != null) {
      bundle = savedInstanceState;
    } else {
      bundle = getArguments();
    }
    mMapsModel = bundle.getParcelable(ArgKeys.MAPS_MODEL);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_maps, container, false);

    mRootView = (LinearLayout) view.findViewById(R.id.mapsFragment_rootView);
    mCupSpinner = (Spinner) view.findViewById(R.id.mapsFragment_cupSpinner);
    mCourseSpinner = (Spinner) view.findViewById(R.id.mapsFragment_courseSpinner);
    mUpwardTriangle = (ImageView) view.findViewById(R.id.mapsFragment_upwardTriangle);
    mMapViewPager = (MapViewPager) view.findViewById(R.id.mapsFragment_mapViewPager);
    mDownwardTriangle = (ImageView) view.findViewById(R.id.mapsFragment_downwardTriangle);

    UiUtils.setCustomVerticalViewPagerScroller(mMapViewPager,
        Constants.VERTICAL_VIEW_PAGER_SCROLL_DURATION_MS);

    // Initialize and set adapters.
    mCupSpinnerAdapter = new CupSpinnerAdapter(this.getActivity());
    mCupSpinner.setAdapter(mCupSpinnerAdapter);
    mCourseSpinnerAdapter = new CourseSpinnerAdapter(this.getActivity(), mMapsModel.getCup());
    mCourseSpinner.setAdapter(mCourseSpinnerAdapter);
    mMapViewPagerAdapter = new MapViewPagerAdapter(this.getActivity());
    mMapViewPager.setAdapter(mMapViewPagerAdapter);

    // Initialize and set listeners.
    mCupSpinnerSelectListener = new CupSpinnerSelectListener();
    mCupSpinner.setOnItemSelectedListener(mCupSpinnerSelectListener);
    mCourseSpinnerSelectListener = new CourseSpinnerSelectListener();
    mCourseSpinner.setOnItemSelectedListener(mCourseSpinnerSelectListener);
    mMapViewPageChangeListener = new MapViewPageChangeListener();
    mMapViewPager.setOnPageChangeListener(mMapViewPageChangeListener);

    mMapViewPager.setCurrentItem(mMapsModel.getCourse().getIndex(), false);

    // Update triangles.
    updateTriangles();

    return view;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    UiUtils.unbindDrawables(mRootView);
  }

  private void setCupSelectionSilently(int cupIndex) {
    FilteredLogger.d(TAG, "setCupSelectionSilently cupIndex: " + cupIndex);
    if (mCupSpinner.getSelectedItemPosition() != cupIndex) {
      mCupSpinnerSelectListener.setNextSelectSilent(true);
      mCupSpinner.setSelection(cupIndex, true);
    }
  }

  private void setCourseSelectionSilently(int courseIndex) {
    FilteredLogger.d(TAG, "setCourseSelectionSilently courseIndex: " + courseIndex);
    if (mCourseSpinner.getSelectedItemPosition() != courseIndex) {
      mCourseSpinnerSelectListener.setNextSelectSilent(true);
      mCourseSpinner.setSelection(courseIndex, true);
    }
  }

  private void setCurrentMapItemSilently(int mapIndex, boolean animate) {
    FilteredLogger.d(TAG, "setCurrentMapItemSilently mapIndex: " + mapIndex);
    if (mMapViewPager.getCurrentItem() != mapIndex) {
      mMapViewPageChangeListener.setNextChangeSilent(true);
      mMapViewPager.setCurrentItem(mapIndex, animate);
    }
  }

  private void updateTriangles() {
    mUpwardTriangle.setVisibility(mMapViewPager.getCurrentItem() == 0 ?
        View.INVISIBLE :
        View.VISIBLE);
    mDownwardTriangle.setVisibility(
        mMapViewPager.getCurrentItem() == mMapViewPagerAdapter.getCount() - 1 ?
            View.INVISIBLE :
            View.VISIBLE);
  }

  private class CupSpinnerSelectListener implements AdapterView.OnItemSelectedListener {
    private boolean mIsSilent;

    public void setNextSelectSilent(boolean isSilent) {
      mIsSilent = isSilent;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      if (!mIsSilent) {
        Cup cup = CourseData.CUPS.get(position);
        mCourseSpinnerAdapter.updateObjects(cup.getCourses());
        setCourseSelectionSilently(0);
        setCurrentMapItemSilently(((Course) mCourseSpinner.getSelectedItem()).getIndex(), true);
        mMapsModel.setCup(cup);
        mMapsModel.setCourse(cup.getCourses().get(0));
        if (mListener != null) {
          mListener.onMapsModelUpdated(mMapsModel);
        }
      }

      mIsSilent = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
      // Do nothing.
    }
  }

  private class CourseSpinnerSelectListener implements AdapterView.OnItemSelectedListener {
    private boolean mIsSilent;

    public void setNextSelectSilent(boolean isSilent) {
      mIsSilent = isSilent;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      if (!mIsSilent) {
        Course course = ((Course) mCourseSpinner.getSelectedItem());
        setCurrentMapItemSilently(course.getIndex(), true);
        mMapsModel.setCourse(course);
        if (mListener != null) {
          mListener.onMapsModelUpdated(mMapsModel);
        }
      }

      mIsSilent = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
      // Do nothing.
    }
  }

  private class MapViewPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
    private boolean mIsSilent;

    public void setNextChangeSilent(boolean isSilent) {
      mIsSilent = isSilent;
    }

    @Override
    public void onPageSelected(int position) {
      updateTriangles();

      if (!mIsSilent) {
        int cupIndex = position / NUM_COURSES_PER_CUP;
        int courseIndex = position % NUM_COURSES_PER_CUP;
        setCupSelectionSilently(cupIndex);
        mCourseSpinnerAdapter.updateObjects(CourseData.CUPS.get(cupIndex).getCourses());
        setCourseSelectionSilently(courseIndex);
        mMapsModel.setCup(CourseData.CUPS.get(cupIndex));
        mMapsModel.setCourse(CourseData.CUPS.get(cupIndex).getCourses().get(courseIndex));
        if (mListener != null) {
          mListener.onMapsModelUpdated(mMapsModel);
        }
      }

      mIsSilent = false;
    }
  }
}
