package tool.imageloadercompact.activity;

import android.app.Activity;
import android.compact.utils.FileCompactUtil;
import android.compact.utils.IntentCompactUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tool.imageloadercompact.app2.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn1 = (Button) findViewById(R.id.btn1);
        final TextView btn1ret = (TextView) findViewById(R.id.btn1_ret);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PPP", FileCompactUtil.getCacheDirPath(view.getContext()));
                Log.d("PPP", IntentCompactUtil.convertIntentToString(getIntent()));
            }
        });
    }
}