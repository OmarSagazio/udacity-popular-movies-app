package app.movies.popular.udacity.udacitypopularmoviesapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Omar on 16/11/2015.
 * Movie Details Activity
 */
public class MovieDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra("movie");

        ImageView ivPoster = (ImageView) findViewById(R.id.ivPoster);
        Picasso.with(this).load(movie.getPosterUrl()).into(ivPoster);

        TextView tvMovieTitle = (TextView) findViewById(R.id.movie_title);
        tvMovieTitle.setText(movie.getOriginalTitle());
        TextView tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvReleaseDate.setText(movie.getReleaseDate());
        TextView tvVoteAverage = (TextView) findViewById(R.id.tvVoteAverage);
        tvVoteAverage.setText(movie.getVoteAverage());
        TextView tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvOverview.setText(movie.getOverview());
    }
}
