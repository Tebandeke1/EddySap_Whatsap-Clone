package com.tabutech.eddysap.View.Activities.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.interfaces.OnCallBack;

public class DialogReviewSendImage {
     private Context context;
     private Dialog dialog;
     private Bitmap bitmap;
     private ZoomageView view;
     private FloatingActionButton send;

    public DialogReviewSendImage(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
        dialog = new Dialog(context);

        initialize();
    }

    private void initialize() {
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);//before
        dialog.setContentView(R.layout.activity_review_send_image);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams layout = new WindowManager.LayoutParams();

        layout.copyFrom(dialog.getWindow().getAttributes());
        layout.width  = WindowManager.LayoutParams.MATCH_PARENT;
        layout.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layout);

        view = dialog.findViewById(R.id.image_zoom);
        send = dialog.findViewById(R.id.fbBtn);

    }

    public void show(OnCallBack callBack){
        dialog.show();
        view.setImageBitmap(bitmap);

        send.setOnClickListener(view -> {
            callBack.onButtonSendClick();
            dialog.dismiss();
        });
    }

}
