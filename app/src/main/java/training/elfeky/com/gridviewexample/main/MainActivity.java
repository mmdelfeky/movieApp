package training.elfeky.com.gridviewexample.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import training.elfeky.com.gridviewexample.R;
import training.elfeky.com.gridviewexample.fragments.MobileMainFragment;
import training.elfeky.com.gridviewexample.helpers.JsonUtils;


public class MainActivity extends android.support.v7.app.ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState)  ;
        setContentView(R.layout.activity_main);
    }

/*
    @Override
    public void onBackPressed(){

            if(MobileMainFragment.isInFavourite)
            {
              MobileMainFragment mainFragment = ((MobileMainFragment)getFragmentManager().findFragmentById(R.id.fragment2));
                if(mainFragment.isVisible()&&mainFragment!=null) {
                    mainFragment.update();
                   MobileMainFragment.isInFavourite = false;
                }
            }
            else {
                super.onBackPressed();
            }
        }*/


}
