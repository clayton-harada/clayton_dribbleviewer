package com.clayton.dribble;

import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextClock;
import android.widget.TextView;

import com.agilie.dribbblesdk.domain.Shot;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by clayton on 31/08/16.
 */
public class FeedLoaderCallback extends LoaderCallback<List<Shot>> {
    protected final static Logger log = Logger.getLogger(FeedLoaderCallback.class.getName());

    protected FeedListFragment feedListFragment;

    public FeedLoaderCallback(FeedListFragment feedListFragment) {
        super();
        this.feedListFragment = feedListFragment;
    }

    @Override
    void consumeResponseBody(List<Shot> shots) {
        for (Shot shot : shots) {
            log.info(shot.getUser().getUserName() + " " + shot.getTitle());
            feedListFragment.getList().add(shot);
        }
        feedListFragment.getAdapter().notifyDataSetChanged();
        feedListFragment.setLoading(false);
    }
}
