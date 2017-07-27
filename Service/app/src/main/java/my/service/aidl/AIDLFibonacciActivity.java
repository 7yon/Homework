package my.service.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import my.service.IService;
import my.service.IServiceClient;
import my.service.R;

/**
 * Created by simen on 17.07.2017.
 */

public class AIDLFibonacciActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AIDLFibonacciActivity";
    private Button start;
    private Button stop;
    private TextView number;

    private IService mService;
    private Handler mHandler = new Handler();
    private Intent intent;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);

        start = (Button) findViewById(R.id.start_fibonacci);
        stop = (Button) findViewById(R.id.stop_fibonacci);
        number = (TextView) findViewById(R.id.text);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        intent = new Intent(this, Service.class);
//        startService(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.start_fibonacci:
                // bindService(new Intent(this, Service.class), mConnection,
                //        Context.BIND_AUTO_CREATE);

                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                startService(intent);
                unbindService(mConnection);

                break;
            case R.id.stop_fibonacci:
                if (!bound) return;
                unbindService(mConnection);
                bound = false;
                break;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            Log.d(TAG, "onServiceConnected");
            mService = IService.Stub.asInterface(service);
            bound = true;

            try {
                mService.startCount(client);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "onServiceDisconnected");
            bound = false;
            mService = null;
        }
    };

    private IServiceClient.Stub client = new IServiceClient.Stub(){
        public void handleCount(final int n){
            Log.d(TAG, "handleCount(" + n + ")");

            mHandler.post(new Runnable(){
                public void run(){
                    number.setText(String.valueOf(n));
                }
            });
        }
    };

    @Override
    protected void onDestroy() {

        //stop.callOnClick();
        //stopService(intent);
        if (!bound) return;
        unbindService(mConnection);
        bound = false;
        super.onDestroy();
    }
}
