package com.example.readingJournal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.readingJournal.R;
import com.example.readingJournal.datatypes.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String LOG_TAG = NoteAdapter.class.getSimpleName();
    private ArrayList<Note> mNotes;
    private Context         mContext;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        mContext = context;
        mNotes = notes;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = mNotes.get(position);

        TextView detailTextView = holder.detailTextView;
        detailTextView.setText(note.getContent());

        TextView pageButton = holder.pageButton;
        if (note.getPage() == -1 || note.getPage() == -2) {
            pageButton.setVisibility(View.INVISIBLE);
        } else {
            pageButton.setText(String.valueOf(note.getPage()));
            pageButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context        context  = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View           noteView = inflater.inflate(R.layout.note_item, parent, false);

        return new ViewHolder(noteView);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView detailTextView;
        public TextView pageButton;

        public ViewHolder(View itemView) {
            super(itemView);

            detailTextView = (TextView) itemView.findViewById(R.id.note_item_detail);
            pageButton = (TextView) itemView.findViewById(R.id.note_item_page);
        }
    }

}
