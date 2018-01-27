package robotika.unikom.semihumanoid.TTS;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import java.util.Locale;

import robotika.unikom.semihumanoid.Json.TanyaJawab;
import robotika.unikom.semihumanoid.SpeechRecognition.SpeechRecognition;
import robotika.unikom.semihumanoid.Wajah;

/**
 * Created by Dena Meidina on 29/12/2016.
 */

public class TTS implements TextToSpeech.OnInitListener {

    //=======================================Variable===============================================
    //Text To Speech
    public static TextToSpeech tts = null;
    public static boolean isReady = false;

    //Context
    private Context context;

    //boolean
    public static boolean isTalking = false;

    //float
    public static float pitch;
    public static float speedRate;
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public TTS(Context context) {
        this.context = context;
        tts = new TextToSpeech(context, this);

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            //=======================Implement_UtteranceProgressListener============================
            @Override
            public void onStart(String utteranceId) {
                isTalking = true;
                Wajah.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //stop listening
                        if(SpeechRecognition.mGoogleSpeechRecognizer != null) {
                            SpeechRecognition.mGoogleSpeechRecognizer.cancel();
                            Wajah.unmute();
                        }
                    }
                });
            }

            @Override
            public void onDone(String utteranceId) {
                isTalking = false;
                if(SpeechRecognition.isCallName){
                    Wajah.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SpeechRecognition.restartListen();
                        }
                    });
                }else {
                    SpeechRecognition.restartSearch(SpeechRecognition.KWS_SEARCH);
                }
            }

            @Override
            public void onError(String utteranceId) {
                isTalking = false;
                if(SpeechRecognition.isCallName){
                    Wajah.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SpeechRecognition.restartListen();
                        }
                    });
                }else {
                    SpeechRecognition.restartSearch(SpeechRecognition.KWS_SEARCH);
                }
            }
            //=====================End_Implement_UtteranceProgressListener==========================
        });
    }
    //==================================End_Constructor=============================================

    //=========================================Speak================================================
    public static void speak(String s) {
        String str = s;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle map = new Bundle();
            map.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "id");
            tts.speak(str, TextToSpeech.QUEUE_FLUSH, map, "UniqueID");
        } else {
            tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    //=======================================End_Speak==============================================

    //===============================Implement_OnInitListener=======================================
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "TTS not supported", Toast.LENGTH_SHORT).show();
            }
            isReady = true;
            setPitch_Speed(1.0f, 1.0f);
            TTS.speak(TanyaJawab.jawab);
        } else {
            Toast.makeText(context, "Initilization Failed!", Toast.LENGTH_SHORT).show();
        }
    }
    //=============================End_Implement_OnInitListener=====================================

    //==========================================Destroy=============================================
    public void destroy(){
        tts.shutdown();
    }
    //========================================End_Destroy===========================================

    //==================================Set_Pitch_SpeedRate=========================================
    public void setPitch_Speed(float pitch, float speed) {
        this.pitch = pitch;
        this.speedRate = speed;
        tts.setPitch(pitch);
        tts.setSpeechRate(speed);
    }
    //================================End_Set_Pitch_SpeedRate=======================================

    //========================================Get_Pitch=============================================
    public static float getPitch() {
        return pitch;
    }
    //======================================End_Get_Pitch===========================================

    //======================================Get_SpeedRate===========================================
    public static float getSpeedRate() {
        return speedRate;
    }
    //====================================End_Get_SpeedRate=========================================
}
