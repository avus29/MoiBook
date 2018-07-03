package com.example.user.moibook;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.params.HttpConnectionParams;
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
import java.util.ArrayList;
import java.util.List;

/**Helper method related to requesting and receiving book listing from Google Book API
 *
 */
public final class QueryUtils {
    public static final String LOG_TAG = Context.class.getName();

    private QueryUtils(){

    }

    /*return a list of {@Link Book} objects that has been created from parsing the given JSON response.
     */
    public static List<Book> extractBooksFromJson(String bookJSON){
        //Return early if JSON string is empty or null.
        if (TextUtils.isEmpty(bookJSON)){
            return null;
        }
        //create an empty arraylist that we can start adding books to.
        List<Book> books = new ArrayList<Book>();

        //Try to parse the JSON response string, if there is a problem with the way the JSON is parsed
        //a JSONException will be thrown, catch this exception, so the app doesn't crash and print the error
        //message on the log.
        try{
            //Create a JSONObject from the JSON response string.
            JSONObject baseJSON = new JSONObject(bookJSON);

            //Extract the JSONArray associated with the key "items"
            //Which represents a list of books
            JSONArray bookArray = baseJSON.getJSONArray("items");

            //for each book in the bookArray, create a Book object.
            for (int i =0; i<bookArray.length();i++){
                //Get a single book at position i within the list of books.
                JSONObject currentBook = bookArray.getJSONObject(i);

                //Extract the value for the key "selfLink"
                String moreInfo = currentBook.getString("selfLink");
                //For a given book, extract the JSONObject with the key "volumeInfo"
                //which represents the information about a book.
                JSONObject bookInfo =  currentBook.getJSONObject("volumeInfo");
                //Extract the value for the key "titie"
                String title = bookInfo.getString("title");

                //Extract the value for the key "authors"
                JSONArray authorArray = bookInfo.optJSONArray("authors");
                String[] authorList = null;
                if (authorArray!=null){
                     authorList= new String[authorArray.length()];
                    for (int j =0;j<authorArray.length();j++){
                        String currentAuthor = authorArray.getString(j);
                        authorList[j] = currentAuthor;
                    }
                }


                //Extract the  value for the key "publisher"
                String publisher = bookInfo.optString("publisher");
                //Extract the value for the key "description"
                String description = bookInfo.optString("description");
                //Extract the value for the key "pageCount"
                int pageCount = bookInfo.optInt("pageCount");
                //Extract the value for the key "categories"
                JSONArray categoryArray = bookInfo.optJSONArray("categories");
                String category = "N/A";
                if (categoryArray!=null){
                    String[] categoryList = new String[categoryArray.length()];
                    for (int j =0;j<categoryArray.length();j++){
                        String currentCategory = categoryArray.getString(j);
                        categoryList[j] = currentCategory;
                    }
                    category = simplyStringArray(categoryList);;
                }


                //Extract the value for the key "averageRating"
                double averageRating = bookInfo.optDouble("averageRating");
                //Extract the value for the key "ratingsCcount"
                int ratingsCount = bookInfo.optInt("ratingsCount");
                //Extract the value for the key "imageLinks"
                JSONObject imageLinks = bookInfo.optJSONObject("imageLinks");
                String imageUrl ="";
                if (imageLinks!=null){
                    imageUrl = imageLinks.optString("thumbnail");
                }
                //Extract the value for the key "language'
                String language = bookInfo.getString("language");
                //Extract the value for the key "industryIdentifiers"
//                JSONArray isbn = bookInfo.optJSONArray("industryIdentifiers");
//                JSONObject isbnObject10 = isbn.optJSONObject(0);
//                String isbn10 = "N/A";
//                isbn10 = isbnObject10.getString("identifier");;
                String isbn10 = "N/A";
//                JSONObject isbnObject13 = isbn.optJSONObject(1);

//                String isbn13 = isbnObject13.optString("identifier");
//                if (isbn13==null){
//                    isbn13 = "N/A";
//                }
                String isbn13 = "N/A";
                //Extract pdf and epub access info by getting the value of the key "accessInfo"
                JSONObject accessInfo =  currentBook.getJSONObject("accessInfo");
                JSONObject epubInfo = accessInfo.getJSONObject("epub");
                Boolean epub = epubInfo.getBoolean("isAvailable");
                JSONObject pdfInfo = accessInfo.getJSONObject("pdf");
                Boolean pdf = epubInfo.getBoolean("isAvailable");

                //create a new book from the parameters above.
                Book book = new Book (title,authorList,publisher,category,description,imageUrl,isbn10,
                        isbn13,language,moreInfo,pageCount,ratingsCount,averageRating,pdf,epub);
                books.add(book);
            }


        }catch (JSONException e){
            Log.e(LOG_TAG, "extractBooksFromJson: Ko le work! ", e);
        }




        return books;
    }

    /*Create new url Object from the string object*/
    private  static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL",e);
        }
        return url;
    }

    /*Make an HTTP request to the given URl and return a String as response*/
    private static String makeHttpRequest (URL url) throws IOException{
        String jsonResponse ="";
        //if URL is null, then return early.
        if (url ==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if the request was successful (responseCode==200)
            //then read the input stream and parse the response
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"problem retrieving the book JSON result", e);
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }

        return  jsonResponse;
    }

    /*convert InputStream to String which contains the whole JSON response from the server*/
    private static String readFromStream(InputStream inputStream)throws IOException{

        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static String simplyStringArray(String[] category) {
        String finalList = "";
        finalList += category[0];

        if (category.length > 1) {
            for (int i = 1; i < category.length; i++) {
                finalList += ", " + category[i];
            }
        }
        return finalList;
    }

    /*Query the book API and return a list of book Objects*/
    public static List<Book> fetchBookData(String requestUrl){
        //create URL object.
        URL url = createUrl(requestUrl);
        //perform HTTP request and get a JSON response.
        String jsonResponse =null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"problem making HTTP request.", e);
        }
        //Extract the relevant fields from the JSON response and create a list of Book objects
        List<Book>  books = extractBooksFromJson(jsonResponse);

        //return the list of Books
        return books;
    }
}
