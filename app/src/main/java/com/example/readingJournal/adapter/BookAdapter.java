package com.example.readingJournal.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readingJournal.R;
import com.example.readingJournal.ShowBookActivity;
import com.example.readingJournal.data.BookContract;
import com.example.readingJournal.datatypes.Book;
import com.example.readingJournal.datatypes.BookBasicInfo;
import com.example.readingJournal.datatypes.Box;
import com.example.readingJournal.datatypes.Note;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private static final String LOG_TAG = BookAdapter.class.getSimpleName();
    private ArrayList<Book> mBookList;
    private Context         mContext;
    private String[]        mGenreList;

    public BookAdapter(Context context) {
        mContext = context;
        mGenreList = context.getResources().getStringArray(R.array.genres);
        loadDatabase();
    }

    private void loadDatabase() {
        if (mBookList == null) {
            mBookList = new ArrayList<>();
        }

        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_BOOK_TITLE,
                BookContract.BookEntry.COLUMN_BOOK_AUTHOR,
                BookContract.BookEntry.COLUMN_BOOK_DESCRIPTION,
                BookContract.BookEntry.COLUMN_BOOK_GENRES,
                BookContract.BookEntry.COLUMN_BOOK_IMAGE_ID,
                BookContract.BookEntry.COLUMN_BOOK_NOTES,
                BookContract.BookEntry.COLUMN_BOOK_QUOTES,
                BookContract.BookEntry.COLUMN_BOOK_STATUS,
                BookContract.BookEntry.COLUMN_BOOK_RATING
        };

        Cursor cursor = mContext.getContentResolver().query(BookContract.BookEntry.CONTENT_URI, projection, null, null, null);
        try {
            int idColumnIndex          = cursor.getColumnIndex(BookContract.BookEntry._ID);
            int titleColumnIndex       = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE);
            int authorColumnIndex      = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_AUTHOR);
            int descriptionColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_DESCRIPTION);
            int genresColumnIndex      = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_GENRES);
            int imageIdColumnIndex     = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_IMAGE_ID);
            int notesColumnIndex       = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NOTES);
            int quotesColumnIndex      = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUOTES);
            int statusColumnIndex      = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_STATUS);
            int ratingColumnIndex      = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_RATING);

            while (cursor.moveToNext()) {
                int     currentID          = cursor.getInt(idColumnIndex);
                String  currentTitle       = cursor.getString(titleColumnIndex);
                String  currentAuthor      = cursor.getString(authorColumnIndex);
                String  currentDescription = cursor.getString(descriptionColumnIndex);
                String  currentGenres      = cursor.getString(genresColumnIndex);
                int     currentImageId     = cursor.getInt(imageIdColumnIndex);
                String  currentNotes       = cursor.getString(notesColumnIndex);
                String  currentQuotes      = cursor.getString(quotesColumnIndex);
                int     currentStatus      = cursor.getInt(statusColumnIndex);
                boolean currentStatusB;
                currentStatusB = currentStatus != 0;
                float currentRating = cursor.getInt(ratingColumnIndex);

                BookBasicInfo bookBasicInfo = new BookBasicInfo(currentID, currentTitle, currentAuthor, currentDescription, currentImageId, currentRating, currentStatusB);
                mBookList.add(new Book(bookBasicInfo, currentGenres, currentNotes, currentQuotes));
            }
        } finally {
            cursor.close();
        }
    }

    public void removeBook(int order) {
        mBookList.remove(order);
    }

    public void saveBook(int currentId, Uri currentUri, BookBasicInfo bookBasicInfo, String genres) {
        if (bookBasicInfo.getBookTitle() != null) {
            ContentValues values = new ContentValues();
            values.put(BookContract.BookEntry.COLUMN_BOOK_TITLE, bookBasicInfo.getBookTitle());
            values.put(BookContract.BookEntry.COLUMN_BOOK_AUTHOR, bookBasicInfo.getBookAuthor());
            values.put(BookContract.BookEntry.COLUMN_BOOK_DESCRIPTION, bookBasicInfo.getBookDescription());
            values.put(BookContract.BookEntry.COLUMN_BOOK_STATUS, bookBasicInfo.getBookStatus());
            values.put(BookContract.BookEntry.COLUMN_BOOK_RATING, bookBasicInfo.getBookRating());
            values.put(BookContract.BookEntry.COLUMN_BOOK_IMAGE_ID, bookBasicInfo.getBookImageId());
            values.put(BookContract.BookEntry.COLUMN_BOOK_GENRES, genres);

            if (currentUri == null) {
                Uri newUri = mContext.getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(mContext, "Error with saving book", Toast.LENGTH_SHORT).show();
                } else {
                    mBookList.add(new Book(bookBasicInfo, genres, "", ""));
                    Toast.makeText(mContext, "Book saved", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            } else {
                int rowAffected = mContext.getContentResolver().update(currentUri, values, null, null);

                if (rowAffected == 0) {
                    Toast.makeText(mContext, "Error with saving book", Toast.LENGTH_SHORT).show();
                } else {
                    mBookList.get(currentId).editBook(bookBasicInfo, genres);
                    Toast.makeText(mContext, "Book saved", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        }
    }

    public void addNote(int currentId, Uri currentUri, String content, int page) {
        Book          currentBook = mBookList.get(currentId);
        ContentValues value       = new ContentValues();
        value.put(BookContract.BookEntry.COLUMN_BOOK_NOTES, currentBook.encodeNote());
        int rowAffected = mContext.getContentResolver().update(currentUri, value, null, null);

        if (rowAffected == 0) {
            Toast.makeText(mContext, "Error with saving note", Toast.LENGTH_SHORT).show();
        } else {
            mBookList.get(currentId).addNote(content, page);
            Toast.makeText(mContext, "Note saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void editNote(int currentId, Uri currentUri, int noteID, String newContent, int newPage) {
        Book currentBook = mBookList.get(currentId);
        mBookList.get(currentId).editNote(noteID, newContent, newPage);
        ContentValues value = new ContentValues();
        value.put(BookContract.BookEntry.COLUMN_BOOK_NOTES, currentBook.encodeNote());
        int rowAffected = mContext.getContentResolver().update(currentUri, value, null, null);

        if (rowAffected == 0) {
            Toast.makeText(mContext, "Error with saving note", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Note saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteNote(int currentId, Uri currentUri, int noteID) {
        Book currentBook = mBookList.get(currentId);
        mBookList.get(currentId).deleteNote(noteID);
        ContentValues value = new ContentValues();
        value.put(BookContract.BookEntry.COLUMN_BOOK_NOTES, currentBook.encodeNote());
        int rowAffected = mContext.getContentResolver().update(currentUri, value, null, null);

        if (rowAffected == 0) {
            Toast.makeText(mContext, "Error with deleting note", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Note deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<String> getGenreList() {
        Set<Integer>      genreList = new HashSet<>();
        ArrayList<String> res       = new ArrayList<>();

        for (int i = 0; i < mBookList.size(); i++) {
            Set<Integer> bookGenres = mBookList.get(i).getBookGenres();
            for (Integer j : bookGenres) {
                genreList.add(j);
            }
        }
        for (Integer i : genreList) {
            res.add(mGenreList[i]);
        }
        return res;
    }

    public ArrayList<Book> getGenre(String genreName) {
        ArrayList<Book>   res       = new ArrayList<>();
        ArrayList<String> genreList = getGenreList();
        int               exist     = -1;

        for (int i = 0; i < genreList.size(); i++)
            if (genreName.equals(genreList.get(i)))
                exist = 1;

        if (exist == 1) {
            int pos = -1;
            for (int i = 0; i < mGenreList.length; i++)
                if (genreName.equals(mGenreList[i])) {
                    pos = i;
                    break;
                }
            for (int i = 0; i < mBookList.size(); i++) {
                Set<Integer> genres = mBookList.get(i).getBookGenres();
                for (Integer j : genres) {
                    if (j == pos) {
                        res.add(mBookList.get(i));
                        break;
                    }
                }
            }
        }

        return res;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos    = position;
        Book      book   = mBookList.get(position);
        String    title  = book.getBookBasicInfo().getBookTitle();
        String    author = book.getBookBasicInfo().getBookAuthor();

        TextView       titleTextView = holder.titleTextView;
        TextView       authTextView  = holder.authTextView;
        Button         showBook      = holder.showDetailButton;
        final CardView quickNoteBox  = holder.quickNoteBox;
        Button         showQuickNote = holder.showQuickNoteButton;

        setupQuickNoteBox(holder, pos, book);
        titleTextView.setText(title);
        authTextView.setTypeface(null, Typeface.ITALIC);
        authTextView.setText(author);
        showBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowBookActivity.class);
                Box    box    = new Box(mBookList.get(pos), pos);
                intent.putExtra("book", box);
                mContext.startActivity(intent);
            }
        });
        showQuickNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation slideUp   = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
                Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);

                if (quickNoteBox.getVisibility() == View.GONE) {
                    quickNoteBox.startAnimation(slideDown);
                    quickNoteBox.setVisibility(View.VISIBLE);
                } else if (quickNoteBox.getVisibility() == View.VISIBLE) {
                    quickNoteBox.startAnimation(slideUp);
                    quickNoteBox.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            quickNoteBox.setVisibility(View.GONE);
                        }
                    }, 500);
                }
            }
        });
    }

    private void setupQuickNoteBox(ViewHolder holder, final int position, Book book) {
        final Book   currentBook      = book;
        final Note   latestNote       = currentBook.getLatestNote();
        final String latestNoteDetail = latestNote.getContent();
        final int    latestNotePage   = latestNote.getPage();
        final int    latestNoteID     = currentBook.getLatestNoteID();

        TextView    latestNoteDetailTextView = holder.latestNoteTextView;
        TextView    latestNotePageTextView   = holder.latestNotePageTextView;
        ImageButton addNoteButton            = holder.addNoteButton;
        ImageButton editButton               = holder.editNoteButton;
        ImageButton deleteButton             = holder.deleteNoteButton;

        latestNoteDetailTextView.setText(latestNoteDetail);
        if (latestNotePage == -1 || latestNotePage == -2) {
            latestNotePageTextView.setVisibility(View.INVISIBLE);
        } else {
            latestNotePageTextView.setText(String.valueOf(latestNotePage));
            latestNotePageTextView.setVisibility(View.VISIBLE);
        }

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.add_note_dialog);
                Button discardButton = (Button) dialog.findViewById(R.id.add_note_dialog_discard_button);
                discardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                final EditText addNoteBox = (EditText) dialog.findViewById(R.id.add_note_field);
                final EditText pageBox    = (EditText) dialog.findViewById(R.id.add_page_field);
                Button         saveButton = (Button) dialog.findViewById(R.id.add_note_dialog_save_button);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String note = addNoteBox.getText().toString().trim();
                        int    page = -2;
                        if (!TextUtils.isEmpty(note)) {
                            if (!TextUtils.isEmpty(pageBox.getText().toString().trim())) {
                                page = Integer.parseInt(pageBox.getText().toString());
                            }
                            Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, currentBook.getBookBasicInfo().getBookId());
                            addNote(position, currentUri, note, page);
                            dialog.dismiss();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "Note is empty", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latestNoteID == -1) {
                    Toast.makeText(mContext, "No note to edit", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.add_note_dialog);
                    Button discardButton = (Button) dialog.findViewById(R.id.add_note_dialog_discard_button);
                    discardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    final EditText addNoteBox = (EditText) dialog.findViewById(R.id.add_note_field);
                    final EditText pageBox    = (EditText) dialog.findViewById(R.id.add_page_field);
                    addNoteBox.setText(latestNoteDetail);
                    if (latestNotePage != -2 && latestNotePage != -1) {
                        pageBox.setText(String.valueOf(latestNotePage));
                    }
                    Button saveButton = (Button) dialog.findViewById(R.id.add_note_dialog_save_button);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String note = addNoteBox.getText().toString().trim();
                            int    page = -2;
                            if (!TextUtils.isEmpty(note)) {
                                if (!TextUtils.isEmpty(pageBox.getText().toString().trim())) {
                                    page = Integer.parseInt(pageBox.getText().toString());
                                }
                                Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, currentBook.getBookBasicInfo().getBookId());
                                editNote(position, currentUri, latestNoteID, note, page);
                                dialog.dismiss();
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(mContext, "Note is empty", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latestNoteID == -1) {
                    Toast.makeText(mContext, "No note to delete", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog alert = new AlertDialog.Builder(mContext)
                            .setMessage("Are you sure you want to delete this note?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, currentBook.getBookBasicInfo().getBookId());
                                    deleteNote(position, currentUri, latestNoteID);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context        context  = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View           bookView = inflater.inflate(R.layout.book_item, parent, false);

        return new ViewHolder(bookView);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public boolean isDataEmpty() {
        return mBookList.isEmpty();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView    titleTextView;
        public TextView    authTextView;
        public ImageView   bookImageView;
        public TextView    latestNoteTextView;
        public TextView    latestNotePageTextView;
        public Button      showQuickNoteButton;
        public Button      showDetailButton;
        public ImageButton addNoteButton;
        public ImageButton editNoteButton;
        public ImageButton deleteNoteButton;
        public CardView    quickNoteBox;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.book_item_title);
            authTextView = (TextView) itemView.findViewById(R.id.book_item_auth);
            bookImageView = (ImageView) itemView.findViewById(R.id.book_item_img);
            latestNoteTextView = (TextView) itemView.findViewById(R.id.latest_note_box_note);
            latestNotePageTextView = (TextView) itemView.findViewById(R.id.latest_note_box_page);
            showQuickNoteButton = (Button) itemView.findViewById(R.id.show_quick_note);
            showDetailButton = (Button) itemView.findViewById(R.id.show_book_detail);
            addNoteButton = (ImageButton) itemView.findViewById(R.id.latest_note_box_add_button);
            editNoteButton = (ImageButton) itemView.findViewById(R.id.latest_note_box_edit_button);
            deleteNoteButton = (ImageButton) itemView.findViewById(R.id.latest_note_box_delete_button);
            quickNoteBox = (CardView) itemView.findViewById(R.id.book_item_quick_note_box);
        }
    }

}
