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


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rootView = convertView;
        if (rootView == null){
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        BooksList booksList = getItem(position);

        ImageView book_image = rootView.findViewById(R.id.book_poster);
        book_image.setImageResource(booksList.getImageResourceID());

        TextView book_title = rootView.findViewById(R.id.book_title);
        book_title.setText(booksList.getTitle());

        TextView book_author = rootView.findViewById(R.id.book_author);
        book_author.setText(booksList.getAuthor());

        TextView book_desc = rootView.findViewById(R.id.book_description);
        book_desc.setText(booksList.getDescription());

        TextView book_date = rootView.findViewById(R.id.book_date);
        book_date.setText(booksList.getPublishDate());

        return rootView;
    }
}