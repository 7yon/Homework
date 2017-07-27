package my.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static my.service.FibonacciService.ACTION_FIBONACCI;

public class FibonacciActivity extends AppCompatActivity implements View.OnClickListener {

    private MyBroadcastReceiver myBroadcastReceiver;
    private Intent fibonacciService;

    private Button start;
    private Button stop;
    private TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);

        start = (Button) findViewById(R.id.start_fibonacci);
        stop = (Button) findViewById(R.id.stop_fibonacci);
        number = (TextView) findViewById(R.id.text);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        myBroadcastReceiver = new MyBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter(ACTION_FIBONACCI);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.start_fibonacci:
                fibonacciService = new Intent(this, FibonacciService.class);
                fibonacciService.setAction(ACTION_FIBONACCI);
                startService(fibonacciService);
                break;
            case R.id.stop_fibonacci:
                if (fibonacciService != null) {
                    stopService(fibonacciService);
                    fibonacciService = null;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(FibonacciService.EXTRA_NUMBER, 1);
            number.setText(String.valueOf(result));
        }
    }
}
