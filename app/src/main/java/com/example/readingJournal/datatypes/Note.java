package com.example.readingJournal.datatypes;

public class Note {
    final static private String mSeparatingChar = Character.toString((char) 27);
    private String mContent;
    private int    mPage;

    public Note(String content, int page) {
        mContent = content;
        mPage = page;
    }

    public String getContent() {
        return mContent;
    }

    public int getPage() {
        return mPage;
    }

    public void edit(String content, int page) {
        mContent = content;
        mPage = page;
    }

    public String encode() {
        return mContent + mSeparatingChar + mPage + mSeparatingChar;
    }
}
