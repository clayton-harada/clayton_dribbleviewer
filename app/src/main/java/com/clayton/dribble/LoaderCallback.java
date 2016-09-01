package com.clayton.dribble;

import android.widget.GridView;

import java.util.logging.Logger;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by clayton on 31/08/16.
 */
public abstract class LoaderCallback<T> implements Callback<T> {
    protected final static Logger log = Logger.getLogger(LoaderCallback.class.getName());

    public LoaderCallback() {
    }

    @Override
    public void onResponse(Response<T> response) {
        if(response.body()!=null) consumeResponseBody(response.body());
    }

    @Override
    public void onFailure(Throwable t) {

    }

    abstract void consumeResponseBody(T t);
}
