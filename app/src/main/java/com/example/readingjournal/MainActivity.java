package com.example.readingjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.chita.readingjournal.R;

public class MainActivity extends AppCompatActivity {
    private static BookAdapter listAdapter;
    private ExpandableListView expListView;

    public static BookAdapter getListAdapter() {
        return listAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        expListView = (ExpandableListView) findViewById(R.id.booklist);
        listAdapter = new BookAdapter(this);
        expListView.setAdapter(listAdapter);
        expListView.setEmptyView(findViewById(R.id.empty_view));
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }
}