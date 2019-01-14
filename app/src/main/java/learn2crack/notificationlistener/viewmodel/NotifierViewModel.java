package learn2crack.notificationlistener.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableField;

import java.util.ArrayList;

import learn2crack.notificationlistener.BluetoothImp;
import learn2crack.notificationlistener.model.InstalledAppsModel;
//import android.databinding.ObservableField;

public class NotifierViewModel extends ViewModel
{
    // Our datamodel
    private InstalledAppsModel mInstalledAppsModel;
    private Context context;
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
        mInstalledAppsModel = new InstalledAppsModel();
        context = appContext;
    }

    public void init()
    {

    }

    public void initializeBLE()
    {
        // See if we can initialize ble
        ble = BluetoothImp.getInstance();
        ble.init(context); // Only need to do once.
    }

    public LiveData<ArrayList<String>> getAppsList()
    {
        return mInstalledAppsModel.installedApps;
    }
}
