package training.elfeky.com.gridviewexample.main;


import java.io.Serializable;

/**
 * Created by f on 03/09/2016.
 */
public class MovieDetails implements Serializable {
    String posterLink ,overview,extra,title,voteAverage,releaseDate,id;

    //setters

    public void setId(String id) {this.id = id;}

    public void setPosterLink(String posterLink) {this.posterLink = posterLink;}

    public void setOverview(String overview) { this.overview = overview;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }


    //getters

    public String getPosterLink() {
        return posterLink;
    }

    public String getOverview() {
        return overview;
    }

    public String getTitle() {return title;}

    public String getVoteAverage() {return voteAverage; }

    public String getReleaseDate() { return releaseDate; }

    public String getId() {return id;}

    public String getExtra() {
        return extra;
    }
}
