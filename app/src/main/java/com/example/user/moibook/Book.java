package com.example.user.moibook;

public class Book {
    //Private variables.
    private String mTitle;//Title of book
    private String[] mAuthor;//An array of author(s)
    private String mPublisher; //Name of publisher
    private String mDescription; //A short description of the book.
    private int mPageCount; //Number of pages in book.
    private int mRatingCount; //Number of individual who have rated the book
    private double mAverageRating; //Average rating of the book
    private String mIsbn10; //ISBN_10 number
    private String mIsbn13; //ISBN_13 number
    private String mCategory; //Category of book
    private String mImageUrl; //Link to download image
    private boolean mPdf; //Pdf available?
    private boolean mEpub; //Epub available?
    private String mMoreInfo;// Link for more information.
    private String mLanguage; //Language of publication.


    //Constructor
    public Book(String title, String[] author,String publisher,String category, String description,
                String imageUrl, String isbn10, String isbn13, String language, String moreInfo,
                int pageCount,int ratingCount, double averageRating, boolean pdf, boolean epub){

        mTitle = title;
        mAuthor = author;
        mPublisher = publisher;
        mDescription = description;
        mPageCount = pageCount;
        mRatingCount = ratingCount;
        mAverageRating = averageRating;
        mIsbn10 = isbn10;
        mIsbn13 = isbn13;
        mCategory = category;
        mImageUrl =imageUrl;
        mPdf = pdf;
        mEpub = epub;
        mMoreInfo = moreInfo;
        mLanguage = language;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String[] getmAuthor() {
        return mAuthor;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public String getmDescription() {
        return mDescription;
    }

    public int getmPageCount() {
        return mPageCount;
    }

    public int getmRatingCount() {
        return mRatingCount;
    }

    public double getmAverageRating() {
        return mAverageRating;
    }

    public String getmIsbn10() {
        return mIsbn10;
    }

    public String getmIsbn13() {
        return mIsbn13;
    }

    public String getmCategory() {
        return mCategory;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public boolean ismPdf() {
        return mPdf;
    }

    public boolean ismEpub() {
        return mEpub;
    }

    public String getmMoreInfo() {
        return mMoreInfo;
    }

    public String getmLanguage() {
        return mLanguage;
    }
}
