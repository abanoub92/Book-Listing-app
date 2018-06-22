package com.abanoub.unit.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends ArrayAdapter<BooksList> {


    public BookAdapter(@NonNull Context context, @NonNull List<BooksList> objects) {
        super(context, R.layout.book_item, objects);
    }


    /** preparing custom layout as row in listView */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /** connecting the custom adapter with custom list item */
        View rootView = convertView;
        if (rootView == null){
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        /** get all items of an array (one by one) by using booksList model class */
        BooksList booksList = getItem(position);

        /** get connection with xml imageView
          * and setting the book poster to it */
        ImageView book_image = rootView.findViewById(R.id.book_poster);
        book_image.setImageBitmap(booksList.getImage());

        /** get connection with xml TextView
         * and setting the title of the book */
        TextView book_title = rootView.findViewById(R.id.book_title);
        book_title.setText(booksList.getTitle());

        /** get connection with xml TextView
         * and setting the author/s of the book */
        TextView book_author = rootView.findViewById(R.id.book_author);
        book_author.setText(booksList.getAuthor());

        /** get connection with xml TextView
         * and setting the description of the book */
        TextView book_desc = rootView.findViewById(R.id.book_description);
        book_desc.setText(booksList.getDescription());

        /** get connection with xml TextView
         * and setting the Published date of the book */
        TextView book_date = rootView.findViewById(R.id.book_date);
        book_date.setText(booksList.getPublishDate());

        /** get connection with xml TextView
         * and setting the count of the book pages */
        TextView page_count = rootView.findViewById(R.id.book_page_count);
        page_count.setText(String.valueOf(booksList.getPageCount() + "p"));

        /** get connection with xml TextView
         * and setting the language which wrote the book */
        TextView book_language = rootView.findViewById(R.id.book_language);
        book_language.setText(booksList.getBookLang());

        /** return custom adapter for book_item */
        return rootView;
    }
}
