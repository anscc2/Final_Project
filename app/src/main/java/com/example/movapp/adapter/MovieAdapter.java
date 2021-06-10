package com.example.movapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movapp.Movie.ResultMovie;
import com.example.movapp.activities.DetailActivity;
import com.example.movapp.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private List<ResultMovie> list;

    public MovieAdapter(Context context, List<ResultMovie> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.list_movie, parent, false);
        MovieAdapter.ViewHolder viewHolder = new MovieAdapter.ViewHolder(view);

        viewHolder.cl_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(parent.getContext(), DetailActivity.class);

                String [] imports = {
                        list.get(viewHolder.getAdapterPosition()).getTitle(),
                        list.get(viewHolder.getAdapterPosition()).getOverview(),
                        list.get(viewHolder.getAdapterPosition()).getPosterPath(),
                        list.get(viewHolder.getAdapterPosition()).getReleaseDate(),
                        list.get(viewHolder.getAdapterPosition()).getVoteAverage() + "",
                        list.get(viewHolder.getAdapterPosition()).getBackdropPath(),
                        list.get(viewHolder.getAdapterPosition()).getId() + "",
                };
                a.putExtra("data", imports);
                parent.getContext().startActivity(a);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvTitle.setText(list.get(i).getTitle());
        viewHolder.tvYear.setText(list.get(i).getReleaseDate());
        if (list.get(i).getPosterPath() != null) {
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185" + list.get(i).getPosterPath())
                    .into(viewHolder.ivPoster);

        } else {
            viewHolder.ivPoster.setImageResource(R.drawable.poster_noimage);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvYear;
        ConstraintLayout cl_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvYear = itemView.findViewById(R.id.tv_year);
            cl_card = itemView.findViewById(R.id.cl_card);
        }
    }
}
