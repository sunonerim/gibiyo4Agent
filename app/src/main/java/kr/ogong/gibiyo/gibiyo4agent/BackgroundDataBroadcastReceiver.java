package kr.ogong.gibiyo.gibiyo4agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by rim sungwon on 16. 6. 23..
 */
public class BackgroundDataBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle dataBundle = intent.getBundleExtra("data");

        try {
            Log.i("OneSignalExample", "Notification content: " + dataBundle.getString("alert"));
            Log.i("OneSignalExample", "Notification title: "   + dataBundle.getString("title"));
            Log.i("OneSignalExample", "Is Your App Active: "   + dataBundle.getBoolean("isActive"));

            JSONObject customJSON = new JSONObject(dataBundle.getString("custom"));
            if (customJSON.has("a"))
                Log.i("OneSignalExample", "additionalData: " + customJSON.getJSONObject("a").toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
