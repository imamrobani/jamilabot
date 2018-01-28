package robotika.unikom.semihumanoid.WajahPanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import robotika.unikom.semihumanoid.Wajah;

/**
 * Created by Dena Meidina on 28/12/2016.
 */

public class Menu extends WajahObject {

    //=======================================Variable===============================================
    //String
    private final String MENU = "M E N U";

    //boolean
    public static boolean tapMenu = false;
    public static boolean revMenu = false;

    //Bitmap
    private Bitmap dev;
    //private Bitmap logo_video_call;

    //int
    private int i, j;
    private int width;
    private int height;
    private int divWidth;
    private int divHeight;
    private int xText, yText;
    private int widthText;
    private int heightText;

    //Paint
    private Paint pBg = new Paint();
    private Paint pText = new Paint();
    private Paint pButton = new Paint();
    private Paint pComming = new Paint();

    //Rect
    private Rect rectButton = new Rect();

    //RectF
    private RectF rectFButton = new RectF();

    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public Menu(Context context, Bitmap dev, int width, int height) {
        this.dev = dev;
        //logo_video_call = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_video_call);

        this.width = width;
        this.height = height;
        divWidth = Wajah.divWidth;
        divHeight = Wajah.divHeight;
        xText = width - (width / 8);
        yText = height - (height / 8);

        pBg.setARGB(247, 75, 75, 75);

        pText.setTextSize(25);
        pText.setTypeface(Wajah.customFont);

        pButton.setAntiAlias(true);

        pComming.setColor(Color.WHITE);
        pComming.setTextSize(50);
        pComming.setTypeface(Wajah.customFont);

        widthText = getTextWidth(MENU, pText);
        heightText = getTextHeight(MENU, pText);
    }
    //==================================End_Constructor=============================================

    //=======================================Update=================================================
    public void update() {
        //paint text
        pText.setColor(Color.WHITE);

        //paint button
        pButton.setColor(Color.GRAY);
        pButton.setShadowLayer(30, 0, 0, Color.RED);
        if (tapMenu && !revMenu) {
            //paint text
            pText.setColor(Color.BLACK);

            //paint button
            pButton.setColor(Color.LTGRAY);
            pButton.setShadowLayer(25, 0, 0, Color.argb(255, 90, 202, 234));

            i -= 2 * divWidth;
            j -= divHeight;
            if (i <= -width) {
                i = -width;
            }
            if (j <= -height) {
                j = -height;
            }
        } else {
            i += 2 * divWidth;
            j += divHeight;
            if (i >= 0)
                i = 0;
            if (j >= 0)
                j = 0;
        }

        if (revMenu) {
            i += 2 * divWidth;
            j += divHeight;
            if (i >= 0)
                i = 0;
            if (j >= 0)
                j = 0;
        }
    }
    //=====================================End_Update===============================================

    //========================================Drawing===============================================
    public void draw(Canvas canvas) {
        if(tapMenu) {
            canvas.drawRect(width + i, height + j, width, height, pBg);
            //buat logo
            //canvas.drawBitmap(dev, (getTextWidth("Opsi Pengembang", pText) / 2 - dev.getWidth() / 2) + width + i,
              //      (height - (getTextHeight("Opsi Pengembang", pText) + dev.getHeight() + 30)) + height + j, null);
            canvas.drawText("Opsi Pengembang", width + i, (height - getTextHeight("Opsi Pengembang", pText)) + height + j, pText);

            canvas.drawText("COMING SOON", (width / 2 - getTextWidth("COMING SOON", pComming) / 2) + width + i,
                    (height / 2 - getTextHeight("COMING SOON", pComming) / 2) + height + j, pComming);

            //canvas.drawBitmap(logo_video_call, width + i, height + j, null);

            if (!Mic.bukaInfoMic && !Info.bukaInfo) {
                canvas.drawRoundRect(roundedButton(), 12, 12, pButton);
                canvas.drawText(MENU, xText, yText, pText);
            }
        }
    }

    public void startDraw(Canvas canvas){
        canvas.drawRoundRect(roundedButton(), 12, 12, pButton);
        canvas.drawText(MENU, xText, yText, pText);
    }
    //======================================End_Drawing=============================================

    //======================================Button_Rect=============================================
    public Rect buttonText() {
        int textHeight = yText - heightText;
        rectButton.set(xText - 10, textHeight - 10, xText + widthText + 10, textHeight + heightText + 10);
        return rectButton;
    }
    //====================================End_Button_Rect===========================================

    //=====================================Rounded_Rect=============================================
    public RectF roundedButton() {
        int textHeight = yText - heightText;
        rectFButton.set(xText - 10, textHeight - 10, xText + widthText + 10, textHeight + heightText + 10);
        return rectFButton;
    }
    //===================================End_Rounded_Rect===========================================

    public int getWidthLogo(){
        return dev.getWidth();
    }

    public int getHeightLogo(){
        return dev.getHeight();
    }
}
