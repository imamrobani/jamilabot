package robotika.unikom.semihumanoid.SpeechRecognition;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import robotika.unikom.semihumanoid.Json.TanyaJawab;
import robotika.unikom.semihumanoid.TTS.TTS;
import robotika.unikom.semihumanoid.Wajah;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by Dena Meidina on 29/12/2016.
 */
public class SpeechRecognition {


    //=======================================Variable===============================================
    //String
    /* Named searches allow to quickly reconfigure the decoder */
    public static final String KWS_SEARCH = "wakeup";
    /* Keyword we are looking for to activate menu */
    public static final String KEYPHRASE = Wajah.nama_robot;
    
    //TAG
    public static final String TAG = "Speech Recognition";
    
    //SpeechRecognizer Google
    public static SpeechRecognizer mGoogleSpeechRecognizer = null;

    //SpeechRecognizer CMU Sphinx
    public static edu.cmu.pocketsphinx.SpeechRecognizer mPocketSphinxRecognizer = null;

    //Intent
    public static Intent recognizerIntent;

    //boolean
    public static boolean isStartListen = false;
    public static boolean isCallName = false;

    //Context
    private Context mContext;
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public SpeechRecognition(Context context) {
        this.mContext = context;
        initPockerSphinx();
        initGoogleSpeechRecognizer();
    }
    //==================================End_Constructor=============================================

