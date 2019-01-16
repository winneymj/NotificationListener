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

    public void getInstalledApps()
    {
        final PackageManager pm = mContext.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appList = pm.queryIntentActivities(intent, 0);

        // Create list of apps entities to add to the database
//        List<NotificationAppsEntity> notificationApps = new ArrayList<NotificationAppsEntity>();
        int index = 0;

        for (ResolveInfo resolveInfo : appList)
        {
            String packName = resolveInfo.activityInfo.packageName;
            try
            {
                ApplicationInfo appInfo = pm.getApplicationInfo(packName, PackageManager.GET_META_DATA);
                //The log is not required so if you want to and I recommend during release you remove that statement.
                Log.i("Installed package :", appInfo.packageName);
                String pName = appInfo.loadLabel(pm).toString();
                Log.i("package name:", pName);

                // Save to data model
                mInstalledAppsModel.setAppName(pName);

//                TableRow tr = new TableRow(mContext);
//                tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//                Switch switchView = new Switch(mContext);
//                switchView.setText(Html.fromHtml(pName));
//                switchView.setTextSize(20);
//                switchView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
//                switchView.setPadding(10, 15,0,15);
//                tr.addView(switchView);
////                apps.addView(tr);

//                NotificationAppsEntity ent = new NotificationAppsEntity(index++, pName, appInfo.packageName, true);
//                notificationApps.add(ent);

            } catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
        }
//        // Add apps to DB
//        mDatabase.insertData(notificationApps);
    }

    public LiveData<List<String>> getAppsList()
    {
        return mInstalledAppsModel.installedApps;
    }
}
