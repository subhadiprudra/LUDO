package com.cotzero.ludo;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cotzero.ludo.interfaces.Listener;

import java.util.List;

import androidx.annotation.RequiresApi;

public class Cursor {
    Context context;
    RelativeLayout relativeLayout;
    int x,y;
    int color,size;
    ImageView iv;
    PositionsAndSizes positionsAndSizes;
    List<PositionModel> positionModels;
    PositonList positonList;
    private int index,currentX,currentY;
    Handler handler;
    int cursorIndex;
    Listener changeListener;
    Boolean canCursorGo=false;

    int leftMargin,topMargin;
    int xPix,yPix;


    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Cursor(Context context, RelativeLayout relativeLayout, int color, int size,int cursorIndex) {

        this.context = context;
        this.relativeLayout = relativeLayout;
        this.color = color;
        this.size=size;
        this.cursorIndex=cursorIndex;
        positionsAndSizes = new PositionsAndSizes(context);
        iv = new ImageView(context.getApplicationContext());


        changeListener = (Listener) context;

        switch (color) {
            case 1:

                iv.setImageDrawable(context.getDrawable(R.raw.red_curser));
                positonList = new PositonList(1);
                break;

            case 2:
                iv.setImageDrawable(context.getDrawable(R.raw.green_curser));
                positonList = new PositonList(2);
                break;

            case 3:
                iv.setImageDrawable(context.getDrawable(R.raw.yellow_curser));
                positonList = new PositonList(3);
                break;

            case 4:
                iv.setImageDrawable(context.getDrawable(R.raw.blue_curser));
                positonList = new PositonList(4);
                break;

        }
        positionModels = positonList.getList();
        Log.i("size",positionModels.size()+"");

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size, size);
        lp.setMargins(x-size,y,0,0);
        iv.setLayoutParams(lp);
        changeLocationByIndex(0);


        handler= new Handler(context.getMainLooper());


        }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    void changeLoaction(int x, int y){

        int a=positionsAndSizes.getXPix(x);
        int b =positionsAndSizes.getYPix(y);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size,size);
        relativeLayout.removeView(iv);
        leftMargin=a-size/2;
        topMargin=b-size+(size/6);
        lp.setMargins(leftMargin,topMargin,0,0);
        iv.setLayoutParams(lp);
        relativeLayout.addView(iv);

    }

    void changeLocationByIndex(int index){

        this.index=index;
        currentX= positionModels.get(index).getX();
        currentY = positionModels.get(index).getY();
        int a=xPix=positionsAndSizes.getXPix(currentX);
        int b =yPix= positionsAndSizes.getYPix(currentY);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size,size);
        relativeLayout.removeView(iv);
        leftMargin=a-size/2;
        topMargin=b-size+(size/6);
        lp.setMargins(leftMargin,topMargin,0,0);
        iv.setLayoutParams(lp);
        relativeLayout.addView(iv);


    }

    void multipleMember(int totalMember,int memberIndex){

        int p = positionsAndSizes.getXPix(currentX);
        int b=yPix = positionsAndSizes.getYPix(currentY);

        int start = p-(size/2);
        int oneStepSize = size/(totalMember+1);

        int newPix=xPix= start+(oneStepSize*memberIndex);

        //int s =(int) (size-totalMember*10);
        int s = size;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(s,s);
        relativeLayout.removeView(iv);
        leftMargin=newPix-s/2;
        topMargin=b-s+(s/6);
        lp.setMargins(leftMargin,topMargin,0,0);
        iv.setLayoutParams(lp);
        relativeLayout.addView(iv);

    }

    void setCanCursorGo(int color,Boolean value,int steps){

            if (color==this.color && value && index+steps<57) {
                canCursorGo = true;
                int s = (int) (size + 25);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(s, s);
                relativeLayout.removeView(iv);
                lp.setMargins(xPix - s / 2, yPix - s + (s / 6), 0, 0);
                iv.setLayoutParams(lp);
                relativeLayout.addView(iv);

            } else {
               // int s = (int) (size - 10);
                int s=size;
                canCursorGo=false;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(s, s);
                relativeLayout.removeView(iv);
                lp.setMargins(xPix - s / 2, yPix - s + (s / 6), 0, 0);
                iv.setLayoutParams(lp);
                relativeLayout.addView(iv);
            }
        }





    int getCurrentIndex(){
        return index;
    }

    int getCurrentX(){
        return currentX;
    }

    int getCurrentY(){
        return currentY;
    }

    ImageView getIv(){
        return iv;
    }

    int getColor(){
        return color ;
    }

    void go(final int count){

        if(canCursorGo) {


            changeListener.onBeforeGo(color, currentX, currentY, index, cursorIndex,false);


            new Thread(new Runnable() {
                @Override
                public void run() {

                    for(int i =1;i<=count;i++){

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (positionModels.size() - 1 > index) {
                                    index = index + 1;
                                    changeLocationByIndex(index);
                                    changeListener.onPositionChange(index);
                                }

                            }
                        });

                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changeListener.onCompleteGo(color, currentX, currentY, index, cursorIndex,false);
                        }
                    });
                }
            }).start();


        }


    }


    void goToHome(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changeListener.onBeforeGo(color,currentX,currentY,index,cursorIndex,true);
                    }
                });

                for(int i=index;i>0;i--) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(positionModels.size()-1>index){
                                index =index-1;
                                changeLocationByIndex(index);
                                changeListener.onPositionChange(index);
                            }
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changeListener.onCompleteGo(color,currentX,currentY,index,cursorIndex,true);
                    }
                });


            }
        }).start();

    }





}
