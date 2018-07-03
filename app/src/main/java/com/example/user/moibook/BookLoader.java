package com.example.user.moibook;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private  String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        //Early return if no url was passed.
        if (mUrl == null){
            return null;
        }
        return QueryUtils.fetchBookData(mUrl);
    }
}