    //==============================Initialize_Poket_Sphinx=========================================
    private void initPockerSphinx() {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(mContext);

                    //Performs the synchronization of assets in the application and external storage
                    File assetDir = assets.syncAssets();

                    //Creates a new SpeechRecognizer builder with a default configuration
                    SpeechRecognizerSetup speechRecognizerSetup = defaultSetup();

                    //Set Dictionary and Acoustic Model files
                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));

                    //speechRecognizerSetup.setRawLogDir(assetDir); // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                    // Threshold to tune for keyphrase to balance between false positives and false negatives
                    speechRecognizerSetup.setKeywordThreshold(1e-25f);

                    //Creates a new SpeechRecognizer object based on previous set up.
                    mPocketSphinxRecognizer = speechRecognizerSetup.getRecognizer();

                    mPocketSphinxRecognizer.addListener(new PocketSphinxRecognitionListener());

                    // Create keyword-activation search.
                    mPocketSphinxRecognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(mContext, "Failed to init mPocketSphinxRecognizer ", Toast.LENGTH_SHORT).show();
                } else {
                    if(Wajah.isStopListen) {
                        restartSearch(KWS_SEARCH);
                    }
                }
            }
        }.execute();
    }
    //============================End_Initialize_Poket_Sphinx=======================================

    //============================Initialize_Google_Recognizer======================================
    private void initGoogleSpeechRecognizer() {
        //SpeechRecognizer
        mGoogleSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
        mGoogleSpeechRecognizer.setRecognitionListener(new Listener());

        //Intent
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 50);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 50);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());
    }
    //==========================End_Initialize_Google_Recognizer====================================

    //=======================================Destroy================================================
    public void destroy(){
        if (mPocketSphinxRecognizer != null) {
            mPocketSphinxRecognizer.cancel();
            mPocketSphinxRecognizer.shutdown();
            mPocketSphinxRecognizer = null;
        }


        if (mGoogleSpeechRecognizer != null) {
            mGoogleSpeechRecognizer.cancel();
            mGoogleSpeechRecognizer.destroy();
            mPocketSphinxRecognizer = null;
        }
    }
    //=====================================End_Destroy==============================================

    public static void stopListen(){
        Wajah.mute();
        Wajah.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGoogleSpeechRecognizer.cancel();
            }
        });
        mPocketSphinxRecognizer.stop();

        mPocketSphinxRecognizer.cancel();
    }

    //================================Restart_Listening_Google======================================
    public static void restartListen(){
        Wajah.mute();
        Wajah.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGoogleSpeechRecognizer.cancel();
                mGoogleSpeechRecognizer.startListening(recognizerIntent);
            }
        });
    }
    //==============================End_Restart_Listening_Google====================================

    //==============================Restart_Listening_CMU_Sphinx====================================
    public static void restartSearch(String searchName) {
        Wajah.mute();
        mPocketSphinxRecognizer.stop();

        mPocketSphinxRecognizer.cancel();

        mPocketSphinxRecognizer.startListening(searchName);
    }
    //============================End_Restart_Listening_CMU_Sphinx==================================

    //=================================Java_Class_CMU_Sphinx========================================
    protected class PocketSphinxRecognitionListener implements edu.cmu.pocketsphinx.RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
            //Log.d(TAG, "onBeginningOfSpeech: ");
        }


        /**
         * In partial result we get quick updates about current hypothesis. In
         * keyword spotting mode we can react here, in other modes we need to wait
         * for final result in onResult.
         */
        @Override
        public void onPartialResult(Hypothesis hypothesis) {
            if (hypothesis == null)
            {
                //Log.d(TAG,"null");
                return;

            }

            String text = hypothesis.getHypstr();
            Log.d(TAG,text);
            if (text.equals(KEYPHRASE)) {
                isCallName = true;
                //mGoogleSpeechRecognizer.startListening(recognizerIntent);
                mPocketSphinxRecognizer.cancel();
                //Toast.makeText(mContext, "You said: "+text, Toast.LENGTH_SHORT).show();
                TanyaJawab.jawab = "ya,  bisa saya bantu";
                TTS.speak(TanyaJawab.jawab);
            }
        }

        @Override
        public void onResult(Hypothesis hypothesis) {
        }


        /**
         * We stop mPocketSphinxRecognizer here to get a final result
         */
        @Override
        public void onEndOfSpeech() {
            //Log.d(TAG, "onEndOfSpeech: ");
        }

        public void onError(Exception error) {
            //Log.e(TAG, "onError: ", error);
        }

        @Override
        public void onTimeout() {
            //Log.d(TAG, "onTimeout: ");
        }

    }
    //===============================End_Java_Class_CMU_Sphinx======================================

    //==============================Java_Class_Google_Recognizer====================================
    class Listener implements RecognitionListener {
        //Java Class
        //TODO : ngedit ieu
        //private BackgroundWorker post;
        private TanyaJawab post;

        Listener(){
            //post = new BackgroundWorker();
            post = new TanyaJawab();
        }

        //============================Impelement_RecognitionListener================================
        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onBeginningOfSpeech() {
            isStartListen = true;
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            isStartListen = false;
            Log.d(TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            Log.e(TAG, "onError " + getErrorText(error));
            isStartListen = false;
            if(error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT){
                isCallName = false;
                restartSearch(KWS_SEARCH);
            }else {
                if (isCallName) {
                    mGoogleSpeechRecognizer.cancel();
                    mGoogleSpeechRecognizer.startListening(recognizerIntent);
                } else {
                    restartSearch(KWS_SEARCH);
                }
            }
        }

        @Override
        public void onResults(Bundle results) {
            isStartListen = false;
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text;

            if (matches != null) {
                text = matches.get(0);
            } else {
                return;
            }

            text = text.toLowerCase();

            Log.d(TAG, "onResults: " + text);
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();

            //TODO: Tempat Pertanyaan Hardcode
            if(isCallName) {
                //post.setData("question", text);
                post.getRespone(text);
            }else{
                restartSearch(KWS_SEARCH);
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        //==========================End_Impelement_RecognitionListener==============================
        public String getErrorText(int errorCode) {
            String message;
            switch (errorCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "Audio recording error";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "Client side error";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "Insufficient permissions";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "Network error";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "Network timeout";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "No match";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RecognitionService busy";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "error from server";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "No mGoogleSpeechRecognizer input";
                    break;
                default:
                    message = "Didn't understand, please try again.";
                    break;
            }
            return message;
        }
    }
    //============================End_Java_Class_Google_Recognizer==================================
}
