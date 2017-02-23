package com.example.readingjournal;

public class Comment {
    final static private String mSeparatingChar = Character.toString((char) 27);
    private String mValue;
    private int mPage;

    public Comment(String value, int page) {
        mValue = value;
        mPage = page;
    }

    public String getDetail() {
        return mValue;
    }

    public int getPage() {
        return mPage;
    }

    public void edit(String value, int page) {
        mValue = value;
        mPage = page;
    }

    public String encode() {
        String res = mValue + mSeparatingChar + mPage + mSeparatingChar;
        return res;
    }
}
