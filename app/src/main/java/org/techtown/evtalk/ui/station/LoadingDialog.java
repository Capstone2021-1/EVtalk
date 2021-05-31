package org.techtown.evtalk.ui.station;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import org.techtown.evtalk.R;

public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading_dialog);
    }
}