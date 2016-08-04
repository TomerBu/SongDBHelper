package tomerbu.edu.songdbhelper.controllers;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import tomerbu.edu.songdbhelper.R;
import tomerbu.edu.songdbhelper.adapters.SongAdapter;
import tomerbu.edu.songdbhelper.db.SongContract;
import tomerbu.edu.songdbhelper.db.SongDAO;
import tomerbu.edu.songdbhelper.db.SongDBHelper;
import tomerbu.edu.songdbhelper.models.Song;

public class SongRecyclerActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    SongDAO dao;
    RecyclerView recyclerView;

    @Override
    protected void onResume() {
        super.onResume();
        SongAdapter adapter = (SongAdapter) recyclerView.getAdapter();
        adapter.requery();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dao = new SongDAO(this);

        recyclerView = (RecyclerView) findViewById(R.id.songRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SongAdapter songAdapter = new SongAdapter(this);


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //remove from the arrayList
                //remove from the database
                //notify item removed(position)
                //int position = viewHolder.getAdapterPosition();
                SongAdapter.SongViewHolder holder = (SongAdapter.SongViewHolder) viewHolder;
                songAdapter.deleteByHolder(holder);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                float right = viewHolder.itemView.getRight();
                float top  = viewHolder.itemView.getTop();
                float bottom = viewHolder.itemView.getBottom();

                Drawable d = new ColorDrawable(Color.parseColor("#4CAF50"));
                d.setBounds((int) (right + dX), (int) top, (int) right, (int) bottom);
                d.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        helper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(songAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void gotoDetails(View view) {
        Intent intent = new Intent(this, SongDetailsActivity.class);
        startActivity(intent);
    }

}
