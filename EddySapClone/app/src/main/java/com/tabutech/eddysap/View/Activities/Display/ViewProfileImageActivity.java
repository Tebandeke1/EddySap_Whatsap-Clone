package com.tabutech.eddysap.View.Activities.Display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.tabutech.eddysap.Common.Common;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.databinding.ActivityViewProfileImageBinding;

public class ViewProfileImageActivity extends AppCompatActivity {

    private ActivityViewProfileImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_profile_image);

        binding.imageZoom.setImageBitmap(Common.IMAGE_BITMAP);
    }
}