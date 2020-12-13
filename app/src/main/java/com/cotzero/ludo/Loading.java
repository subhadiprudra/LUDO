package com.cotzero.ludo;

import android.app.Dialog;
import android.content.Context;

public class Loading {

    Context context;
    Dialog dialog;

    public Loading(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading);

    }

    void show(){
        if(!dialog.isShowing()) {
            dialog.show();
        }
    }
    void dismiss(){
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
