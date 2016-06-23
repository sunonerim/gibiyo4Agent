package kr.ogong.gibiyo.gibiyo4agent;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationPayload;

import java.math.BigInteger;

/**
 * Created by gubeobseo on 16. 6. 23..
 */
public class Notification4Agent extends NotificationExtenderService {
    @Override
    public void onCreate() {
        // code to execute when the service is first created
        super.onCreate();
        Log.i("MyService", "Service Started.");
    }

    @Override
    protected boolean onNotificationProcessing(OSNotificationPayload notification) {

        Log.d("Eeeeeeeeeeeewerwrwr", "sdfsfsfsfsdf");
        Log.d("Eeeeeeeeeerwrwrwrwr", "sdfsfsfsfsdf");
        Log.d("Eeeeeeeeeeeewerwrwr", "sdfsfsfsfsdf");
        Log.d("Eeeeeeeeeerwrwrwrwr", "sdfsfsfsfsdf");

        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                return builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            }
        };

        OSNotificationDisplayedResult result = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + result.notificationId);

        return true;
    }
}