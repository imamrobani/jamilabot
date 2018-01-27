package robotika.unikom.semihumanoid.Json;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import robotika.unikom.semihumanoid.Komunikasi.Komunikasi;
import robotika.unikom.semihumanoid.SpeechRecognition.SpeechRecognition;
import robotika.unikom.semihumanoid.TTS.TTS;
import robotika.unikom.semihumanoid.Wajah;

/**
 * Created by Dena on 04-Sep-17.
 */

public class TanyaJawab {
    public static String jawab = "saya " + Wajah.robot + " operator yang siap membantu anda";
    private static final String TAG = "POST";
    String request = "";

    AsyncHttpClient client = new AsyncHttpClient();

    public void getRespone(String s) {
        String url = "http://" + Komunikasi.ipServer + "/post/public/search";
        RequestParams params = new RequestParams();
        params.put("pertanyaan", s);
        client.setConnectTimeout(2000);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                Log.d(TAG, "onSuccess: Konek");
                if (TTS.isReady) {
                    try {
                        jawab = response.getString("jawaban");
                        Log.d(TAG, "onPostExecute: " + jawab);
                        TTS.speak(jawab);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resetListen();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                super.onFailure(statusCode, headers, res, t);
                Log.d(TAG, "onFailure: Trowable");
                jawab = "maaf tidak bisa menjawab, cari admin untuk memeriksa saya";
                TTS.speak(jawab);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(TAG, "onFailure: jsonObject");
                jawab = "maaf tidak bisa menjawab, cari admin untuk memeriksa saya";
                TTS.speak(jawab);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                jawab = "maaf tidak bisa menjawab, cari admin untuk memeriksa saya";
                TTS.speak(jawab);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                client.cancelAllRequests(true);
                jawab = "maaf tidak bisa menjawab, cari admin untuk memeriksa saya";
                TTS.speak(jawab);
            }
        });

    }

    public void resetListen() {
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
