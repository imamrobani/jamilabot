package robotika.unikom.semihumanoid.WajahPanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Dena Meidina on 29/12/2016.
 */

public class Background {
    //=======================================Variable===============================================
    private Paint pBg1 = new Paint();
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public Background() {
        String warna;
        warna = "#ffe0bd";
        pBg1.setColor(Color.parseColor(warna));
    }
    //==================================End_Constructor=============================================

    //========================================Drawing===============================================
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth() * 2, canvas.getHeight() * 2, pBg1);
    }
    //======================================End_Drawing=============================================
}
