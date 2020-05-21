public class Movies {
	private String id;
	private String title; 
    private int year; 
    private String director;
    
    public Movies() {
        this.title = "";
        this.year = -1;
        this.director = "";
        this.id = "";
    }
    
    public Movies(String id, String movietitle, int movieyear, String moviedirector) {
    	this.id = id;
        this.title = movietitle;
        this.year = movieyear;
        this.director = moviedirector;
    }
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getDirector() { return director; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setYear(int year) { this.year = year; }
    public void setDirector(String director) { this.director = director; }
}
