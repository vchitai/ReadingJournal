package com.example.readingjournal;

import android.text.TextUtils;

import java.util.ArrayList;

public class Book {
    final static private String mSeparatingChar = Character.toString((char) 27);
    final static private Comment mDefaultComment = new Comment("No comment", -1);
    private long mBookId;
    private String mBookTitle;
    private String mBookAuthor;
    private String mBookDescription;
    private int mBookRating;
    private ArrayList<Comment> mBookComments;

    public Book(long bookId, String bookTitle, String bookAuthor, String bookDesc, String bookCommentsEncoded, int bookRating) {
        mBookId = bookId;
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookDescription = bookDesc;
        mBookRating = bookRating;
        decodeComment(bookCommentsEncoded);
    }

    public String encodeComment() {
        String res = new String();
        for (int i = 0; i < mBookComments.size(); i++) {
            res += mBookComments.get(i).encode();
        }
        return res;
    }

    private void decodeComment(String commentsEncoded) {
        mBookComments = new ArrayList<Comment>();
        if (!TextUtils.isEmpty(commentsEncoded)) {
            String comments[] = commentsEncoded.split(mSeparatingChar);
            for (int i = 0; i < comments.length; i++) {
                if (i % 2 == 0) {
                    mBookComments.add(new Comment(comments[i], Integer.parseInt(comments[i + 1])));
                }
            }
        }
    }

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

    public int getBookRating() {
        return mBookRating;
    }

    public ArrayList<Comment> getBookComments() {
        return mBookComments;
    }

    public void deleteComment(int order) {
        mBookComments.remove(order);
    }

    public void editComment(int order, String newValue, int newPage) {
        mBookComments.get(order).edit(newValue, newPage);
    }

    public void editBook(String bookTitle, String bookAuthor, String bookDesc, int bookRating) {
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookDescription = bookDesc;
        mBookRating = bookRating;
    }

    public void addComment(String comment, int page) {
        mBookComments.add(new Comment(comment, page));
    }

    public Comment getLastestComment() {
        if (mBookComments.size() == 0) {
            return mDefaultComment;
        } else {
            return mBookComments.get(mBookComments.size() - 1);
        }
    }

    public int getLastestCommentID() {
        return mBookComments.size() - 1;
    }
}
