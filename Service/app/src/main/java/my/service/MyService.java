package my.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    final String LOG_TAG = "MyService";
    private MyBinder binder;

    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();

        Log.d(LOG_TAG, "onCreate");
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        Log.d(LOG_TAG, String.valueOf(startId));
        return super.onStartCommand(intent, flags, startId);
    }
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    public int getNumber(){
        return 1;
    }
}
