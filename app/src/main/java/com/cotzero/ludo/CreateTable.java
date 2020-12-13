package com.cotzero.ludo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Random;

public class CreateTable extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView dpView;
    TextView name;
    String tableCodeToJoin,tableCodeCreated;

    TextView joinBtn,createBtn,tableCodeTv,joinList;
    LinearLayout joinLayout,createLayout;
    EditText editText;
    int i=0;

    FirebaseUser user;
    CardView start,joinGame;
    int playerCount=0;
    DatabaseReference reference;
    int c=0;
    Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);

        dpView=findViewById(R.id.dp);
        name=findViewById(R.id.nameTv);
        joinBtn =findViewById(R.id.join_btn);
        createBtn = findViewById(R.id.create_btn);
        joinLayout = findViewById(R.id.join_layout);
        createLayout = findViewById(R.id.create_layout);
        tableCodeTv=findViewById(R.id.table_code);
        start = findViewById(R.id.start_btn);
        joinList=findViewById(R.id.join_list);
        joinGame =findViewById(R.id.join);
        editText=findViewById(R.id.code_et);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Picasso.with(this).load(user.getPhotoUrl()).into(dpView);
        name.setText(user.getDisplayName());

        loading = new Loading(this);



        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinBtn.setTextSize(18);
                createBtn.setTextSize(20);
                joinLayout.setVisibility(View.VISIBLE);
                createLayout.setVisibility(View.GONE);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBtn.setTextSize(18);
                joinBtn.setTextSize(20);
                joinLayout.setVisibility(View.GONE);
                createLayout.setVisibility(View.VISIBLE);
                if(i==0){
                    String tableCode = getRandomNumberString();
                    tableCodeTv.setText(tableCode);
                    createTable(tableCode);
                    loading.show();
                }
                i=1;
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerCount>0){
                    reference.child("action").setValue("start").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            goToGame(tableCodeCreated);
                        }
                    });
                }else {
                    Toast.makeText(CreateTable.this, "No one joined", Toast.LENGTH_SHORT).show();
                }
            }
        });


        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals("")){
                    tableCodeToJoin = editText.getText().toString();
                    loading.show();


                    FirebaseDatabase.getInstance().getReference().child("pwf").child(tableCodeToJoin).child("joined").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            c=0;
                            String emails="";
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                String email = dataSnapshot.child("email").getValue(String.class);
                                emails= emails+email+"\n";
                                c++;
                            }

                            if(c==0 || emails.contains(user.getEmail())  || c>4){
                                Toast.makeText(CreateTable.this, "Wrong code", Toast.LENGTH_SHORT).show();
                            }else {

                                reference=FirebaseDatabase.getInstance().getReference().child("pwf").child(editText.getText().toString());
                                HashMap<String,String> map = new HashMap<>();
                                map.put("name",user.getDisplayName());
                                map.put("email",user.getEmail());
                                map.put("uid",user.getUid());
                                map.put("playerNum","0");
                                reference.child("joined").child(user.getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loading.dismiss();
                                        goToGame(tableCodeToJoin);
                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }else {
                    Toast.makeText(CreateTable.this, "Enter your code", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }


    void createTable(String code){

        tableCodeCreated=code;
        reference=FirebaseDatabase.getInstance().getReference().child("pwf").child(code);

        HashMap<String,String> map = new HashMap<>();
        map.put("name",user.getDisplayName());
        map.put("email",user.getEmail());
        map.put("uid",user.getUid());
        map.put("playerNum","0");

        reference.child("joined").child(user.getUid()).setValue(map);
        reference.child("action").setValue("stop");
        reference.child("steps").setValue("0");

        reference.child("joined").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerCount=0;
                String names="joined players : \n";
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name = dataSnapshot.child("name").getValue(String.class);
                    names= names+name+"\n";
                    playerCount++;
                    String uid= dataSnapshot.child("uid").getValue(String.class);
                    reference.child("joined").child(uid).child("playerNum").setValue(playerCount+"");
                }
                joinList.setText(names);
                loading.dismiss();
                Toast.makeText(CreateTable.this, "Table create", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    void goToGame(String code){
        Intent intent = new Intent(CreateTable.this,MainActivity.class);
        intent.putExtra("code",code);
        startActivity(intent);
    }


}