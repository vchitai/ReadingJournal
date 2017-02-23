package com.example.readingjournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private static final String LOG_TAG = CommentAdapter.class.getSimpleName();

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.comment_item, parent, false);
        }

        Comment currentComment = getItem(position);
        if (currentComment.getPage() == -1 || currentComment.getPage() == -2) {
            Button pageTextView = (Button) listItemView.findViewById(R.id.comment_item_page);
            pageTextView.setVisibility(View.INVISIBLE);
        } else {
            Button pageTextView = (Button) listItemView.findViewById(R.id.comment_item_page);
            pageTextView.setText(String.valueOf(currentComment.getPage()));
            pageTextView.setVisibility(View.VISIBLE);
        }
        TextView detailTextView = (TextView) listItemView.findViewById(R.id.comment_item_detail);
        detailTextView.setText(currentComment.getDetail());
        return listItemView;
    }
}
