package com.example.readingJournal.datatypes;

public class BookBasicInfo {
    private long    mBookId;
    private String  mBookTitle;
    private String  mBookAuthor;
    private String  mBookDescription;
    private int     mBookImageId;
    private float   mBookRating;
    private boolean mBookStatus;

    //init
    public BookBasicInfo(long bookId, String bookTitle, String bookAuthor, String bookDesc, int bookImageId, float bookRating, boolean bookStatus) {
        mBookId = bookId;
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookDescription = bookDesc;
        mBookImageId = bookImageId;
        mBookRating = bookRating;
        mBookStatus = bookStatus;
    }

    //getInfo
    public long getBookId() {
        return mBookId;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }

    public String getBookDescription() {
        return mBookDescription;
    }

    public boolean getBookStatus() {
        return mBookStatus;
    }

    public float getBookRating() {
        return mBookRating;
    }

    public int getBookImageId() {
        return mBookImageId;
    }
}
