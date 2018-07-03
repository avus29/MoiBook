package com.example.user.moibook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class BookAdapter extends ArrayAdapter<Book> {

    private URL imageUrl;

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context,0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Check if the existing view is being used, otherwise inflate view.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_entry, parent, false);
        }
        //get the {@link Book} item at the position in the list.
        final Book currentBook = (Book) getItem(position);

        //find corresponding view in the book_entry.xml layout and set values accordingly.
        TextView titleView = listItemView.findViewById(R.id.title_view);
        titleView.setText(currentBook.getmTitle());

        TextView authorView = listItemView.findViewById(R.id.author_name_view);
        String[]authorsList = currentBook.getmAuthor();
        String authors = "N/A";
        if (authorsList!=null){
            authors =simplyStringArray(authorsList);
        }
        authorView.setText(authors);

        TextView descriptionView = listItemView.findViewById(R.id.description_view);
        descriptionView.setText(currentBook.getmDescription());

        TextView publisherView = listItemView.findViewById(R.id.publisher_name_view);
        publisherView.setText(currentBook.getmPublisher());

        TextView pageCountView = listItemView.findViewById(R.id.page_count_view);
        pageCountView.setText(String.valueOf(currentBook.getmPageCount()));

        TextView ratingCountView = listItemView.findViewById(R.id.ratings_count_view);
        ratingCountView.setText(String.valueOf(currentBook.getmRatingCount()));

        RatingBar averageRatingView = listItemView.findViewById(R.id.ratings_view);
        averageRatingView.setRating(((float) currentBook.getmAverageRating()));

        TextView isbnView10 = listItemView.findViewById(R.id.isbn_10_view);
        isbnView10.setText(currentBook.getmIsbn10());

        TextView isbnView13 = listItemView.findViewById(R.id.isbn_13_view);
        isbnView13.setText(currentBook.getmIsbn13());

        TextView categoryView = listItemView.findViewById(R.id.category_view);
        categoryView.setText(currentBook.getmCategory());

        WebView imageview = listItemView.findViewById(R.id.web_view);
        String imageUrl = currentBook.getmImageUrl();
        imageview.loadUrl(imageUrl);


        TextView languageView = listItemView.findViewById(R.id.language_view);
        languageView.setText(currentBook.getmLanguage());

        TextView epubView = listItemView.findViewById(R.id.epub_availble_view);
        epubView.setText(parseBoolean(currentBook.ismEpub()));

        TextView pdfView = listItemView.findViewById(R.id.pdf_availble_view);
        pdfView.setText(parseBoolean(currentBook.ismPdf()));

        TextView moreInfo = listItemView.findViewById(R.id.info_view);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(currentBook.getmMoreInfo()));
                startActivity(getContext(),webIntent,null);
            }
        });


        return listItemView;
    }

    private Drawable LoadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "name");
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseBoolean(Boolean option) {
        if (option == true) {
            return "Yes";
        } else {
            return "No";
        }
    }

    private String simplyStringArray(String[] authors) {
        String finalList = "";
        finalList += authors[0];

        if (authors.length > 1) {
            for (int i = 1; i < authors.length; i++) {
                finalList += ", " + authors[i];
            }
        }
        return finalList;
    }

}
