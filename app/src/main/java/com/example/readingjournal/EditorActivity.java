package com.example.readingjournal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.chita.readingjournal.R;

public class EditorActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private EditText mDescriptionEditText;
    private RatingBar mRatingBar;
    private Uri mCurrentUri;
    private int mCurrentId;
    private Book mBook;
    private int mBookPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mTitleEditText = (EditText) findViewById(R.id.edit_book_title);
        mAuthorEditText = (EditText) findViewById(R.id.edit_book_author);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_book_description);
        mRatingBar = (RatingBar) findViewById(R.id.edit_rating_bar);
        Intent intent = getIntent();
        mCurrentUri = intent.getData();
        if (mCurrentUri == null) {
            setTitle("Add Book");
        } else {
            setTitle("Edit Book");
            Box box = (Box) intent.getSerializableExtra("book");
            mBook = box.unbox();
            mBookPosition = box.getBookPosition();
            mTitleEditText.setText(mBook.getBookTitle());
            mAuthorEditText.setText(mBook.getBookAuthor());
            mDescriptionEditText.setText(mBook.getBookDescription());
            mRatingBar.setRating(mBook.getBookRating());
        }
        mCurrentId = intent.getIntExtra("id", -1);
    }

    private void saveBook() {
        String title = mTitleEditText.getText().toString().trim();
        String description = mDescriptionEditText.getText().toString().trim();
        String author = mAuthorEditText.getText().toString().trim();
        float rating = mRatingBar.getRating();

        MainActivity.getListAdapter().saveBook(mCurrentId, mCurrentUri, title, author, description, (int) rating);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBook();
                finish();
                return true;
            case R.id.action_discard:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}