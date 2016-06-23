package kr.ogong.gibiyo.gibiyo4agent;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * Created by sungwon rim on 16. 6. 23..
 */
public class MyApplication extends Application {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        OneSignal.startInit(this)
//                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
//                .init();

    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                    if (additionalData.has("actionSelected"))
                        Log.d("OneSignalExample", "OneSignal notification button with id " + additionalData.getString("actionSelected") + " pressed");

                    Log.d("OneSignalExample", "Full additionalData:\n" + additionalData.toString());
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

            // The following can be used to open an Activity of your choice.
      /*
      Intent intent = new Intent(getApplication(), YourActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      */
            // Follow the instructions in the link below to prevent the launcher Activity from starting.
            // https://documentation.onesignal.com/docs/android-notification-customizations#changing-the-open-action-of-a-notification
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
