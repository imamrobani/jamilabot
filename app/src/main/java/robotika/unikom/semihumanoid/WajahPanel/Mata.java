package robotika.unikom.semihumanoid.WajahPanel;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import robotika.unikom.semihumanoid.Animasi.Animasi;
import robotika.unikom.semihumanoid.Wajah;

/**
 * Created by Dena Meidina on 27/12/2016.
 */

public class Mata extends WajahObject {

    //=======================================Variable===============================================
    //Bitmap
    private Bitmap spriteSheet;

    //Java Class
    private Animasi animation = new Animasi();

    //int
    public static int heightMata;
    //=====================================End_Variable=============================================


    //====================================Constructor===============================================
    public Mata(Bitmap res, int w, int h, int numFrames) {
        x = 0;
        y = 0;
        height = h;
        width = w;
        heightMata = h;

        Bitmap[] image = new Bitmap[numFrames];
        spriteSheet = res;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spriteSheet, 0, i * height, width, height);
        }

        animation.setFrames(image);
    }
    //==================================End_Constructor=============================================

    //=======================================Update=================================================
    public void update(){
        animation.update();
    }
    //=====================================End_Update===============================================


    //========================================Drawing===============================================
    public void draw(Canvas canvas){
        int panjang = Wajah.WIDTH;
        int lebar = Wajah.HEIGHT;
        x = (panjang - width)/2;
        y = (lebar - (height / 2)) / 4;
        canvas.drawBitmap(animation.getImageMata(), x, y, null);
    }
    //======================================End_Drawing=============================================
}
