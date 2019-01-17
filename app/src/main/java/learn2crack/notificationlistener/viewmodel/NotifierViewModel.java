package learn2crack.notificationlistener.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Html;
import android.util.Log;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import learn2crack.notificationlistener.BluetoothImp;
import learn2crack.notificationlistener.model.InstalledAppsModel;
import learn2crack.notificationlistener.persistence.db.entity.NotificationAppsEntity;
//import android.databinding.ObservableField;

public class NotifierViewModel extends ViewModel
{
    // Our datamodel
    private InstalledAppsModel mInstalledAppsModel;
    private Context mContext;
    private BluetoothImp ble;

    // Required
    public NotifierViewModel() {};

//    /*
//    * These are observable variables that the viewModel will update as appropriate
//    * The view components are bound directly to these objects and react to changes
//    * immediately, without the ViewModel needing to tell it to do so. They don't
//    * have to be public, they could be private with a public getter method too.
//    */
//    public final ObservableArrayMap<String, String> cells = new ObservableArrayMap<>();
//    public final ObservableField<String> winner = new ObservableField<>();

    public void setArguments(final Context appContext)
    {
        mContext = appContext;
        // Create the datamodel
        mInstalledAppsModel = new InstalledAppsModel();

        // Initialize the BLE
        initializeBLE();

//        // Load the installed apps
//        getInstalledApps();
    }

    private void initializeBLE()
    {
        // See if we can initialize ble
        ble = BluetoothImp.getInstance();
        ble.setArguments(mContext);
        if (ble.verifyBleSupported())
        {
            ble.init();
        }
        else
        {
            Toast.makeText(mContext, "BLE is not supported", Toast.LENGTH_SHORT).show();
            Log.i("BluetoothImp.init() : return false", "");
        }
    }

}
