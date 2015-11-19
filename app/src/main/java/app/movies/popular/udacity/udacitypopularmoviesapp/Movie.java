package app.movies.popular.udacity.udacitypopularmoviesapp;

import java.io.Serializable;

/**
 * Created by Omar on 20/10/2015.
 * -
 */
public class Movie implements Serializable {
    private static final String BASE_IMG_URL = "http://image.tmdb.org/t/p/w185/";

    private String id;
    private String posterPath;
    private String originalTitle;
    private String overview;        // Plot
    private String releaseDate;
    private String voteAverage;    // User Rating
    private String posterUrl;

    public Movie(String id, String posterPath, String originalTitle,
                 String overview, String releaseDate, String voteAverage) {
        this.setId(id);
        this.setPosterPath(posterPath);
        this.setOriginalTitle(originalTitle);
        this.setOverview(overview);
        this.setReleaseDate(releaseDate);
        this.setVoteAverage(voteAverage);
    }


    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        if (posterPath.equals("null"))
            return;

        this.posterPath = posterPath;
        setPosterUrl(posterPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage + "/10";
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    private void setPosterUrl(String posterPath) {
        this.posterUrl = BASE_IMG_URL + posterPath;
    }
}
