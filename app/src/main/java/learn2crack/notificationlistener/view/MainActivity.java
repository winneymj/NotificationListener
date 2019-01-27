package learn2crack.notificationlistener.view;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import learn2crack.notificationlistener.BluetoothLEService2;
import learn2crack.notificationlistener.R;
import learn2crack.notificationlistener.model.InstalledAppsModel;
import learn2crack.notificationlistener.viewmodel.InstalledAppsViewModel;
import learn2crack.notificationlistener.viewmodel.NotifierViewModel;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
import static android.provider.Settings.ACTION_BLUETOOTH_SETTINGS;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private InstalledAppsViewModel mAppViewModel;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    static final int ENABLE_BLUETOOTH_REQUEST = 1;  // The request code
    static final int ENABLE_LOCATION_REQUEST = 2;  // The request code

    private AlertDialog enableNotificationListenerAlertDialog;
    private AlertDialog enableBluetoothAlertDialog;
    private AlertDialog enableLocationAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            enableBluetoothAlertDialog = buildBLEServiceAlertDialog();
            enableBluetoothAlertDialog.show();
        }

        // If the user did not turn the notification listener service on we prompt him to do so
        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        List<String> permissions = isLocationPermissionsEnabled();
        if (permissions.size() > 0)
        {
            requestPermissions(permissions.toArray(new String[0]), 1);
        }

        setContentView(R.layout.activity_main);
        // Get an instance of the view model.
        mAppViewModel = new InstalledAppsViewModel(new InstalledAppsModel(), getApplicationContext());

        // Setup the view
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // See if we can initialize ble
//        ble = BluetoothHelper.getInstance();
//        ble.init(getApplicationContext()); // Only need to do once.
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        OneFragment frag1 = new OneFragment();
        frag1.setArguments(mAppViewModel); // Pass viewmodel to Fragment

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

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if enabled, false otherwise.
     */
    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivityForResult(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS), ENABLE_LOCATION_REQUEST);
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }

    /**
     * Build Bluetooth Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private AlertDialog buildBLEServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.bluetooth_enable_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                         startActivityForResult(new Intent(ACTION_BLUETOOTH_SETTINGS), ENABLE_BLUETOOTH_REQUEST);
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }

    /**
    * Returns true if location permissions needed else false if permission
    * has already been given.
    * @return True if enabled, false otherwise.
    */
    private List<String> isLocationPermissionsEnabled() {
        // Request permission from user to access location data so we can use BLE
        String[] allPermissionNeeded = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        List<String> permissionNeeded = new ArrayList<>();

        for (String permission : allPermissionNeeded) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED)
                permissionNeeded.add(permission);
        }

        return permissionNeeded;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// Check which request we're responding to
        if (requestCode == ENABLE_BLUETOOTH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Perhaps toast here.
            }
            else
            {
                // Can we make them accept bluetooth before we continue?
            }
            // If the user did not turn the notification listener service on we prompt him to do so
            if(!isNotificationServiceEnabled()){
                enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
                enableNotificationListenerAlertDialog.show();
            }
        }
        if (requestCode == ENABLE_LOCATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Perhaps toast here.
            }

            // We need location permission also enabled.
            List<String> permissions = isLocationPermissionsEnabled();
            if (permissions.size() > 0)
            {
                requestPermissions(permissions.toArray(new String[0]), 1);
            }

        }
    }

    //    /**
//     * Build Location Alert Dialog.
//     * Builds the alert dialog that pops up if the user has not turned
//     * the Notification Listener Service on yet.
//     * @return An alert dialog which leads to the location enabling screen
//     */
//    private AlertDialog buildLocationServiceAlertDialog(){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle(R.string.notification_listener_service);
//        alertDialogBuilder.setMessage(R.string.location_enable_explanation);
//        alertDialogBuilder.setPositiveButton(R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(ACTION_C));
//                    }
//                });
//        alertDialogBuilder.setNegativeButton(R.string.no,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // If you choose to not enable the notification listener
//                        // the app. will not work as expected
//                    }
//                });
//        return(alertDialogBuilder.create());
//    }
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
