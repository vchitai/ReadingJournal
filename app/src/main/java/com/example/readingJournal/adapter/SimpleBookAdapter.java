package com.example.readingJournal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.readingJournal.R;
import com.example.readingJournal.datatypes.Book;
import com.example.readingJournal.datatypes.BookBasicInfo;

import java.util.ArrayList;


public class SimpleBookAdapter extends RecyclerView.Adapter<SimpleBookAdapter.ViewHolder> {
    private static final String LOG_TAG = SimpleBookAdapter.class.getSimpleName();
    private ArrayList<Book> mBookList;
    private Context         mContext;

    public SimpleBookAdapter(Context context, ArrayList<Book> bookList) {
        mContext = context;
        mBookList = bookList;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book          book          = mBookList.get(position);
        BookBasicInfo bookBasicInfo = book.getBookBasicInfo();

        TextView titleTextView = holder.titleTextView;
        TextView authTextView  = holder.authTextView;

        titleTextView.setText(bookBasicInfo.getBookTitle());
        authTextView.setText(bookBasicInfo.getBookAuthor());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView authTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.book_item_title);
            authTextView = (TextView) itemView.findViewById(R.id.book_item_auth);
        }
    }

}
