import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.lang.reflect.Field;
 
public class SaxHandler extends DefaultHandler {
	   boolean bTitle = false;
	   boolean bYear = false;
	   boolean bDirector = false;
	   boolean bGenre = false;
	   boolean bFid = false;
	   boolean bFilm = false;
	   private Movies newMovie;
	   private Genres newGenre;
	   boolean invalid = false;
	   int maxGenreId = 0;
	   int count = 0;
	   int movienum;
	   
	   //private Map<Movies,Genres> movies = new HashMap<Movies,Genres>();
	   private Map<String,Movies> tobeinserted = new HashMap<String,Movies>();
	   private Map<Integer, String> genretobeinserted = new HashMap<Integer, String>();
	   private Map<Movies,String> invalidmovies = new HashMap<Movies,String>();
	   private Map<String,Movies> movies = new HashMap<String,Movies>();
	   private Map<Integer, String> genres = new HashMap<Integer, String>();
	   private Map<Integer,String> genres_in_movies = new HashMap<Integer,String>();
	   //private Map<Integer,String> genres_in_moviesinsert = new HashMap<Integer,String>();
	   private ArrayList<ArrayList<String>> genres_in_moviesinsert = new ArrayList<ArrayList<String>>();
;	   private Connection dbcon;
		
	   public SaxHandler() throws Exception
	   {
		   Context initCtx = new InitialContext();

	        Context envCtx = (Context) initCtx.lookup("java:comp/env");
	        
	        // Look up our data source
	        DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

	        // the following commented lines are direct connections without pooling
	        //Class.forName("org.gjt.mm.mysql.Driver");
	        //Class.forName("com.mysql.jdbc.Driver").newInstance();
	        //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	   

	        Connection dbcon = ds.getConnection();
	       
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
	    	dbcon.setAutoCommit(false);
			String query = "select * from movies";
			java.sql.PreparedStatement Statement = dbcon.prepareStatement(query);
			ResultSet moviers = Statement.executeQuery();
			String query1 = "SELECT g.name, gm.movieId, gm.genreId \n" + 
			   				"FROM movies m, genres g, genres_in_movies gm\n" + 
			   				"where m.id = gm.movieId and g.id = gm.genreId and m.title = ?;";
			java.sql.PreparedStatement Statement1 = dbcon.prepareStatement(query1);
			while(moviers.next())
			{
				/*ArrayList<String> genreList = new ArrayList<String>();
				Statement1 = connection.prepareStatement(query1);
				Statement1.setString(1, moviers.getString("title"));
				ResultSet moviers1 = Statement1.executeQuery();
				while(moviers1.next())
				{
					genreList.add(moviers1.getString("name"));
					genres_in_movies.put(moviers1.getInt("genreId"), moviers1.getString("movieId"));
				}
				movies.put(new Movies(moviers.getString("id"), moviers.getString("title"), moviers.getInt("year"), moviers.getString("director")), new Genres(genreList));*/
				
				movies.put(moviers.getString("id"), new Movies(moviers.getString("id"), moviers.getString("title"), moviers.getInt("year"), moviers.getString("director")));
			}
			//Statement1.close();
			//find the max id and create a new id after the max id
			query = "select max(id) as id from movies";
			Statement =dbcon.prepareStatement(query);
			ResultSet movierss = Statement.executeQuery();
			String movieid = "";
			while (movierss.next()) {
				movieid = movierss.getString("id");
			}
			movienum = Integer.parseInt(movieid.replace("tt",""));
			query = "select * from genres";
			Statement =dbcon.prepareStatement(query);
			ResultSet genrers = Statement.executeQuery();
			while(genrers.next())
			{
				genres.put(genrers.getInt("id"), genrers.getString("name"));
			}
			query = "select * from genres_in_movies";
			Statement =dbcon.prepareStatement(query);
			ResultSet genre_in_rs = Statement.executeQuery();
			while(genre_in_rs.next())
			{
				if(maxGenreId < genre_in_rs.getInt("genreId"))
				{
					maxGenreId = genre_in_rs.getInt("genreId");
				}
				genres_in_movies.put(genre_in_rs.getInt("genreId"), genre_in_rs.getString("movieId"));
			}
			Statement.close();
	   }
	   
