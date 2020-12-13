package com.cotzero.ludo;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;

public class PositionsAndSizes {

    Context context;
    String TAG = "Position";

    public PositionsAndSizes(Context context) {
        this.context = context;

    }

    public float getStatusBarHeight() {
        float result = 0;
        float resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize((int) resourceId);
        }
       // int result = (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        Log.i(TAG,"status_bar"+result);
        return result;
    }

    public float getDisplayWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Log.i(TAG,"display_width:"+displayMetrics.widthPixels);
        return  displayMetrics.widthPixels;
    }


    public float getDisplayHeight(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Log.i("full_size",displayMetrics.heightPixels+"");

        float x = displayMetrics.heightPixels-getStatusBarHeight();
        Log.i(TAG,"display_height:"+x);
        return x ;
      //  return relativeLayout.getHeight();
    }

    public float getOneSqureSize(){
        float width = getDisplayWidth();
        float a = 12/11;
        float b= 15+a;
        float x = width/b;
        Log.i(TAG,"one_square:"+x);
        Log.i(TAG,"board_size:"+x*15);

        return  x;
    }



    public int getYPix(int y){
        float boardPadding = getDisplayWidth()-(15*getOneSqureSize());
        boardPadding=boardPadding/2;
        Log.i("boardPadding",boardPadding+"");

        float v=getDisplayWidth()-boardPadding+(getDisplayWidth()/3);
        float u = (float) (v-(y*getOneSqureSize()) + 0.5*getOneSqureSize());
        return (int) u;

    }

    public int getXPix(int x){
        float boardPadding = getDisplayWidth()-(15*getOneSqureSize());
        boardPadding= boardPadding/2;

        float u = (float) (getOneSqureSize()*x +boardPadding - getOneSqureSize()*0.5 );

        return (int) u;

    }


}
