package com.tabutech.eddysap.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.tabutech.eddysap.BuildConfig;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Activities.Contact.ContactActivity;
import com.tabutech.eddysap.View.Activities.Settings.SettingsActivity;
import com.tabutech.eddysap.View.Activities.Status.AddStatusPicActivity;
import com.tabutech.eddysap.databinding.ActivityMainBinding;
import com.tabutech.eddysap.menu.CallsFragment;
import com.tabutech.eddysap.menu.CameraFragment;
import com.tabutech.eddysap.menu.ChatsFragment;
import com.tabutech.eddysap.menu.StatusFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int GAlLERY_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding  = DataBindingUtil.setContentView(this,R.layout.activity_main);



        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        View tab1 = LayoutInflater.from(this).inflate(R.layout.custom_camera_tab,null);
        try {
            binding.tabLayout.getTabAt(0).setCustomView(tab1);
        }catch (Exception e){e.printStackTrace();}

        binding.viewPager.setCurrentItem(1);
        setSupportActionBar(binding.toolbar);


        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFabIcon(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.search_menu: Toast.makeText(this, "The search icon pressed", Toast.LENGTH_SHORT).show();break;
            case R.id.action_more: Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();break;
            case R.id.action_newGroup: Toast.makeText(this, "The New Group icon pressed", Toast.LENGTH_SHORT).show();break;
            case R.id.action_newBroadcast: Toast.makeText(this, "The New Broadcast icon pressed", Toast.LENGTH_SHORT).show();break;
            case R.id.action_ed_web: Toast.makeText(this, "The EddySap web icon pressed", Toast.LENGTH_SHORT).show();break;
            case R.id.action_starred: Toast.makeText(this, "The Starred icon pressed", Toast.LENGTH_SHORT).show();break;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                Toast.makeText(this, "The Settings icon pressed", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpWithViewPager(ViewPager pager){

        MainActivity.SectionsPagerAdaptor adaptor = new SectionsPagerAdaptor(getSupportFragmentManager());

        adaptor.addFragment(new CameraFragment(),"");
        adaptor.addFragment(new ChatsFragment(),"Chats");
        adaptor.addFragment(new StatusFragment(),"Status");
        adaptor.addFragment(new CallsFragment(),"Calls");
        pager.setAdapter(adaptor);
    }

    public static class SectionsPagerAdaptor extends FragmentPagerAdapter {


        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String>   mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdaptor(@NonNull FragmentManager fragmentActivity) {
            super(fragmentActivity);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        public void addFragment(Fragment fragment,String title  ){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public CharSequence getPageTitle(int position){

            return mFragmentTitleList.get(position);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeFabIcon(final int index){

        binding.fabaction.hide();
        binding.editWords.setVisibility(View.GONE);

        new Handler().postDelayed(() ->{
            switch (index){
                case 0: binding.fabaction.hide();break;
                case 1:
                    binding.fabaction.show();
                    binding.fabaction.setImageDrawable(getDrawable(R.drawable.cart));break;
                case 2:
                    binding.fabaction.show();
                    binding.editWords.setVisibility(View.VISIBLE);
                    binding.fabaction.setImageDrawable(getDrawable(R.drawable.camera));break;
                case 3:
                    binding.fabaction.show();
                    binding.fabaction.setImageDrawable(getDrawable(R.drawable.call));break;
            }
        },400);

        performOnclick(index);
    }

    private void performOnclick(int index) {
        binding.fabaction.setOnClickListener(v -> {
            if (index == 1){
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
            }
            if (index == 2){
                checkCameraPermissions();
            }
            if (index == 3){
                Toast.makeText(MainActivity.this, "Call.", Toast.LENGTH_SHORT).show();
            }
        });


        binding.editWords.setOnClickListener(view ->
            Toast.makeText(MainActivity.this, "Words edited.", Toast.LENGTH_SHORT).show());

    }

    private void checkCameraPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},221);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},222);
        }else {
            openCamera();
        }
    }

    public static Uri image = null;
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFile = "IMG_"+timeStamp+".jpg";

        try {
            File file = File.createTempFile("IMG_"+timeStamp,".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            image = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider",file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,image);
            intent.putExtra("listPhotoName",imageFile);
            startActivityForResult(intent,400);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 & resultCode == RESULT_OK){
           // uploadToFireBase();
            if (image != null){
                startActivity(new Intent(MainActivity.this, AddStatusPicActivity.class)
                .putExtra("image",image));
            }
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return  typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }


}