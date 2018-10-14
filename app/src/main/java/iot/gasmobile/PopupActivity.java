package iot.gasmobile;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

/**
 * Created by 황도현 on 2018-05-05.
 */

public class PopupActivity extends FragmentActivity {
    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity_main);

        vp = (ViewPager)findViewById(R.id.vp);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }
        public  android.support.v4.app.Fragment getItem(int position){
            switch (position){
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                default:
                    return null;
            }
        }
        public int getCount(){
            return 3;
        }
    }

    public void close(View view){
        finish();
    }
}
