package com.example.melaniepeng.project_4;

import java.util.*;
import java.io.Serializable;

public class Movie implements Serializable{
    private String title;
    private String rating;
    private String director;
    private String year;
    private Set<String> stars;
    private Set<String> genres;

    public Movie()
    {
        title = "";
        rating = "";
        director = "";
        year = "";
        stars = new HashSet<String>();
        genres = new HashSet<String>();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getRating() {
        return rating;
    }

    public void setDirector(String director) {
        this.director = director;
    }
    public String getDirector() {
        return director;
    }

    public void setYear(String year) {
        this.year = year;
    }
    public String getYear() {
        return year;
    }

    public void addGenre(String genre) {
        this.genres.add(genre);
    }
    public Set getGenre() {
        return genres;
    }

    public void addStar(String star) {
        this.stars.add(star);
    }
    public Set getStar() {
        return stars;
    }

}
