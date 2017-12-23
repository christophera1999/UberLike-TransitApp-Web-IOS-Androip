package com.tranxitpro.app.Helper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Window;

import com.tranxitpro.app.R;


public class CustomDialog extends ProgressDialog {

    public CustomDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setIndeterminate(true);
        setMessage("Please wait...");
      //  setContentView(R.layout.custom_dialog);
    }
}
