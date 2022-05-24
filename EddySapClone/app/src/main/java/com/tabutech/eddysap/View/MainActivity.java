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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.auth.PhoneLoginActivity;
import com.tabutech.eddysap.View.auth.SetUserInfoActivity;
import com.tabutech.eddysap.databinding.ActivityMainBinding;
import com.tabutech.eddysap.menu.CallsFragment;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.search_menu){
            Toast.makeText(this, "The search icon pressed", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.menu_more){
            startActivity(new Intent(MainActivity.this, SetUserInfoActivity.class));
            Toast.makeText(this, "The menu more icon pressed", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpWithViewPager(ViewPager pager){

        MainActivity.SectionsPagerAdaptor adaptor = new SectionsPagerAdaptor(getSupportFragmentManager());

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

        binding.fabAction.hide();

        new Handler().postDelayed(() ->{
            switch (index){
                case 0: binding.fabAction.setImageDrawable(getDrawable(R.drawable.cart));break;
                case 1: binding.fabAction.setImageDrawable(getDrawable(R.drawable.camera));break;
                case 2: binding.fabAction.setImageDrawable(getDrawable(R.drawable.call));break;
            }
            binding.fabAction.show();
        },400);
    }
}