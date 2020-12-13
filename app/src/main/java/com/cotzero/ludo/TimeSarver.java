package com.cotzero.ludo;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.cotzero.ludo.interfaces.TimeListener;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;


public class TimeSarver {

    Context context;
    Handler handler;
    TimeListener timeListener;

    public TimeSarver(Context context) {
        this.context = context;
        handler= new Handler(context.getMainLooper());
        final Boolean shouldRun=true;
        timeListener = (TimeListener)context;

        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "ntp02.oal.ul.pt";
                      //  , "ntp04.oal.ul.pt", "ntp.xs4all.nl"};

                NTPUDPClient client = new NTPUDPClient();
                client.setDefaultTimeout(5000);



                    try {
                        InetAddress hostAddr = InetAddress.getByName(host);
                        System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());

                        while (shouldRun) {

                            final TimeInfo info = client.getTime(hostAddr);
                            final Date date = new Date(info.getReturnTime());

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    timeListener.onTimeChange(info.getReturnTime(), date);
                                }
                            });

                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        timeListener.onError(e);

                    }


                client.close();



            }
        }).start();
    }
}
