package com.abanoub.unit.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class BookUtils {

    /** */
    private static final String LOG_TAG = BooksList.class.getSimpleName();


    /**
     * Method to create url to fetching the data of books
     * @param requestUrl string to convert it to an url
     * @return url to use it in connection
     */
    private static URL createUrl(String requestUrl){
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating an url", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromInputStream(InputStream inputStream){
        StringBuilder builder = new StringBuilder();
            try {
                if (inputStream != null) {
                    InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String line = bufferedReader.readLine();
                    while (line != null){
                        builder.append(line);
                        line = bufferedReader.readLine();
                    }
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while reading from inputStream", e);
            }
            return builder.toString();
    }

    /**
     * Make an HTTP request to the given URL.
     * @param url well fetch from it all the data
     * @return a String as the response
     * @throws IOException in case a null input when getting the data from stream
     */
    private static String makeHttpConnection(URL url) throws IOException {
        String bookJSON = "";

        if (url == null){
            return bookJSON;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream stream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200){
                stream = httpURLConnection.getInputStream();
                bookJSON = readFromInputStream(stream);
            }else {
                Log.e(LOG_TAG, "Error response Code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while creating Http connection", e);
        }finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }

            if (stream != null){
                stream.close();
            }
        }
        return bookJSON;
    }

    /**
     * @return a list of {@link BooksList} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<BooksList> extractDataFromJSON(String bookJSON) throws IOException {
        if (TextUtils.isEmpty(bookJSON)){
            return null;
        }

        List<BooksList> listOfBooks = new LinkedList<>();

        try {
            JSONObject root = new JSONObject(bookJSON);
            JSONArray items = root.getJSONArray("items");

            for (int i = 0; i < items.length(); i++){
                JSONObject objectIndex = items.getJSONObject(i);
                JSONObject volumeInfo = objectIndex.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");

                JSONArray authors = volumeInfo.getJSONArray("authors");
                String author = authors.getString(0);

                String desc = volumeInfo.getString("description");
                String date = volumeInfo.getString("publishedDate");

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String imageUrl = imageLinks.getString("smallThumbnail");
                Bitmap image = getBitmapFromUrl(imageUrl);
                Bitmap resizedImage = getResizedBitmap(image, 160, 80);

                BooksList booksList = new BooksList(resizedImage, title, author, desc, date);

                listOfBooks.add(booksList);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error while extracting the Json", e);
        }

        return listOfBooks;
    }

    /**
     * get the image of book from internet
     * @param imgUrl to get a string url
     * @return bitmap image to use in display as poster
     * @throws IOException in case a null input in getting the image from stream
     */
    private static Bitmap getBitmapFromUrl(String imgUrl) throws IOException {
        URL url = createUrl(imgUrl);

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error in image response Code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while fetch the image", e);
        }finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return bitmap;
    }

    /**
     * Query the Books dataset
     * @return (List<BooksList>) object to represent a single book.
     */
    public static List<BooksList> fetchDataFromInternet(String booksUrl){
        URL url = createUrl(booksUrl);

        String bookData;
        List<BooksList> list = null;

        try {
            bookData = makeHttpConnection(url);
            list = extractDataFromJSON(bookData);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in input stream in use it methods", e);
        }

        return list;
    }


    private static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
