package com.example.movapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.movapp.R;
import com.example.movapp.menu.Favorite;
import com.example.movapp.menu.NowPlaying;
import com.example.movapp.menu.Popular;
import com.example.movapp.menu.Upcoming;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    boolean isFavorite = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bnv_main);
        bottomNavigationView.setSelectedItemId(R.id.item_now_playing);

        sharedPreferences = getSharedPreferences("FavoriteMovie", Context.MODE_PRIVATE);

        bottomNavigationView.getMenu().findItem(R.id.item_now_playing).setTitle("Now Playing");
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.item_now_playing:
                        getSupportActionBar().setTitle("Now Playing");
                        item.setTitle("Now Playing");
                        fragment = new NowPlaying();
                        isFavorite = false;
                        break;
                    case R.id.item_upcoming:
                        getSupportActionBar().setTitle("Upcoming");
                        item.setTitle("Upcoming");
                        fragment = new Upcoming();
                        isFavorite = false;
                        break;
                    case R.id.item_popular:
                        getSupportActionBar().setTitle("Popular");
                        fragment = new Popular();
                        item.setTitle("Popular");
                        isFavorite = false;
                        break;
                    case R.id.nav_fav:
                        getSupportActionBar().setTitle("Favorite");
                        item.setTitle("Favorite");
                        isFavorite = true;
                        fragment = new Favorite();
                        break;
                }


                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, fragment).commit();
                return true;
            }
        });

        Fragment f = new NowPlaying();
        getSupportActionBar().setTitle("Now Playing");
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, f).commit();
    }
}