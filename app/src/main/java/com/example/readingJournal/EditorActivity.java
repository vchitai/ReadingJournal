package com.example.readingJournal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.example.readingJournal.datatypes.Book;
import com.example.readingJournal.datatypes.BookBasicInfo;
import com.example.readingJournal.datatypes.Box;

public class EditorActivity extends AppCompatActivity {

    private EditText  mTitleEditText;
    private EditText  mAuthorEditText;
    private EditText  mDescriptionEditText;
    private CheckBox  mStatusCheckBox;
    private RatingBar mRatingBar;
    private Uri       mCurrentUri;
    private int       mCurrentId;
    private Book      mBook;
    private int       mBookPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mTitleEditText = (EditText) findViewById(R.id.edit_book_title);
        mAuthorEditText = (EditText) findViewById(R.id.edit_book_author);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_book_description);
        mStatusCheckBox = (CheckBox) findViewById(R.id.edit_status_check_box);
        mRatingBar = (RatingBar) findViewById(R.id.edit_rating_bar);
        Intent intent = getIntent();
        mCurrentUri = intent.getData();
        mCurrentId = intent.getIntExtra("id", -1);


        if (mCurrentUri == null) {
            setTitle("Add Book");
        } else {
            setTitle("Edit Book");
            Box box = (Box) intent.getSerializableExtra("book");
            mBook = box.unBox();
            mBookPosition = box.getBookPosition();
            BookBasicInfo bookBasicInfo = mBook.getBookBasicInfo();
            mTitleEditText.setText(bookBasicInfo.getBookTitle());
            mAuthorEditText.setText(bookBasicInfo.getBookAuthor());
            mDescriptionEditText.setText(bookBasicInfo.getBookDescription());
            mStatusCheckBox.setChecked(bookBasicInfo.getBookStatus());
            mRatingBar.setRating(bookBasicInfo.getBookRating());
        }
        mRatingBar.setVisibility(View.INVISIBLE);
        mStatusCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRatingBar.setVisibility(View.VISIBLE);
                } else {
                    mRatingBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.activity_editor);
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FunctionUtils.hideSoftKeyboard(EditorActivity.this);
                return false;
            }
        });
    }

    private void saveBook() {
        String        title                 = mTitleEditText.getText().toString().trim();
        String        description           = mDescriptionEditText.getText().toString().trim();
        String        author                = mAuthorEditText.getText().toString().trim();
        boolean       status                = mStatusCheckBox.isChecked();
        float         rating                = mRatingBar.getRating();
        BookBasicInfo originalBookBasicInfo = null;

        if (mBook != null)
            originalBookBasicInfo = mBook.getBookBasicInfo();
        BookBasicInfo bookBasicInfo;
        if (originalBookBasicInfo == null)
            bookBasicInfo = new BookBasicInfo(0, title, author, description, -1, rating, status);
        else
            bookBasicInfo = new BookBasicInfo(originalBookBasicInfo.getBookId(), title, author, description, originalBookBasicInfo.getBookImageId(), rating, status);
        MainActivity.getListAdapter().saveBook(mCurrentId, mCurrentUri, bookBasicInfo, "");
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