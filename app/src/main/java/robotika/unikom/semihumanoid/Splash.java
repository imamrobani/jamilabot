package robotika.unikom.semihumanoid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Dena Meidina on 31/12/2016.
 */

public class Splash extends Activity {

    private final static int volume = 2;
    //====================================Constructor===============================================
    public Splash(){
        //do nothing
    }
    //==================================End_Constructor=============================================

    //=======================================Create=================================================
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
        setContentView(R.layout.splash);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Do some stuff
            unmute();
            final MediaPlayer sound = MediaPlayer.create(Splash.this, R.raw.blip);
            Thread timer = new Thread(){
                public void run(){
                    try{
                        sleep(2000);
                        sound.start();
                        sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        sound.release();
                        Intent openMain = new Intent("android.intent.action.WAJAH");
                        startActivity(openMain);
                        finish();
                    }
                }
            };
            timer.start();
        }

    }
    //=====================================End_Create===============================================

    //=======================================Resume=================================================
    @Override
    protected void onResume() {
        super.onResume();
    }
    //======================================End_Resume==============================================

    //========================================Pause=================================================
    @Override
    protected void onPause() {
        super.onPause();
    }
    //=======================================End_Pause==============================================

    //========================================Unmute================================================
    public void unmute(){
        AudioManager amanager=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }
    //======================================End_Unmute==============================================
}
