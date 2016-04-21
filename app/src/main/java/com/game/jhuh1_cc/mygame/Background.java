package com.game.jhuh1_cc.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Dennis on 2016-03-25.
 */
public class Background {

    static String TAG1 = "Background";
    private Bitmap image;
    private int x=0, y=0, dx=0;

    public Background(Bitmap res)
    {
        image = res;
        dx = GamePanelView.MOVESPEED;
    }

    public void update(){
        x += dx;
        if(x < -GamePanelView.WIDTH){
            Log.i(TAG1, String.valueOf(x));
            x = 0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
        if(x < 0){
            canvas.drawBitmap(image, x+GamePanelView.WIDTH, y, null);
        }
    }

}
