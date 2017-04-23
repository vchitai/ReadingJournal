package com.example.readingJournal.datatypes;

import java.io.Serializable;

public class Box implements Serializable {
    private long    mBookId;
    private String  mBookTitle;
    private String  mBookAuthor;
    private String  mBookDescription;
    private float   mBookRating;
    private boolean mBookStatus;
    private int     mBookImageId;
    private String  mBookNotes;
    private String  mBookQuotes;
    private String  mBookGenres;
    private int     mBookPosition;

    public Box(Book book, int bookPosition) {
        BookBasicInfo bookBasicInfo = book.getBookBasicInfo();
        mBookId = bookBasicInfo.getBookId();
        mBookTitle = bookBasicInfo.getBookTitle();
        mBookAuthor = bookBasicInfo.getBookAuthor();
        mBookDescription = bookBasicInfo.getBookDescription();
        mBookRating = bookBasicInfo.getBookRating();
        mBookStatus = bookBasicInfo.getBookStatus();
        mBookImageId = bookBasicInfo.getBookImageId();
        mBookNotes = book.encodeNote();
        mBookGenres = book.encodeGenre();
        mBookQuotes = book.encodeQuote();
        mBookPosition = bookPosition;
    }

    public Book unBox() {
        BookBasicInfo bookBasicInfo = new BookBasicInfo(mBookId, mBookTitle, mBookAuthor, mBookDescription, mBookImageId, mBookRating, mBookStatus);

        return new Book(bookBasicInfo, mBookGenres, mBookNotes, mBookQuotes);
    }

    public int getBookPosition() {
        return mBookPosition;
    }
}
