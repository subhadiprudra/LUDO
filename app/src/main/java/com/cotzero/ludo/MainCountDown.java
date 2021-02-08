package com.cotzero.ludo;

import android.content.Context;
import android.os.Handler;

import com.cotzero.ludo.interfaces.MainCountDownListener;


public class MainCountDown {

    private Context context;
    private int countDownTime;
    private Handler handler;
    private int min,sec;
    private MainCountDownListener mainCountDownListener;


    public MainCountDown(Context context, int countDownTime) {
        this.context = context;
        this.countDownTime = countDownTime;
        handler= new Handler(context.getMainLooper());
        mainCountDownListener = (MainCountDownListener) context;

    }
    void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i =countDownTime; i>=0;i--){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    min= i/60;
                    sec=i%60;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mainCountDownListener.onTick(min,sec);
                        }
                    });


                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainCountDownListener.onTimeOver();
                    }
                });


            }
        });

        thread.start();
    }


}
