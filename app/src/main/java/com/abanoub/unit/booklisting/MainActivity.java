package com.abanoub.unit.booklisting;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BooksList>> {

    private ProgressBar progressBar;

    private BookAdapter bookAdapter;

    private static final String BOOKS_LIST_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.books_list);

        bookAdapter = new BookAdapter(this, new LinkedList<BooksList>());

        listView.setAdapter(bookAdapter);

        LoaderManager manager = getLoaderManager();
        manager.initLoader(1, null, this);
    }


    @Override
    public Loader<List<BooksList>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, BOOKS_LIST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<BooksList>> loader, List<BooksList> booksLists) {
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        bookAdapter.clear();

        if (booksLists != null && !booksLists.isEmpty()){
            bookAdapter.addAll(booksLists);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BooksList>> loader) {
        bookAdapter.clear();
    }
}
