package com.example.melaniepeng.project_4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SingleMovieActivity extends ActionBarActivity {

    private ArrayList<Movie>displayMovie;
    private int position;
    private int page;
    private String title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemovie);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = (int)getIntent().getSerializableExtra("position");
            displayMovie = (ArrayList<Movie>) getIntent().getSerializableExtra("movieList");
            page = (int)getIntent().getSerializableExtra("page");
            title = (String)getIntent().getSerializableExtra("title");
        }

        Movie singleMovie = displayMovie.get(position);

        ((TextView)findViewById(R.id.title)).setText(singleMovie.getTitle());
        ((TextView)findViewById(R.id.rating)).setText(singleMovie.getRating());
        ((TextView)findViewById(R.id.director)).setText(singleMovie.getDirector());
        String totalGenre = "";
        int countGenre = 0;
        for(Object genre : singleMovie.getGenre())
        {
            countGenre++;
            if(countGenre == singleMovie.getGenre().size()) {
                totalGenre = totalGenre + genre;
            }
            else {
                totalGenre = totalGenre + genre + ", ";
            }
        }
        ((TextView)findViewById(R.id.genres)).setText(totalGenre);
        String totalStar = "";
        int countStar = 0;
        for(Object star : singleMovie.getStar())
        {
            countStar++;
            if(countStar == singleMovie.getStar().size()) {
                totalStar = totalStar + star;
            }
            else {
                totalStar = totalStar + star + ", ";
            }
        }
        ((TextView)findViewById(R.id.stars)).setText(totalStar);
        ((TextView)findViewById(R.id.year)).setText(singleMovie.getYear());
    }

    public void goBack(View view) {
        Intent intent = new Intent(SingleMovieActivity.this, MovieListActivity.class);
        intent.putExtra("movieList", displayMovie);
        intent.putExtra("page", page);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
