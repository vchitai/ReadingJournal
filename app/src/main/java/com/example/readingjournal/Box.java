package com.example.readingjournal;

import java.io.Serializable;

public class Box implements Serializable {
    private long mBookId;
    private String mBookTitle;
    private String mBookAuthor;
    private String mBookDescription;
    private int mBookRating;
    private String mBookComments;
    private int mBookPosition;

    public Box(Book book, int bookPosition) {
        mBookId = book.getBookId();
        mBookTitle = book.getBookTitle();
        mBookAuthor = book.getBookAuthor();
        mBookDescription = book.getBookDescription();
        mBookRating = book.getBookRating();
        mBookComments = book.encodeComment();
        mBookPosition = bookPosition;
    }

    public Book unbox() {
        return new Book(mBookId, mBookTitle, mBookAuthor, mBookDescription, mBookComments, mBookRating);
    }

    public int getBookPosition() {
        return mBookPosition;
    }
}
