package com.cotzero.ludo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cotzero.ludo.interfaces.Listener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Listener {

    ImageView board;
    PositionsAndSizes positionsAndSizes;

    String TAG = "MainActivity";
    RelativeLayout relativeLayout;
    LottieAnimationView lottieAnimationView,lottieAnimationTimeout;


    Cursor red1Cursor,red2Cursor,red3Cursor,red4Cursor, yellow1Cursor,yellow2Cursor,yellow3Cursor,yellow4Cursor;
    List<Cursor> cursors;
    int cursorSize;
    PositonList safeZones;
    List<PositionModel> safeZonesList;
    Random random;

    int steps=0;
    int turn =1;
    int player=2;

    Boolean canDiceRoll = true;
    Boolean canCursorGo= true;
    String tableCode;

    DatabaseReference reference;
    RelativeLayout relativeLayout1;
    TextView name2,name3,name1,name4;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableCode= getIntent().getStringExtra("code");
        reference = FirebaseDatabase.getInstance().getReference().child("pwf").child(tableCode);

        random = new Random();
        board= findViewById(R.id.board);
        relativeLayout = findViewById(R.id.rev);
        lottieAnimationView=findViewById(R.id.animationView);
        lottieAnimationView.setAnimation(R.raw.dice1);
        name1=findViewById(R.id.name1);
        name4=findViewById(R.id.name4);
        name2=findViewById(R.id.name2);
        name3=findViewById(R.id.name3);


        positionsAndSizes= new PositionsAndSizes(this);
        cursors = new ArrayList<>();
        safeZones = new PositonList(5);
        safeZonesList =safeZones.getList();


        int w= (int) positionsAndSizes.getDisplayWidth();
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(w, w);
        parms.setMargins(0, (int) (positionsAndSizes.getDisplayWidth()/3),0,0);
        board.setLayoutParams(parms);

        RelativeLayout.LayoutParams nameParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        nameParams2.setMargins(0, (int) (positionsAndSizes.getDisplayWidth()/3)-100,0,0);
        name2.setLayoutParams(nameParams2);

        RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(0, (int) (positionsAndSizes.getDisplayWidth()/3)-100,0,0);
        nameParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        name3.setLayoutParams(nameParams);

        RelativeLayout.LayoutParams nameParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        nameParams1.setMargins(0, (int) (1.36*positionsAndSizes.getDisplayWidth()),0,0);
        name1.setLayoutParams(nameParams1);

        RelativeLayout.LayoutParams nameParams4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        nameParams4.setMargins(0, (int) (1.36*positionsAndSizes.getDisplayWidth()),0,0);
        nameParams4.addRule(RelativeLayout.ALIGN_PARENT_END);
        name4.setLayoutParams(nameParams4);

        cursorSize=(int)(positionsAndSizes.getOneSqureSize()*1.5);


        red1Cursor = new Cursor(this,relativeLayout,1,cursorSize ,0);
        red2Cursor = new Cursor(this,relativeLayout,1,cursorSize ,1);
        red3Cursor = new Cursor(this,relativeLayout,1,cursorSize ,2);
        red4Cursor = new Cursor(this,relativeLayout,1,cursorSize ,3);

        yellow1Cursor = new Cursor(this,relativeLayout,3,cursorSize,4);
        yellow2Cursor = new Cursor(this,relativeLayout,3,cursorSize,5);
        yellow3Cursor = new Cursor(this,relativeLayout,3,cursorSize,6);
        yellow4Cursor = new Cursor(this,relativeLayout,3,cursorSize,7);


        cursors.add(red1Cursor);
        cursors.add(red2Cursor);
        cursors.add(red3Cursor);
        cursors.add(red4Cursor);
        cursors.add(yellow1Cursor);
        cursors.add(yellow2Cursor);
        cursors.add(yellow3Cursor);
        cursors.add(yellow4Cursor);


       int j = 0;
        for(int i=0;i<cursors.size();i++){
            if(i<4){
                j=i+1;
            }else if(i>=4){
                j=(i+1)-4;
            }
            cursors.get(i).multipleMember(4, j);
        }




        for(int i = 0; i< cursors.size(); i++){
            final int finalI = i;
            cursors.get(i).getIv().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(canCursorGo) {
                        cursors.get(finalI).go(steps);
                    }
                }
            });
        }

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(canDiceRoll) {
                    int randomNumber = random.nextInt(7);
                    rollTheDice(randomNumber);
                }

            }
        });

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                canDiceRoll=false;
                canCursorGo=false;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                setTurn(turn,steps);

                canCursorGo=true;
                canDiceRoll=false;

                Toast.makeText(MainActivity.this, "Roll completed turn : "+turn, Toast.LENGTH_SHORT).show();

                if(steps!=6) {
                    if (turn == 3) {
                        turn = 1;
                    } else {
                        turn = 3;
                    }
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

                Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


    }

    void setTurn(int turn,int steps){
        for(int i = 0; i<cursors.size();i++){

            if(cursors.get(i).getColor()==turn){
                cursors.get(i).setCanCursorGo(turn,true,steps);
            }else {
                cursors.get(i).setCanCursorGo(turn,false,steps);
            }
        }

    }



    @Override
    public void onPositionChange(int a) {

    }

    int beforeX,beforeY;

    @Override
    public void onBeforeGo(int color, int xPosition, int yPosition, int index, int cursorIndex) {
        canCursorGo=false;
        beforeX=xPosition;
        beforeY = yPosition;
    }


    @Override
    public void onCompleteGo(int color, int xPosition, int yPosition, int index, int cursorIndex) {

        int count =0;
        int countB =0;
        int j=0,k=0;

        for(int i=0;i<cursors.size();i++){
            cursors.get(i).setCanCursorGo(color,false,steps);
            if(cursors.get(i).getCurrentX()==xPosition && cursors.get(i).getCurrentY()==yPosition){
                count++;
            }
            if(cursors.get(i).getCurrentX()==beforeX && cursors.get(i).getCurrentY()==beforeY){
                countB++;
            }
        }

        for(int i=0;i<cursors.size();i++){

            if(cursors.get(i).getCurrentX()==xPosition && cursors.get(i).getCurrentY()==yPosition) {
                j++;
                if (color == cursors.get(i).color) {
                    cursors.get(i).multipleMember(count, j);
                } else {
                    if (isSafeZone(xPosition, yPosition)) {
                        cursors.get(i).multipleMember(count, j);
                    } else {
                        turn=color;
                        cursors.get(i).goToHome();
                    }
                }
            }

            if(cursors.get(i).getCurrentX()==beforeX && cursors.get(i).getCurrentY()==beforeY){
                k++;
                cursors.get(i).multipleMember(countB,k);
            }
        }

        canDiceRoll=true;


    }

    Boolean isSafeZone(int x,int y){

        int i=0;
        for(PositionModel position : safeZonesList){
            if(position.getX() == x && position.getY() == y){
                i=1;
                break;
            }
        }

        if(i==0){
            return false;
        }else {
            return true;
        }

    }

    void rollTheDice(int randomNumber){


        switch (randomNumber + 1) {

            case 1:
                lottieAnimationView.setAnimation(R.raw.dice1);
                steps = 1;
                break;
            case 2:
                lottieAnimationView.setAnimation(R.raw.dice2);
                steps = 2;
                break;
            case 3:
                lottieAnimationView.setAnimation(R.raw.dice3);
                steps = 3;
                break;
            case 4:
                lottieAnimationView.setAnimation(R.raw.dice4);
                steps = 4;
                break;
            case 5:
                lottieAnimationView.setAnimation(R.raw.dice5);
                steps = 5;
                break;
            case 6:
                lottieAnimationView.setAnimation(R.raw.dice6);
                steps = 6;
                break;
        }

        lottieAnimationView.playAnimation();
    }


    void players(){
        
    }



}