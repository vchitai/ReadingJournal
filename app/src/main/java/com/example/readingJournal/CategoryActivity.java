package com.example.readingJournal;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.readingJournal.adapter.SimpleBookAdapter;
import com.example.readingJournal.datatypes.Book;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private DrawerLayout          mDrawerLayout;
    private ListView              mDrawerList;
    private ArrayAdapter<String>  mDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Book>       mBookList;
    private RecyclerView          mBookListView;
    private SimpleBookAdapter     mBookListAdapter;
    private String                mCategoryName;
    private LinearLayoutManager         mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mCategoryName = getIntent().getStringExtra("category_name");
        final String title = getTitle().toString();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
        setTitle(mCategoryName);

        mDrawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MainActivity.getListAdapter().getGenreList());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.category_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.category_left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerList.setAdapter(mDrawerAdapter);

        mBookList = MainActivity.getListAdapter().getGenre(mCategoryName);
        mBookListAdapter = new SimpleBookAdapter(this, mBookList);
        mLayoutManager = new LinearLayoutManager(CategoryActivity.this);

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mBookListView.setLayoutManager(mLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

}
