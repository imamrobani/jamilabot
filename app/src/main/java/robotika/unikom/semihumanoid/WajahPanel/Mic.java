package robotika.unikom.semihumanoid.WajahPanel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import robotika.unikom.semihumanoid.SpeechRecognition.SpeechRecognition;
import robotika.unikom.semihumanoid.TTS.TTS;
import robotika.unikom.semihumanoid.Wajah;

/**
 * Created by Dena Meidina on 28/12/2016.
 */

public class Mic extends WajahObject {

    //=======================================Variable===============================================
    //Bitmap
    private Bitmap mic;

    //Paint
    private Paint pBg = new Paint();
    private Paint pShadow = new Paint();
    private Paint pLine = new Paint();
    private Paint pText = new Paint();
    private Paint pTitle = new Paint();
    private Paint pMic = new Paint();
    private Paint pMicStop = new Paint();
    private Paint pMicReady = new Paint();
    private Paint pMicListen = new Paint();

    //boolean
    public static boolean startDragKanan = false;
    public static boolean startDragKiri = false;
    public static boolean bukaInfoMic = false;
    private boolean revShadow = false;

    //int
    private final int MAXDRAG = 700;
    private int jarakDrag = 0;
    private int width;
    private int height;
    private int divWidth;
    private int scale;
    private int radius_shadow = 10;
    private int geser;

    //String
    private String nama_robot;
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public Mic(Bitmap mic, int width, int height) {
        this.mic = mic;
        this.width = width;
        this.height = height;

        this.nama_robot = Wajah.nama_robot.toUpperCase();

        divWidth = Wajah.divWidth;

        scale = width / 7;

        pBg.setARGB(247, 75, 75, 75);

        pText.setColor(Color.WHITE);
        pText.setTextSize(25);
        pText.setTypeface(Wajah.customFont);

        pTitle.setColor(Color.WHITE);
        pTitle.setTextSize(40);
        pTitle.setTypeface(Wajah.customFont);

        pShadow.setColor(Color.rgb(72, 72, 72));

        pLine.setStrokeWidth(5);
        pLine.setColor(Color.rgb(120, 120, 120));

        //Shadow Red
        pMicStop.setColor(Color.argb(255, 72, 72, 72));
        //Shadow Blue
        pMicReady.setColor(Color.argb(255, 72, 72, 72));
        //Shadow Green
        pMicListen.setColor(Color.argb(255, 72, 72, 72));
    }
    //==================================End_Constructor=============================================

    //=======================================Update=================================================
    public void update() {
        //=================================Update_Background========================================
        if (startDragKanan && !bukaInfoMic) {
            jarakDrag = Wajah.x;
            if (jarakDrag > 599) {
                jarakDrag = 600 + (Wajah.x - 600) / 10;
                if (jarakDrag >= MAXDRAG) {
                    jarakDrag = MAXDRAG;
                }
            }
        }

        if (startDragKiri && bukaInfoMic) {
            jarakDrag = Wajah.x;
        }

        if (bukaInfoMic) {
            jarakDrag += 2 * divWidth;
            if (jarakDrag >= width) {
                jarakDrag = width;
            }
        }
        if (!bukaInfoMic) {
            jarakDrag -= 2 * divWidth;
            if (jarakDrag <= 0) {
                jarakDrag = 0;
            }
        }
        geser = width - jarakDrag;
        //===============================End_Update_Background======================================

        //===============================Update_Radius_Shadow=======================================
        if (!revShadow) {
            if (SpeechRecognition.isStartListen) {
                radius_shadow += 5;
                if (radius_shadow > 91) {
                    revShadow = true;
                }
            }
            if (!SpeechRecognition.isStartListen) {
                radius_shadow++;
                if (radius_shadow > 50) {
                    revShadow = true;
                }
            }
        }
        if (revShadow) {
            if (SpeechRecognition.isStartListen) {
                radius_shadow -= 5;
                if (radius_shadow < 6) {
                    revShadow = false;
                }
            } else {
                radius_shadow--;
                if (radius_shadow < 6) {
                    revShadow = false;
                }
            }
        }

        pMicStop.setShadowLayer(radius_shadow, 0, 0, Color.RED);
        pMicReady.setShadowLayer(radius_shadow, 0, 0, Color.rgb(90, 202, 234));
        pMicListen.setShadowLayer(radius_shadow, 0, 0, Color.GREEN);

        if (SpeechRecognition.isStartListen && SpeechRecognition.isCallName)
            pMic = pMicListen;
        else if (SpeechRecognition.isCallName)
            pMic = pMicReady;
        else
            pMic = pMicStop;
        //=============================End_Update_Radius_Shadow=====================================
    }
    //=====================================End_Update===============================================

