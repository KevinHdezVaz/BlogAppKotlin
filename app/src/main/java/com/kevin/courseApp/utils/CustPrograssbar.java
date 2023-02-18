package com.kevin.courseApp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.kevin.courseApp.R;

public class CustPrograssbar {


    Dialog epicDialog2;

    public void prograssCreate(Context context){

        epicDialog2 = new Dialog(context);
        epicDialog2.setContentView(R.layout.progress_layout);
        epicDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog2.setCanceledOnTouchOutside(false);
        epicDialog2.show();

    }


    public void closePrograssBar() {

        epicDialog2.dismiss();
    }
}
