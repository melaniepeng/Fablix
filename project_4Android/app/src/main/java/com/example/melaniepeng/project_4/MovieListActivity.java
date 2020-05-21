package com.example.melaniepeng.project_4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MovieListActivity extends ActionBarActivity {

    private ArrayList<Movie> displayMovie;
    private int page;
    private String title;
    private String id = "";
    private Map<String,Movie> allMovies = new HashMap<String,Movie>();
    private ArrayList<Movie> movieList = new ArrayList<Movie>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("Search") != null) {
                Toast.makeText(this, "Search " + bundle.get("Search") + ".", Toast.LENGTH_LONG).show();
            }
            displayMovie = (ArrayList<Movie>) getIntent().getSerializableExtra("movieList");
            page = (int)getIntent().getSerializableExtra("page");
            title = (String)getIntent().getSerializableExtra("title");
        }
        for(Movie movie: displayMovie) {
            System.out.println(movie.getTitle());
            System.out.println(movie.getRating());
            System.out.println(movie.getGenre());
        }
        MovieListViewAdapter adapter = new MovieListViewAdapter(displayMovie, this);

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = displayMovie.get(position);
                String message = String.format("Clicked on position: %d, title: %s", position, movie.getTitle());
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MovieListActivity.this, SingleMovieActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("movieList", displayMovie);
                intent.putExtra("page",page);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }
    public void previous(View view){
        if(page == 0)
        {
            String message = String.format("Currently at page: %d, no previous page", page);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
        else
        {
            page--;
            final RequestQueue queue = NetworkManager.sharedManager(this).queue;
            final JsonArrayRequest searchRequest = new JsonArrayRequest(Request.Method.GET, "https://3.18.108.153:8443/project4/api/movies?num=10&title="+title+"&page="+Integer.toString(page),null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            ((TextView) findViewById(R.id.textView3)).setText("");
                            Log.d("search.success", response.toString());
                            System.out.println(response);
                            Movie searchedMovie = new Movie();
                            for(int i = 0; i < response.length(); i++)
                            {
                                try {
                                    JSONObject jsonObject1 = response.getJSONObject(i);
                                    String movie_id = jsonObject1.optString("movie_id");
                                    String movie_title = jsonObject1.optString("movie_title");
                                    String movie_year = jsonObject1.optString("movie_year");
                                    String movie_director = jsonObject1.optString("movie_director");
                                    String movie_rating = jsonObject1.optString("rating");
                                    String movie_genre = jsonObject1.optString("genre");
                                    String star_name = jsonObject1.optString("star_name");
                                    if (id.equals(""))
                                    {
                                        id = movie_id;
                                        System.out.println("id: " + id);
                                        searchedMovie.setTitle(movie_title);
                                        searchedMovie.setRating(movie_rating);
                                        searchedMovie.setDirector(movie_director);
                                        searchedMovie.setYear(movie_year);
                                        searchedMovie.addGenre(movie_genre);
                                        searchedMovie.addStar(star_name);
                                    }
                                    else if(id.equalsIgnoreCase(movie_id))
                                    {
                                        System.out.println("id equals movie_id: " + id);
                                        searchedMovie.addGenre(movie_genre);
                                        searchedMovie.addStar(star_name);
                                    }
                                    else
                                    {
                                        System.out.println("id not equals movie_id: " + id + "\t"+ movie_id);
                                        if(allMovies.containsKey(id))
                                        {
                                            searchedMovie = allMovies.get(id);
                                            searchedMovie.addGenre(movie_genre);
                                            searchedMovie.addStar(star_name);
                                        }
                                        else
                                        {
                                            allMovies.put(id,searchedMovie);
                                            id = movie_id;
                                            searchedMovie = new Movie();
                                            searchedMovie.setTitle(movie_title);
                                            searchedMovie.setRating(movie_rating);
                                            searchedMovie.setDirector(movie_director);
                                            searchedMovie.setYear(movie_year);
                                            searchedMovie.addGenre(movie_genre);
                                            searchedMovie.addStar(star_name);
                                        }
                                    }
                                    //System.out.println(movie_id + "\t" + movie_title + "\t" + movie_year + "\t" +
                                    //        movie_director + "\t" + movie_rating + "\t" + movie_genre + "\t" + star_name);

                                }
                                catch (JSONException e)
                                {
                                    System.out.println("does not work");
                                }
                            }
                            allMovies.put(id,searchedMovie);
                            for(Map.Entry<String,Movie>entry:allMovies.entrySet())
                            {
                                movieList.add(entry.getValue());
                            }
                            Intent intent = new Intent(MovieListActivity.this, MovieListActivity.class);
                            intent.putExtra("Search", "successful");
                            intent.putExtra("movieList", movieList);
                            intent.putExtra("page", page);
                            intent.putExtra("title", title);
                            startActivity(intent);
                            //((TextView) findViewById(R.id.textView5)).setText(response);
                            // Add the request to the RequestQueue.
                            //queue.add(afterLoginRequest);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Search.error", error.toString());
                            ((TextView) findViewById(R.id.textView3)).setText("Not full text");
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    // Post request form data
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("num", Integer.toString(10));
                    params.put("title", title);
                    params.put("page", Integer.toString(page));

                    return params;
                }
            };

            // !important: queue.add is where the login request is actually sent
            queue.add(searchRequest);

        }


    }

    public void goNext(View view)
    {
        if(displayMovie.size() < 10)
        {
            String message = String.format("Currently at page: %d, no next page", page);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
        else {
            page++;
            final RequestQueue queue = NetworkManager.sharedManager(this).queue;
            final JsonArrayRequest searchRequest = new JsonArrayRequest(Request.Method.GET, "https://3.18.108.153:8443/project4/api/movies?num=10&title=" + title + "&page=" + Integer.toString(page), null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            ((TextView) findViewById(R.id.textView3)).setText("");
                            Log.d("search.success", response.toString());
                            System.out.println(response);
                            Movie searchedMovie = new Movie();
                            if (response.length() == 0) {
                                String message = String.format("Currently at page: %d, no next page", page);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject jsonObject1 = response.getJSONObject(i);
                                        String movie_id = jsonObject1.optString("movie_id");
                                        String movie_title = jsonObject1.optString("movie_title");
                                        String movie_year = jsonObject1.optString("movie_year");
                                        String movie_director = jsonObject1.optString("movie_director");
                                        String movie_rating = jsonObject1.optString("rating");
                                        String movie_genre = jsonObject1.optString("genre");
                                        String star_name = jsonObject1.optString("star_name");
                                        if (id.equals("")) {
                                            id = movie_id;
                                            System.out.println("id: " + id);
                                            searchedMovie.setTitle(movie_title);
                                            searchedMovie.setRating(movie_rating);
                                            searchedMovie.setDirector(movie_director);
                                            searchedMovie.setYear(movie_year);
                                            searchedMovie.addGenre(movie_genre);
                                            searchedMovie.addStar(star_name);
                                        } else if (id.equalsIgnoreCase(movie_id)) {
                                            System.out.println("id equals movie_id: " + id);
                                            searchedMovie.addGenre(movie_genre);
                                            searchedMovie.addStar(star_name);
                                        } else {
                                            System.out.println("id not equals movie_id: " + id + "\t" + movie_id);
                                            if (allMovies.containsKey(id)) {
                                                searchedMovie = allMovies.get(id);
                                                searchedMovie.addGenre(movie_genre);
                                                searchedMovie.addStar(star_name);
                                            } else {
                                                allMovies.put(id, searchedMovie);
                                                id = movie_id;
                                                searchedMovie = new Movie();
                                                searchedMovie.setTitle(movie_title);
                                                searchedMovie.setRating(movie_rating);
                                                searchedMovie.setDirector(movie_director);
                                                searchedMovie.setYear(movie_year);
                                                searchedMovie.addGenre(movie_genre);
                                                searchedMovie.addStar(star_name);
                                            }
                                        }
                                        //System.out.println(movie_id + "\t" + movie_title + "\t" + movie_year + "\t" +
                                        //        movie_director + "\t" + movie_rating + "\t" + movie_genre + "\t" + star_name);

                                    } catch (JSONException e) {
                                        System.out.println("does not work");
                                    }
                                }
                                allMovies.put(id, searchedMovie);
                                for (Map.Entry<String, Movie> entry : allMovies.entrySet()) {
                                    movieList.add(entry.getValue());
                                }
                                Intent intent = new Intent(MovieListActivity.this, MovieListActivity.class);
                                intent.putExtra("Search", "successful");
                                intent.putExtra("movieList", movieList);
                                intent.putExtra("page", page);
                                intent.putExtra("title", title);
                                startActivity(intent);
                                //((TextView) findViewById(R.id.textView5)).setText(response);
                                // Add the request to the RequestQueue.
                                //queue.add(afterLoginRequest);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Search.error", error.toString());
                            ((TextView) findViewById(R.id.textView3)).setText("Not full text");
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    // Post request form data
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("num", Integer.toString(10));
                    params.put("title", title);
                    params.put("page", Integer.toString(page));

                    return params;
                }
            };

            // !important: queue.add is where the login request is actually sent
            queue.add(searchRequest);
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(MovieListActivity.this, SearchBoxActivity.class);
        startActivity(intent);
    }

}
