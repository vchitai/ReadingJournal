package com.example.readingjournal;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readingjournal.data.BookContract;

import java.util.ArrayList;

public class BookAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<Book> mBookList = new ArrayList<>();

    public BookAdapter(Context context) {
        this.mContext = context;
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
                BookContract.BookEntry.COLUMN_BOOK_COMMENTS,
                BookContract.BookEntry.COLUMN_BOOK_STATUS,
                BookContract.BookEntry.COLUMN_BOOK_RATING
        };

        Cursor cursor = mContext.getContentResolver().query(BookContract.BookEntry.CONTENT_URI, projection, null, null, null);
        try {
            int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_AUTHOR);
            int descriptionColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_DESCRIPTION);
            int statusColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_STATUS);
            int ratingColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_RATING);
            int commentsColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_COMMENTS);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                String currentDescription = cursor.getString(descriptionColumnIndex);
                String currentComments = cursor.getString(commentsColumnIndex);
                int currentStatus = cursor.getInt(statusColumnIndex);
                boolean currentStatusB = false;
                if (currentStatus == 0) {
                    currentStatusB = false;
                } else {
                    currentStatusB = true;
                }
                float currentRating = cursor.getInt(ratingColumnIndex);

                mBookList.add(new Book(currentID, currentTitle, currentAuthor, currentDescription, currentComments, currentStatusB, currentRating));
            }
        } finally {
            cursor.close();
        }
    }

    public void removeBook(int order) {
        mBookList.remove(order);
    }

    public void saveBook(int currentId, Uri currentUri, String title, String author, String description, boolean status, float rating) {
        if (title != null) {
            String comments = "";
            ContentValues values = new ContentValues();
            values.put(BookContract.BookEntry.COLUMN_BOOK_TITLE, title);
            values.put(BookContract.BookEntry.COLUMN_BOOK_AUTHOR, author);
            values.put(BookContract.BookEntry.COLUMN_BOOK_DESCRIPTION, description);
            values.put(BookContract.BookEntry.COLUMN_BOOK_STATUS, status);
            values.put(BookContract.BookEntry.COLUMN_BOOK_RATING, rating);

            if (currentUri == null) {
                Uri newUri = mContext.getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(mContext, "Error with saving book", Toast.LENGTH_SHORT).show();
                } else {
                    mBookList.add(new Book(BookContract.WORKING_ID, title, author, description, comments, status, rating));
                    Toast.makeText(mContext, "Book saved", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            } else {
                int rowAffected = mContext.getContentResolver().update(currentUri, values, null, null);

                if (rowAffected == 0) {
                    Toast.makeText(mContext, "Error with saving book", Toast.LENGTH_SHORT).show();
                } else {
                    mBookList.get(currentId).editBook(title, author, description, status, rating);
                    Toast.makeText(mContext, "Book saved", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        }
    }

    public void addComment(int currentId, Uri currentUri, String comment, int page) {
        Book currentBook = mBookList.get(currentId);
        ContentValues value = new ContentValues();
        value.put(BookContract.BookEntry.COLUMN_BOOK_COMMENTS, currentBook.encodeComment());
        int rowAffected = mContext.getContentResolver().update(currentUri, value, null, null);

        if (rowAffected == 0) {
            Toast.makeText(mContext, "Error with saving comment", Toast.LENGTH_SHORT).show();
        } else {
            mBookList.get(currentId).addComment(comment, page);
            Toast.makeText(mContext, "Comment saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void editComment(int currentId, Uri currentUri, int commentID, String comment, int page) {
        Book currentBook = mBookList.get(currentId);
        mBookList.get(currentId).editComment(commentID, comment, page);
        ContentValues value = new ContentValues();
        value.put(BookContract.BookEntry.COLUMN_BOOK_COMMENTS, currentBook.encodeComment());
        int rowAffected = mContext.getContentResolver().update(currentUri, value, null, null);

        if (rowAffected == 0) {
            Toast.makeText(mContext, "Error with saving comment", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Comment saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteComment(int currentId, Uri currentUri, int commentID) {
        Book currentBook = mBookList.get(currentId);
        mBookList.get(currentId).deleteComment(commentID);
        ContentValues value = new ContentValues();
        value.put(BookContract.BookEntry.COLUMN_BOOK_COMMENTS, currentBook.encodeComment());
        int rowAffected = mContext.getContentResolver().update(currentUri, value, null, null);

        if (rowAffected == 0) {
            Toast.makeText(mContext, "Error with deleting comment", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Comment deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mBookList.get(groupPosition).getLatestComment();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.book_item, null);
        }

        final Book currentBook = mBookList.get(groupPosition);
        final Comment latestComment = currentBook.getLatestComment();
        final String latestCommentDetail = latestComment.getDetail();
        final int latestCommentPage = latestComment.getPage();
        final int latestCommentID = currentBook.getLatestCommentID();

        TextView latestCommentDetailTextView = (TextView) convertView.findViewById(R.id.lastest_comment);
        TextView latestCommentPageTextView = (TextView) convertView.findViewById(R.id.comment_page);
        ImageButton addCommentButton = (ImageButton) convertView.findViewById(R.id.add_comment_button);
        ImageButton editButton = (ImageButton) convertView.findViewById(R.id.edit_button);
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_button);

        latestCommentDetailTextView.setText(latestCommentDetail);
        if (latestCommentPage == -1 || latestCommentPage == -2) {
            latestCommentPageTextView.setVisibility(View.INVISIBLE);
        } else {
            latestCommentPageTextView.setText(String.valueOf(latestCommentPage));
            latestCommentPageTextView.setVisibility(View.VISIBLE);
        }

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.add_comment_dialog);
                Button discardButton = (Button) dialog.findViewById(R.id.add_comment_dialog_discard_button);
                discardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                final EditText addCommentBox = (EditText) dialog.findViewById(R.id.add_comment_field);
                final EditText pageBox = (EditText) dialog.findViewById(R.id.page_edit_field);
                Button saveButton = (Button) dialog.findViewById(R.id.add_comment_dialog_save_button);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = addCommentBox.getText().toString().trim();
                        int page = -2;
                        if (!TextUtils.isEmpty(comment)) {
                            if (!TextUtils.isEmpty(pageBox.getText().toString().trim())) {
                                page = Integer.parseInt(pageBox.getText().toString());
                            }
                            Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, currentBook.getBookId());
                            addComment(groupPosition, currentUri, comment, page);
                            dialog.dismiss();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "Comment is empty", Toast.LENGTH_SHORT).show();
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
                if (latestCommentID == -1) {
                    Toast.makeText(mContext, "No comment to edit", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.add_comment_dialog);
                    Button discardButton = (Button) dialog.findViewById(R.id.add_comment_dialog_discard_button);
                    discardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    final EditText addCommentBox = (EditText) dialog.findViewById(R.id.add_comment_field);
                    final EditText pageBox = (EditText) dialog.findViewById(R.id.page_edit_field);
                    addCommentBox.setText(latestCommentDetail);
                    if (latestCommentPage != -2 && latestCommentPage != -1) {
                        pageBox.setText(String.valueOf(latestCommentPage));
                    }
                    Button saveButton = (Button) dialog.findViewById(R.id.add_comment_dialog_save_button);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String comment = addCommentBox.getText().toString().trim();
                            int page = -2;
                            if (!TextUtils.isEmpty(comment)) {
                                if (!TextUtils.isEmpty(pageBox.getText().toString().trim())) {
                                    page = Integer.parseInt(pageBox.getText().toString());
                                }
                                Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, currentBook.getBookId());
                                editComment(groupPosition, currentUri, latestCommentID, comment, page);
                                dialog.dismiss();
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(mContext, "Comment is empty", Toast.LENGTH_SHORT).show();
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
                if (latestCommentID == -1) {
                    Toast.makeText(mContext, "No comment to delete", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog alert = new AlertDialog.Builder(mContext)
                            .setMessage("Are you sure you want to delete this comment?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, currentBook.getBookId());
                                    deleteComment(groupPosition, currentUri, latestCommentID);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mBookList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mBookList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Book group = (Book) getGroup(groupPosition);
        String title = group.getBookTitle();
        String author = group.getBookAuthor();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.book_list, null);
        }

        TextView bookTitle = (TextView) convertView.findViewById(R.id.book_title);
        bookTitle.setTypeface(null, Typeface.BOLD);
        bookTitle.setText(title);

        TextView bookAuthor = (TextView) convertView.findViewById(R.id.book_auth);
        bookAuthor.setTypeface(null, Typeface.ITALIC);
        bookAuthor.setText(author);

        ImageView showBook = (ImageView) convertView.findViewById(R.id.show_book);
        showBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowBookActivity.class);
                Box box = new Box(mBookList.get(groupPosition), groupPosition);
                intent.putExtra("book", box);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}