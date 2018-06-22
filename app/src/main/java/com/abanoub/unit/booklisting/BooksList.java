package com.abanoub.unit.booklisting;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

public class BooksList implements Parcelable {

    /**contain the image of the book */
    private Bitmap image;

    /**contain the title of the book */
    private String title;

    /**contain the author name of the book */
    private String author;

    /**contain little description of the book */
    private String description;

    /**contain the date of published date of the book */
    private String publishDate;

    /**contain the count of book pages */
    private int pageCount;

    /**contain the language of the book */
    private String bookLang;

    public BooksList(Bitmap image, String title, String author, String description, String publishDate, int pageCount, String bookLang){
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publishDate = publishDate;
        this.pageCount = pageCount;
        this.bookLang = bookLang;
    }


    /**@return the image of the book*/
    public Bitmap getImage(){
        return image;
    }

    /**@return the title of the book*/
    public String getTitle() {
        return title;
    }

    /**@return array of authors names of the book */
    public String getAuthor() {
        return author;
    }

    /**@return the description about what this book token about? */
    public String getDescription() {
        return description;
    }

    /**@return the published date for this book */
    public String getPublishDate() {
        return publishDate;
    }

    /**@return count of book pages */
    public int getPageCount() {
        return pageCount;
    }

    /**@return language of the book */
    public String getBookLang() {
        return bookLang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.author);
        parcel.writeString(this.description);
        parcel.writeString(this.publishDate);
        parcel.writeInt(this.pageCount);
        parcel.writeString(this.bookLang);
        parcel.writeTypedObject(this.image, Bitmap.PARCELABLE_WRITE_RETURN_VALUE);
    }
}
