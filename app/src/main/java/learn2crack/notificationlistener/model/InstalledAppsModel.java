package learn2crack.notificationlistener.model;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class InstalledAppsModel
{

    private MutableLiveData<List<String>> installedApps = new MutableLiveData<>();

    public InstalledAppsModel()
    {
        Log.i("InstalledAppsModel.InstalledAppsModel()", "");
    }

    public MutableLiveData<List<String>> getInstalledApps()
    {
        return installedApps;
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

        // Save into livedata
        installedApps.setValue(data);
    }
}
