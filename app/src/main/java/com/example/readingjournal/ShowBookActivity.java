package com.example.readingjournal;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chita.readingjournal.R;
import com.example.readingjournal.data.BookContract;

public class ShowBookActivity extends AppCompatActivity {
    private Book mBook;
    private int mBookPosition;
    private TextView mShowBookTitle;
    private TextView mShowBookAuthor;
    private TextView mShowBookDesc;
    private RatingBar mShowRatingBar;
    private ListView mCommentsList;
    private CommentAdapter mCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);
        Box box = (Box) getIntent().getSerializableExtra("book");
        mBook = box.unbox();
        mBookPosition = box.getBookPosition();
        setTitle(mBook.getBookTitle());
        mCommentAdapter = new CommentAdapter(this, mBook.getBookComments());
        mCommentsList = (ListView) findViewById(R.id.show_comment_list);
        mCommentsList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.book_detail, null));
        mShowBookTitle = (TextView) findViewById(R.id.show_book_title);
        mShowBookAuthor = (TextView) findViewById(R.id.show_book_author);
        mShowBookDesc = (TextView) findViewById(R.id.show_book_description);
        mShowRatingBar = (RatingBar) findViewById(R.id.show_rating_bar);
        mShowBookTitle.setTypeface(null, Typeface.BOLD);
        mShowBookTitle.setText(mBook.getBookTitle());
        mShowBookAuthor.setTypeface(null, Typeface.ITALIC);
        mShowBookAuthor.setText(mBook.getBookAuthor());
        mShowBookDesc.setText(mBook.getBookDescription());
        mShowRatingBar.setRating(mBook.getBookRating());
        mCommentsList.setAdapter(mCommentAdapter);
        mShowRatingBar.setIsIndicator(true);
        final EditText addCommentBox = (EditText) findViewById(R.id.bd_add_comment_field);
        final EditText pageBox = (EditText) findViewById(R.id.bd_page_edit_field);
        Button saveCommentButton = (Button) findViewById(R.id.bd_save_comment_button);
        saveCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = addCommentBox.getText().toString().trim();
                int page = -2;
                if (!TextUtils.isEmpty(comment)) {
                    if (!TextUtils.isEmpty(pageBox.getText().toString().trim())) {
                        page = Integer.parseInt(pageBox.getText().toString());
                    }
                    mBook.addComment(comment, page);
                    Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, mBook.getBookId());
                    MainActivity.getListAdapter().addComment(mBookPosition, currentUri, comment, page);
                    mCommentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ShowBookActivity.this, "Comment is empty", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, EditorActivity.class);
        Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, mBook.getBookId());
        intent.setData(currentUri);
        Box box = new Box(mBook, mBookPosition);
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
                        Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, mBook.getBookId());
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
