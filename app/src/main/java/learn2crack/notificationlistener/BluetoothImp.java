package learn2crack.notificationlistener;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothImp {

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private BluetoothLeScanner mBleScanner;
    private Context appContext;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private final static int REQUEST_ENABLE_BT = 1;


    public BluetoothImp(final Context appContext) {

        this.appContext = appContext;
    }

    public Boolean initializeBle(final AppCompatActivity activity) {
        Log.i("BluetoothImp.init() : enter", "");

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) this.appContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Log.i("BluetoothImp.init() : no ble device or not enabled", "");
            return false;
        }

        if (Build.VERSION.SDK_INT >= 21) {
            mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();
            settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            filters = new ArrayList<ScanFilter>();
        }

        mHandler = new Handler();
        scanLeDevice(true);

        Log.i("BluetoothImp.init() : exit", "");

        return true;
    }

    public boolean verifyBleSupported()
    {
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!this.appContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this.appContext, "BLE is not supported", Toast.LENGTH_SHORT).show();
            Log.i("BluetoothImp.init() : return false", "");
            return false;
        }

        return true;
    }

//    private void waitForPermission(final AppCompatActivity activity) {
//
//        boolean isAnswered = false;
//
//        while (!isAnswered) {
//            if (ActivityCompat.checkSelfPermission(this.appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(this.appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                Log.d("isanswered", String.valueOf(isAnswered));
//            } else {
//                isAnswered = true;
//                Log.d("isanswered", String.valueOf(isAnswered));
//            }
//            android.os.SystemClock.sleep(1000);
//        }
//    }

    // Returns true if permissions needed else false if permission
    // has already been given.
    public boolean checkAndRequestPermissions(final AppCompatActivity activity) {
        // Request permission from user to access location data so we can use BLE
        String[] allPermissionNeeded = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        List<String> permissionNeeded = new ArrayList<>();

        for (String permission : allPermissionNeeded) {
            if (ContextCompat.checkSelfPermission(this.appContext, permission) != PackageManager.PERMISSION_GRANTED)
                permissionNeeded.add(permission);
        }

        // We need to ask for permission
        if (permissionNeeded.size() > 0) {
            activity.requestPermissions(permissionNeeded.toArray(new String[0]), 1);
            return true;
        }

        return false;
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mBleScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mBleScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mBleScanner.stopScan(mScanCallback);
            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            // Callback type 1 = CALLBACK_TYPE_ALL_MATCHES
            Log.i("ScanCallback().onScanResult():callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
//            BluetoothDevice btDevice = result.getDevice();
//            connectToDevice(btDevice);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanCallback().onBatchScanResults - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("ScanCallback().onScanFailed: Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
//                    AsyncTask.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.i("onLeScan", device.toString());
////                            connectToDevice(device);
//                        }
//                    });
//
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            Log.i("onLeScan", device.toString());
////                            connectToDevice(device);
////                        }
////                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            mGatt = device.connectGatt(appContext, false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            gatt.readCharacteristic(services.get(1).getCharacteristics().get
                    (0));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            gatt.disconnect();
        }
    };
}

