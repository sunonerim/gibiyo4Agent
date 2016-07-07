package kr.ogong.gibiyo.gibiyo4agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;

import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by rim sungwon  on 16. 6. 23..
 */
public class IncomingCallBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "PHONE STATE";
    public static final String $CheckUrl = "http://gibiyo-ogong.rhcloud.com/tsi/agent_call_request.jsp?AgentPhone=%s";
    private static String mLastState;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive()");

        /**
         * http://mmarvick.github.io/blog/blog/lollipop-multiple-broadcastreceiver-call-state/
         * 2번 호출되는 문제 해결
         */
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        Log.d(TAG, "state>>" + state );

        if (state.equals(mLastState)) {
            return;
        } else {
            mLastState = state;
        }

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {

            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);
            Log.d(TAG, "phone no>>" + phone_number  );

            PrimeThread p = new PrimeThread(143 , context );
            p.start();

            /*
            Intent intent_show =new Intent( context, MainActivity.class);
            intent_show.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            intent_show.putExtra("show", phone_number );
            context.startActivity(intent_show);
            */

            /*
            Intent serviceIntent = new Intent(context, CallingService.class);
            serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
            context.startService(serviceIntent);
            */
        }

    }


    class PrimeThread extends Thread {
        long    minPrime;
        Context mContext;

        public PrimeThread(int i, Context context) {
            this.minPrime = minPrime;
            this.mContext = context;
        }



        public void run() {
            String  check_url =  String.format( IncomingCallBroadcastReceiver.$CheckUrl, MainActivity.getAgentPhone() );
            Log.d( "check_url", check_url  );

            String content = readThroughHttp( check_url );
            Log.d( "customer_id", content );

            String customer_id = getCustomerId( content );
            Log.d( "customer_id", customer_id );

            if ( ! "no".equals(customer_id)  ){
                Intent intent_show =new Intent( mContext, MainActivity.class);
                intent_show.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                intent_show.putExtra("showLog", customer_id );
                mContext.startActivity(intent_show);
            }

        }

        public String   readThroughHttp( String strUrl ){


            try{
                URL Url = new URL(strUrl);  // URL화 한다.
                HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
                conn.setRequestMethod("GET");       // get방식 통신
                conn.setDoOutput(true);             // 쓰기모드 지정
                conn.setDoInput(true);              // 읽기모드 지정
                conn.setUseCaches(false);           // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false);    // 캐싱데이터 디폴트 값 설정

//            strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

                InputStream is = conn.getInputStream();        //input스트림 개방

                StringBuilder builder = new StringBuilder();   //문자열을 담기 위한 객체
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));  //문자열 셋 세팅
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line+ "\n");
                }

                return builder.toString();

            }catch(MalformedURLException | ProtocolException exception) {
                exception.printStackTrace();
            }catch(IOException io){
                io.printStackTrace();
            }

            return null;
        }

        public String   getCustomerId ( String content ){
            try {
                JSONObject json = new JSONObject (content);
                String customer_id = json.getString("result");
                return customer_id;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

}

