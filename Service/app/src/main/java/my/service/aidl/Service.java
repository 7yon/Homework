package my.service.aidl;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import my.service.IService;
import my.service.IServiceClient;

/**
 * Created by simen on 18.07.2017.
 */

public class Service extends android.app.Service {
    private static final String TAG = "Service";
    private boolean isStopped;

    @Override
    public void onCreate() {
        isStopped = false;
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        isStopped = true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    private final IService.Stub mBinder = new IService.Stub() {
        @Override
        public void startCount(final IServiceClient callback) throws RemoteException {

            Thread thread = new Thread() {
                public void run() {
                    int firstNumber = 0, secondNumber = 0, number = 0;
                    for (int i = 0; i < 20; i++) {

                        if (isStopped){
                            break;
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (i == 0) {
                            firstNumber = 0;
                            secondNumber = 1;
                        } else {
                            number = firstNumber + secondNumber;
                            firstNumber = secondNumber;
                            secondNumber = number;
                        }


                        try {
                            callback.handleCount(number);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            thread.start();
        }
    };
}
