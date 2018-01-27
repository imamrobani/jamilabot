package robotika.unikom.semihumanoid.WajahPanel;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Dena Meidina on 27/12/2016.
 */

public abstract class WajahObject {

    //=======================================Variable===============================================
    //int
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    Rect bounds = new Rect();;
    //=====================================End_Variable=============================================

    //=======================================Get_Rect===============================================
    public Rect getRecteangle(){
        return new Rect(x , y , x+width , y+height);
    }
    //=====================================End_Get_Rect=============================================

    //mendapatkan width text dalam px
    public int getTextWidth(String text, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.left + bounds.width();
    }
    //==================================End_Get_Text_Width==========================================

    //====================================Get_Text_Height===========================================
    //mendapatkan height text dalam px
    public int getTextHeight(String text, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.bottom + bounds.height();
    }
    //==================================End_Get_Text_Height=========================================
}
