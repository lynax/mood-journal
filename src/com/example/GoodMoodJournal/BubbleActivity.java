package com.example.GoodMoodJournal;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BubbleActivity extends Activity implements View.OnTouchListener {
    BubbleView surfaceView;
    float canvasWidth, canvasHeight, x, y, padding;
    List<Point> points;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new BubbleView(this);
        surfaceView.setOnTouchListener(this);
        canvasWidth = 0;
        canvasHeight = 0;
        padding = 40;
        x = 0;
        y= 0;
        points = new ArrayList<>();
        setContentView(surfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.resume();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        x = event.getX();
        y = event.getY();
        points.add(new Point((int) x, (int) y));
        return true;
    }

    public class BubbleView extends SurfaceView implements Runnable {

        private SurfaceHolder surfaceHolder;
        private Thread thread;
        private boolean isRunning = false;

        public BubbleView(Context context) {
            super(context);
            surfaceHolder = getHolder();
        }

        @Override
        public void run() {
            while(isRunning) {
                if(!surfaceHolder.getSurface().isValid())
                    continue;

                Canvas canvas = surfaceHolder.lockCanvas();

                if(canvasWidth == 0 && canvasHeight == 0) {
                    canvasWidth = canvas.getWidth();
                    canvasHeight = canvas.getHeight();
                }

                canvas.drawRGB(255,255,255);
                TextPaint paint = new TextPaint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(30);
                String text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

                canvas.save();
                StaticLayout layout = new StaticLayout(text, paint, (int) (canvasWidth - 2*padding), Layout.Alignment.ALIGN_NORMAL, 2, 0, true);
                canvas.translate(padding, padding);
                layout.draw(canvas);

                canvas.restore();

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        public void pause() {
            isRunning = false;
            while(true) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            thread = null;
        }

        public void resume() {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }
    }
}