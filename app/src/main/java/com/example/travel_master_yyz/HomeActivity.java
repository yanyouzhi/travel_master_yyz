package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.travel_master_yyz.page_fragment.HomePageViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 设置适配器
        HomePageViewPagerAdapter adapter = new HomePageViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false); // 禁止手势滑动

        // ViewPager 页面滑动时，更新 BottomNavigation 选中状态
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });


        // 底部导航栏点击时，切换 ViewPager 页面
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0);
            } else if (itemId == R.id.nav_spots) {
                viewPager.setCurrentItem(1);
            } else if (itemId == R.id.nav_community) {
                viewPager.setCurrentItem(2);
            } else if (itemId == R.id.nav_mine) {
                viewPager.setCurrentItem(3);
            }
            return true;
        });

        // 监听 ViewPager2 滑动事件
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }
}