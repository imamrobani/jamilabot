package robotika.unikom.semihumanoid.Json;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import robotika.unikom.semihumanoid.Komunikasi.Komunikasi;
import robotika.unikom.semihumanoid.SpeechRecognition.SpeechRecognition;
import robotika.unikom.semihumanoid.TTS.TTS;
import robotika.unikom.semihumanoid.Wajah;

public class BackgroundWorker {

    //public static String jawab = "saya "+Wajah.robot+" robot icon universitas komputer indonesia";
    private static final String TAG = "POST";
    String request = "";

    public BackgroundWorker() {
        //do nothing
    }

    public void setData(String... s) {
        //new SendData().execute(s);
        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            new SendData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
        } else {
            new SendData().execute(s);
        }
    }


    class SendData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            request = params[0];
            Log.d(TAG, "doInBackground: "+request);
            if(request.equals("question")) {
                String pertanyaan = params[1];
                String url_str = "http://"+Komunikasi.ipServer + "/post/public/search";
                try {
                    URL url = new URL(url_str);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("pertanyaan", "UTF-8") + "=" + pertanyaan;
                    Log.d(TAG, "doInBackground: "+post_data);
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                if (TTS.isReady) {
                    try {
                        JSONObject parse = new JSONObject(s);
                        //jawab = parse.getString("jawaban");
                        //Log.d(TAG, "onPostExecute: "+jawab);
                        //TTS.speak(jawab);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resetListen();
                    }
                }
            }else{
                resetListen();
            }
        }

        public void resetListen(){
            if (SpeechRecognition.isCallName) {
                Wajah.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SpeechRecognition.restartListen();
                    }
                });
            } else {
                SpeechRecognition.restartSearch(SpeechRecognition.KWS_SEARCH);
            }
        }
    }
}
