package learn2crack.notificationlistener.model;

import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InstalledAppsModel
{

    public MutableLiveData<List<String>> installedApps = new MutableLiveData<>();

    public InstalledAppsModel()
    {
        Log.i("InstalledAppsModel.InstalledAppsModel()", "");
    }

    public void setAppName(final String name)
    {
        // See if whe have data and add to it.
        List<String> data = installedApps.getValue();
        if (null == data)
        {
            data = new ArrayList<>();
        }

        data.add(name);

        installedApps.setValue(data);

//        Handler myHandler = new Handler();
//        myHandler.postDelayed(() -> {
//            List<String> fruitsStringList = new ArrayList<>();
//            fruitsStringList.add("@ode_wizard");
//            fruitsStringList.add("ninja_developer");
//            fruitsStringList.add("denzel");
//            fruitsStringList.add("bananaPeel");
//            fruitsStringList.add("kioko");
//            long seed = System.nanoTime();
//            Collections.shuffle(fruitsStringList, new Random(seed));
//
//            installedApps.setValue(fruitsStringList);
//        }, 5000);

//        installedApps.setValue(name);
    }
}
