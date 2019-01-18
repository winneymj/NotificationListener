package learn2crack.notificationlistener.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.List;

import learn2crack.notificationlistener.model.AppDataModel;
import learn2crack.notificationlistener.model.InstalledAppsModel;

public class InstalledAppsViewModel extends ViewModel
{
    private InstalledAppsModel _model;
    private Context mAppContext;
    private MutableLiveData<List<AppDataModel>> _installedApps = new MutableLiveData<>();


    public InstalledAppsViewModel(final InstalledAppsModel model, final Context appContext)
    {
        _model = model;
        mAppContext = appContext;
    }

    public void getInstalledApps()
    {
        final PackageManager pm = mAppContext.getPackageManager();

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
                _model.addApp(pName, appInfo.packageName, false);

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
        _installedApps.setValue(_model.data());
    }

    public LiveData<List<AppDataModel>> getAppsList()
    {
        return _installedApps;
    }

    public void setAppEnabledState(final String name, final Boolean enabled)
    {
        _model.setAppEnabled(name, enabled);
    }
}
