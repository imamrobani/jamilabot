package robotika.unikom.semihumanoid.Fitur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import robotika.unikom.semihumanoid.R;

/**
 * Created by Dena Meidina on 02/01/2017.
 */

public class WebUnikom extends Activity implements View.OnClickListener {

    //=======================================Variable===============================================
    //WebView
    private WebView webView;

    //ImageButton
    private ImageButton back, home, reload;
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public WebUnikom(){
        //do nothing
    }
    //==================================End_Constructor=============================================

    //=======================================Create=================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.webunikom);

        webView = (WebView) findViewById(R.id.webView);
        back = (ImageButton) findViewById(R.id.ibBack);
        reload = (ImageButton) findViewById(R.id.ibReload);
        home = (ImageButton) findViewById(R.id.ibHome);

        back.setOnClickListener(this);
        home.setOnClickListener(this);
        reload.setOnClickListener(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new ViewClient());

        try{
            webView.loadUrl("http://www.unikom.ac.id");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //=====================================End_Create===============================================

    //=======================================Start==================================================
    @Override
    protected void onStart() {
        super.onStart();
    }
    //======================================End_Start===============================================

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

    //=========================================Stop=================================================
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    //=======================================End_Stop===============================================

    //======================================Destroy=================================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //====================================End_Destroy===============================================

    //=================================Implement_OnClickListene=====================================
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibBack:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.ibReload:
                webView.reload();
                break;
            case R.id.ibHome:
                finish();
                break;
        }
    }
    //===============================End_Implement_OnClickListene===================================

    //==================================Java_Class_ViewClient=======================================
    public class ViewClient extends WebViewClient {
        ProgressDialog progressDialog;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(WebUnikom.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        }

        public void onPageFinished(WebView view, String url) {
            try{
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
    //================================End_Java_Class_ViewClient=====================================
}
