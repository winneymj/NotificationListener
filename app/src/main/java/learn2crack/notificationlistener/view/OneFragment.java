package learn2crack.notificationlistener.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import learn2crack.notificationlistener.R;
import learn2crack.notificationlistener.persistence.db.AppDatabase;
import learn2crack.notificationlistener.persistence.db.entity.NotificationAppsEntity;
import learn2crack.notificationlistener.viewmodel.InstalledAppsViewModel;
import learn2crack.notificationlistener.viewmodel.NotifierViewModel;

public class OneFragment extends Fragment
{
    private TableLayout apps;
    private AppDatabase mDatabase;
    private InstalledAppsViewModel mViewModel;
    private Context mAppContext;

    /**
     * Constructor
     */
    public OneFragment(){};

    public void setArguments(final InstalledAppsViewModel viewModel)
    {
        mViewModel = viewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        apps = v.findViewById(R.id.tab1);

        mAppContext = getActivity().getApplicationContext();

        // Get instance of DB
        mDatabase = AppDatabase.getInstance(mAppContext);

        // Setup databindings with the viewmodel.
        initDataBindings();

        // Retrieve the installed apps
        mViewModel.getInstalledApps();

        return v;
    }

//    private void getInstalledApps()
//    {
//        final PackageManager pm = mAppContext.getPackageManager();
//
//        Intent intent = new Intent(Intent.ACTION_MAIN, null);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> appList = pm.queryIntentActivities(intent, 0);
//
//        // Create list of apps entities to add to the database
//        List<NotificationAppsEntity> notificationApps = new ArrayList<NotificationAppsEntity>();
//        int index = 0;
//
//        for (ResolveInfo resolveInfo : appList)
//        {
//            String packName = resolveInfo.activityInfo.packageName;
//            try
//            {
//                ApplicationInfo appInfo = pm.getApplicationInfo(packName, PackageManager.GET_META_DATA);
//                //The log is not required so if you want to and I recommend during release you remove that statement.
//                Log.i("Installed package :", appInfo.packageName);
//                String pName = appInfo.loadLabel(pm).toString();
//                Log.i("package name:", pName);
//
//                TableRow tr = new TableRow(mAppContext);
//                tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//                Switch switchView = new Switch(mAppContext);
//                switchView.setText(Html.fromHtml(pName));
//                switchView.setTextSize(20);
//                switchView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
//                switchView.setPadding(10, 15,0,15);
//                tr.addView(switchView);
//                apps.addView(tr);
//
//                NotificationAppsEntity ent = new NotificationAppsEntity(index++, pName, appInfo.packageName, true);
//                notificationApps.add(ent);
//
//            } catch (PackageManager.NameNotFoundException e)
//            {
//                e.printStackTrace();
//            }
//        }
////        // Add apps to DB
////        mDatabase.insertData(notificationApps);
//    }

    private void initDataBindings()
    {
        setUpInstalledAppsListener();
    }

    private void setUpInstalledAppsListener()
    {
        mViewModel.getAppsList().observe(this, this::onInstalledAppsChanged);
    }

    @VisibleForTesting
    public void onInstalledAppsChanged(List<String> appList)
    {
        Log.i("onInstalledAppsChanged:list size=", Integer.toString(appList.size()));
        for(String appName: appList)
        {
            Log.i("onInstalledAppsChanged:list name=", appName);

            // Update the display
            TableRow tr = new TableRow(mAppContext);
            tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            Switch switchView = new Switch(mAppContext);
            switchView.setText(Html.fromHtml(appName));
            switchView.setTextSize(20);
            switchView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
            switchView.setPadding(10, 15,0,15);
            tr.addView(switchView);
            apps.addView(tr);

            // TODO - Update database
        }
    }

}
