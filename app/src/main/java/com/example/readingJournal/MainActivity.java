package com.example.readingJournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.readingJournal.adapter.BookAdapter;

public class MainActivity extends AppCompatActivity {
    private static BookAdapter           mBookAdapter;
    private        DrawerLayout          mDrawerLayout;
    private        ListView              mDrawerList;
    private        ArrayAdapter<String>  mDrawerAdapter;
    private        ActionBarDrawerToggle mDrawerToggle;
    private        RecyclerView          mBookListView;
    private        LinearLayoutManager   mLayoutManager;
    private        RelativeLayout        mEmptyBookView;

    public static BookAdapter getListAdapter() {
        return mBookAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBookListView();
        setupUI();
        checkEmpty();
    }

    private void setupUI() {
        FloatingActionButton fab   = (FloatingActionButton) findViewById(R.id.main_floating_action_button);
        final String         title = getTitle().toString();

        mEmptyBookView = (RelativeLayout) findViewById(R.id.empty_view);
        mDrawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mBookAdapter.getGenreList());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
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

        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("category_name", mBookAdapter.getGenreList().get(position));
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);

    }

    private void setupBookListView() {
        mBookAdapter = new BookAdapter(this);
        mBookListView = (RecyclerView) findViewById(R.id.main_book_list);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mBookListView.setAdapter(mBookAdapter);
        mBookListView.setLayoutManager(mLayoutManager);
    }

    private void checkEmpty() {
        if (mBookAdapter.isDataEmpty()) {
            mBookListView.setVisibility(View.GONE);
            mEmptyBookView.setVisibility(View.VISIBLE);
        } else {
            mBookListView.setVisibility(View.VISIBLE);
            mEmptyBookView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        mBookAdapter.notifyDataSetChanged();
        checkEmpty();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}