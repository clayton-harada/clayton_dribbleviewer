package com.clayton.dribble;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.agilie.dribbblesdk.domain.Shot;
import com.agilie.dribbblesdk.service.retrofit.DribbbleServiceGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;

/**
 * Created by clayton on 31/08/16.
 */
public class FeedListFragment extends Fragment  {
    protected final static Logger log = Logger.getLogger(FeedListFragment.class.getName());

    private static final String DRIBBBLE_CLIENT_ACCESS_TOKEN = "3f4b08a584a0b7cb770990ca5d83050a9761d48d0611dbfc4b944ecf1cbac7a2";

    protected GridView scrollGrid;

    protected List<Shot> list;
    protected FeedListAdapter adapter;
    protected int currpage = 1;
    protected static int PERPAGE = 20;
    protected boolean loading = false;

    public List<Shot> getList() {
        return list;
    }

    public FeedListAdapter getAdapter() {
        return adapter;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log.info("FeedList Create View");
        return inflater.inflate(R.layout.fragment_feedlist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log.info("FeedList Activity Created");

        currpage = 1;
        scrollGrid = (GridView) getView().findViewById(R.id.feedlistscrollgrid);

        initFeeds();

        scrollGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!loading && visibleItemCount + firstVisibleItem >= (totalItemCount - 2*(scrollGrid.getNumColumns()))) {
                    log.info("loadFeeds: " + (currpage+1));
                    loadFeeds(currpage+1);
                }
            }
        });
    }

    protected void initFeeds() {
        list = new LinkedList<Shot>();
        adapter = new FeedListAdapter(this.getActivity(), android.R.layout.simple_list_item_1, list);
        scrollGrid.setAdapter(adapter);
        loadFeeds(currpage, PERPAGE);

    }

    protected void loadFeeds(int page, int perpage) {
        if(!loading) {
            loading = true;
            currpage = page;
            Call<List<Shot>> shotsCall = DribbbleServiceGenerator
                    .getDribbbleShotService(DRIBBBLE_CLIENT_ACCESS_TOKEN)
                    .fetchShots(page, perpage);
            shotsCall.enqueue(new FeedLoaderCallback(this));
        }
    }

    protected void loadFeeds(int page) {
        loadFeeds(page, PERPAGE);
    }

    @Override
    public void onDestroyView() {
        log.info("FeedList Destroy View");
        super.onDestroyView();
    }
}
