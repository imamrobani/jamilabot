package robotika.unikom.semihumanoid.Fitur;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import robotika.unikom.semihumanoid.R;


public class VideoCall extends XWalkActivity {

    //=======================================Variable===============================================
    //XWalkView (API)
    private XWalkView xWalkWebView;
    //=====================================End_Variable=============================================

    //=======================================OnCreate===============================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN //fulscreen
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //tetap nyala
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.video_call);

        xWalkWebView=(XWalkView)findViewById(R.id.xwalkWebView);

        // turn on debugging
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
    }
    //=====================================End_OnCreate=============================================

    //=====================================OnXWalkReady=============================================
    @Override
    protected void onXWalkReady() {
        // Do anything with embedding API
        xWalkWebView.load("file:///android_asset/client/index.html", null);
    }
    //===================================End_OnXWalkReady===========================================
}
