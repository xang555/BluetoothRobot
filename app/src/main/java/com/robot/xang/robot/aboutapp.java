package com.robot.xang.robot;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class aboutapp extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.exittransition,R.anim.exittransition);
        setContentView(R.layout.activity_aboutapp);
    }//end oncreate



    @Override
    public void onBackPressed() {
         super.onBackPressed();
        overridePendingTransition(R.anim.entertransition,R.anim.entertransition);
    }//end onbackpressed

}//end class
