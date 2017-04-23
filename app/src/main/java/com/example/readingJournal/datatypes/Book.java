package com.example.readingJournal.datatypes;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Book {
    final static private String mSeparatingChar = Character.toString((char) 27);
    final static private Note   M_DEFAULT_NOTE  = new Note("No note available", -1);
    private BookBasicInfo    mBookBasicInfo;
    private ArrayList<Note>  mBookNotes;
    private HashSet<Integer> mBookGenres;
    private ArrayList<Quote> mBookQuotes;

    //init
    public Book(BookBasicInfo bookBasicInfo, String bookGenresEndcoded, String bookNotesEncoded, String bookQuotesEncoded) {
        mBookBasicInfo = bookBasicInfo;
        decodeGenre(bookGenresEndcoded);
        decodeNote(bookNotesEncoded);
        decodeQuote(bookQuotesEncoded);
    }

    public void editBook(BookBasicInfo bookBasicInfo, String genres) {
        mBookBasicInfo = bookBasicInfo;
        decodeGenre(genres);
    }

    //getInfo
    public BookBasicInfo getBookBasicInfo() {
        return mBookBasicInfo;
    }

    public Set<Integer> getBookGenres() {
        return mBookGenres;
    }

    public ArrayList<Quote> getBookQuotes() {
        return mBookQuotes;
    }

    public ArrayList<Note> getBookNotes() {
        return mBookNotes;
    }

    public Note getLatestNote() {
        if (mBookNotes.isEmpty()) {
            return M_DEFAULT_NOTE;
        } else {
            return mBookNotes.get(mBookNotes.size() - 1);
        }
    }

    public int getLatestNoteID() {
        return mBookNotes.size() - 1;
    }

    //Note handle functions
    public String encodeNote() {
        String res = "";
        for (int i = 0; i < mBookNotes.size(); i++) {
            res += mBookNotes.get(i).encode();
        }

        return res;
    }

    private void decodeNote(String notesEncoded) {
        if (mBookNotes == null)
            mBookNotes = new ArrayList<>();
        else
            mBookNotes.clear();

        if (!TextUtils.isEmpty(notesEncoded)) {
            String notes[] = notesEncoded.split(mSeparatingChar);
            for (int i = 0; i < notes.length; i++) {
                if (i % 2 == 0) {
                    mBookNotes.add(new Note(notes[i], Integer.parseInt(notes[i + 1])));
                }
            }
        }
    }

    public void addNote(String content, int page) {
        mBookNotes.add(new Note(content, page));
    }

    public void deleteNote(int order) {
        mBookNotes.remove(order);
    }

    public void editNote(int order, String newContent, int newPage) {
        mBookNotes.get(order).edit(newContent, newPage);
    }

    //Quote handle functions
    public String encodeQuote() {
        String res = "";
        for (int i = 0; i < mBookQuotes.size(); i++) {
            res += mBookQuotes.get(i).encode();
        }

        return res;
    }

    private void decodeQuote(String quotesEncoded) {
        if (mBookQuotes == null)
            mBookQuotes = new ArrayList<>();
        else
            mBookQuotes.clear();

        if (!TextUtils.isEmpty(quotesEncoded)) {
            String quotes[] = quotesEncoded.split(mSeparatingChar);
            for (int i = 0; i < quotes.length; i++) {
                if (i % 3 == 0) {
                    mBookQuotes.add(new Quote(quotes[i], quotes[i + 1], Integer.parseInt(quotes[i + 2])));
                }
            }
        }
    }

    public void addQuote(String content, String comment, int page) {
        mBookQuotes.add(new Quote(content, comment, page));
    }

    public void deleteQuote(int order) {
        mBookQuotes.remove(order);
    }

    public void editQuote(int order, String newContent, String newNote, int newPage) {
        mBookQuotes.get(order).edit(newContent, newNote, newPage);
    }

    //Genre handle functions
    public String encodeGenre() {
        String res = "";
        for (Integer i : mBookGenres) {
            res += String.valueOf(i) + mSeparatingChar;
        }

        return res;
    }

    private void decodeGenre(String genresEncoded) {
        if (mBookGenres == null)
            mBookGenres = new HashSet<>();
        else
            mBookGenres.clear();

        if (!TextUtils.isEmpty(genresEncoded)) {
            String genres[] = genresEncoded.split(mSeparatingChar);
            for (int i = 0; i < genres.length; i++) {
                mBookGenres.add(Integer.valueOf(genres[i]));
            }
        }
    }

    public void addGenre(int genre) {
        mBookGenres.add(genre);
    }

    public void deleteGenre(int genre) {
        mBookGenres.remove(genre);
    }

    public void editGenre(int genre, int newGenre) {
        mBookGenres.remove(genre);
        mBookGenres.add(newGenre);
    }
}
