package com.tabutech.eddysap.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Activities.Contact.ContactActivity;
import com.tabutech.eddysap.View.Activities.Settings.SettingsActivity;
import com.tabutech.eddysap.databinding.ActivityMainBinding;
import com.tabutech.eddysap.menu.CallsFragment;
import com.tabutech.eddysap.menu.CameraFragment;
import com.tabutech.eddysap.menu.ChatsFragment;
import com.tabutech.eddysap.menu.StatusFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
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

        new Handler().postDelayed(() ->{
            switch (index){
                case 0: binding.fabaction.hide(); break;
                case 1:
                    binding.fabaction.show();
                    binding.fabaction.setImageDrawable(getDrawable(R.drawable.cart));
                    binding.fabaction.setOnClickListener(view ->
                            startActivity(new Intent(MainActivity.this, ContactActivity.class)));
                    break;
                case 2:
                    binding.fabaction.show();
                    binding.fabaction.setImageDrawable(getDrawable(R.drawable.camera));break;
                case 3:
                    binding.fabaction.show();
                    binding.fabaction.setImageDrawable(getDrawable(R.drawable.call));break;
            }
        },400);
    }

}