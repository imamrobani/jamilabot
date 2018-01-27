package robotika.unikom.semihumanoid.Komunikasi;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import robotika.unikom.semihumanoid.SpeechRecognition.SpeechRecognition;
import robotika.unikom.semihumanoid.TTS.TTS;
import robotika.unikom.semihumanoid.Wajah;


/**
 * Created by Dena Meidina on 02/01/2017.
 */

public class ServerUdp extends Thread {

    //=======================================Variable===============================================
    //DatagramSocket
    private DatagramSocket serverSocket;

    //Handler
    private Handler mHandler = new Handler();

    //boolean
    private boolean running;

    //String
    private String data;
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public ServerUdp() {
        //do nothing
    }

    public ServerUdp(Context context) {
        //do nothing
        running = true;
        start();
    }
    //==================================End_Constructor=============================================

    //==================================Implement_Thread============================================
    @Override
    public void run() {
        super.run();
        try {
            serverSocket = new DatagramSocket(Komunikasi.portServer);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] receiveData = new byte[64];
        while (running) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sentence = new String(receivePacket.getData());
            data = sentence.substring(receivePacket.getOffset(), receivePacket.getLength());
            if (data.equals("Ans")) {
                ClientCamera.req = false;
            } else if (data.equals("BM")) {
                Wajah.bicara = true;
                SpeechRecognition.isCallName = false;
                SpeechRecognition.stopListen();
                Wajah.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Wajah.startRecord();
                                while (Wajah.bicara) {
                                    Wajah.readAudioBuffer();
                                }
                            }
                        }).start();
                    }
                });
            } else if (data.equals("BA")) {
                Wajah.bicara = false;
                Wajah.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Wajah.stopRecord();
                            }
                        }).start();
                    }
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SpeechRecognition.restartSearch(SpeechRecognition.KWS_SEARCH);
            }/*else if (data.equals("intro")) {
                SpeechRecognition.isCallName = false;
                SpeechRecognition.stopListen();
                TTS.speak("hallo nama saya nakula");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TTS.speak("Saya robot icon universitas komputer indonesia, salam kenal");
            } */ else {
                mHandler.post(send);
            }
        }
    }
    //================================End_Implement_Thread==========================================

    //====================================Runnable_Send=============================================
    Runnable send = new Runnable() {
        @Override
        public void run() {
            if (Wajah.mConnected && Wajah.bReady) {
                Wajah.mBluetoothLeService.writeCharacteristic(data);
            }
        }
    };
    //==================================End_Runnable_Send===========================================

    //=======================================Resume=================================================
    public void onResume() {
    }
    //=====================================End_Resume===============================================

    //======================================Destroy=================================================
    public void onDestroy() {
        running = false;
        serverSocket.close();
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //====================================End_Destroy===============================================
}