	   private void insertMovie(Movies film)
	   {
		   for (Map.Entry<String,Movies> movie : movies.entrySet())
	       {
				if(film.getTitle().equals(movie.getValue().getTitle()) &&
						film.getYear() == movie.getValue().getYear() &&
								film.getDirector().equals(movie.getValue().getDirector()))
				{
					System.out.println(film.getTitle() + " Already Exists!!!");
					invalidmovies.put(film, film.getId());
				}
				else
				{
					tobeinserted.put(film.getId(),film);
				}
	       }
		   /*for(Movies movie: tobeinserted)
		   {
			   if(film.getTitle().equals(movie.getTitle()) &&
					   film.getYear() == movie.getYear() &&
							   film.getDirector().equals(movie.getDirector()))
			   {
				   System.out.println(film.getTitle() + " Already Exists!");
				   invalidmovies.put(film, film.getId());
			   }
			   else if(movie.getTitle().equals(movie.getTitle()))
			   {
				   movienum++;
				   String newid = "tt"+ Integer.toString(movienum);
				   film.setId(newid);
				   //tobeinserted.add(film);
			   }
		   }*/
	   }
	   
	   private void insertGenre(Genres genre)
	   {
		   for(int i = 0; i < genre.getGenre().size(); i++)
		   {
			   
			   if(!genres.containsValue(genre.getGenre().get(i)) && !genretobeinserted.containsValue(genre.getGenre().get(i)))
			   {
				   maxGenreId ++;
				   genretobeinserted.put(maxGenreId, genre.getGenre().get(i));
			   }
			   else
			   {
				   //System.out.println(genre.getGenre().get(i) + " Already Exists!!!");
			   }
		   }
	   }
	   
	   private void relateGenre(Genres genre, Movies film)
	   {
		   for(int i = 0; i < genre.getGenre().size(); i++)
		   {
			   for (Map.Entry<Integer,String> eachgenre : genres.entrySet())
			   {
				   if (genre.getGenre().get(i).equals(eachgenre.getValue()))
				   {
					   //System.out.println("genres_in_moviesinsert:" + eachgenre.getKey() + "\t" + film.getId());
					   genres_in_moviesinsert.add(new ArrayList<String>(Arrays.asList(Integer.toString(eachgenre.getKey()), film.getId())));
				   }
			   }
			   
			   if(genretobeinserted.containsValue(genre.getGenre().get(i)))
			   {
				   for(Map.Entry<Integer,String> eachgenre :genretobeinserted.entrySet())
				   {
					   if (genre.getGenre().get(i).equals(eachgenre.getValue()))
					   {
						   //System.out.println("genres_in_moviesinsert:" + eachgenre.getKey() + "\t" + film.getId());
						   genres_in_moviesinsert.add(new ArrayList<String>(Arrays.asList(Integer.toString(eachgenre.getKey()), film.getId())));
					   }
				   }
			   }
			   
	       }
	   }
	   
