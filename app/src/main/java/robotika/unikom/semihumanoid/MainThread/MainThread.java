package robotika.unikom.semihumanoid.MainThread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.Random;

import robotika.unikom.semihumanoid.Json.BackgroundWorker;
import robotika.unikom.semihumanoid.Json.TanyaJawab;
import robotika.unikom.semihumanoid.SpeechRecognition.SpeechRecognition;
import robotika.unikom.semihumanoid.TTS.TTS;
import robotika.unikom.semihumanoid.Wajah;

/**
 * Created by Dena Meidina on 27/12/2016.
 */

public class MainThread extends Thread {

    //=======================================Variable===============================================
    //SurfaceHolder
    private SurfaceHolder surfaceHolder;

    //Java Class
    private Wajah wajahPanel;

    //Canvas
    private Canvas canvas;

    //Random
    public static Random r = new Random();

    //boolean
    private boolean running;
    private boolean changeRequest = true;

    //int
    public static int dKedip = 2 + r.nextInt(4);
    private int noAlphabet = 0;

    //long
    public static int getBitmapBicara;
    public static long elapsed = 0;
    private long delayIsCallName = 0;
    private long delayBicara = 0;
    private long delayRequest = 0;
    //=====================================End_Variable=============================================


    //====================================Constructor===============================================
    public MainThread(SurfaceHolder surfaceHolder, Wajah wajahPanel) {
        //super();
        this.surfaceHolder = surfaceHolder;
        this.wajahPanel = wajahPanel;
    }
    //==================================End_Constructor=============================================

    //===================================Thread.Start===============================================
    @Override
    public void run() {
        //super.run();
        long startTime;
        while (running) {
            startTime = System.nanoTime();
            //try locking canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.wajahPanel.update();
                    this.wajahPanel.draw(canvas);
                }
            } catch (Exception e) {

            }finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //Timer kedip
            elapsed += System.nanoTime() - startTime;
            if (elapsed / 1000000 > dKedip * 1000) {
                Wajah.mata.update();
            }

            //Delay bicara
            if (TTS.isTalking) {
                delayBicara += System.nanoTime() - startTime;
                char[] chr = TanyaJawab.jawab.toCharArray();
                if (noAlphabet == chr.length) {
                    noAlphabet = 0;
                }
                if (chr[noAlphabet] == 'a')
                    getBitmapBicara = 0;
                else if (chr[noAlphabet] == 'i')
                    getBitmapBicara = 1;
                else if (chr[noAlphabet] == 'u')
                    getBitmapBicara = 2;
                else if (chr[noAlphabet] == 'e')
                    getBitmapBicara = 3;
                else if (chr[noAlphabet] == 'o')
                    getBitmapBicara = 4;
                else if (chr[noAlphabet] == 'l')
                    getBitmapBicara = 5;
                else if (chr[noAlphabet] == 'b' || chr[noAlphabet] == 'm' || chr[noAlphabet] == 'p')
                    getBitmapBicara = 6;
                else if (chr[noAlphabet] == 'f' || chr[noAlphabet] == 'v')
                    getBitmapBicara = 7;
                else if (chr[noAlphabet] == 'w' || chr[noAlphabet] == 'q')
                    getBitmapBicara = 8;
                if (delayBicara / 1000000 >= 60 / TTS.getSpeedRate()) {
                    noAlphabet++;
                    delayBicara = 0;
                }
            } else {
                noAlphabet = 0;
            }

            //Delay for call name
            if (SpeechRecognition.isCallName) {
                delayIsCallName += System.nanoTime() - startTime;
                if (delayIsCallName / 1000000 > 30000) { //30 detik
                    SpeechRecognition.isCallName = false;
                    delayIsCallName = 0;
                }
            } else {
                delayIsCallName = 0;
            }

            //BLE
            if (Wajah.startRequest) {
                delayRequest += System.nanoTime() - startTime;
                if (delayRequest / 1000000 >= 250) {
                    Wajah.display = "";
                    if (changeRequest) {
                        Wajah.mBluetoothLeService.writeCharacteristic("55AA030A01223A95FF");
                        changeRequest = false;
                        delayRequest = 0;
                    } else {
                        Wajah.mBluetoothLeService.writeCharacteristic("55AA030A01473C6EFF");
                        changeRequest = true;
                        delayRequest = 0;
                    }
                }
            }
        }
    }
    //===================================End_Thread.Start===========================================

    //========================================Setting===============================================
    public void setRunning(boolean b) {
        running = b;
    }
    //======================================End_Setting=============================================
}
