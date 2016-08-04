package tomerbu.edu.songdbhelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import tomerbu.edu.songdbhelper.models.Song;

/**
 * Created by stud27 on 02/08/16.
 */
public class SongDAO {
    private SQLiteDatabase db;
    private Context context;

    public SongDAO(Context context) {
        this.context = context;
        SongDBHelper helper = new SongDBHelper(context);
        db = helper.getWritableDatabase();
    }


    public long insert(Song s) {

        ContentValues values = new ContentValues();
        values.put(SongContract.Song.COL_TITLE, s.getTitle());
        values.put(SongContract.Song.COL_ARTIST, s.getArtist());
        values.put(SongContract.Song.COL_DURATION, s.getDuration());
        values.put(SongContract.Song.COL_IMAGE_URI, s.getImageURI());

        long insertedID = db.insert(SongContract.Song.TABLE_NAME, null, values);

        return insertedID;
    }


    public ArrayList<Song> query() {
        ArrayList<Song> songs = new ArrayList<>();

        //The Data set: The Query result:
        Cursor cursor = db.query(SongContract.Song.TABLE_NAME, null, null, null, null, null, null);

        //move to the first row:
        if (cursor.moveToFirst()) {

            do {
                Song s = parseCursor(cursor);
                songs.add(s);
            } while (cursor.moveToNext());
        }

        return songs;
    }

    public Song query(String id) {
        //The Data set: The Query result:
        Cursor cursor = db.query(SongContract.Song.TABLE_NAME, null, " _id = ? ", new String[]{id}, null, null, null);
        return parseCursor(cursor);
    }

    public ArrayList<Song> queryByTitle(String title) {
        //The Data set: The Query result:
        Cursor cursor = db.query(SongContract.Song.TABLE_NAME,
                null,
                SongContract.Song.COL_TITLE + " LIKE ?",
                new String[]{title + "%"},
                null, null,
                SongContract.Song.COL_TITLE + " DESC"
        );

        ArrayList<Song> songs = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                songs.add(parseCursor(cursor));
            } while (cursor.moveToNext());
        }

        return songs;
    }

    public ArrayList<Song> parseSongs(Cursor cursor) {
        ArrayList<Song> songs = new ArrayList<>();
        if (cursor.moveToFirst()) {

            do {
                songs.add(parseCursor(cursor));
            } while (cursor.moveToNext());
        }
        return songs;
    }

    private Song parseCursor(Cursor cursor) {
        //Read the Line:
        String title = cursor.getString(cursor.getColumnIndex(SongContract.Song.COL_TITLE));
        String artist = cursor.getString(cursor.getColumnIndex(SongContract.Song.COL_ARTIST));
        String id = cursor.getString(cursor.getColumnIndex(SongContract.Song.COL_ID));
        String duration = cursor.getString(cursor.getColumnIndex(SongContract.Song.COL_DURATION));
        String imageURI = cursor.getString(cursor.getColumnIndex(SongContract.Song.COL_IMAGE_URI));

        return new Song(title, id, artist, duration, imageURI);
    }

    public int delete(String id) {
        int rowsAffected = db.delete(SongContract.Song.TABLE_NAME, SongContract.Song.COL_ID + " = ?", new String[]{id});
        return rowsAffected;
    }

    public int update(String id, Song s) {
        ContentValues values = getContentValues(s);


        int rowsAffected = db.update(SongContract.Song.TABLE_NAME, values, SongContract.Song.COL_ID + " = ?", new String[]{id});
        return rowsAffected;
    }

    private ContentValues getContentValues(Song s) {
        ContentValues values = new ContentValues();
        values.put(SongContract.Song.COL_TITLE, s.getTitle());
        values.put(SongContract.Song.COL_ARTIST, s.getArtist());
        values.put(SongContract.Song.COL_DURATION, s.getDuration());
        values.put(SongContract.Song.COL_IMAGE_URI, s.getImageURI());
        return values;
    }
}
