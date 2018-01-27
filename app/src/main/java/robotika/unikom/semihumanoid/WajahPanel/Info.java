package robotika.unikom.semihumanoid.WajahPanel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import robotika.unikom.semihumanoid.Wajah;

/**
 * Created by Dena Meidina on 28/12/2016.
 */

public class Info extends WajahObject {

    //=======================================Variable===============================================
    //Bitmap
    private Bitmap info;
    private Bitmap cursor1;
    private Bitmap cursor2;
    private Bitmap cursor3;
    
    //Paint
    private Paint pInfo = new Paint();
    private Paint pLine = new Paint();
    private Paint pCircle = new Paint();
    private Paint pText = new Paint();

    //boolean
    public static boolean bukaInfo = false;
    public static boolean revCur1 = false;
    
    //int
    public static int xAnim1 = 200;
    public static int yAnim1 = 20;
    public static int xAnim2 = 0;
    public static int yAnim2 = 0;
    private int widthCur1 = 215;
    private int heightCur1 = 165;
    private int widthCur2;
    private int heightCur2;
    private int width;
    private int height;

    //Rect
    Rect rectInfo = new Rect();
    //=====================================End_Variable=============================================

    //====================================Constructor===============================================
    public Info(Bitmap info, Bitmap cursor1, Bitmap cursor2, Bitmap cursor3, int width, int height){
        this.info = info;
        this.cursor1 = cursor1;
        this.cursor2 = cursor2;
        this.cursor3 = cursor3;
        this.width = width;
        this.height = height;

        pInfo.setColor(Color.WHITE);
        pInfo.setTextSize(20);
        pInfo.setTypeface(Wajah.customFont);

        pText.setColor(Color.WHITE);
        pText.setTextSize(25);
        pText.setTypeface(Wajah.customFont);

        pCircle.setARGB(150, 255, 255, 255);
        pCircle.setShadowLayer(20, 0, 0, Color.WHITE);

        pLine.setARGB(150, 255, 255, 255);
        pLine.setShadowLayer(20, 0, 0, Color.WHITE);
        pLine.setStrokeWidth(60);
    }
    //==================================End_Constructor=============================================

    //=======================================Update=================================================
    public void update() {
        if(bukaInfo){
            //Cursor 1
            if(!revCur1){
                xAnim1 -= 7;
                if(xAnim1 < 1){
                    xAnim1 = 0;
                    yAnim1 --;
                    if(yAnim1 < 1){
                        yAnim1 = 0;
                        revCur1 = true;
                    }
                }
            }else{
                xAnim1 += 9;
                if(xAnim1 >= 500){
                    xAnim1 = 500;
                }
            }
            
            //Cursor 2
            widthCur2 = (width + getTextWidth("M E N U", pText) / 2 - (width / 8)) - (width / 2 + 2 * widthCur1);
            heightCur2 = (short) ((height - height / 8) - (height / 2 + widthCur1));

            xAnim2 += 5;
            yAnim2 += 6;

            if (xAnim2 >= widthCur2) {
                xAnim2 = widthCur2;
            }
            if (yAnim2 >= heightCur2) {
                yAnim2 = heightCur2;
            }
        }
    }
    //=====================================End_Update===============================================

    //========================================Drawing===============================================
    public void startDraw(Canvas canvas){
        canvas.drawBitmap(info, width - info.getWidth() - info.getWidth() / 2, info.getHeight() / 2, null);
        canvas.drawText("CLICK", width - info.getWidth() - getTextWidth("CLICK", pInfo) / 2,
                info.getHeight() + info.getHeight() / 2 + getTextHeight("CLIK", pInfo) + width / 192, pInfo);

    }
    public void draw(Canvas canvas) {
        if(bukaInfo){
            //Cursor 1
            if(yAnim1 == 0) {
                canvas.drawBitmap(cursor2, xAnim1 - widthCur1, height / 2 - heightCur1 - yAnim1, null);
            }else{
                canvas.drawBitmap(cursor1, xAnim1 - widthCur1, height / 2 - heightCur1 - yAnim1, null);
            }
            if(revCur1){
                canvas.drawLine(0, height / 2, xAnim1, height / 2, pLine);
                canvas.drawCircle(xAnim1, height / 2, 30, pCircle);
                canvas.drawText("D R A G", xAnim1 - widthCur1 - widthCur1 / 2,
                        height / 2 + getTextHeight("D R A G", pText) + 80, pText);
                canvas.drawText("TANDA WARNA MIC",
                        xAnim1 - widthCur1 - widthCur1 / 2 - getTextWidth("TANDA WARNA MIC",
                                pText) / 2 + getTextWidth("D R A G", pText) / 2,
                        height / 2 - getTextHeight("TANDA WARNA MIC", pText) - 80, pText);
            }
            
            //Cursor 2
            if (xAnim2 >= widthCur2 && yAnim2 >= heightCur2) {
                canvas.drawBitmap(cursor3, width / 2 + widthCur1 + xAnim2, height / 2 + yAnim2, null);
                canvas.drawText("T A P",
                        width + getTextWidth("M E N U", pText) / 2 - width / 8 - getTextWidth("T A P", pText) / 2,
                        height - height / 8 - heightCur1 - getTextHeight("T A P", pText) / 2, pText);
            } else
                canvas.drawBitmap(cursor1, width / 2 + widthCur1 + xAnim2, height / 2 + yAnim2, null);
        }
    }
    //======================================End_Drawing=============================================

    //=======================================Get_Rect===============================================
    public Rect getRectInfo(){
        rectInfo.set(width - info.getWidth() - info.getWidth() / 2, info.getHeight() / 2,
                width - info.getWidth() / 2, info.getHeight() + info.getHeight() / 2 + getTextHeight("CLIK", pInfo) + 10);
        return rectInfo;
    }
    //=====================================End_Get_Rect=============================================
}
