package tomerbu.edu.songdbhelper.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class SongProvider extends ContentProvider {
    private static final String AUTHORITY = "tomerbu.edu.songdbhelper";
    private static final int SONGS = 10;
    private static final int SONGS_ID = 11;

    private UriMatcher matcher;
    private SongDAO dao;
    private SongDBHelper helper;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Songs");

    @Override
    public boolean onCreate() {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "Songs", SONGS);
        matcher.addURI(AUTHORITY, "Songs/#", SONGS_ID);
        dao = new SongDAO(getContext());
        helper = new SongDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case SONGS:
                break;
            case SONGS_ID:
                selection = getModifiedSelectionWithID(uri, selection);
                break;
            default:
                throw new UnsupportedOperationException("No Such uri");
        }
        return helper.getWritableDatabase().query("Songs", projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
        // consider using the vnd string instead:
        //    private String MIME_SONGS = "vnd.android.cursor.dir/vnd.tomerbu.edu.songdbhelper.Songs";
        //   private String MIME_SONGS_SINGLE = "vnd.android.cursor.item/vnd.tomerbu.edu.songdbhelper.Song";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case SONGS:
                long insertedID = helper.getWritableDatabase().insert("Songs", null, values);
                //getContext().getContentResolver().notifyChange(, null);
                Uri build = CONTENT_URI.buildUpon().appendEncodedPath("" + insertedID).build();
                return build;
             default:
                throw new UnsupportedOperationException("No Such uri");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Helper methods
     **/
    @NonNull
    private String getModifiedSelectionWithID(Uri uri, String selection) {
        if (!TextUtils.isEmpty(selection))
            selection = selection.concat(" AND _ID = " + uri.getLastPathSegment());
        else
            selection = "_ID = " + uri.getLastPathSegment();
        return selection;
    }
}
