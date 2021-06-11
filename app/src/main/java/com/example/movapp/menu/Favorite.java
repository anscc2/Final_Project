package com.example.movapp.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movapp.R;
import com.example.movapp.adapter.MovAdapter;
import com.example.movapp.networks.Const;
import com.example.movapp.networks.MovieApiClient;
import com.example.movapp.networks.MovieApiInterface;
import com.example.movapp.Movie.MovResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class Favorite extends Fragment {
    List<MovResponse> list = new ArrayList<MovResponse>();
    MovAdapter adapter;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView tv_status;
    RecyclerView rv_favorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager grid = new GridLayoutManager(getContext(), 3);

        rv_favorite = view.findViewById(R.id.rv_favorite);
        rv_favorite.setHasFixedSize(true);
        rv_favorite.setLayoutManager(grid);

        preferences = getActivity().getSharedPreferences("FavoriteMovie", Context.MODE_PRIVATE);
        editor = preferences.edit();

        tv_status = view.findViewById(R.id.tv_status);
        rv_favorite = view.findViewById(R.id.rv_favorite);

        if (preferences.getString("favorite", "").equals("")) {
            tv_status.setVisibility(View.VISIBLE);
            tv_status.setText("Film Favorite Tidak Ditemukan");
            rv_favorite.setVisibility(View.GONE);
        } else {
            rv_favorite.setVisibility(View.VISIBLE);
            tv_status.setVisibility(View.GONE);
        }
        return view;
    }

    //    @Override
    public void onResume() {
        if (preferences.getString("favorite", "").equals("")) {
            tv_status.setVisibility(View.VISIBLE);
            tv_status.setText("Film Favorite Tidak Ditemukan");
            rv_favorite.setVisibility(View.GONE);
        } else {
            rv_favorite.setVisibility(View.VISIBLE);
            tv_status.setVisibility(View.GONE);

            MovieApiInterface apiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);
            String[] allFav = preferences.getString("favorite", "").split(" ");
            System.out.println("On Resume All Fav : " + preferences.getString("favorite", "") + " End");

            list.clear();
            for (int i = 0; i < allFav.length; i++) {
                Call<MovResponse> call = apiInterface.getMovieById(allFav[i], Const.API_KEY);
                int x = i;
                call.enqueue(new Callback<MovResponse>() {
                    @Override
                    public void onResponse(Call<MovResponse> call, retrofit2.Response<MovResponse> response) {
                        list.add(response.body());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<MovResponse> call, Throwable t) {

                    }
                });
            }
            adapter = new MovAdapter(getContext(), list);
            rv_favorite.setAdapter(adapter);
        }
        super.onResume();
    }
}