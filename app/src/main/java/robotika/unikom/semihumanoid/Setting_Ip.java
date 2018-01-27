package robotika.unikom.semihumanoid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import robotika.unikom.semihumanoid.Komunikasi.Komunikasi;

/**
 * Created by Dena Meidina on 02-Aug-17.
 */

public class Setting_Ip extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN //fulscreen
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //tetap nyala
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.firstrun);

        final EditText editText = (EditText) findViewById(R.id.ip);
        Button button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = editText.getText().toString();
                if(ip.isEmpty()){
                    return;
                }
                Komunikasi.ipServer = ip;

                SharedPreferences settings = getSharedPreferences("prefs", 0); //0 mode private
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("run", false);
                editor.putString("ip", ip);
                editor.commit();

                Intent i = new Intent("android.intent.action.SPLASH");
                startActivity(i);
                finish();
            }
        });
    }
}
