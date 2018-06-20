package com.abanoub.unit.booklisting;


import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.books_list);
        bookAdapter = new BookAdapter(this, new LinkedList<BooksList>());
        listView.setAdapter(bookAdapter);

        empty_text = findViewById(R.id.empty_text);
        listView.setEmptyView(empty_text);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        ImageButton search = findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAdapter.clear();

                search_text = findViewById(R.id.book_search);
                search_text.clearFocus();

                String query = search_text.getText().toString().replace(" ", "+");

                searchUrl = BOOKS_LIST_URL + query;

                new getAllData().execute(searchUrl);
                /*
                LoaderManager manager = getLoaderManager();
                manager.initLoader(1, null, MainActivity.this);
                */

                //progressBar.animate().getStartDelay();
                progressBar.setVisibility(View.VISIBLE);
                empty_text.setVisibility(View.GONE);
            }
        });

    }



    private class getAllData extends AsyncTask<String, Void, List<BooksList>>{

        @Override
        protected List<BooksList> doInBackground(String... urls) {
            if (urls[0] == null){
                return null;
            }

            List<BooksList> list = BookUtils.fetchDataFromInternet(urls[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<BooksList> booksLists) {
            bookAdapter.clear();

            progressBar.setVisibility(View.GONE);

            if (listView == null || bookAdapter.isEmpty()){
                empty_text.setVisibility(View.VISIBLE);
                empty_text.setText(R.string.no_books);
            }else {
                empty_text.setVisibility(View.GONE);
            }

            if (booksLists != null && !booksLists.isEmpty()){
                bookAdapter.addAll(booksLists);
            }
        }
    }
}
