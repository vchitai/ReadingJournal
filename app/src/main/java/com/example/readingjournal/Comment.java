package com.example.readingjournal;

public class Comment {
    final static private String mSeparatingChar = Character.toString((char) 27);
    private String mValue;
    private int mPage;
    private boolean mStatus;

    public Comment(String value, int page) {
        mValue = value;
        mPage = page;
        mStatus = true;
    }

    public String getValue() {
        return mValue;
    }

    public int getPage() {
        return mPage;
    }

    public boolean getStatus() {
        return mStatus;
    }

    public void turnOff() {
        mStatus = false;
    }

    public void turnOn() {
        mStatus = true;
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
