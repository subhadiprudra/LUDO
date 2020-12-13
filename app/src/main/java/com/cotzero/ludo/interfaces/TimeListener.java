package com.cotzero.ludo.interfaces;

import java.util.Date;

public interface TimeListener {

    void onTimeChange(long time, Date date);
    void onError(Exception e);
}
