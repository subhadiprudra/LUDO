package com.cotzero.ludo;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;



public class CountDown {

    private ProgressBar progressBar;
    private Context context;
    private Handler handler;
    private boolean shouldStop;
    private com.cotzero.ludo.interfaces.CountDown countDownL;
    private  int i;

    public CountDown(Context context,ProgressBar progressBar) {
        this.progressBar=progressBar;
        this.context = context;
        handler= new Handler(context.getMainLooper());
        countDownL = (com.cotzero.ludo.interfaces.CountDown) context;
    }

    void startCountDown(){

        progressBar.setProgress(0);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(200);
                    shouldStop=false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                for(i=100;i>=0;i--){

                    if(shouldStop){
                        break;
                    }

                    final int finalI = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!shouldStop) {
                                progressBar.setProgress(finalI);
                            }else {
                                progressBar.setProgress(0);
                            }
                        }
                    });

                    if(shouldStop){
                        break;
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("i",i+"");
                        if(i==-1) {
                            countDownL.onCountDownComplete();
                        }
                        progressBar.setProgress(0);
                    }
                });


            }
        }).start();
    }

    void stop(){
        shouldStop=true;
    }



}
