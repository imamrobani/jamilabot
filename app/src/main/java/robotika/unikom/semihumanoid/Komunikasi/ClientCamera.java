package robotika.unikom.semihumanoid.Komunikasi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import robotika.unikom.semihumanoid.Wajah;

public class ClientCamera extends Thread {

    //=======================================Variable===============================================
    //Socket
    private static Socket mSocket;
    private Client client = new Client();

    //String
    private static final String TAG = "socket";

    //int
    private int mPort = Komunikasi.portCamera;

    //boolean
    private boolean running;
    public static boolean req;
    //=====================================End_Variable=============================================

    //=====================================Constructor==============================================
    public ClientCamera() {
    }
    //===================================End_Constructor============================================

    //===================================Implement_Thread===========================================
    @Override
    public void run() {
        super.run();
        while (running) {
            try {
                req = true;
                mSocket = new Socket();
                mSocket.connect(new InetSocketAddress(Komunikasi.ipServer, mPort), 10000); // hard-code server address
                BufferedOutputStream outputStream = new BufferedOutputStream(mSocket.getOutputStream());
                BufferedInputStream inputStream = new BufferedInputStream(mSocket.getInputStream());

                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("type", "data");
                jsonObj.addProperty("length", Wajah.getPreviewLength());
                jsonObj.addProperty("width", Wajah.getPreviewWidth());
                jsonObj.addProperty("height", Wajah.getPreviewHeight());

                byte[] buff = new byte[80];
                int len = 0;
                String msg = null;
                outputStream.write(jsonObj.toString().getBytes());
                outputStream.flush();

                while (running && (len = inputStream.read(buff)) != -1) {
                    msg = new String(buff, 0, len);

                    // JSON analysis
                    JsonParser parser = new JsonParser();
                    boolean isJSON = true;
                    JsonElement element = null;
                    try {
                        element = parser.parse(msg);
                    } catch (JsonParseException e) {
                        Log.e(TAG, "exception: " + e);
                        isJSON = false;
                    }
                    if (isJSON && element != null) {
                        JsonObject obj = element.getAsJsonObject();
                        element = obj.get("state");
                        if (element != null && element.getAsString().equals("ok")) {
                            // send data
                            while (running) {
                                if (Wajah.getImageBuffer() != null) {
                                    outputStream.write(Wajah.getImageBuffer());
                                    outputStream.flush();
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
                outputStream.close();
                inputStream.close();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                long startTime;
                float elapsed = 0;
                while (req) {
                    try {
                        startTime = System.nanoTime();
                        Thread.sleep(10);
                        elapsed += System.nanoTime() - startTime;
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if (elapsed / 1000000 > 60000) {
                        //TODO : kerjakeun ngke
                        client.setData("client", "Req");
                        elapsed = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    mSocket.close();
                    mSocket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //=================================End_Implement_Thread=========================================

    public void setRunning(boolean b) {
        running = b;
    }

    //========================================Close=================================================
    public void onDestroy() {
        if (mSocket != null) {
            try {
                running = false;
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //======================================End_Close===============================================
}