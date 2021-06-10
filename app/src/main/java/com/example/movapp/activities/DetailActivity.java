package com.example.movapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.movapp.R;
import com.example.movapp.networks.MovieApiClient;
import com.example.movapp.networks.MovieApiInterface;

public class DetailActivity extends AppCompatActivity {
    ImageView ivBackground;
    ImageView ivPoster;

    TextView tvTitle;
    TextView tvRating;

    TextView tvReleaseDate;
    TextView tvOverview;

    RatingBar ratingBar;
    ConstraintLayout clDetail;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        clDetail = findViewById(R.id.cl_detail);
        clDetail.setVisibility(View.GONE);

        ivBackground = findViewById(R.id.iv_background);
        ivPoster = findViewById(R.id.iv_poster);

        tvTitle = findViewById(R.id.tv_title);
        tvRating = findViewById(R.id.tv_rating);

        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvOverview = findViewById(R.id.tv_overview);

        ratingBar = findViewById(R.id.rating_bar);

        preferences = getSharedPreferences("FavoriteMovie", Context.MODE_PRIVATE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String data[] = bundle.getStringArray("data");

        if (data[2] != null) {
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w185" + data[2])
                    .into(ivPoster);
        }

        if (data[5] != null) {
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w185" + data[5])
                    .into(ivBackground);
        }

        getSupportActionBar().setTitle(data[0]);
        id = data[6];

        MovieApiInterface apiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);

        tvTitle.setText(data[0]);
        tvOverview.setText(data[1]);
        tvRating.setText(data[4]);
        tvReleaseDate.setText(data[3]);
        ratingBar.setRating(Float.parseFloat(data[4]) / 2);

        clDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.fav_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_fav);
        if (containsLike(id)) {
            menuItem.setIcon(R.drawable.ic_baseline_favorite_24);
        } else {
            menuItem.setIcon(R.drawable.ic_baseline_favorite_border_24);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        preferences = getSharedPreferences("FavoriteMovie", Context.MODE_PRIVATE);
        editor = preferences.edit();
        switch (item.getItemId()) {
            case R.id.nav_fav :
                if (item.getIcon().getConstantState().equals(getDrawable(R.drawable.ic_baseline_favorite_border_24).getConstantState())) {
                    Toast t = Toast.makeText(getApplicationContext(), "Favorite", Toast.LENGTH_SHORT);
                    editor.putString("favorite", preferences.getString("favorite", "") + (preferences.getString("favorite", "").equals("") ? "" : " " ) + id);
                    editor.apply();
                    t.show();
                    System.out.println(preferences.getString("favorite", ""));
                    item.setIcon(R.drawable.ic_baseline_favorite_24);
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "Unfavorite", Toast.LENGTH_SHORT);
                    t.show();
                    String [] allFav = preferences.getString("favorite", "").split(" ");
                    String newFav = "";
                    for (String fav: allFav) {
                        if (!id.equals(fav)) {
                            newFav += (newFav.isEmpty() ? "" : " ") + fav;
                        }
                    }
                    System.out.println(newFav);
                    editor.putString("favorite", newFav);
                    editor.apply();
                    item.setIcon(R.drawable.ic_baseline_favorite_border_24);
                }
                break;
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean containsLike(String id) {
        preferences = getSharedPreferences("FavoriteMovie", Context.MODE_PRIVATE);
        String [] all = preferences.getString("favorite", "").split(" ");

        return preferences.getString("favorite", "").contains(id);
    }

}
