package jdziekonski.movieanalysisservice;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Movie {
    private String title;
    private String director;
    private int positiveSentimentCounter;
    private int negativeSentimentCounter;
    private String id;

    public Movie() {
        this.id = String.join("", title.split(" ")).toLowerCase();
    }

    public Movie(String title, String director) {
        this.id = String.join("", title.split(" ")).toLowerCase();
        this.title = title;
        this.director = director;
        this.positiveSentimentCounter = 0;
        this.negativeSentimentCounter = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getPositiveSentimentCounter() {
        return positiveSentimentCounter;
    }

    public void setPositiveSentimentCounter(int positiveSentimentCoutner) {
        this.positiveSentimentCounter = positiveSentimentCoutner;
    }

    public int getNegativeSentimentCounter() {
        return negativeSentimentCounter;
    }

    public void setNegativeSentimentCounter(int negativeSentimentCounter) {
        this.negativeSentimentCounter = negativeSentimentCounter;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }
    
    public void recordPositiveSentimentReview()
    {
        this.positiveSentimentCounter++;
    }
    
    public void recordNegativeSentimentReview()
    {
        this.negativeSentimentCounter++;
    }   
}