	   private void insertToDB() throws SQLException
	   {
		   //String query = "INSERT INTO movies (id, title, year, director) VALUES (?,?,?,?);";
		   java.sql.PreparedStatement Statement;
		   for (Map.Entry<String, Movies> m: tobeinserted.entrySet())
		   {
			   String query = "INSERT INTO movies (id, title, year, director) VALUES (?,?,?,?);";
			   Statement =dbcon.prepareStatement(query);
			   Statement.setString(1, m.getValue().getId());
			   Statement.setString(2, m.getValue().getTitle());
			   Statement.setInt(3, m.getValue().getYear());
			   Statement.setString(4, m.getValue().getDirector());
			   //System.out.println("MovieId: " + m.getValue().getId() + ",\tMovieTitle: "+ m.getValue().getTitle() + ",\tMovieYear: " + m.getValue().getYear() + ",\tMovieDirector: " + m.getValue().getDirector());
			   Statement.executeUpdate();
			   query = "INSERT INTO ratings (movieId, rating, numVotes) VALUES (?,?,?)";
				Statement =dbcon.prepareStatement(query);
				Statement.setString(1, m.getValue().getId());
				Statement.setFloat(2, -1);
				Statement.setInt(3,0);
				Statement.executeUpdate();
		   } 
		   
		   for(Map.Entry<Integer,String> eachgenre :genretobeinserted.entrySet())
		   {
			   String query = "INSERT INTO genres (id, name) VALUES (?,?);";
			   Statement =dbcon.prepareStatement(query);
			   Statement.setInt(1, eachgenre.getKey());
			   Statement.setString(2, eachgenre.getValue());
			   //System.out.println(eachgenre.getKey()+"\t" + eachgenre.getValue());
			   Statement.executeUpdate();
		   }
		   
		   for(int i = 0; i < genres_in_moviesinsert.size(); i++)
		   {
			   String query = "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?,?);";
			   Statement =dbcon.prepareStatement(query);
			   Statement.setInt(1, Integer.parseInt(genres_in_moviesinsert.get(i).get(0)));
			   Statement.setString(2, genres_in_moviesinsert.get(i).get(1));
			   //System.out.println(genres_in_moviesinsert.get(i).get(0)+"\t" + genres_in_moviesinsert.get(i).get(1));
			   Statement.executeUpdate();
		   }
		   /*
		   for(Map.Entry<Integer,String> eachgenre :genres_in_moviesinsert.entrySet())
		   {
			   String query = "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?,?);";
			   Statement =connection.prepareStatement(query);
			   Statement.setInt(1, eachgenre.getKey());
			   Statement.setString(2, eachgenre.getValue());
			   System.out.println(eachgenre.getKey()+"\t" + eachgenre.getValue());
			   Statement.executeUpdate();
		   }*/
		   //Statement.close();
	   }
 
    /* 此方法有三个参数
       arg0是传回来的字符数组，其包含元素内容
       arg1和arg2分别是数组的开始位置和结束位置 */
    @Override
    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
        //String content = new String(arg0, arg1, arg2);
        //System.out.println(content);
        
