import java.util.ArrayList;

public class Genres {
	private ArrayList<String> genre; 
    private int genreId; 
    private String movieId;
    
    public Genres(){
    	this.genre = new ArrayList<String>();
        this.genreId = -1;
        this.movieId = "";
    }
     public Genres(int genreid, String movieid)
     {
    	 this.genreId = genreid;
    	 this.movieId = movieid;
     }
     
     public Genres(ArrayList<String> genre)
     {
    	 this.genre = genre;
     }

     public ArrayList<String> getGenre() { return genre; }
     public int getGenreId() { return genreId; }
     public String getMovieId() { return movieId; }
     

     public void setGenre(ArrayList<String> genre) { this.genre = genre; }
     public void setGenreId(int genreId) { this.genreId = genreId; }
     public void setMovieId(String movieId) { this.movieId = movieId; }
}