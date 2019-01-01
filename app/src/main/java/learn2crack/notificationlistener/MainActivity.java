package learn2crack.notificationlistener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.List;
import android.util.Log;



public class MainActivity extends Activity {

    TableLayout tab;
    TableLayout apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab = (TableLayout) findViewById(R.id.tab);
        apps = (TableLayout) findViewById(R.id.apps);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        final PackageManager pm = getApplicationContext().getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appList = pm.queryIntentActivities(intent, 0);

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
            } catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
        }

//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//        for (ApplicationInfo packageInfo : packages)
//        {
//            //The log is not required so if you want to and I recommend during release you remove that statement.
//            Log.i("Installed package :", packageInfo.packageName);
//            String pName = packageInfo.loadLabel(pm).toString();
//            Log.i("package name:", pName);
//            try
//            {
//                Drawable icon = pm.getApplicationIcon(packageInfo.packageName);
//            } catch (PackageManager.NameNotFoundException e)
//            {
//                e.printStackTrace();
//            }
////            imageView.setImageDrawable(icon);
//        }
    }


    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");



            TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml(pack +"<br><b>" + title + " : </b>" + text));
            tr.addView(textview);
            tab.addView(tr);




        }
    };
}
