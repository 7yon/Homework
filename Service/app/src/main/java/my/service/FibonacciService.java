package my.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class FibonacciService extends IntentService {
    public static final String ACTION_FIBONACCI = "my.service.action.FIBONACCI";
    public static final String EXTRA_NUMBER = "my.service.extra.NUMBER";
    private final String LOG_TAG = "FibonacciService";

    private boolean isStopped;

    public FibonacciService() {
        super("FibonacciService");

        isStopped = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent");

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FIBONACCI.equals(action)) {
                handleActionFoo();
            }
        }
    }

    private void handleActionFoo() {
        Log.d(LOG_TAG, "handleActionFoo");

        int firstNumber = 0, secondNumber = 0;
        for (int i = 0; i < 10000; i++ ){

            if (isStopped){
                break;
            }

            if(i == 0){
                firstNumber = 0;
                secondNumber = 0;
            } else {
                firstNumber = secondNumber;
                secondNumber = i;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int number = firstNumber + secondNumber;
            Intent responseIntent = new Intent();
            responseIntent.setAction(ACTION_FIBONACCI);
            responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
            responseIntent.putExtra(EXTRA_NUMBER, number);
            sendBroadcast(responseIntent);

            Log.d(LOG_TAG, String.valueOf(number));
        }
    }

    @Override
    public void onDestroy() {

        isStopped = true;
        super.onDestroy();
    }
}
