package learn2crack.notificationlistener.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import learn2crack.notificationlistener.R;
import learn2crack.notificationlistener.viewmodel.NotifierViewModel;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NotifierViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get an instance of the view model.
        mViewModel = ViewModelProviders.of(this).get(NotifierViewModel.class);
        mViewModel.setArguments(getApplicationContext());

        // Setup the view
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // See if we can initialize ble
//        ble = BluetoothImp.getInstance();
//        ble.init(getApplicationContext()); // Only need to do once.

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        OneFragment frag1 = new OneFragment();
        frag1.setArguments(mViewModel); // Pass viewmodel to Fragment

        adapter.addFragment(frag1, "ONE");
        adapter.addFragment(new TwoFragment(), "TWO");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

//    /**
//     * Method called when permission is request to Location for example.  User is prompted.
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult (int requestCode, String[] permissions,
//                                            int[] grantResults) {
//        int index = 0;
//        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
//        for (String permission : permissions){
//            PermissionsMap.put(permission, grantResults[index]);
//            index++;
//        }
//
//        if((PermissionsMap.get(Manifest.permission.ACCESS_FINE_LOCATION) != 0)
//                || PermissionsMap.get(Manifest.permission.ACCESS_COARSE_LOCATION) != 0){
//            Toast.makeText(this, "Location permission is a must", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        else
//        {
//            this.ble.initializeBle(this);
//        }
//    }
}
