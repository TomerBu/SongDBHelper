package tomerbu.edu.songdbhelper.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tomerbu.edu.songdbhelper.R;
import tomerbu.edu.songdbhelper.controllers.SongDetailsActivity;
import tomerbu.edu.songdbhelper.db.SongDAO;
import tomerbu.edu.songdbhelper.models.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final LayoutInflater inflater;
    private ArrayList<Song> songs;
    private Context context;
    private SongDAO dao;

    public SongAdapter(Context context) {
        this.context = context;
        requery();
        inflater = LayoutInflater.from(context);
    }

    public void requery() {
        dao = new SongDAO(context);
        songs = dao.query();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        final Song s = songs.get(position);

        holder.tvTitle.setText(s.getTitle());
        holder.tvArtist.setText(s.getArtist());
        holder.tvDuration.setText(s.getDuration());
        holder._ID = s.getId();

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteByHolder(holder);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SongDetailsActivity.class);
                intent.putExtra("_ID", s.getId());
                context.startActivity(intent);
            }
        });
    }

    public void deleteByHolder(SongViewHolder holder){
        //Search the song in the arraylist
        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            //if found:
            if (s.getId().equals(holder._ID)){
                //remove the song from the arrayList
                songs.remove(s);
                //remove the song from the dao
                dao.delete(s.getId());
                //notify the adapter
                notifyItemRemoved(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDelete;
        String _ID;
        TextView tvTitle;
        TextView tvDuration;
        TextView tvArtist;
        ImageView ivArt;
        RelativeLayout layout;


        public SongViewHolder(View v) {
            super(v);

            tvArtist = (TextView) v.findViewById(R.id.tvArtist);
            tvTitle = (TextView) v.findViewById(R.id.tvSongTitle);
            tvDuration = (TextView) v.findViewById(R.id.tvDuration);

            ivArt = (ImageView) v.findViewById(R.id.imageView);
            layout = (RelativeLayout) v.findViewById(R.id.layout);
            ivDelete = (ImageView) v.findViewById(R.id.ivDelete);
        }
    }
}
