package my.service;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import my.service.aidl.AIDLFibonacciActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private Button stop;
    private Button bind;
    private Button unbind;
    private Button startIntentService;
    private Button startFibonacciActivity;

    private ServiceConnection serviceConnection;
    private Intent service;
    private Intent intent;
    private boolean bound = false;
    private String LOG_TAG = "MainActivity";

    public static final String AUTHORITY = "study.contentprovider.MyContentProvider";

    // path
    static final String ITEM_PATH = "item";

    // Общий Uri
    public static final Uri ITEM_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + ITEM_PATH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        bind = (Button) findViewById(R.id.bind);
        unbind = (Button) findViewById(R.id.unbind);
        startIntentService = (Button) findViewById(R.id.start_intent_service);
        startFibonacciActivity = (Button) findViewById(R.id.start_fibonacci_activity);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        bind.setOnClickListener(this);
        unbind.setOnClickListener(this);
        startIntentService.setOnClickListener(this);
        startFibonacciActivity.setOnClickListener(this);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(LOG_TAG, "onServiceConnected");
                MyService.MyBinder binder = (MyService.MyBinder) service;

                Log.d(LOG_TAG, String.valueOf(binder.getService().getNumber()));
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "onServiceDisconnected");
                bound = false;
            }
        };

        service = new Intent(this, MyService.class);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.start:
                ContentValues cv = new ContentValues();
                cv.put("text", String.valueOf("hello"));
                Uri newUri = getContentResolver().insert(ITEM_CONTENT_URI, cv);
                startService(new Intent(this, MyService.class));
                break;
            case R.id.stop:
                stopService(new Intent(this, MyService.class));
                break;
            case R.id.bind:
                bindService(new Intent(this, MyService.class), serviceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                if (!bound) return;
                unbindService(serviceConnection);
                bound = false;
                break;
            case R.id.start_intent_service:
                intent = new Intent(this, MyIntentService.class);
                startService(intent);
                break;
            case R.id.stop_intent_service:
                stopService(intent);

                break;
            case R.id.start_fibonacci_activity:
                startActivity(new Intent(this, AIDLFibonacciActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.callOnClick();
    }
}
