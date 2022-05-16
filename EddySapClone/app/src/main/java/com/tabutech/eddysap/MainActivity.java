package com.tabutech.eddysap;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.tabutech.eddysap.databinding.ActivityMainBinding;
import com.tabutech.eddysap.menu.CallsFragment;
import com.tabutech.eddysap.menu.ChatsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding  = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private void setUpWithViewPager(ViewPager pager){

        MainActivity.SectionsPagerAdaptor adaptor = new SectionsPagerAdaptor(getSupportFragmentManager());

        adaptor.addFragment(new ChatsFragment(),"Chats");
        adaptor.addFragment(new ChatsFragment(),"Status");
        adaptor.addFragment(new ChatsFragment(),"Calls");
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


}