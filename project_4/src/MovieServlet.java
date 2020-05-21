import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/movies"
@WebServlet(name = "MovieServlet", urlPatterns = "/api/movies")
public class MovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    String loginUser = "mytestuser";
	String loginPasswd = "mypassword";
	String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

    // Create a dataSource which registered in web.xml
 

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        System.out.println("MovieServlet");
        String gettitle = request.getParameter("title");
        String num = request.getParameter("num");
        String page = request.getParameter("page");
        System.out.println("gettitle"+gettitle);
        System.out.println("num"+num);
        System.out.println("page"+page);
        // Output stream to STDOUT
       
        try {
            // Get a connection from dataSource
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
	 		// create database connection
	 		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	 		String query = "";
	 		query ="select m.id, m.title, m.year, m.director, g.name as genre, g.id as gid, s.id as starid,s.name as starname, r.rating\n" + 
					"from (SELECT * FROM movies where title = ?) m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r \n" + 
					"where m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and r.movieId = m.id;";
	 		PreparedStatement statement = connection.prepareStatement(query);
            
			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1,gettitle);
			ResultSet rs = statement.executeQuery();
			System.out.println("excute1");
            JsonArray jsonArray = new JsonArray();
            List<List<String>> newinfo = new ArrayList<List<String>>();
			String id1="";
			String movietitle="";
			String year="";
			String director="";
			String rating="";
			String gname="";
			String sname="";
			int intnum = Integer.parseInt(num);
			int intpage = Integer.parseInt(page);
			int checkpage = 0;
			int pass = intnum*intpage;
			Map<String,String> checktitle = new HashMap<String,String>();
			Map<String,String> checknum = new HashMap<String,String>();
			 System.out.println("num"+num);
		     System.out.println("page"+page);
			while (rs.next()) {
            	
		            	id1 = rs.getString("id");
		            	 movietitle = rs.getString("title"); 
		            	 year = rs.getString("year");
		            	 director = rs.getString("director");
		            	 rating = rs.getString("rating");
		            	 gname = rs.getString("genre");
		            	 sname = rs.getString("starname");
		            	 newinfo.add(Arrays.asList(id1,movietitle, year, director, rating,gname,sname));
		            	 checktitle.put(id1, movietitle);
		                // Create a JsonObject based on the data we retrieve from rs
		            	JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("movie_id", id1);
						jsonObject.addProperty("movie_title", movietitle);
						jsonObject.addProperty("movie_year", year);
						jsonObject.addProperty("movie_director", director);
						jsonObject.addProperty("rating", rating);
						jsonObject.addProperty("genre", gname);
						jsonObject.addProperty("star_name", sname);
						
		                jsonArray.add(jsonObject);
			};
			System.out.println("good query1");
			System.out.println(newinfo);
			checktitle.clear();
			checknum.clear();
			if(newinfo.isEmpty()) {
			String add ="";
			String[] splited = gettitle.split("\\s+");
			int i =0;
			for(i=0; i<splited.length; i++){
				add+="+"+splited[i]+"*";
			 }
        	query = "select m.id, m.title, m.year, m.director, g.name as genre, g.id as gid, s.id as starid,s.name as starname, r.rating\n" + 
					"from movies m, genres_in_movies gm, genres g, stars_in_movies sm, stars s, ratings r\n" + 
					"where m.id = gm.movieId and gm.genreId = g.id and m.id = sm.movieId and sm.starId = s.id and m.id = r.movieId and match (title) against (\""+add+"\" in boolean mode);";
            
			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			// Perform the query
			//System.out.println("excute2");
			ResultSet nrs = statement.executeQuery(query);
			System.out.println("excute2-Sucess");
            // Iterate through each row of rs
			 System.out.println("num"+num);
		    System.out.println("page"+page);
			while (nrs.next()) {
            	if(intpage ==0) {
            		//System.out.println("page=0 c2");
            		 //System.out.println("num"+intnum);
            		if(checktitle.size()<intnum) {
		            	id1 = nrs.getString("id");
		            	 movietitle = nrs.getString("title");
		            	 year = nrs.getString("year");
		            	 director = nrs.getString("director");
		            	 rating = nrs.getString("rating");
		            	 gname = nrs.getString("genre");
		            	 sname = nrs.getString("starname");
		            	
			                // Create a JsonObject based on the data we retrieve from rs
			            	JsonObject jsonObject = new JsonObject();
							jsonObject.addProperty("movie_id", id1);
							jsonObject.addProperty("movie_title", movietitle);
							jsonObject.addProperty("movie_year", year);
							jsonObject.addProperty("movie_director", director);
							jsonObject.addProperty("rating", rating);
							jsonObject.addProperty("genre", gname);
							jsonObject.addProperty("star_name", sname);
							
		                jsonArray.add(jsonObject);
		                checktitle.put(id1, movietitle);
            		}
            		id1 = nrs.getString("id");
            		//System.out.println(id1);
            		//System.out.println(checktitle.containsKey(id1));
	            	 if(checktitle.containsKey(id1)) {
	            		 movietitle = nrs.getString("title");
		            	 year = nrs.getString("year");
		            	 director = nrs.getString("director");
		            	 rating = nrs.getString("rating");
		            	 gname = nrs.getString("genre");
		            	 sname = nrs.getString("starname");
		            	
			                // Create a JsonObject based on the data we retrieve from rs
			            	JsonObject jsonObject = new JsonObject();
							jsonObject.addProperty("movie_id", id1);
							jsonObject.addProperty("movie_title", movietitle);
							jsonObject.addProperty("movie_year", year);
							jsonObject.addProperty("movie_director", director);
							jsonObject.addProperty("rating", rating);
							jsonObject.addProperty("genre", gname);
							jsonObject.addProperty("star_name", sname);
							
		                jsonArray.add(jsonObject);
	            	 }
            		//System.out.println("checktitle"+checktitle);
            		//System.out.println("checktitle-size"+checktitle.size());
            	}else {   
            		//System.out.println("page>0 c2");
            		//System.out.println(pass);
            		if(checktitle.size()<pass) {
            			id1 = nrs.getString("id");
		            	movietitle = nrs.getString("title"); 
		            	checktitle.put(id1, movietitle);
		            	//System.out.println("title passed:"+checktitle);
            		}else {
            			//System.out.println("pass"+pass);
                		//System.out.println("title passed"+checktitle);
            			if(checknum.size()<intnum) {
            			id1 = nrs.getString("id");
   		            	 movietitle = nrs.getString("title");
   		            	 year = nrs.getString("year");
   		            	 director = nrs.getString("director");
   		            	 rating = nrs.getString("rating");
   		            	 gname = nrs.getString("genre");
   		            	 sname = nrs.getString("starname");
   		            	checknum.put(id1, movietitle);
   		            	
   		            	
   		                // Create a JsonObject based on the data we retrieve from rs
   		            	JsonObject jsonObject = new JsonObject();
   						jsonObject.addProperty("movie_id", id1);
   						jsonObject.addProperty("movie_title", movietitle);
   						jsonObject.addProperty("movie_year", year);
   						jsonObject.addProperty("movie_director", director);
   						jsonObject.addProperty("rating", rating);
   						jsonObject.addProperty("genre", gname);
   						jsonObject.addProperty("star_name", sname);
   						
   		                jsonArray.add(jsonObject);
            			}
            			id1 = nrs.getString("id");
                		//System.out.println(id1);
                		//System.out.println(checknum.containsKey(id1));
    	            	 if(checknum.containsKey(id1)) {
    	            		 movietitle = nrs.getString("title");
    		            	 year = nrs.getString("year");
    		            	 director = nrs.getString("director");
    		            	 rating = nrs.getString("rating");
    		            	 gname = nrs.getString("genre");
    		            	 sname = nrs.getString("starname");
    		            	
    			                // Create a JsonObject based on the data we retrieve from rs
    			            	JsonObject jsonObject = new JsonObject();
    							jsonObject.addProperty("movie_id", id1);
    							jsonObject.addProperty("movie_title", movietitle);
    							jsonObject.addProperty("movie_year", year);
    							jsonObject.addProperty("movie_director", director);
    							jsonObject.addProperty("rating", rating);
    							jsonObject.addProperty("genre", gname);
    							jsonObject.addProperty("star_name", sname);
    							
    		                jsonArray.add(jsonObject);
    	            	 }
                		//System.out.println("checktitle"+checktitle);
                		//System.out.println("checktitle-size"+checktitle.size());
            		}
            		//System.out.println("title entered"+checknum);
            		//System.out.println("title entered-size"+checknum.size());
            			
            		}
            	
     };
            
			}
	 		
            
            System.out.println("MovieServlet-ok");
            System.out.println("json"+jsonArray.toString());
            // write JSON string to output
            out.write(jsonArray.toString());
            response.getWriter().write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
        	
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			response.getWriter().write(jsonObject.toString());
			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
        out.close();

    }
}
