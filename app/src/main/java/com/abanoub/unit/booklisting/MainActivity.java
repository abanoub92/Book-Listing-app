package com.abanoub.unit.booklisting;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** global variable store msg in case list is empty */
    private TextView empty_text;

    /** global variables for xml Views */
    private ListView listView;
    private EditText search_text;
    private ProgressBar progressBar;

    /** global variable for custom ArrayAdapter for listView */
    private BookAdapter bookAdapter;

    /** global two String variables the first one for data URL and second one for the query */
    private static final String BOOKS_LIST_URL = "https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";
    private String searchUrl;

    /** global final variable (constant) for logs it refer to the errors for this class */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** global variable for check if there's an internet */
    private boolean isConnect;

    List<BooksList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /** service of connection of internet in the device which run the app */
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        /** check if connectivity manager doesn't return an null */
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        /** if networkInfo != null do ..., otherwise do ... */
        isConnect = networkInfo != null && networkInfo.isConnectedOrConnecting();


        /** connect global listView variable with xml ListView */
        listView = findViewById(R.id.books_list);

        /** create an object from custom ArrayAdapter */
        list = new ArrayList<>();
        bookAdapter = new BookAdapter(this, list);

        /** add the bookAdapter to ListView */
        listView.setAdapter(bookAdapter);

        /** connect textView variable with xml TextView and add it to ListView
         * in case of List has no data (empty)*/
        empty_text = findViewById(R.id.empty_text);
        listView.setEmptyView(empty_text);

        /** display when the user going to search
         *  and gone if he didn't */
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        /** button responsible of search about what user need */
        ImageButton search = findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnect) {
                    /** cleaning the adapter before make any search */
                    bookAdapter.clear();

                    /** connect to editText xml view which well take from it
                     *  the key word to make search*/
                    search_text = findViewById(R.id.book_search);

                    /** get key word and if there's an any spaces ( ) replace it with plus sign (+) */
                    String query = search_text.getText().toString().replace(" ", "+");

                    /** concat the search url with key word search in one variable */
                    searchUrl = BOOKS_LIST_URL + query;

                    /** connect to data server to fetch the data */
                    new getAllData().execute(searchUrl);

                    /** while fetching the data, progressBar well shown until the fetching is done */
                    progressBar.setVisibility(View.VISIBLE);

                    /** hide while making a process for data */
                    empty_text.setVisibility(View.GONE);

                    /** confirm that the connection is working */
                    isConnect = true;
                }else {

                    /** if the connection is not working by any means
                     *  displays an msg to user to looking for the reason*/
                    empty_text.setText(R.string.no_internet);
                    empty_text.setVisibility(View.VISIBLE);
                    isConnect = false;
                }
            }
        });

    }


    /**
     * responsible of making the connection and fetching the data
     * in separate thread (background thread)
     */
    private class getAllData extends AsyncTask<String, Void, List<BooksList>>{

        @Override
        protected List<BooksList> doInBackground(String... urls) {
            if (urls[0] == null){
                return null;
            }

            /** fetching the data from the server and store it in a list */
            List<BooksList> list = BookUtils.fetchDataFromInternet(urls[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<BooksList> booksLists) {
            bookAdapter.clear();

            /** progress hide when fetching data is over */
            progressBar.setVisibility(View.GONE);

            /** check if the listView or the custom adapter is null
              * displays a msg to attention the user */
            if (listView == null || bookAdapter.isEmpty()){
                empty_text.setVisibility(View.VISIBLE);
                empty_text.setText(R.string.no_books);
            }else {
                /** hide the msg if listView or adater in not null*/
                empty_text.setVisibility(View.GONE);
            }

            if (booksLists != null && !booksLists.isEmpty()){
                /** add the list of data to adapter */
                bookAdapter.addAll(booksLists);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (list != null){
            outState.putParcelableArrayList("list", (ArrayList<BooksList>) list);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList("list");
            bookAdapter.clear();
            bookAdapter.addAll(list);
            listView.setAdapter(bookAdapter);
        }
    }
}
