//======================================Bismillahirohmanirohim======================================
package robotika.unikom.semihumanoid;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import robotika.unikom.semihumanoid.BLE.BluetoothLeService;
import robotika.unikom.semihumanoid.BLE.SampleGattAttributes;
import robotika.unikom.semihumanoid.Camera.CameraManager;
import robotika.unikom.semihumanoid.Komunikasi.Client;
import robotika.unikom.semihumanoid.Komunikasi.ClientCamera;
import robotika.unikom.semihumanoid.Komunikasi.Komunikasi;
import robotika.unikom.semihumanoid.Komunikasi.ServerUdp;
import robotika.unikom.semihumanoid.MainThread.MainThread;
import robotika.unikom.semihumanoid.SpeechRecognition.SpeechRecognition;
import robotika.unikom.semihumanoid.TTS.TTS;
import robotika.unikom.semihumanoid.WajahPanel.Background;
import robotika.unikom.semihumanoid.WajahPanel.Info;
import robotika.unikom.semihumanoid.WajahPanel.Mata;
import robotika.unikom.semihumanoid.WajahPanel.Menu;
import robotika.unikom.semihumanoid.WajahPanel.Mic;
import robotika.unikom.semihumanoid.WajahPanel.Mulut;

public class Wajah extends Activity implements View.OnTouchListener {

    //=======================================Variable===============================================
    //Activity
    public static Wajah wajahActivity;

    //SurfaceView
    private SurfaceView svWajah, svCamera;

    //Java Class
    public static TTS tts;
    public static SpeechRecognition speechRecognition;
    private ServerUdp serverUdp;
    private Client client = new Client();

    //int
    public static int maxHeight;
    public static int maxWidth;
    public static int divWidth;
    public static int divHeight;

    //Context
    public static Context context;

    //=====================================Variable_BLE=============================================
    //BluetoothAdapter
    private BluetoothAdapter mBluetoothAdapter;

    //BluetoothGattCharacteristic
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    //Java Class
    public static BluetoothLeService mBluetoothLeService;

    //Handler
    private Handler mHandler;

    //boolean
    public static boolean startRequest = false;
    public static boolean mConnected = false;
    public static boolean isStopListen = false;
    public static boolean bReady = false;
    private boolean mScanning;
    private boolean isRegister = false;
    public static boolean bicara = false;

    //String
    public static final String robot = "jamila";
    public static final String nama_robot = "hello "+robot;
    private final String ninebot = "CD:3B:7F:02:2D:33"; //Ninebot 1
    //private final String ninebot = "D1:33:4F:99:F4:A4"; // Ninebot 2
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    public static String display = "";
    private String mDeviceAddress;

    //int
    public final static int BATTERY = 0x22;
    public final static int CURRENT_SPEED = 0x26;
    public final static int TEMPERATURE = 0x3E;
    public final static int VOLTAGE = 0x47;
    public final static int CURRENT = 0x50;
    public final static int PITCH_ANGLE = 0x61;
    public final static int ROLL_ANGLE = 0x62;
    public final static int PITCH_ANGLE_VELOCITY = 0x63;
    public final static int ROLL_ANGLE_VELOCITY = 0x64;

    public final static int volume = 100;

    //float
    private float[] value = new float[10];

    //int Array
    private int[] allRequest = {BATTERY, CURRENT_SPEED, TEMPERATURE, VOLTAGE, CURRENT, PITCH_ANGLE
            , ROLL_ANGLE, PITCH_ANGLE_VELOCITY, ROLL_ANGLE_VELOCITY};

    //ArrayList
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();


    private static final int sampleRate = 8000;
    public static AudioRecord audio;
    private static int bufferSize;
    private static double lastLevel = 0;
    //===================================End_Variable_BLE===========================================


    //=======================================Variable===============================================
    //Conts int
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1200;

    //Java Class
    public static Mata mata;
    private Background bg;
    private Mulut mulut;
    private Mulut alphabet;
    private Menu menu;
    private Mic mic;
    private Info info;
    private MainThread thread;

    //Resource Asset
    public static Typeface customFont;

