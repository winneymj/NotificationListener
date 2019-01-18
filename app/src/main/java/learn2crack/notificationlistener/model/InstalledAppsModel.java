package learn2crack.notificationlistener.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class InstalledAppsModel
{
    private List<AppDataModel> _data = new ArrayList<AppDataModel>();

    public InstalledAppsModel()
    {
        Log.i("InstalledAppsModel.InstalledAppsModel()", "");
    }

    public void addApp(final String name, final String packageName, final Boolean enabled)
    {
        // See if whe have data and add to it.
//        List<AppDataModel> data = _installedApps.getValue();
//        if (null == data)
//        {
//            data = new ArrayList<>();
//        }

        AppDataModel model = new AppDataModel(name, packageName, enabled);
        _data.add(model);
    }

    private AppDataModel locateApp(final String name)
    {
        // Find matching name and set the enabled state
        for (AppDataModel model: _data)
        {
            if (model.name().equalsIgnoreCase(name))
            {
                return model;
            }
        }

        return null;
    }

    public void setAppEnabled(final String name, final Boolean enabled)
    {
        // Find matching name and set the enabled state
        AppDataModel model = locateApp(name);
        if (null != model)
        {
            model.enabled(enabled);
        }
    }

    public List<AppDataModel> data()
    {
        return _data;
    }

}
