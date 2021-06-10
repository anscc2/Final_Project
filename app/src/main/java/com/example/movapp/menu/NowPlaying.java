package com.example.movapp.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.movapp.adapter.MovieAdapter;
import com.example.movapp.Movie.MovieResponse;
import com.example.movapp.Movie.ResultMovie;
import com.example.movapp.R;
import com.example.movapp.networks.Const;
import com.example.movapp.networks.MovieApiClient;
import com.example.movapp.networks.MovieApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class NowPlaying extends Fragment {
    List<ResultMovie> mlist;
    RecyclerView rv_content;
    TextView tv_statusSearch;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private MovieAdapter adapter;
    String lang = "en-US";
    String category = "now_playing";
    int PAGE = 1;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("FavoriteMovie", Context.MODE_PRIVATE);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager grid = new GridLayoutManager(getContext(), 3);

        rv_content = view.findViewById(R.id.rv_now_playing);
        tv_statusSearch = view.findViewById(R.id.tv_search_status);

        rv_content.setHasFixedSize(true);
        rv_content.setLayoutManager(grid);

        rv_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = grid.getChildCount();
                    totalItemCount = grid.getItemCount();
                    pastVisiblesItems = grid.findFirstVisibleItemPosition();

                    if (loading) {

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            PAGE += 1;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    MovieApiInterface apiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);
                                    Call<MovieResponse> call = apiInterface.getMovie(category, Const.API_KEY, lang, PAGE);

                                    call.enqueue(new Callback<MovieResponse>() {
                                        @Override
                                        public void onResponse(Call<MovieResponse> call, retrofit2.Response<MovieResponse> response) {
                                            mlist.addAll(response.body().getResultMovies());
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Call<MovieResponse> call, Throwable t) {

                                        }
                                    });
                                }
                            }, 2000);


                            loading = true;
                        }
                    }
                }
            }
        });

        MovieApiInterface apiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);
        Call<MovieResponse> call = apiInterface.getMovie(category, Const.API_KEY, lang, PAGE);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, retrofit2.Response<MovieResponse> response) {
                mlist = response.body().getResultMovies();
                adapter = new MovieAdapter(getContext(), mlist);
                rv_content.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Cari");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                MovieApiInterface apiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);
                Call<MovieResponse> call = apiInterface.getSearchMovie(Const.API_KEY, s);

                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, retrofit2.Response<MovieResponse> response) {
                        List<ResultMovie> mlist = response.body().getResultMovies();
                        if (mlist.isEmpty()) {
                            tv_statusSearch.setVisibility(View.VISIBLE);
                            tv_statusSearch.setText("Film Tidak Ditemukan");
                            rv_content.setVisibility(View.GONE);
                        } else {
                            tv_statusSearch.setVisibility(View.GONE);
                            rv_content.setVisibility(View.VISIBLE);
                            adapter = new MovieAdapter(getContext(), mlist);
                            rv_content.setAdapter(adapter);
                            System.out.println(s);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        System.out.println(t.getMessage());

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        menuItem.setActionView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }
}