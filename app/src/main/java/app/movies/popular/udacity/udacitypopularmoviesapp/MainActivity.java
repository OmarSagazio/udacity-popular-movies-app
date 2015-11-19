package app.movies.popular.udacity.udacitypopularmoviesapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Omar on 16/11/2015.
 * .
 */
public class MainActivity extends Activity {

    private static final String SORT_POPULARITY = "popularity.desc";
    private static final String SORT_RATING = "vote_average.desc";
    private static final String KEY = "insert the key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = this.getIntent();
        if(intent != null) {
            new FetchMoviesTask().execute(SORT_POPULARITY);
        } else {
            // Implement the case: Up Button Back
            // 1. user order by highest vote
            // 2. click to see a Movie Detail
            // 3. then goes back by the Up Button
            // Note using the back button is ok
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GridLayout gl = (GridLayout) findViewById(R.id.main_grid_layout);
        switch (item.getItemId()) {
            case R.id.menu_sort_by_highest_rated:
                gl.removeAllViews();
                new FetchMoviesTask().execute(SORT_RATING);
                return true;
            case R.id.menu_sort_by_popularity:
                gl.removeAllViews();
                new FetchMoviesTask().execute(SORT_POPULARITY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        /**
         * Take the String representing the complete list of Movies in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         */
        private Movie[] getMovieDataFromJson(String moviesJsonStr, int movieLimit)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String ID = "id";
            final String POSTER_PATH = "poster_path";
            final String ORIGINAL_TITLE = "original_title";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String VOTE_AVERAGE = "vote_average";
            final String RESULTS = "results";

            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray resultsJson = movieJson.getJSONArray(RESULTS);

            Movie[] movies = new Movie[movieLimit];
            for (int i = 0; i < movies.length; i++) {
                // Get the JSON object representing a Movie
                JSONObject movie = resultsJson.getJSONObject(i);
                movies[i] = new Movie(
                        movie.getString(ID), movie.getString(POSTER_PATH),
                        movie.getString(ORIGINAL_TITLE), movie.getString(OVERVIEW),
                        movie.getString(RELEASE_DATE), movie.getString(VOTE_AVERAGE)
                );
            }

//            DEBUG
//            for (Movie m : movies) {
//                Log.v(LOG_TAG, "Movie with id: " + m.getId() + " - poster path: " + m.getPosterPath());
//            }
            return movies;
        }

        @Override
        protected Movie[] doInBackground(String... sorts) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            int movieLimit = 20;

            try {
                final String THEMOVIEDB_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(THEMOVIEDB_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, sorts[0])
                        .appendQueryParameter(API_KEY, KEY)
                        .build();

                URL url = new URL(builtUri.toString());

//                DEBUG
//                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to "themoviedb.org", and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
//                DEBUG
//                Log.v(LOG_TAG, "Movie JSON String: " + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(movieJsonStr, movieLimit);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the movies.
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] result) {

            if (result != null) {

                GridLayout gLayout = (GridLayout) findViewById(R.id.main_grid_layout);
                for (final Movie movie : result) {
                    String posterUrl = movie.getPosterUrl();
                    if (posterUrl != null) {
                        ImageView imgVW = new ImageView(MainActivity.this);
                        imgVW.setContentDescription(movie.getOriginalTitle());
                        gLayout.addView(imgVW);

                        Picasso.with(MainActivity.this).load(posterUrl).into(imgVW);

                        imgVW.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                                intent.putExtra("movie", movie);
                                startActivity(intent);
                            }
                        });
                    }
                }

            }
        }
    }
}
