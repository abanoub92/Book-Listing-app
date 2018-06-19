package com.abanoub.unit.booklisting;

import android.graphics.Bitmap;

public class BooksList {

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


    public BooksList(Bitmap image, String title, String author, String description, String publishDate){
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publishDate = publishDate;
    }


    /**@return the image of the book*/
    public Bitmap getImage(){
        return image;
    }

    /**@return the title of the book*/
    public String getTitle() {
        return title;
    }

    /**@return the author name of the book */
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
}
