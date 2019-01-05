package learn2crack.notificationlistener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import learn2crack.notificationlistener.persistence.db.AppDatabase;
import learn2crack.notificationlistener.persistence.db.entity.NotificationAppsEntity;

public class OneFragment extends Fragment{

    private TableLayout apps;
    private AppDatabase mDatabase;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_one, container, false);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        final Context appContext = getActivity().getApplicationContext();
        final PackageManager pm = appContext.getPackageManager();

        // Get instance of DB
        mDatabase = AppDatabase.getInstance(appContext);

        View v = inflater.inflate(R.layout.fragment_one, container, false);
        apps = v.findViewById(R.id.tab1);

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appList = pm.queryIntentActivities(intent, 0);

        // Create list of apps entities to add to the database
        List<NotificationAppsEntity> notificationApps = new ArrayList<NotificationAppsEntity>();
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

                TableRow tr = new TableRow(appContext);
                tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Switch switchView = new Switch(appContext);
                switchView.setText(Html.fromHtml(pName));
                switchView.setTextSize(20);
                switchView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                switchView.setPadding(10, 15,0,15);
                tr.addView(switchView);
                apps.addView(tr);

                NotificationAppsEntity ent = new NotificationAppsEntity(index++, pName, appInfo.packageName, true);
                notificationApps.add(ent);

            } catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        // Add apps to DB
        mDatabase.insertData(notificationApps);

        return v;
    }
}
