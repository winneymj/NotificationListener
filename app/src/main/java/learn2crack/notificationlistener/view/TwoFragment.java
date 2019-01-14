package learn2crack.notificationlistener.view;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import learn2crack.notificationlistener.BluetoothImp;
import learn2crack.notificationlistener.R;

public class TwoFragment extends Fragment{

    private BluetoothImp ble;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        ble = BluetoothImp.getInstance();
//        boolean bleSupported = ble.verifyBleSupported();
//        if (!bleSupported) {
//            // Output error situation
//        }
//        else {
//            // See if we have permission to use bluetooth.
//            // This is an async dialog so we have to assume
//            // that we will continue after this call without
//            // the user having accepted the permission.
//            if (!ble.checkAndRequestPermissions((AppCompatActivity)getActivity()))
//            {
//                // We have permissions so initialise ble here.
//                this.ble.initializeBle((AppCompatActivity)getActivity());
//            }
//        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    /**
     * Method called when permission is request to Location for example.  User is prompted.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions,
                                            int[] grantResults) {
        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions){
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        if((PermissionsMap.get(Manifest.permission.ACCESS_FINE_LOCATION) != 0)
                || PermissionsMap.get(Manifest.permission.ACCESS_COARSE_LOCATION) != 0){
            Toast.makeText((AppCompatActivity)getActivity(), "Location permission is a must", Toast.LENGTH_SHORT).show();
//            finish();
        }
        else
        {
//            this.ble.initializeBle((AppCompatActivity)getActivity());
        }
    }
}