        super.characters(arg0, arg1, arg2);
        if (bTitle) {
            //System.out.println("Title: " + new String(arg0, arg1, arg2));
            newMovie.setTitle(new String(arg0, arg1, arg2).trim());
            bTitle = false;
         } else if (bYear) {
            //System.out.println("Year: " + new String(arg0, arg1, arg2));
            try
            {
            	newMovie.setYear(Integer.parseInt(new String(arg0, arg1, arg2).trim()));
            }
            catch (Exception e) {
            	//newMovie.setYear(Integer.parseInt(new String(arg0, arg1, arg2).trim()));
            	//invalid = true;
            	newMovie.setYear(-1);
            }
            //newMovie.setYear(Integer.parseInt(new String(arg0, arg1, arg2).trim()));
            bYear = false;
         } else if (bDirector) {
            //System.out.println("Director: " + new String(arg0, arg1, arg2));
            newMovie.setDirector(new String(arg0, arg1, arg2).trim());
            bDirector = false;
         } else if (bGenre) {
        	String genre = new String(arg0, arg1, arg2);
            //System.out.println("Genre: " + genre);
            ArrayList<String> newstring = new ArrayList<String>();
            //System.out.println(newstring.size());
            for(String a: genre.split(" "))
            {
            	newstring.add(a.trim());
            	//System.out.println(a.trim());
            }
            newGenre.setGenre(newstring);
            //System.out.println(genre.split(" ").toString());
            bGenre = false;
         } else if (bFid) {
        	 /*if(new String(arg0, arg1, arg2).trim().equals(""))
        	 {
        		 movienum++;
        		 String newid = "tt"+ Integer.toString(movienum);
        		 newMovie.setId(newid);
        	 }
        	 else
        	 {
        		 
        	 }*/
        	 if(tobeinserted.containsKey(new String(arg0, arg1, arg2).trim()))
        	 {
        		 invalid = true;
        	 }
        	 else
        	 {
        		 if(!new String(arg0, arg1, arg2).trim().equals(""))
            	 {
        			 newMovie.setId(new String(arg0, arg1, arg2).trim());
            	 }
        	 }
        	 
             //System.out.println("fid: " + new String(arg0, arg1, arg2));
             bFid = false;
         }
        
    }
 
    @Override
    public void endDocument() throws SAXException {
        System.out.println("\n…………结束解析文档…………");
        System.out.println(movies.size());
        System.out.println(count);
        try {
			insertToDB();
			dbcon.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println();
        super.endDocument();
    }
 
    /* arg0是名称空间
       arg1是包含名称空间的标签，如果没有名称空间，则为空
       arg2是不包含名称空间的标签 */
    @Override
    public void endElement(String arg0, String arg1, String arg2)
            throws SAXException {
    	if (arg2.equalsIgnoreCase("directorfilms")) {
    		//System.out.println("end of director");
    		//System.out.println();
    	}else if (arg2.equalsIgnoreCase("film")) {
    		//System.out.println("end of films");
    		//System.out.println("MovieId: " + newMovie.getId() + ",\tMovieTitle: "+ newMovie.getTitle() + ",\tMovieYear: " + newMovie.getYear() + ",\tMovieDirector: " + newMovie.getDirector());
    		//System.out.println("Genres:" + newGenre.getGenre());
    		if(invalid)
    		{
    			invalidmovies.put(newMovie, newMovie.getId());
        		System.out.println(" Invalid Movie: Id-" + newMovie.getId() + ",\tTitle-"+ newMovie.getTitle() + ",\tYear-" + newMovie.getYear() + ",\tDirector-" + newMovie.getDirector());

    		}
    		else
    		{
    			insertMovie(newMovie);
    			insertGenre(newGenre);
        		relateGenre(newGenre, newMovie);
    		}
    		//insertMovie(newMovie);
    		//insertGenre(newGenre);
    		//relateGenre(newGenre, newMovie);
    		//System.out.println();
    	}
        super.endElement(arg0, arg1, arg2);
    }
 
    @Override
    public void startDocument() throws SAXException {
        System.out.println("…………开始解析文档…………\n");
        super.startDocument();
    }
 
    /*arg0是名称空间
      arg1是包含名称空间的标签，如果没有名称空间，则为空
      arg2是不包含名称空间的标签
      arg3很明显是属性的集合 */
    @Override
    public void startElement(String arg0, String arg1, String arg2,
            Attributes arg3) throws SAXException {
    	if (arg2.equalsIgnoreCase("directorfilms")) {
    		//System.out.println("start of director");
    	}
    	else if(arg2.equalsIgnoreCase("film")) {
    		bFilm = true;
    		newMovie = new Movies();
    		newGenre = new Genres();
    		invalid = false;
    		count++;
    	}
    	else if (arg2.equalsIgnoreCase("t")) {
    		bTitle = true;
         } else if (arg2.equalsIgnoreCase("year")) {
            bYear = true;
         } else if (arg2.equalsIgnoreCase("dirn")) {
        	//arg2.equalsIgnoreCase("dirname") || 
            bDirector = true;
         }
         else if (arg2.equalsIgnoreCase("cats")) {
        	 bGenre = true;
         }
         else if (arg2.equalsIgnoreCase("fid")) {
             bFid = true;
          }
    	
        //System.out.print(arg2 + ":");
        super.startElement(arg0, arg1, arg2, arg3);
    }
    
    /*public static void main(String[] args) throws Exception {
        // 1.实例化SAXParserFactory对象
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 2.创建解析器
        SAXParser parser = factory.newSAXParser();
        // 3.获取需要解析的文档，生成解析器,最后解析文档
        
        File f = new File("WebContent/mains243.xml");
        SaxHandler dh = new SaxHandler();
        parser.parse(f, dh);
    }*/
}