    //Resource Drawable
    private Bitmap R_mata;
    private Bitmap R_mulut;
    private Bitmap R_alphabet;
    private Bitmap R_mic;
    private Bitmap R_info;
    private Bitmap R_cursor1;
    private Bitmap R_cursor2;
    private Bitmap R_cursor3;
    private Bitmap R_DRU;

    //int
    public static int x, y;

    //float
    private float scaleFactorX;
    private float scaleFactorY;

    //Rect
    private Rect cur = new Rect();
    //=====================================End_Variable=============================================


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

        setContentView(R.layout.activity_wajah);

        //set custom font
        customFont = Typeface.createFromAsset(this.getAssets(), "nasalization-rg.ttf");

        R_mata = BitmapFactory.decodeResource(getResources(), R.drawable.mata);
        R_mulut = BitmapFactory.decodeResource(getResources(), R.drawable.mulut);
        R_alphabet = BitmapFactory.decodeResource(getResources(), R.drawable.alphabet);
        R_mic = BitmapFactory.decodeResource(getResources(), R.drawable.mic);
        R_info = BitmapFactory.decodeResource(getResources(), R.drawable.info);
        R_cursor1 = BitmapFactory.decodeResource(getResources(), R.drawable.cursor1);
        R_cursor2 = BitmapFactory.decodeResource(getResources(), R.drawable.cursor2);
        R_cursor3 = BitmapFactory.decodeResource(getResources(), R.drawable.cursor3);
        R_DRU = BitmapFactory.decodeResource(getResources(), R.drawable.dru1);

        svWajah = (SurfaceView) findViewById(R.id.svWajah);
        //TODO : coba
        svWajah.setZOrderOnTop(true);
        svWajah.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                bg = new Background();
                mata = new Mata(R_mata, R_mata.getWidth(), R_mata.getHeight() / 5, 5);
                mulut = new Mulut(R_mulut, R_mulut.getWidth(), R_mulut.getHeight() / 3, 3);
                alphabet = new Mulut(R_alphabet, R_alphabet.getWidth(), R_alphabet.getHeight() / 9, 9);
                mic = new Mic(R_mic, WIDTH, HEIGHT);
                info = new Info(R_info, R_cursor1, R_cursor2, R_cursor3, WIDTH, HEIGHT);
                menu = new Menu(Wajah.this, R_DRU, WIDTH, HEIGHT);

