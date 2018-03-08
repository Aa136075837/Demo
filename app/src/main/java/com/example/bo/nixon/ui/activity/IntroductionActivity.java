package com.example.bo.nixon.ui.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseActivity;
import com.example.bo.nixon.manager.FontManager;

public class IntroductionActivity extends BaseActivity {

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_introduction);
        FontManager.changeFonts ((ViewGroup) findViewById (R.id.activity_introduction));
    }

}
