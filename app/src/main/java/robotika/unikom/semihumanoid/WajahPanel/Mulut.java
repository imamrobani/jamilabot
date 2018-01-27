package robotika.unikom.semihumanoid.WajahPanel;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import robotika.unikom.semihumanoid.Animasi.Animasi;
import robotika.unikom.semihumanoid.MainThread.MainThread;
import robotika.unikom.semihumanoid.Wajah;

/**
 * Created by Dena Meidina on 27/12/2016.
 */

public class Mulut extends WajahObject {

    //=======================================Variable===============================================
    //Bitmap
    private Bitmap spriteSheet;

    //Java Class
    private Animasi animasi = new Animasi();
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public Mulut(Bitmap res, int w, int h, int numFrames){
        x = 0;
        y = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spriteSheet = res;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spriteSheet, 0, i * height, width, height);
        }
        animasi.setFrames(image);
    }
    //==================================End_Constructor=============================================

    //=======================================Update=================================================
    public void update(){
    }
    //=====================================End_Update===============================================

    //========================================Drawing===============================================
    public void draw(Canvas canvas){
        int panjang = Wajah.WIDTH;
        int lebar = Wajah.HEIGHT;
        x = (panjang - width)/2;
        y = ((lebar - (Mata.heightMata / 2)) / 4) + Mata.heightMata + 20;
        canvas.drawBitmap(animasi.getImageMulut(), x, y, null);
    }

    public void drawAlphabet(Canvas canvas){
        int panjang = Wajah.WIDTH;
        int lebar = Wajah.HEIGHT;
        x = (panjang - width)/2;
        y = ((lebar - (Mata.heightMata / 2)) / 4) + Mata.heightMata + 20;
        canvas.drawBitmap(animasi.getImageBicara(), x, y, null);
    }
    public void drawAlphabet(Canvas canvas, int i){
        MainThread.getBitmapBicara = i;
        int panjang = Wajah.WIDTH;
        int lebar = Wajah.HEIGHT;
        x = (panjang - width)/2;
        y = ((lebar - (Mata.heightMata / 2)) / 4) + Mata.heightMata + 20;
        canvas.drawBitmap(animasi.getImageBicara(), x, y, null);
    }
    //======================================End_Drawing=============================================
}