                thread = new MainThread(svWajah.getHolder(), Wajah.this);
                //we can safely start the loop
                thread.setRunning(true);
                thread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                thread.setRunning(false);
                while (retry) {
                    try {
                        thread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }
                thread = null;
            }
        });
        svWajah.setOnTouchListener(this);

        svCamera = (SurfaceView) findViewById(R.id.svCamera);
        //TODO: Coba di bikin transparant
        svCamera.getHolder().setFormat(PixelFormat.TRANSPARENT);
        svCamera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        //====================================BLUETOOTH=============================================
        mHandler = new Handler();

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //===================================END BLUETOOTH==========================================

        context = this;

        Display d = getWindowManager().getDefaultDisplay();
        maxHeight = d.getHeight();
        maxWidth = d.getWidth();
        divWidth = maxWidth / 14;
        divHeight = maxHeight / 8;

        try {
            bufferSize = AudioRecord
                    .getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);
        } catch (Exception e) {
            android.util.Log.e("TrackingFlow", "Exception", e);
        }

        serverUdp = new ServerUdp(this);
        tts = new TTS(this);

        wajahActivity = this;
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
//        Log.d("ON RESUME", "onResume");
        //audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
        //        AudioFormat.CHANNEL_IN_MONO,
        //        AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        //audio.startRecording();
        resumeCam();
        //====================================BLUETOOTH=============================================
        scanLeDevice(true);
        //===================================END BLUETOOTH==========================================
        serverUdp.onResume();
        speechRecognition = new SpeechRecognition(this);
    }
    //======================================End_Resume==============================================

    //========================================Pause=================================================
    @Override
    protected void onPause() {
        super.onPause();
        client.setData("client", "OFF");
        try {
            if (audio != null) {
                audio.stop();
                audio.release();
                //audio = null;
            }
        } catch (Exception e) {e.printStackTrace();}
        pauseCam();
//        Log.d("ON PAUSE", "onPause");
    }
    //=======================================End_Pause==============================================

    //=========================================Stop=================================================
    @Override
    protected void onStop() {
        super.onStop();
//        Log.d("ON STOP", "onStop");
        scanLeDevice(false);
        isStopListen = true;
        speechRecognition.destroy();
        speechRecognition = null;
    }
    //=======================================End_Stop===============================================

    //======================================Destroy=================================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.d("ON Destroy", "onDestroy");
        destroyCam();
        serverUdp.onDestroy();
        tts.destroy();

        //====================================BLUETOOTH=============================================
        startRequest = false;
        if (isRegister) {
            bReady = false;
            unregisterReceiver(mGattUpdateReceiver);
            unbindService(mServiceConnection);
        }
        mBluetoothLeService = null;
        //===================================END BLUETOOTH==========================================
    }
    //====================================End_Destroy===============================================

    public static void startRecord(){
        audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        audio.startRecording();
    }

    public static void stopRecord(){
        try {
            if (audio != null) {
                audio.stop();
                audio.release();
                //audio = null;
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public static void readAudioBuffer() {

        try {
            short[] buffer = new short[bufferSize];

            int bufferReadResult = 1;

            if (audio != null) {

                // Sense the voice...
                bufferReadResult = audio.read(buffer, 0, bufferSize);
                double sumLevel = 0;
                for (int i = 0; i < bufferReadResult; i++) {
                    sumLevel += buffer[i];
                }
                lastLevel = Math.abs((sumLevel / bufferReadResult));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=====================================Get_Instance_Wajah=======================================
    public static Wajah getInstance() {
        return wajahActivity;
    }
    //===================================End_Get_Instance_Wajah=====================================

    //=========================================Mute=================================================
    public static void mute() {
        AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }
    //=========================================Mute=================================================

    //========================================Unmute================================================
    public static void unmute() {
        AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }
    //======================================End_Unmute==============================================

    /*==========================================BLE=================================================*/
    //========================================Runnable==============================================
    Runnable setCharacteristic = new Runnable() {
        @Override
        public void run() {
            /*check if the service is available on the device*/
            BluetoothGattService mCustomService = BluetoothLeService.mBluetoothGatt
                    .getService(UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"));

            /*get the read characteristic from the service*/
            BluetoothGattCharacteristic characteristic = mCustomService
                    .getCharacteristic(UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e"));
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
            }
            mBluetoothLeService.setCharacteristicNotification(
                    characteristic, true);
            mBluetoothLeService.readCharacteristic(characteristic);
            //kirim perintah bluetooth ready
            bReady = true;
            client.setData("client", "ON");
            mHandler.postDelayed(modeRide, 1000);
        }
    };
    //======================================End_Runnable============================================

    Runnable modeRide = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                mBluetoothLeService.writeCharacteristic("55AA040A037A010073FF");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startRequest = true;

        }
    };

    //==============================BluetoothAdapter_LeScanCallback=================================
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (device.getAddress().equals(ninebot)) {
                                if (mScanning) {
                                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                                    mScanning = false;
                                }
                                //input code to control
                                mDeviceAddress = device.getAddress();
                                Intent gattServiceIntent = new Intent(Wajah.this, BluetoothLeService.class);
                                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                                isRegister = true;
                            }
                        }
                    });
                }
            };
    //============================End_BluetoothAdapter_LeScanCallback===============================

    //====================================Service_Connection========================================
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    //==================================End_Service_Connection======================================

    //====================================Broadcast_Receiver========================================
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                System.out.println("Discovered Sucsess");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                displayNotif(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
    //==================================End_Broadcast_Receiver======================================

    //===================================Dispay_GATT_Service========================================
    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
            mHandler.postDelayed(setCharacteristic, 1000);
        }
    }
    //=================================End_Dispay_GATT_Service======================================

    //==================================Display_Notification========================================
    public void displayNotif(String notif) {
        for (int i = 0; i < notif.length(); i += 2) {
            display += notif.substring(i, i + 2);
        }
        String[] parts = display.split("#");
        int lengthData = Integer.parseInt(parts[2], 16) + 6;
        String d = "";
        for (int i = 0; i < parts.length; i++) {
            d += parts[i];
        }
        if (lengthData == parts.length) {
            parsingData(parts);
        }
    }
    //================================End_Display_Notification======================================

    //======================================Parsing_Data============================================
    private void parsingData(String[] parts) {
        int[] indeks = new int[10];
        String[] str = new String[10];
        int[] intData = new int[10];
        if (parts != null) {
            Integer variable = Integer.parseInt(parts[5], 16);
            if (variable == BATTERY) {
                for (int i = 0; i < 3; i++) {
                    indeks[i] = (allRequest[i] - variable) * 2 + 6;
                    str[i] = parts[indeks[i] + 1] + parts[indeks[i]];
                    intData[i] = Integer.parseInt(str[i], 16);
                }
                //battery
                value[0] = (float) (intData[0]);
                //current speed
                if (intData[1] > 32768) {
                    value[1] = (float) ((intData[1] - 65536) * 0.001);
                } else {
                    value[1] = (float) (intData[1] * 0.001);
                }
                //temperature
                value[2] = (float) (intData[2] * 0.1);
            } else if (variable == VOLTAGE) {
                for (int i = 3; i < 9; i++) {
                    indeks[i] = (allRequest[i] - variable) * 2 + 6;
                    str[i] = parts[indeks[i] + 1] + parts[indeks[i]];
                    intData[i] = Integer.parseInt(str[i], 16);
                }

                //voltage
                value[3] = (float) (intData[3] * 0.01);

                /**
                 *  current,
                 *  pitch angle,
                 *  roll angle,
                 *  pitch angle velocity,
                 *  roll angle velocity
                 **/
                for (int i = 4; i < 9; i++) {
                    if (intData[i] > 32768)
                        value[i] = (float) (intData[i] - 65536);
                    else {
                        value[i] = (float) (intData[i] * 0.01);
                    }
                }
            }
            DecimalFormat df = new DecimalFormat("#.##");
            client.setData("client", df.format(value[0]) + "#" + df.format(value[1]) + "#" + df.format(value[2]) + "#"
                    + df.format(value[3]) + "#" + df.format(value[4]) + "#" + df.format(value[5]) + "#"
                    + df.format(value[6]) + "#" + df.format(value[7]) + "#" + df.format(value[8]));
        }
    }
    //====================================End_Parsing_Data==========================================

    //======================================IntenFilter=============================================
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    //====================================End_IntenFilter===========================================

    //=====================================Scan_BLE_Deive===========================================
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    //===================================End_Scan_BLE_Deive=========================================
    /*========================================End_BLE===============================================*/

    //======================================Update==================================================
    public void update() {
        if(bicara){
            //readAudioBuffer();
        }
        info.update();
        mic.update();
        menu.update();
    }
    //=====================================End_Update===============================================

    public void displayDialogWindow() {
//        final AlertDialog.Builder changeIp = new AlertDialog.Builder(new ContextThemeWrapper(mContext,
//                android.R.style.Theme_DeviceDefault_Light_Dialog));
//        LayoutInflater factory = LayoutInflater.from(mContext);
//        final View f = factory.inflate(R.layout.dialog_ip_address, null);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ip_address);
        dialog.setTitle("Ganti Ip Address");

        final EditText etIp = (EditText) dialog.findViewById(R.id.et_ip_address);
        //final EditText etIpPost = (EditText) dialog.findViewById(R.id.et_ip_address_post);

        final Button ok = (Button) dialog.findViewById(R.id.bOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etIp.getText().toString().equals("")) {
                    Toast.makeText(Wajah.this, "Tidak Ada Perubahan!", Toast.LENGTH_SHORT).show();
                } else {
                    String temp = "";
                    if (!etIp.getText().toString().equals("")) {
                        temp += "Ip Server " + Komunikasi.ipServer;
                        Komunikasi.ipServer = etIp.getText().toString();
                        temp += " Menjadi " + Komunikasi.ipServer + "\n";

                        SharedPreferences settings = getSharedPreferences("prefs", 0); //0 mode private
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("ip", Komunikasi.ipServer);
                        editor.commit();

                        if (Wajah.bReady) {
                            client.setData("client", "ON");
                        } else {
                            client.setData("client", "OFF");
                        }
                    }
                    Toast.makeText(Wajah.this, temp, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        final Button cancel = (Button) dialog.findViewById(R.id.bCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Button check = (Button) dialog.findViewById(R.id.bCheck);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Wajah.this, "IP Server : " + Komunikasi.ipServer, Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }

    public void draw(Canvas canvas) {
        scaleFactorX = canvas.getWidth() / (WIDTH * 1.f);
        scaleFactorY = canvas.getHeight() / (HEIGHT * 1.f);
        if (canvas != null) {
            final int saveState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            //Background
            bg.draw(canvas);

            //Mata
            mata.draw(canvas);
            //Mulut
            if(bicara){
                if(lastLevel > -1 && lastLevel <= 15){
                    mulut.draw(canvas);
                }else if(lastLevel > 15 && lastLevel <= 30){
                    alphabet.drawAlphabet(canvas, 6);
                }else if(lastLevel > 30 && lastLevel <= 50){
                    alphabet.drawAlphabet(canvas, 7);
                }else if(lastLevel > 50 && lastLevel <= 90){
                    alphabet.drawAlphabet(canvas, 4);
                }else if(lastLevel > 90 && lastLevel <= 130){
                    alphabet.drawAlphabet(canvas, 0);
                }if(lastLevel > 130){
                    alphabet.drawAlphabet(canvas, 5);
                }
            } else if (TTS.isTalking) {
                alphabet.drawAlphabet(canvas);
            } else {
                mulut.draw(canvas);
            }

            //Starting Draw
            info.startDraw(canvas);
            mic.startDraw(canvas);
            menu.startDraw(canvas);

            //Mic
            mic.draw(canvas);
            //Info
            info.draw(canvas);
            //Menu
            menu.draw(canvas);

            canvas.restoreToCount(saveState);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            //Ketika jari menyentuh layar
            case MotionEvent.ACTION_DOWN:
                x = (int) (event.getX() / scaleFactorX);
                y = (int) (event.getY() / scaleFactorY);
                break;
            //Ketika jari terlepas dari layar
            case MotionEvent.ACTION_UP:
                tap();

                Mic.startDragKanan = false;
                Mic.startDragKiri = false;
                x = 0;
                y = 0;
                break;
            //Ketika jari bergerak
            case MotionEvent.ACTION_MOVE:
                x = (int) (event.getX() / scaleFactorX);
                y = (int) (event.getY() / scaleFactorY);

                collision();
                break;
            //Ketika bergerak keluar dari layar
            case MotionEvent.ACTION_OUTSIDE:
                x = 0;
                y = 0;
                break;
        }
        return true;
    }

    //========================================Tap===================================================
    //jika rect di tap dan berintersets(berpotongan)
    public void tap() {
        //menu
        if (!Mic.bukaInfoMic && !Menu.tapMenu && Rect.intersects(cursor(), menu.buttonText())) {
            Menu.tapMenu = true;
            Menu.revMenu = false;
        } else if (Menu.tapMenu && Rect.intersects(cursor(), menu.buttonText())) {
            Menu.tapMenu = false;
            Menu.revMenu = true;
        }

//        if (Menu.tapMenu && Rect.intersects(cursor(), new Rect(0, 0, 200, 200))) {
//            Intent i = new Intent("android.intent.action.WEBUNIKOM");
//            startActivity(i);
//        }

        if (Menu.tapMenu && Rect.intersects(cursor(), new Rect(30, HEIGHT - 50 - menu.getHeightLogo(), menu.getWidthLogo(), HEIGHT - 50))) {
            displayDialogWindow();
        }

        //mic
        if (!Mic.bukaInfoMic && Mic.startDragKanan && x > 480) {
            Mic.bukaInfoMic = true;
        } else if (Mic.bukaInfoMic && Mic.startDragKiri && x < WIDTH - 300) {
            Mic.bukaInfoMic = false;
        }

        //info
        if (!Menu.tapMenu && !Mic.bukaInfoMic && Rect.intersects(cursor(), info.getRectInfo())) {
            Info.bukaInfo = true;
        } else if (Info.bukaInfo || Rect.intersects(cursor(), info.getRectInfo())) {
            Info.bukaInfo = false;
            Info.revCur1 = false;
            Info.xAnim1 = 200;
            Info.yAnim1 = 20;
            Info.xAnim2 = 0;
            Info.yAnim2 = 0;
        }
    }
    //=======================================End_Tap================================================

    //=====================================Collision================================================
    //jika rect berpotongan
    public void collision() {
        //mic
        if (!Menu.tapMenu && Rect.intersects(cursor(), new Rect(0, 0, 100, HEIGHT))) {
            Mic.startDragKanan = true;
        }
        if (Mic.bukaInfoMic && Rect.intersects(cursor(),
                new Rect(WIDTH - 200, 0, WIDTH, HEIGHT))) {
            Mic.startDragKiri = true;
        }
    }
    //===================================End_Collision==============================================

    //=======================================Cursor=================================================
    //cursor jika di touch
    public Rect cursor() {
        cur.set(x - 2, y - 2, x + 2, y + 2);
        return cur;
    }
    //=====================================End_Cursor===============================================

    //Camera
    private Camera mCamera;
    //Size
    private static Camera.Size mPreviewSize;
    //byte Array
    private static byte[] mLastFrame = null;

    //int
    private static final int MAX_BUFFER = 3;
    private static int mFrameLength;

    //LinkedList
    private static LinkedList<byte[]> mQueue = new LinkedList<byte[]>();

    private ClientCamera mThread = null;

    public void resumeCam() {
        mCamera = CameraManager.getCameraInstance();
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewSize(320, 240); // set preview size. smaller is better
        mCamera.setParameters(params);

        mPreviewSize = mCamera.getParameters().getPreviewSize();

        int format = mCamera.getParameters().getPreviewFormat();
        mFrameLength = mPreviewSize.width * mPreviewSize.height * ImageFormat.getBitsPerPixel(format) / 8;
        try {
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.setPreviewDisplay(svCamera.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
        }
        if (mThread == null) {
            mThread = new ClientCamera();

            mThread.setRunning(true);

            mThread.start();
        }
    }

    //========================================Pause=================================================
    public void pauseCam() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
        }
        mCamera = null;
        resetBuff();
    }
    //======================================End_Pause===============================================

    //=====================================Reset_Buffer=============================================
    private void resetBuff() {

        synchronized (mQueue) {
            mQueue.clear();
            mLastFrame = null;
        }
    }
    //=====================================Reset_Buffer=============================================

    //================================Camera_PreviewCallback========================================
    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            synchronized (mQueue) {
                if (data != null) {
                    if (mQueue.size() >= MAX_BUFFER) {
                        mQueue.poll();
                    }
                    mQueue.add(data);
                }
            }
        }
    };
    //==============================End_Camera_PreviewCallback======================================

    //===================================Get_Image_Buffer===========================================
    public static byte[] getImageBuffer() {
        synchronized (mQueue) {
            if (mQueue.size() > 0) {
                mLastFrame = mQueue.poll();
            }
        }

        return mLastFrame;
    }
    //=================================End_Get_Image_Buffer=========================================

    //==================================Get_Preview_Length==========================================
    public static int getPreviewLength() {
        return mFrameLength;
    }
    //================================End_Get_Preview_Length========================================

    //==================================Get_Preview_Width===========================================
    public static int getPreviewWidth() {
        return mPreviewSize.width;
    }
    //================================End_Get_Preview_Width=========================================

    //==================================Get_Preview_Height==========================================
    public static int getPreviewHeight() {
        return mPreviewSize.height;
    }
    //================================End_Get_Preview_Height========================================

    //=================================Close_Client_Camera==========================================
    private void closeClientCamera() {
        if (mThread == null)
            return;
        mThread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
        mThread = null;
    }
    //===============================End_Close_Client_Camera========================================

    public void destroyCam() {
        mThread.onDestroy();
        closeClientCamera();
    }
    //======================================Alhamdulillah===========================================
}