package com.clayton.dribble;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.logging.Logger;

public class MainActivity extends Activity {
    protected final static Logger log = Logger.getLogger(MainActivity.class.getName());

    protected static FeedListFragment feedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log.info("MainActivity onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if(savedInstanceState==null) {
            feedlist = new FeedListFragment();
            this.getFragmentManager().beginTransaction().add(R.id.mainactivityview, feedlist).commit();
        }
        else this.getFragmentManager().beginTransaction().replace(R.id.mainactivityview, feedlist).commit();
    }
}
