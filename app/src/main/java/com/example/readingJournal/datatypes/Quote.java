package com.example.readingJournal.datatypes;

public class Quote {
    final static private String mSeparatingChar = Character.toString((char) 27);
    private String mContent;
    private String mComment;
    private int    mPage;

    public Quote(String content, String comment, int page) {
        mContent = content;
        mComment = comment;
        mPage = page;
    }

    public String getContent() {
        return mContent;
    }

    public String getComment() {
        return mComment;
    }

    public int getPage() {
        return mPage;
    }

    public void edit(String content, String comment, int page) {
        mContent = content;
        mComment = comment;
        mPage = page;
    }

    public String encode() {
        return mContent + mSeparatingChar + mComment + mSeparatingChar + mPage + mSeparatingChar;
    }
}
