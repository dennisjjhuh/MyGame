package com.game.jhuh1_cc.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.animation.Animation;

/**
 * Created by Dennis on 2016-03-25.
 */
public class Player extends  GameObject{

    private Bitmap spritesheet;
    private int score;
    private boolean up;
    private boolean playing;
    private com.game.jhuh1_cc.mygame.Animation animation = new com.game.jhuh1_cc.mygame.Animation();
    private long startTime;


    public Player(Bitmap res, int w, int h, int numFrames) {
        //x = 100;
        x = GamePanelView.WIDTH / 4;
        y = GamePanelView.HEIGHT / 2;
        dy = 0;
        score = 0;
        width = w;
        height = h;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setUp(boolean b){
        up = b;
    }

    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100)
        {
            score++;
            startTime = System.nanoTime();
        }
        //animation.update();

        /*if(up){
            dy = (int)(dya-=1.1);

        }
        else{
            dy = (int)(dya+=1.1);
        }*/

        /*if(up){
            dy = (int)(dya -= 0.7);
        }
        else{
            dy = (int)(dya += 0.7);
        }*/

        if(up){
            dy -= 1.7;
        }
        else{
            dy += 1.7;
        }

        if(dy > 14)
            dy = 14;

        if(dy < -14)
            dy = -14;

/*      y += dy*2;
        dy = 0;*/

        y += dy*2;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }

    public int getScore(){
        return score;
    }

    public boolean getPlaying(){
        return playing;
    }

    public void setPlaying(boolean b){
        playing = b;
    }

    public void resetDY(){
        dy = 0;
    }

    public void resetScore(){
        score = 0;
    }

}