    //========================================Drawing===============================================
    public void startDraw(Canvas canvas) {
        canvas.drawCircle(mic.getWidth() / 2, height - mic.getHeight() / 2, 59, pMic);

        canvas.drawBitmap(mic, 0, height - mic.getHeight(), null);

        if(!SpeechRecognition.isCallName) {
            canvas.drawText("Say \"" + nama_robot + "\"", 15,
                    5 + getTextHeight("Say \"" + nama_robot + "\"", pText), pText);
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, jarakDrag, height, pBg);

        int heightText = getTextHeight("A", pText) + 10;
        canvas.drawText("TANDA WARNA MIC", (width / 2 - getTextWidth("TANDA WARNA MIC", pTitle) / 2) - geser,
                height / 2 - mic.getHeight() / 2, pTitle);

        //mic stop
        canvas.drawCircle((scale + mic.getWidth() / 2) - geser, height / 2, 59, pMicStop);
        canvas.drawBitmap(mic, scale - geser, height / 2 - mic.getHeight() / 2, null);
        canvas.drawText("Mode tidak mendengar", (scale + mic.getWidth() / 2 - getTextWidth("Mode tidak mendengar", pText) / 2) - geser,
                height / 2 + mic.getHeight() / 2, pText);
        canvas.drawText("panggil \"" + Wajah.nama_robot + "\" untuk", (scale + mic.getWidth() / 2 - getTextWidth("panggil \"" + Wajah.nama_robot + "\" untuk", pText) / 2) - geser,
                height / 2 + mic.getHeight() / 2 + heightText, pText);
        canvas.drawText("siap mendengarkan", (scale + mic.getWidth() / 2 - getTextWidth("siap mendengarkan", pText) / 2) - geser,
                height / 2 + mic.getHeight() / 2 + 2 * heightText, pText);

        //mic ready
        canvas.drawCircle((3 * scale + mic.getWidth() / 2) - geser, height / 2, 59, pMicReady);
        canvas.drawBitmap(mic, (3 * scale) - geser, height / 2 - mic.getHeight() / 2, null);
        canvas.drawText("Mode siap mendengarkan", (3 * scale + mic.getWidth() / 2 - getTextWidth("Mode siap mendengarkan", pText) / 2) - geser,
                height / 2 + mic.getHeight() / 2, pText);
        canvas.drawText("sebut apa yang anda perlukan?", (3 * scale + mic.getWidth() / 2 - getTextWidth("sebut apa yang anda perlukan?", pText) / 2) - geser,
                height / 2 + mic.getHeight() / 2 + heightText, pText);

        //mic listen
        canvas.drawCircle((5 * scale + mic.getWidth() / 2) - geser, height / 2, 59, pMicListen);
        canvas.drawBitmap(mic, (5 * scale) - geser, height / 2 - mic.getHeight() / 2, null);
        canvas.drawText("Mode mendengarkan", (5 * scale + mic.getWidth() / 2 - getTextWidth("Mode mendengarkan", pText) / 2) - geser,
                height / 2 + mic.getHeight() / 2, pText);
        canvas.drawText("akan terus mendengar", (5 * scale + mic.getWidth() / 2 - getTextWidth("akan terus mendengar", pText) / 2) - geser,
                height / 2 + mic.getHeight() / 2 + heightText, pText);
        canvas.drawText("selama anda bicara", (5 * scale + mic.getWidth() / 2 - getTextWidth("selama anda bicara", pText) / 2) - geser,
                height / 2 + mic.getHeight() / 2 + 2 * heightText, pText);

        if (startDragKanan) {
            canvas.drawLine(jarakDrag - 15, height / 2 - 35, jarakDrag - 5, height / 2, pLine);
            canvas.drawLine(jarakDrag - 5, height / 2, jarakDrag - 15, height / 2 + 35, pLine);
        } else if (!bukaInfoMic && jarakDrag <= 0) {
            canvas.drawLine(0, height / 2 - 35, 20, height / 2, pLine);
            canvas.drawLine(20, height / 2, 0, height / 2 + 35, pLine);
        }
        if (startDragKiri) {
            canvas.drawLine(jarakDrag - 5, height / 2 - 35, jarakDrag - 15, height / 2, pLine);
            canvas.drawLine(jarakDrag - 15, height / 2, jarakDrag - 5, height / 2 + 35, pLine);
        } else if (bukaInfoMic && jarakDrag >= width) {
            canvas.drawLine(width, height / 2 - 35, width - 20, height / 2, pLine);
            canvas.drawLine(width - 20, height / 2, width, height / 2 + 35, pLine);
        }
    }
    //======================================End_Drawing=============================================
}
