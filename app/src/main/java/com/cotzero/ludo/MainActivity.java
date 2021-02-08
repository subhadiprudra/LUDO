package com.cotzero.ludo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.asp.fliptimerviewlibrary.CountDownClock;
import com.cotzero.ludo.interfaces.Listener;
import com.cotzero.ludo.interfaces.MainCountDownListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Listener, com.cotzero.ludo.interfaces.CountDown, MainCountDownListener {

    ImageView board;
    PositionsAndSizes positionsAndSizes;

    String TAG = "MainActivity";
    RelativeLayout relativeLayout;
    LottieAnimationView lottieAnimationView,lottieAnimationTimeout;
    ProgressBar p1,p2,p3,p4;
    TextView scoreTv1,scoreTv2,scoreTv3,scoreTv4;


     Cursor red1Cursor,
            red2Cursor,
            red3Cursor,
            red4Cursor,
            yellow1Cursor,
            yellow2Cursor,
            yellow3Cursor,
            yellow4Cursor,
            green1Cursor,
            green2Cursor,
            green3Cursor,
            green4Cursor,
            blue1Cursor,
            blue2Cursor,
            blue3Cursor,
            blue4Cursor;

    List<Cursor> cursors;
    int cursorSize;
    PositonList safeZones;
    List<PositionModel> safeZonesList;
    Random random;

    int steps=0;
    int turnColor =1;
    int playerCount=2;
    int myColor;

    Boolean canDiceRoll = true;
    Boolean canCursorGo = true;
    String tableCode;

    //DatabaseReference reference;
    RelativeLayout relativeLayout1;
    TextView name2,name3,name1,name4;
    RelativeLayout r1,r2,r3,r4;
    int nameHolderWidth;
    int namHolderHeight=160;
    CountDown countDown1,countDown2,countDown3,countDown4;
    Boolean goStarted=false;
    Boolean isCut=false;
    MainCountDown mainCountDown;
    TextView minTv,secTv;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableCode= getIntent().getStringExtra("code");
//        reference = FirebaseDatabase.getInstance().getReference().child("pwf").child(tableCode);

        random = new Random();
        board= findViewById(R.id.board);
        relativeLayout = findViewById(R.id.rev);
        lottieAnimationView=findViewById(R.id.animationView);
        lottieAnimationView.setAnimation(R.raw.dice1);

        minTv =findViewById(R.id.minutes);
        secTv = findViewById(R.id.seconds);


        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);

        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);

        scoreTv1 = findViewById(R.id.score1);
        scoreTv2 = findViewById(R.id.score2);
        scoreTv3 = findViewById(R.id.score3);
        scoreTv4 = findViewById(R.id.score4);

        positionsAndSizes= new PositionsAndSizes(this);
        cursors = new ArrayList<>();
        safeZones = new PositonList(5);
        safeZonesList =safeZones.getList();
        mainCountDown = new MainCountDown(this,300);


        int w= (int) positionsAndSizes.getDisplayWidth();
        nameHolderWidth = (int) (w/2.1);

        int topMargin= (int) ((positionsAndSizes.getDisplayHeight()-positionsAndSizes.getDisplayWidth())/2);


        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(w, w);
        parms.setMargins(0, (int) (topMargin),0,0);
        board.setLayoutParams(parms);

        RelativeLayout.LayoutParams nameParams2 = new RelativeLayout.LayoutParams(nameHolderWidth,namHolderHeight);
        nameParams2.setMargins(0, (int) (topMargin)-namHolderHeight-30,0,0);
        r2.setLayoutParams(nameParams2);


        RelativeLayout.LayoutParams nameParams3 = new RelativeLayout.LayoutParams(nameHolderWidth,namHolderHeight);
        nameParams3.setMargins(0, (int) (topMargin)-namHolderHeight-30,0,0);
        nameParams3.addRule(RelativeLayout.ALIGN_PARENT_END);
        r3.setLayoutParams(nameParams3);
        if(playerCount==2)r2.setVisibility(View.GONE);


        RelativeLayout.LayoutParams nameParams1 = new RelativeLayout.LayoutParams(nameHolderWidth,namHolderHeight);
        nameParams1.setMargins(0, (int) (topMargin+positionsAndSizes.getDisplayWidth()+30),0,0);
        r1.setLayoutParams(nameParams1);

        RelativeLayout.LayoutParams nameParams4 = new RelativeLayout.LayoutParams(nameHolderWidth,namHolderHeight);
        nameParams4.setMargins(0, (int) (topMargin+positionsAndSizes.getDisplayWidth()+30),0,0);
        nameParams4.addRule(RelativeLayout.ALIGN_PARENT_END);
        r4.setLayoutParams(nameParams4);
        if(playerCount==3 || playerCount==2)r4.setVisibility(View.GONE);

        cursorSize=(int)(positionsAndSizes.getOneSqureSize()*1.5);

        red1Cursor = new Cursor(this,relativeLayout,1,cursorSize ,0);
        red2Cursor = new Cursor(this,relativeLayout,1,cursorSize ,1);
        red3Cursor = new Cursor(this,relativeLayout,1,cursorSize ,2);
        red4Cursor = new Cursor(this,relativeLayout,1,cursorSize ,3);

        cursors.add(red1Cursor);
        cursors.add(red2Cursor);
        cursors.add(red3Cursor);
        cursors.add(red4Cursor);



        countDown1 = new CountDown(this,p1);
        countDown2 = new CountDown(this,p2);
        countDown3 = new CountDown(this,p3);
        countDown4 = new CountDown(this,p4);



        if(playerCount == 3 || playerCount ==4){

            green1Cursor = new Cursor(this,relativeLayout,2,cursorSize ,4);
            green2Cursor = new Cursor(this,relativeLayout,2,cursorSize ,5);
            green3Cursor = new Cursor(this,relativeLayout,2,cursorSize ,6);
            green4Cursor = new Cursor(this,relativeLayout,2,cursorSize ,7);

            cursors.add(green1Cursor);
            cursors.add(green2Cursor);
            cursors.add(green3Cursor);
            cursors.add(green4Cursor);

            name1 = findViewById(R.id.n1);
            name2 = findViewById(R.id.n2);
            name3 = findViewById(R.id.n3);
            name4 = findViewById(R.id.n4);

            name1.setText("Player 1");
            name2.setText("Player 2");
            name3.setText("Player 3");
            name4.setText("Player 4");




        }else {

            name1 = findViewById(R.id.n1);
            name2 = findViewById(R.id.n3);

            name1.setText("Player 1");
            name2.setText("player 2");
        }

        yellow1Cursor = new Cursor(this,relativeLayout,3,cursorSize,8);
        yellow2Cursor = new Cursor(this,relativeLayout,3,cursorSize,9);
        yellow3Cursor = new Cursor(this,relativeLayout,3,cursorSize,10);
        yellow4Cursor = new Cursor(this,relativeLayout,3,cursorSize,11);


        cursors.add(yellow1Cursor);
        cursors.add(yellow2Cursor);
        cursors.add(yellow3Cursor);
        cursors.add(yellow4Cursor);

        if(playerCount == 4){

            blue1Cursor = new Cursor(this,relativeLayout,4,cursorSize ,12);
            blue2Cursor = new Cursor(this,relativeLayout,4,cursorSize ,13);
            blue3Cursor = new Cursor(this,relativeLayout,4,cursorSize ,14);
            blue4Cursor = new Cursor(this,relativeLayout,4,cursorSize ,15);

            cursors.add(blue1Cursor);
            cursors.add(blue2Cursor);
            cursors.add(blue3Cursor);
            cursors.add(blue4Cursor);

        }



      // int j = 0;
        for(int i=0;i<cursors.size();i++){

            if((i+1)%4==0){
                cursors.get(i).multipleMember(4, 4);

            }else {
                cursors.get(i).multipleMember(4, (i+1)%4);
            }

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
                    int randomNumber = random.nextInt(6);
                    randomNumber=randomNumber+1;
                   // reference.child("steps").setValue(randomNumber+"");
                    rollTheDice(randomNumber);

                }

            }
        });

        /*reference.child("steps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String number= snapshot.getValue(String.class);
                rollTheDice(Integer.parseInt(number));
                setTurn(turnColor,steps);

                if(steps!=6) {
                    changeTurn();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                canDiceRoll=false;
                canCursorGo=false;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                canCursorGo=true;
                canDiceRoll=false;

                setTurn(turnColor,steps);

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        countDown1.startCountDown();
        mainCountDown.start();

    }

    void changeTurn(){

        if(playerCount == 2) {
            if (turnColor == 3) {
                turnColor = 1;
            } else {
                turnColor = 3;
            }
        }else if(playerCount==3) {

            if(turnColor == 1){
                turnColor=2;
            }else if(turnColor == 2){
                turnColor=3;
            }else if(turnColor == 3){
                turnColor = 1;
            }

        }else if(playerCount==4) {

            if(turnColor == 1){
                turnColor=2;
            }else if(turnColor == 2){
                turnColor=3;
            }else if(turnColor == 3){
                turnColor = 4;
            }else if(turnColor == 4){
                turnColor =1;
            }

        }
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
    public void onCountDownComplete() {

        Log.i(TAG,"onCountDownComplete");

        if(!goStarted) {

            changeTurn();
            canDiceRoll = true;

            for (int i = 0; i < cursors.size(); i++) {
                cursors.get(i).setCanCursorGo(1, false, 0);
            }

            if (turnColor == 1) {
                countDown1.startCountDown();
            }
            if (turnColor == 2) {
                countDown2.startCountDown();
            }
            if (turnColor == 3) {
                countDown3.startCountDown();
            }
            if (turnColor == 4) {
                countDown4.startCountDown();
            }

        }

    }


    @Override
    public void onPositionChange(int a) {

    }

    int beforeX,beforeY;

    @Override
    public void onBeforeGo(int color, int xPosition, int yPosition, int index, int cursorIndex,Boolean isGoingHome) {
        canCursorGo=false;
        beforeX=xPosition;
        beforeY = yPosition;
        goStarted=true;
        stopAllCountDown();


    }


    @Override
    public void onCompleteGo(int color, int xPosition, int yPosition, int index, int cursorIndex,Boolean isGoingHome) {

        if(steps!=6 && !isGoingHome){
            changeTurn();
        }
        isCut=false;

        int count =0;
        int countB =0;
        int j=0,k=0;


        Log.i(TAG,"onCompleteGo");

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
                } else{
                    if (isSafeZone(xPosition, yPosition)) {
                        cursors.get(i).multipleMember(count, j);

                    } else {
                        turnColor = color;
                        cursors.get(i).goToHome();
                        isCut=true;
                    }
                }
            }

            if(cursors.get(i).getCurrentX()==beforeX && cursors.get(i).getCurrentY()==beforeY){
                k++;
                cursors.get(i).multipleMember(countB,k);
            }
        }


        goStarted=false;
        canDiceRoll=true;

        stopAllCountDown();

        if(!isCut) {
           setCountDown();
        }

        setScores();



    }

    void setCountDown(){
        if (turnColor == 1) {
            countDown1.startCountDown();
        }
        if (turnColor == 2) {
            countDown2.startCountDown();
        }
        if (turnColor == 3) {
            countDown3.startCountDown();
        }
        if (turnColor == 4) {
            countDown4.startCountDown();
        }
    }

    void setScores(){

        int score1=0,score2=0,score3=0,score4=0;

        for(int i=0;i<cursors.size();i++){
            if(cursors.get(i).color==1){
                score1=score1+cursors.get(i).getCurrentIndex();
            }
            if(cursors.get(i).color==2){
                score2=score2+cursors.get(i).getCurrentIndex();
            }
            if(cursors.get(i).color==3){
                score3=score3+cursors.get(i).getCurrentIndex();
            }
            if(cursors.get(i).color==4){
                score4=score4+cursors.get(i).getCurrentIndex();
            }
        }

        scoreTv1.setText("Score : "+score1);
        scoreTv2.setText("Score : "+score2);
        scoreTv3.setText("Score : "+score3);
        scoreTv4.setText("Score : "+score4);
    }

    void stopAllCountDown(){
        countDown1.stop();
        countDown2.stop();
        countDown3.stop();
        countDown4.stop();
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

        switch (randomNumber) {

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

    @Override
    public void onTick(int min, int sec) {

        minTv.setText(min+"");
        secTv.setText(sec+"");

    }

    @Override
    public void onTimeOver() {

    }




    void players(){

        /*reference.child("joined").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        
    }

    BottomSheetDialog exitDialog;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        exitDialog = new BottomSheetDialog(this);
        if(!exitDialog.isShowing()) {
            exitDialog.setContentView(R.layout.exit_dialog);
            exitDialog.show();
        }else {
            exitDialog.dismiss();
        }

    }



}