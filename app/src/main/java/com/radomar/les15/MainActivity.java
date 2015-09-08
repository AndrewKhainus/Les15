package com.radomar.les15;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.radomar.les15.adapters.ImageRecyclerAdapter;
import com.radomar.les15.tasks.GetUriTask;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://random.cat/meow";
    private static final String KEY = "key";

    private Toolbar mToolbar;
    private ImageRecyclerAdapter mAdapter;
    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.tbToolbar_AM);
        setSupportActionBar(mToolbar);

        final RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.rvList_AM);
        setLayoutManager(mRecyclerView);

        if (savedInstanceState == null) {
            GetUriTask getUriTask = new GetUriTask() {
                @Override
                protected void onPostExecute(ArrayList<String> urls) {
                    mData.addAll(urls);

                    mAdapter = new ImageRecyclerAdapter(MainActivity.this, urls);
                    mRecyclerView.setAdapter(mAdapter);
                }
            };
            getUriTask.execute(URL);
        } else {
            mData = savedInstanceState.getStringArrayList(KEY);
            mAdapter = new ImageRecyclerAdapter(this, mData);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_clear_cache:
                clearCache();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(KEY, mData);
        super.onSaveInstanceState(outState);
    }

    private void setLayoutManager(RecyclerView recyclerView) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    private void clearCache() {
        if(getCacheDir().exists()) {
            File[] files = getCacheDir().listFiles();

            if(files != null) {
                for (File file: files) {
                    file.delete();
                }
            }
        }
    }
}
