package com.mychild.view.Parent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ParentModel;
import com.mychild.model.StudentDTO;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.view.CommonToApp.LoginActivity;
import com.mychild.view.CommonToApp.NotificationActivity;
import com.mychild.view.R;
import com.mychild.volley.AppController;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by Antony on 23-05-2015.
 */
public class ProfileFragmentActivity extends FragmentActivity implements View.OnClickListener, RequestCompletion, IOnSwichChildListener {
    public static final String TAG = ProfileFragmentActivity.class.getSimpleName();
    private TopBar topBar;
    private SwitchChildView switchChild;
    public AppController appController = null;
    private int selectedChildPosition = 0;
    private CircularImageView circularImageView;
    private Dialog dialog = null;
    private ParentModel parentModel = null;
    private ViewPager pager;
    String childName = null;
    private MyPagerAdapter adapter;
    private TextView mChildName, mChildId, mChildDOB, mChildMailId, mchildPhoneNo, mchildAddress,mChildPlace,mChildClass;
    ImageLoader imageLoader;
    //  private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        setContentView(R.layout.activity_child_profile);
        initializeView();
        setTopBar();
        switchChildBar();
        //  tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        List<Fragment> fragments = setUpView();
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);
        assignDataToView();

        //  inboxWebServiceCall();
       /* final int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
                        .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
        pager.setOffscreenPageLimit(2);
        pager.setPageTransformer(true, new CardTransformer(0.7f));
        tabs.setOnPageChangeListener(adapter);*/
    }

    private void assignDataToView() {
        parentModel = appController.getParentsData();
        appController.setParentData(parentModel);
        circularImageView.setImageResource(R.drawable.ic_launcher);
        circularImageView.setBorderColor(getResources().getColor(R.color.Darkgreen));
        circularImageView.setBorderWidth(3);
        circularImageView.addShadow();
        mChildName.setText(Constants.getChildNameAfterSelecting(appController.getSelectedChild(), appController
                .getParentsData()).toString());
        mChildId.setText(String.valueOf(Constants.getChildIdAfterSelecting(appController.getSelectedChild(), appController
                .getParentsData())));
        ArrayList<StudentDTO> arr = parentModel.getChildList();
        StudentDTO stu = arr.get(appController.getSelectedChild());
        try {
            mChildDOB.setText(stu.getDob().toString());
            mChildClass.setText(stu.getGrade().toString() + stu.getSection().toString());
            mChildMailId.setText(parentModel.getEmail().toString());
            mchildAddress.setText(stu.getAddressModel().getAddress().toString());
            mchildPhoneNo.setText(parentModel.getMobileNumber().toString());
            mChildPlace.setText(stu.getAddressModel().getPlace().toString());
            String encodedUrl = (stu.getStudentPhoto().toString()).replaceAll(" ", "%20");
            imageLoader.displayImage(encodedUrl, circularImageView);
        }
        catch (Exception c) {
            c.printStackTrace();
        }
    }

    private void initializeView() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        pager = (ViewPager) findViewById(R.id.viewPager_details);
        mChildName = (TextView) findViewById(R.id.child_nameTV);
        circularImageView = (CircularImageView) findViewById(R.id.circularImageView);
        mChildId = (TextView) findViewById(R.id.child_idTV);
        mChildDOB = (TextView) findViewById(R.id.DOBdataTV);
        mChildClass= (TextView) findViewById(R.id.classdataTV);
        mChildMailId = (TextView) findViewById(R.id.emailTV);
        mchildPhoneNo = (TextView) findViewById(R.id.mobilenoTV);
        mchildAddress = (TextView) findViewById(R.id.addressTV);
        mChildPlace= (TextView) findViewById(R.id.addressplaceTV);
    }

    private List<Fragment> setUpView() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(Fragment.instantiate(this,
                ProfileGeneralFragment.class.getName()));
        fList.add(Fragment.instantiate(this,
                ProfileMedicalFragment.class.getName()));
        fList.add(Fragment.instantiate(this,
                ProfileAchievementFragment.class.getName()));
        return fList;
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.profile));
        topBar.logoutIV.setOnClickListener(this);
        ImageView notification = (ImageView) topBar.findViewById(R.id.notification);
        notification.setVisibility(View.VISIBLE);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileFragmentActivity.this, NotificationActivity.class));
            }
        });
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.childNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
    }

    public void onSwitchChild(int selectedChildPosition) {

        childName = Constants.getChildNameAfterSelecting(selectedChildPosition, appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        assignDataToView();
        dialog.dismiss();
    }

    public void setSwitchChildDialogueData() {
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.logoutIV:
                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
                Constants.logOut(this);

                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
        switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
    }

    @Override
    public void onRequestCompletionError(String error) {

    }

    public class MyPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        private List<Fragment> fragments;
        private FragmentManager mFragmentManager;
        private final String[] TITLES = {"GENERAL", "MEDICAL", "ACHIEVEMENTS"};

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
            this.mFragmentManager = fm;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub

        }
    }
}
