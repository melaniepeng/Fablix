package com.example.melaniepeng.project_4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movie;

    public MovieListViewAdapter(ArrayList<Movie> movie, Context context) {
        super(context, R.layout.layout_movie_row, movie);
        this.movie = movie;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_movie_row, parent, false);

        Movie singleMovie = movie.get(position);

        TextView titleView = (TextView)view.findViewById(R.id.title);
        TextView ratingView = (TextView)view.findViewById(R.id.rating);
        TextView directorView = (TextView)view.findViewById(R.id.director);
        TextView genreView = (TextView)view.findViewById(R.id.genres);
        TextView starView = (TextView)view.findViewById(R.id.stars);
        TextView yearView = (TextView)view.findViewById(R.id.year);

        titleView.setText(singleMovie.getTitle());
        ratingView.setText(singleMovie.getRating());
        directorView.setText(singleMovie.getDirector());
        yearView.setText(singleMovie.getYear());
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
        genreView.setText(totalGenre);
        String totalStar = "";
        int countline = 1;
        int countEach = 0;
        int countStar = 0;
        for(Object star : singleMovie.getStar())
        {
            countStar++;
            countEach++;
            if(countStar == singleMovie.getStar().size()) {
                totalStar = totalStar + star;
            }
            else {
                /*if (countEach == 4) {
                    totalStar = totalStar + star + ",\n";
                    countline++;
                    countEach = 0;
                } else {*/
                totalStar = totalStar + star + ", ";
                //}
            }
        }
        System.out.println(totalStar);
        starView.setText(totalStar);

        return view;
    }
}
