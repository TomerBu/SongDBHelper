package tomerbu.edu.songdbhelper.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tomerbu.edu.songdbhelper.R;
import tomerbu.edu.songdbhelper.db.SongDAO;
import tomerbu.edu.songdbhelper.models.Song;

public class SongDetailsActivity extends AppCompatActivity {

    EditText etSongTitle, etArtist, etDuration, etImageUri;
    Button btnSaveOrUpdate;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);

        etArtist = (EditText) findViewById(R.id.etArtist);
        etSongTitle = (EditText) findViewById(R.id.etTitle);
        etDuration = (EditText) findViewById(R.id.etDuration);
        etImageUri = (EditText) findViewById(R.id.etImage);
        btnSaveOrUpdate = (Button) findViewById(R.id.btnSave);

        Intent intent = getIntent();
        id = intent.getStringExtra("_ID");

        if (id != null) {
            //init the dao:
            SongDAO dao = new SongDAO(this);
            //get the song from the dao by id
            Song s = dao.query(id);

            //fill the edittext using the song:
            etArtist.setText(s.getArtist());
            etSongTitle.setText(s.getTitle());
            etDuration.setText(s.getDuration());
            etImageUri.setText(s.getImageURI());
        }
        btnSaveOrUpdate.setText(id != null ? "Update" : "Insert");
    }

    public void save(View view) {
        SongDAO dao = new SongDAO(this);

        Song s = new Song(
                etSongTitle.getText().toString(),
                etArtist.getText().toString(),
                etDuration.getText().toString(),
                etImageUri.getText().toString()
        );


        //if (_id!=null) call update instead.
        if (id != null) {
            dao.update(id, s);
        } else {
            dao.insert(s);
        }

        Intent mainIntent = new Intent(this, SongRecyclerActivity.class);
        startActivity(mainIntent);

        //finish();
    }
}
