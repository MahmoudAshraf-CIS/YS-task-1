package com.example.mannas.ytask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mannas.ytask.Content.Feed;

/**
 * Created by Mannas on 6/20/2017.
 */

public class WebPageActivity  extends AppCompatActivity {
    WebPageFragment webPageFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_page_activity);

        webPageFragment = new WebPageFragment();
        webPageFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.web_page_activity_frame,webPageFragment,WebPageFragment.class.getName()).commit();
    }
}