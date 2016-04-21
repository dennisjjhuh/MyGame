package com.game.jhuh1_cc.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jhuh1-cc on 3/16/2016.
 */
public class GamePanelView extends SurfaceView implements SurfaceHolder.Callback {

    Context context;
    public static final int MOVESPEED = -5;
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    private long sharkStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Shark> sharks;
    private Random rand = new Random();
    private boolean newGameCreated;
    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;
    private int best;

    public GamePanelView(Context context) {
        super(context);

        context = context;

        //Add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //Make gamePanelView focusable si it can be handled events
        setFocusable(true);
    }

    public GamePanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.undersea));
        //player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.diver), 130, 70, 3);
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.diver2), 500, 400, 1);
        sharks = new ArrayList<Shark>();
        sharkStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean reTry = true;
        int counter = 0;

        while(reTry && counter < 1000){
            counter++;
            try{
                thread.setRunning(false);
                thread.join(); //Blocks the current thread until the receiver finishes its execution and dies.
                reTry = false;
                thread = null;
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!player.getPlaying() && newGameCreated && reset){
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying()){
                if(!started)
                    started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(){

        if(player.getPlaying()){

            bg.update();
            player.update();

            long sharkElapsed = (System.nanoTime() - sharkStartTime)/1000000;

            if(sharkElapsed > (2000 - player.getScore()/4)){
                /*if(sharks.size() == 0){
                    sharks.add(new Shark(BitmapFactory.decodeResource(getResources(),
                            R.drawable.shark), WIDTH + 10, HEIGHT / 2, 40, 20,
                            player.getScore(), 10));
                }else{
                    sharks.add(new Shark(BitmapFactory.decodeResource(getResources(),
                            R.drawable.shark), WIDTH + 10,
                            (int)(rand.nextDouble() * (HEIGHT)), 40, 20,
                            player.getScore(), 10));
                }*/
                /*if(sharks.size() == 0){
                    sharks.add(new Shark(BitmapFactory.decodeResource(getResources(),
                            R.drawable.shark2), WIDTH + 10, HEIGHT / 2, 400, 230,
                            player.getScore(), 1));
                }else{
                    sharks.add(new Shark(BitmapFactory.decodeResource(getResources(),
                            R.drawable.shark2), WIDTH + 10,
                            (int)(rand.nextDouble() * (HEIGHT)), 400, 230,
                            player.getScore(), 1));
                }*/
                if(sharks.size() == 0){
                    sharks.add(new Shark(BitmapFactory.decodeResource(getResources(),
                            R.drawable.shark2), WIDTH+1000, HEIGHT / 2, 400, 230,
                            player.getScore(), 1));
                }else{
                    sharks.add(new Shark(BitmapFactory.decodeResource(getResources(),
                            R.drawable.shark2), WIDTH+1000,
                            (int)(rand.nextDouble() * (HEIGHT+400)), 400, 230,
                            player.getScore(), 1));
                }
                sharkStartTime = System.nanoTime();
            }

            for(int i=0; i<sharks.size();i++){
                sharks.get(i).update();
                if(collision(sharks.get(i), player)){
                    sharks.remove(i);
                    player.setPlaying(false);
                    break;
                }

                if(sharks.get(i).getX() < -100){
                    sharks.remove(i);
                    break;
                }
            }
        }else{
            player.resetDY();
            if(!reset){
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                /*explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion),
                        player.getX(), player.getY()-30, 100, 100, 1);*/
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion),
                        player.getX(), player.getY()-30, 400, 240, 1);
                disappear = true;
            }

            explosion.update();
            long resetElapsed = (System.nanoTime() - startReset) / 1000000;
            if(resetElapsed > 2500 && !newGameCreated){
                newGame();
            }
        }
    }

    public boolean collision(GameObject a, GameObject b){
        //Returns true if the two specified rectangles intersect.
        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas != null) {
            final int savedState = canvas.save();

            //canvas.scale(scaleFactorX, scaleFactorY);

            bg.draw(canvas);

            if(!disappear){
                player.draw(canvas);
            }

            for(Shark s: sharks){
                s.draw(canvas);
            }

           if(started){
                explosion.draw(canvas);
            }

            drawText(canvas);

            canvas.restoreToCount(savedState);
        }

        /*if(canvas != null) {
            bg.draw(canvas);
            player.draw(canvas);
        }*/
    }

    public void newGame(){

        newGameCreated = true;
        disappear = false;

        sharks.clear();
        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT / 2);

        if(player.getScore() > best){
            best = player.getScore();
        }
    }

    public void drawText(Canvas canvas){
        /*Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE: " + player.getScore() * 3, 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);*/

        if(!player.getPlaying() && newGameCreated && reset){
            Paint paint1 = new Paint();
            //paint1.setTextSize(40);
            paint1.setTextSize(100);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            //canvas.drawText("PRESS TO START", WIDTH / 2 - 50, HEIGHT / 2, paint1);
            canvas.drawText("PRESS TO START", WIDTH, HEIGHT, paint1);

            /*paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH / 2 - 50, HEIGHT / 2 + 20, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH / 2 - 50, HEIGHT / 2 + 40, paint1);*/

        }
    }
}
