package learn2crack.notificationlistener.model;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.ArrayList;

public class InstalledAppsModel
{

    public MutableLiveData<ArrayList<String>> installedApps = new MutableLiveData<>();

    public InstalledAppsModel()
    {
        Log.i("InstalledAppsModel.InstalledAppsModel()", "");
    }

}
