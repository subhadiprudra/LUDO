package com.cotzero.ludo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Random;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CreateTable extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText name;
    Button save;
    String tableCodeToJoin,tableCodeCreated;

    TextView joinBtn,createBtn,tableCodeTv,joinList;
    LinearLayout joinLayout,createLayout;
    EditText editText;
    int i=0;


    CardView start;
    Button joinGame;
    int playerCount=0;

    int c=0;
    Loading loading;
    Socket socket;
    String tableCode="";
    int myIndex;
    int joinPlayerCount=0;
    Pref pref;

    JSONArray playernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);


        name=findViewById(R.id.name);
        joinBtn =findViewById(R.id.join_btn);
        createBtn = findViewById(R.id.create_btn);
        joinLayout = findViewById(R.id.join_layout);
        createLayout = findViewById(R.id.create_layout);
        tableCodeTv=findViewById(R.id.table_code);
        start = findViewById(R.id.start_btn);
        joinList=findViewById(R.id.join_list);
        joinGame =findViewById(R.id.join);
        editText=findViewById(R.id.code_et);
        save = findViewById(R.id.save);
        pref = new Pref(this);
        playernames= new JSONArray();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().equals("")) {
                    pref.write("name", name.getText().toString());
                    Toast.makeText(CreateTable.this, "Name saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(pref.read("name").equals("null")){
            name.setText("");
        }else{
            name.setText(pref.read("name"));
        }




        socket = App.getSocket();
        socket.connect();

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
                    tableCode = getRandomNumberString();
                    tableCodeTv.setText(tableCode);
                    loading.show();
                    socket.emit("join",tableCode+"",name.getText().toString());
                }
                i=1;
            }
        });



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(playerCount>1){
                    goToGame();
                }
            }
        });




        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().equals("")) {
                    if (!editText.getText().toString().equals("")) {
                        tableCodeToJoin = editText.getText().toString();
                        loading.show();
                        socket.emit("join", tableCodeToJoin + "",name.getText().toString());
                    } else {
                        Toast.makeText(CreateTable.this, "Enter table code", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CreateTable.this, "Enter your name", Toast.LENGTH_SHORT).show();
                }

            }
        });

        socket.on("on_join", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String room = (String) args[0];

                Log.i("count__",room+" "+joinPlayerCount);

                if(room.equals(tableCodeToJoin)){
                    joinPlayerCount = (int) args[1];
                }else if(room.equals(tableCode)) {
                    playerCount  = (int) args[1];
                    playernames.put((String) args[2]);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String text="Total player count : " + playerCount;
                            for(int i=0;i<playernames.length();i++){
                                try {
                                    text = text+"\n"+playernames.getString(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            joinList.setText(text);
                            loading.dismiss();
                        }
                    });
                }
            }
        });

        socket.on("msg", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String msg  = (String) args[0];
                Log.i("msg__",msg);
                if(msg.equals("joined")){
                    myIndex = (int)args[1];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.dismiss();
                            joinGame.setText("Joined");
                            joinGame.setClickable(false);
                        }
                    });
                }
                else if(msg.equals("start_game")){

                }

            }
        });

        socket.on("start", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CreateTable.this,PlayWithFriends.class);
                        intent.putExtra("code",tableCodeToJoin);
                        intent.putExtra("count",joinPlayerCount);
                        intent.putExtra("myIndex",myIndex);
                        intent.putExtra("names",(String)args[0]);
                        startActivity(intent);
                    }
                });
            }
        });




    }


    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }




    void goToGame(){
        if(!name.getText().toString().equals("")) {
            socket.emit("start",playernames.toString());
            Intent intent = new Intent(CreateTable.this, PlayWithFriends.class);
            intent.putExtra("code", tableCode);
            intent.putExtra("count", playerCount);
            intent.putExtra("myIndex", myIndex);
            intent.putExtra("names",playernames.toString());
            startActivity(intent);
        }else {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
    }



}