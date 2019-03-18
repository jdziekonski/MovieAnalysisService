package jdziekonski.movieanalysisservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("generic")
@Singleton
public class MovieAnalysisService {

    @Context
    private UriInfo context;
    private List<String> wordsPositive;
    private List<String> wordsNegative;
    private Map<String, Movie> moviesMap;
    public MovieAnalysisService() {
        wordsPositive = Arrays.asList("dobry fajny fantastyczny niezły świetny genialny dobra fajna super fantastyczna niezła świetna genialna dobre fajne fantastyczne niezłe świetne genialne".split(" "));
        wordsNegative = Arrays.asList("słaby kiepski beznadziejny zły paskudny brzydki słaba kiepska beznadziejna zła paskudna brzydka słabe kiepskie beznadziejne złe paskudne brzydkie".split(" "));
        moviesMap = new HashMap<>();
        Movie m1 = new Movie("Pulp Fiction", "Quentin Tarantino");
        moviesMap.put(m1.getId(), m1);
        Movie m2 = new Movie("The Godfather", "Francis Ford Copolla");
        moviesMap.put(m2.getId(), m2);
        Movie m3 = new Movie("Fight Club", "David Fincher");
        moviesMap.put(m3.getId(), m3);
        Movie m4 = new Movie("It", "Andy Muschietti");
        moviesMap.put(m4.getId(), m4);
    }
    
    @GET
    @Path("movie/{movieId}")
    public Response getMovie(@PathParam("movieId") String movieId) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        if (moviesMap.containsKey(movieId))
            return Response.ok(mapper.writeValueAsString(moviesMap.get(movieId))).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @POST
    @Path("movieReview/{movieId}")
    public Response addReview(@PathParam("movieId") String movieId, String content) throws IOException, URISyntaxException {
        Movie mv = moviesMap.get(movieId);
        int tempPositiveCounter=0;
        int tempNegativeCounter=0;
        
        //regular expression - in order to remove any punctuation
        String lettersOnly = content.replaceAll("[!\"#$%&'()*+,-./:;<=>?@\\^_`{|}~]", "");
        String lowerCase = lettersOnly.toLowerCase();
        
        //splitting review into fixed-size list of words
        String[] contentArr = lowerCase.split(" ");
        List<String> splittedContent;
        splittedContent = Arrays.asList(contentArr);
        
        for(int i=0; i<wordsPositive.size(); i++){
            for(int j=0; j<splittedContent.size(); j++){
                if(Objects.equals(wordsPositive.get(i), splittedContent.get(j))){
                    tempPositiveCounter++;
                }
            }
        }
        for(int i=0; i<wordsNegative.size(); i++){
            for(int j=0; j<splittedContent.size(); j++){
                if(Objects.equals(wordsNegative.get(i), splittedContent.get(j))){
                    tempNegativeCounter++;
                }
            }
        }
        
        //compare counters and assign rating
        if (tempPositiveCounter > tempNegativeCounter){
            mv.recordPositiveSentimentReview();
            return Response.ok().entity("POSITIVE").build();
        }
        else if(tempNegativeCounter > tempPositiveCounter){
            mv.recordNegativeSentimentReview();
            return Response.ok().entity("NEGATIVE").build();
        }
        else {
            return Response.ok().entity("NEUTRAL").build();
        }
    }
}


