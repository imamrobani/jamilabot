package robotika.unikom.semihumanoid.Animasi;

import android.graphics.Bitmap;

import robotika.unikom.semihumanoid.MainThread.MainThread;

/**
 * Created by Dena Meidina on 27/12/2016.
 */

public class Animasi {

    //=======================================Variable===============================================
    //Bitmap
    private Bitmap[] frames;

    //boolean
    private boolean reverse = false;

    //int
    private int currentFrame = 0;
    //=====================================End_Variable=============================================

    //=================================Setting_Frame_Bitmap=========================================
    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
    }
    //===============================End_Setting_Frame_Bitmap=======================================

    //====================================Get_Image_Mata============================================
    public Bitmap getImageMata() {
        return frames[currentFrame];
    }
    //==================================End_Get_Image_Mata==========================================

    //===================================Get_Image_Mulut============================================
    public Bitmap getImageMulut() {
        return frames[0];
    }
    //=================================End_Get_Image_Mulut==========================================

    //===================================Get_Image_Mulut============================================
    public Bitmap getImageBicara() {
        return frames[MainThread.getBitmapBicara];
    }
    //=================================End_Get_Image_Mulut==========================================

    //====================================Update_Animasi============================================
    //update mata
    public void update() {
        if(!reverse) {
            currentFrame++;
            if (currentFrame >= frames.length) {
                reverse = true;
            }
        }
        if(reverse){
            currentFrame--;
            if(currentFrame <= 0){
                reverse = false;
                MainThread.dKedip = 2 + MainThread.r.nextInt(4);
                MainThread.elapsed = 0;
            }
        }
    }
    //==================================End_Update_Animasi==========================================
}
