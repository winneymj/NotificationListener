package learn2crack.notificationlistener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;


public class NotificationService extends NotificationListenerService {

    Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {


        String pack = sbn.getPackageName();
        CharSequence cs = sbn.getNotification().tickerText;
        String ticker = (null != cs) ? cs.toString() : "";
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        cs = extras.getCharSequence("android.text");
        String text = (null != cs) ? cs.toString() : "";

        Log.i("Package",pack);
        Log.i("Ticker",ticker);
        Log.i("Title",title);
        Log.i("Text",text);

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);

        // Send notification to the BLE service to send the message to remote device.
        Intent intent = new Intent(this, BluetoothLEService2.class);
        startService(intent);
    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}