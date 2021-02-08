package com.cotzero.ludo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cotzero.ludo.interfaces.TimeListener;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.time.TimeTCPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import static org.apache.commons.net.discard.DiscardTCPClient.DEFAULT_PORT;

public class Home extends AppCompatActivity implements TimeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CardView cardView = findViewById(R.id.pf);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTable();
            }
        });

        CardView cardView1 = findViewById(R.id.po);
        cardView1.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new TimeSarver(Home.this);

            }
        });




    }

    void createTable(){
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTimeChange(long time, Date date) {
        Log.i("timeMillis",time+"    "+date.getHours()+" : "+date.getMinutes()+" : "+date.getSeconds());
    }

    @Override
    public void onError(Exception e) {
        new TimeSarver(Home.this);
    }
}