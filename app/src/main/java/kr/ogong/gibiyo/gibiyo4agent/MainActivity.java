package kr.ogong.gibiyo.gibiyo4agent;

import android.app.Activity;
import android.app.AlertDialog;
//import android.content.Intent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
//import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//import android.widget.TextView;

import com.onesignal.OneSignal;
import com.onesignal.OneSignal.NotificationOpenedHandler;
import com.onesignal.OneSignal.IdsAvailableHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private WebView webview;
    static Activity currentActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        currentActivity = this;

        String  showLogCustomerId = null;

        Intent  intent = getIntent();
        if( intent != null ) {
            if( intent.getExtras() != null ) {
                showLogCustomerId = intent.getExtras().getString("showLog");
                Log.d("SHOW", showLogCustomerId);

            }
        }


//        webView = (WebView) findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://www.google.com");


        webview = (WebView)findViewById(R.id.webView);
        webview.setWebViewClient(new WebClient()); // 응룡프로그램에서 직접 url 처리
        WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(false);


        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("AGENT GIBIYO", consoleMessage.message() + '\n' + consoleMessage.messageLevel() + '\n' + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });

        if (showLogCustomerId == null )
            webview.loadUrl("http://gibiyo-ogong.rhcloud.com/app/agent");
        else
            webview.loadUrl( String.format( "http://gibiyo-ogong.rhcloud.com/app/agent/user_log.jsp?CustomerId=%s", showLogCustomerId) );



        // ########################  OneSignal Begin
        // Logging set to help debug issues, remove before releasing your app.
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);
//
//        OneSignal.startInit(this)
//                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
//                .setAutoPromptLocation(true)
//                .init();
//
//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//                String text = "OneSignal UserID:\n" + userId + "\n\n";
//
//                if (registrationId != null)
//                    text = "Google Registration Id:\n" + registrationId + text ;
//                else
//                    text = "Google Registration Id:\nCould not subscribe for push" + text ;
//
//            }
//        });

        // ########################  OneSignal ENd

        OneSignal.startInit(this).setNotificationOpenedHandler(new ExampleNotificationOpenedHandler()).init();

    }

    // NotificationOpenedHandler is implemented in its own class instead of adding implements to MainActivity so we don't hold on to a reference of our first activity if it gets recreated.
    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        /**
         * Called when a notification is opened from the Android status bar or a new one comes in while the app is in focus.
         *
         * @param message
         *           The message string the user seen/should see in the Android status bar.
         * @param additionalData
         *           The additionalData key value pair section you entered in on onesignal.com.
         * @param isActive
         *           Was the app in the foreground when the notification was received.
         */
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            String messageTitle = "OneSignal Example", messageBody = message;


            Log.d("SUNNY", message);
            Log.d("SUNNY", additionalData.toString() );
            Log.d("SUNNY", "" + isActive );
            try {
                if (additionalData != null) {
                    if (additionalData.has("title"))
                        messageTitle = additionalData.getString("title");
                    if (additionalData.has("actionSelected"))
                        messageBody += "\nPressed ButtonID: " + additionalData.getString("actionSelected");

                    messageBody = message + "\n\nFull additionalData:\n" + additionalData.toString();

                }
            } catch (JSONException e) {
            }

            SafeAlertDialog(messageTitle, messageBody);
        }
    }


    // AlertDialogs do not show on Android 2.3 if they are trying to be displayed while the activity is pause.
    // We sleep for 500ms to wait for the activity to be ready before displaying.
    private static void SafeAlertDialog(final String msgTitle, final String msgBody) {
        new Thread(new Runnable() {
            public void run() {
                try {Thread.sleep(500);} catch(Throwable t) {}

                MainActivity.currentActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(MainActivity.currentActivity)
                                .setTitle(msgTitle)
                                .setMessage(msgBody)
                                .setCancelable(true)
                                .setPositiveButton("OK", null)
                                .create().show();
                    }
                });
            }
        }).start();
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


//    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
//        /**
//         * Callback to implement in your app to handle when a notification is opened from the Android status bar or
//         * a new one comes in while the app is running.
//         * This method is located in this Application class as an example, you may have any class you wish implement NotificationOpenedHandler and define this method.
//         *
//         * @param message        The message string the user seen/should see in the Android status bar.
//         * @param additionalData The additionalData key value pair section you entered in on onesignal.com.
//         * @param isActive       Was the app in the foreground when the notification was received.
//         */
//        @Override
//        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
//            String additionalMessage = "";
//
//            try {
//                if (additionalData != null) {
//                    if (additionalData.has("actionSelected"))
//                        additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");
//
//                    additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
//                }
//
//                Log.d("OneSignalExample", "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//        }
//    }


}
