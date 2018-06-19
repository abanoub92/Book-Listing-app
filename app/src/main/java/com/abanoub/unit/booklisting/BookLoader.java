package com.abanoub.unit.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<BooksList>> {

    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<BooksList> loadInBackground() {
        if (url == null){
            return null;
        }

        List<BooksList> list = BookUtils.fetchDataFromInternet(url);
        return list;
    }

    @Override
    protected void onStartLoading() {
        onForceLoad();
    }
}
