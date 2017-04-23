package com.example.readingJournal;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readingJournal.adapter.NoteAdapter;
import com.example.readingJournal.data.BookContract;
import com.example.readingJournal.datatypes.Book;
import com.example.readingJournal.datatypes.BookBasicInfo;
import com.example.readingJournal.datatypes.Box;

public class ShowBookActivity extends AppCompatActivity {
    private Book         mBook;
    private int          mBookPosition;
    private TextView     mShowBookTitle;
    private TextView     mShowBookAuthor;
    private TextView     mShowBookDesc;
    private TextView     mShowBookRating;
    private CheckBox     mShowBookStatus;
    private EditText     mAddNoteBox;
    private EditText     mPageBox;
    private Button       mSaveNoteButton;
    private RecyclerView mNotesList;
    private NoteAdapter  mNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);
        assign();
        init();
    }

    public void assign() {
        Box box = (Box) getIntent().getSerializableExtra("book");
        mBook = box.unBox();
        mBookPosition = box.getBookPosition();
        mNotesList = (RecyclerView) findViewById(R.id.show_note_list);
        mNoteAdapter = new NoteAdapter(this, mBook.getBookNotes());
        mShowBookTitle = (TextView) findViewById(R.id.show_book_title);
        mShowBookAuthor = (TextView) findViewById(R.id.show_book_author);
        mShowBookDesc = (TextView) findViewById(R.id.show_book_description);
        mShowBookRating = (TextView) findViewById(R.id.show_book_rating);
        mShowBookStatus = (CheckBox) findViewById(R.id.show_book_status);
        mAddNoteBox = (EditText) findViewById(R.id.show_book_add_note_field);
        mPageBox = (EditText) findViewById(R.id.show_book_add_note_page_edit_field);
        mSaveNoteButton = (Button) findViewById(R.id.show_book_save_note_button);
    }

    public void init() {
        final BookBasicInfo bookBasicInfo = mBook.getBookBasicInfo();
        ImageButton         addNoteButton = (ImageButton) findViewById(R.id.show_book_add_note_button);

        setTitle(bookBasicInfo.getBookTitle());
        mShowBookTitle.setTypeface(null, Typeface.BOLD);
        mShowBookTitle.setText(bookBasicInfo.getBookTitle());
        mShowBookAuthor.setTypeface(null, Typeface.ITALIC);
        mShowBookAuthor.setText(bookBasicInfo.getBookAuthor());
        mShowBookDesc.setText(bookBasicInfo.getBookDescription());
        mShowBookRating.setText(Float.toString(bookBasicInfo.getBookRating()));
        mShowBookStatus.setChecked(bookBasicInfo.getBookStatus());
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardView rl = (CardView) findViewById(R.id.show_frame_5);
                if (rl.getVisibility() == View.VISIBLE) {
                    rl.setVisibility(View.GONE);
                } else {
                    rl.setVisibility(View.VISIBLE);
                }
            }
        });

        mNotesList.setAdapter(mNoteAdapter);
        mNotesList.setLayoutManager(new LinearLayoutManager(this) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(ShowBookActivity.this) {

                    private static final float SPEED = 75f;

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }

                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        });
        mShowBookStatus.setEnabled(false);
        mSaveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mAddNoteBox.getText().toString().trim();
                int    page    = -2;
                if (!TextUtils.isEmpty(comment)) {
                    if (!TextUtils.isEmpty(mPageBox.getText().toString().trim())) {
                        page = Integer.parseInt(mPageBox.getText().toString());
                    }
                    mBook.addNote(comment, page);
                    Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, bookBasicInfo.getBookId());
                    MainActivity.getListAdapter().addNote(mBookPosition, currentUri, comment, page);
                    mNoteAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ShowBookActivity.this, "Note is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_book, menu);
        return true;
    }

    public void editBook() {
        Intent intent     = new Intent(this, EditorActivity.class);
        Uri    currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, mBook.getBookBasicInfo().getBookId());
        Box    box        = new Box(mBook, mBookPosition);

        intent.setData(currentUri);
        intent.putExtra("book", box);
        intent.putExtra("id", mBookPosition);
        startActivity(intent);
    }

    public void deleteBook() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this book?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Uri currentUri  = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, mBook.getBookBasicInfo().getBookId());
                        int rowAffected = getContentResolver().delete(currentUri, null, null);
                        MainActivity.getListAdapter().removeBook(mBookPosition);
                        if (rowAffected == 0) {
                            Toast.makeText(ShowBookActivity.this, "Error with deleting book", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ShowBookActivity.this, "Book deleted", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editBook();
                finish();
                return true;
            case R.id.action_delete:
                deleteBook();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
